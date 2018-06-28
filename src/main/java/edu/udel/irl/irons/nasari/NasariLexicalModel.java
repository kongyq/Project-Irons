package edu.udel.irl.irons.nasari;

import com.google.common.base.Stopwatch;
import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.util.CompressionUtil;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by mike on 6/26/18.
 */
public class NasariLexicalModel {

    private static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    private static final boolean isCompressionEnabled = IronsConfiguration.getInstance().getIsCompressedNASARILexicalModel();
    private static final long ONE_GB = 1024 * 1024 * 1024;  // = 2^30
    private static final int ONE_BLOCK = 1024 * 1024 * 1024;

    private static NasariLexicalModel instance = null;

    private final Int2LongMap synset2vectorOffset;
    private final ObjectList<ByteBuffer> vectorBlocks;
//    private final List<ByteBuffer> vectorBlocks;
//    private final ByteBuffer[] vectorBlocks;
//
    private NasariLexicalModel(Int2LongMap synset2vectorOffset, ObjectList<ByteBuffer> vectors){
        this.synset2vectorOffset = Int2LongMaps.unmodifiable(synset2vectorOffset);
        this.vectorBlocks = ObjectLists.unmodifiable(vectors);
    }

//    private NasariLexicalModel(Int2LongMap synset2vectorOffset, ByteBuffer[] vectors) {
//        this.synset2vectorOffset = Int2LongMaps.unmodifiable(synset2vectorOffset);
//        this.vectorBlocks = vectors;
//    }

    public static NasariLexicalModel loadLemmaFile(File vectorFile) throws IOException {

//        System.out.println(sun.misc.VM.maxDirectMemory());

        Stopwatch stopwatch = Stopwatch.createStarted();

        int count = 1;
//        boolean isSingleBlock = true;
        //get number of lines(synset) and tabs
        Stopwatch stopwatch2 = Stopwatch.createStarted();

        System.out.println("Analyzing the vector file...");
        LineNumberReader reader = new LineNumberReader(new FileReader(vectorFile));
        reader.skip(Integer.MAX_VALUE);
        int lineCount = reader.getLineNumber();
        reader.close();

        System.out.println(stopwatch2.stop());
//
        //initial synset2vector offset map.
        Int2LongMap synset2vectorOffset = new Int2LongOpenHashMap(lineCount);

        //memory mapping
        System.out.println("Reading #1 gigabyte of the vector file.");
        FileChannel fileChannel = new FileInputStream(vectorFile).getChannel();
        MappedByteBuffer buffer =
                fileChannel.map(
                        FileChannel.MapMode.READ_ONLY,
                        0,
                        Math.min(fileChannel.size(), Integer.MAX_VALUE));

        //store mapping times
        int bufferCount = 1;
        int blockNumber = 0;
        long bufferIndex = 0;

        //allocate memory for vector space.
        ObjectList<ByteBuffer> vectorBlocks = new ObjectArrayList<>();
        ByteBuffer currentVectorBlock = ByteBuffer.allocateDirect(ONE_BLOCK);

//        ByteBuffer[] vectorBlocks = new ByteBuffer[7];
//        for(int i = 0; i < vectorBlocks.length; ++i) {
//        vectorBlocks[blockNumber] = ByteBuffer.allocateDirect(ONE_BLOCK);
//        }

//        StringBuilder sb = new StringBuilder();
        ByteList bl = new ByteArrayList();
//        StringBuilder synsetBuilder = new StringBuilder();
        while(buffer.hasRemaining()){

//            int vectorCount = 0;

            bl.clear();
//            sb.setLength(0);
//            char c = (char) buffer.get();
            byte b = buffer.get();


            //read a line
            //read a synset
//            while((bl = buffer.get()) == 9){

            while(b != '\t'){
//                sb.append(c);
                bl.add(b);
//                c = (char) buffer.get();
                b = buffer.get();
            }

//            int synset = Integer.parseInt(sb.toString().substring(3,11));
            int synset = Integer.parseInt(new String(bl.toByteArray()).substring(3,11));
//            System.out.println(synset);
//            System.out.flush();

            //prepare to read vectors
            b = buffer.get();
//            c = (char) buffer.get();
//            sb.setLength(0);
            bl.clear();
            while(b != '\t'){b = buffer.get();}
//                System.out.print(c);}
            b = buffer.get();

            //read vectors
            while(b != '\n'){
                while(b != '_'){
//                    sb.append(c);
                    bl.add(b);
                    b = buffer.get();
                }
//                vectorCount++;

                //skip vector values
                while(b != '\t' && b != '\n'){
                    b = buffer.get();
//                    System.out.print(c);
                }
            }
//            sb.append('\t');

            byte[] vectors = bl.toByteArray();
            int vectorLength = bl.size();

//            int vectorLength = vectors.getBytes().length;
//            String[] lineSplited = line.split("\t");

//            System.out.println("test");
            long synsetVectorStartPosition = bufferIndex + (blockNumber * ONE_BLOCK);

            //check current block available space, if not enough to store the new vector, create a new buffer,
            // and add current buffer to the block list. then adjust synset to vector pointer.
            if(!isEnoughSpace(currentVectorBlock.position(),vectorLength)){
//            if(!isEnoughSpace(vectorBlocks[blockNumber].position(), vectors)) {
                System.out.println("created a new 1g bytebuffer.");
                vectorBlocks.add(currentVectorBlock);
//                vectorBlock.clear();
//                ++blockNumber;
                currentVectorBlock = ByteBuffer.allocateDirect(ONE_BLOCK);
//                vectorBlocks[++blockNumber] = ByteBuffer.allocateDirect(ONE_BLOCK);
                blockNumber ++;
                synsetVectorStartPosition = blockNumber * ONE_BLOCK;
//                isSingleBlock = false;
            }

            //enable byte array compression
            if(isCompressionEnabled == true){
                byte[] compressedVectors = CompressionUtil.compress(vectors);
                currentVectorBlock.putInt(compressedVectors.length);
                currentVectorBlock.put(compressedVectors);
            }else {
                currentVectorBlock.putInt(vectorLength);
                currentVectorBlock.put(vectors);
            }
//            vectorBlocks[blockNumber].putInt(vectorLength);
//            vectorBlocks[blockNumber].put(vectors.getBytes());


            synset2vectorOffset.put(synset, synsetVectorStartPosition);

//            bufferIndex = vectorBlocks[blockNumber].position();
            bufferIndex = currentVectorBlock.position();
            //remap if file larger than one gigabyte.
            if(buffer.position() > ONE_GB){
                final int newPosition = (int) (buffer.position() - ONE_GB);
                final long size = Math.min(fileChannel.size() - ONE_GB * bufferCount, Integer.MAX_VALUE);

                System.out.format("Reading #%d gigabyte of the vector file. Start: %d, size: %d%n",
                        bufferCount + 1,
                        ONE_GB * bufferCount,
                        size);

                buffer = fileChannel.map(
                        FileChannel.MapMode.READ_ONLY,
                        ONE_GB * bufferCount,
                        size);

                buffer.position(newPosition);
                bufferCount += 1;
            }
            if(count % 10000 == 0){
                System.out.println(count);
            }
            count += 1;

        }
//        if(isSingleBlock == true){
//            vectorBlocks.add(vectorBlock);
//        }
        stopwatch.stop();
        System.out.println("Vector file successfully loaded! cost " + stopwatch);

        return new NasariLexicalModel(synset2vectorOffset, vectorBlocks);
//        return null;
    }

    public static int countTabs(String str) {
        int count = 0;
        for(int idx = 0; (idx = str.indexOf("\t", idx)) != -1; ++idx) {
            ++count;
        }
        return count;
    }

    public static synchronized NasariLexicalModel getInstance(File vectorFile){
        if (instance == null){
            try {
                instance = NasariLexicalModel.loadLemmaFile(vectorFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String[] getVectors(int synset){
        if(!this.synset2vectorOffset.containsKey(synset)){
            return null;
        }
        long position = this.synset2vectorOffset.get(synset);
        int blockIndex = (int) (position / ONE_BLOCK);
        int blockOffset = (int) (position % ONE_BLOCK);

        //soft copy
//        ByteBuffer threadSafeShadowVectors = this.vectorBlocks[blockIndex].asReadOnlyBuffer();
        ByteBuffer threadSafeShadowVectors = this.vectorBlocks.get(blockIndex).asReadOnlyBuffer();
        threadSafeShadowVectors.position(blockOffset);
        int vectorLength = threadSafeShadowVectors.getInt();
        byte[] vectors = new byte[vectorLength];
        threadSafeShadowVectors.get(vectors);

        if(isCompressionEnabled == true){
            try {
                vectors = CompressionUtil.decompress(vectors);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }
        return new String(vectors).split("\t");
    }

    private static boolean isEnoughSpace(int position, int vectorLenght){
        if(position <= ONE_BLOCK * 0.75){return true;}
        int availible = ONE_BLOCK - 1 - position;

        if(availible >= vectorLenght + 4){
            return true;
        }

        return false;
    }

    private static boolean isEnoughSpace(int position, String vectors){
        if(position <= ONE_BLOCK * 0.75){return true;}
        int availible = ONE_BLOCK - 1 - position;

        if(availible >= vectors.getBytes().length + 4){
            return true;
        }

        return false;
    }
}
