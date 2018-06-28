package edu.udel.irl.irons.nasari;

import com.google.common.base.Stopwatch;
import edu.udel.irl.irons.IronsConfiguration;
import it.unimi.dsi.fastutil.ints.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Created by mike on 6/19/18.
 */
public class NasariModel {

    private static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    private static final long ONE_GB = 1024 * 1024 * 1024;  // = 2^30

    private static NasariModel instance = null;

    private final Int2IntMap synset2vectorOffset;
    private final IntBuffer vectors;

    private NasariModel(Int2IntMap synset2vectorOffset, IntBuffer vectors){
        this.synset2vectorOffset = Int2IntMaps.unmodifiable(synset2vectorOffset);
        this.vectors = vectors;
    }

    public static NasariModel loadVectorFile(File vectorFile) throws IOException {

        Stopwatch stopwatch = Stopwatch.createStarted();

//        int count = 1;

        //get number of lines(synset) and tabs
        Stopwatch stopwatch2 = Stopwatch.createStarted();

        System.out.println("Analyzing the vector file...");
        BufferedReader reader = Files.newBufferedReader(vectorFile.toPath());
        String line;
        int tabCount = 0;
        int lineCount = 0;
        while((line = reader.readLine()) != null){
            tabCount += countTabs(line);
            ++lineCount ;
        }
        reader.close();
        stopwatch2.stop();

        //initial synset2vector offset map.
        Int2IntMap synset2vectorOffset = new Int2IntOpenHashMap(lineCount);

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

        int bufferIndex = 0;
        int vectorLength;

        //allocate memory for vector space.
        IntBuffer vectors = ByteBuffer.allocateDirect(tabCount * 4).asIntBuffer();

        StringBuilder sb = new StringBuilder();
        while(buffer.hasRemaining()){

            sb.setLength(0);
            char c = (char) buffer.get();

            //read a line
            while(c != '\n'){
                sb.append(c);
                c = (char)buffer.get();
            }

            line = sb.toString();
            String[] lineSplited = line.split("\t");

            int synset = Integer.parseInt(lineSplited[0].substring(3,11));

            int synsetVectorStartPosition = bufferIndex;

            vectorLength = lineSplited.length - 2;
            vectors.put(vectorLength);
            bufferIndex ++;
            for(int i = 2; i < lineSplited.length; i++){
                vectors.put(Integer.parseInt(lineSplited[i].substring(3,11)));

                bufferIndex ++;
            }

            synset2vectorOffset.put(synset, synsetVectorStartPosition);

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
//            if(count % 10000 == 0){
//                System.out.println(count);
//            }
//            count += 1;

        }
        stopwatch.stop();
        System.out.println("Vector file successfully loaded! cost " + stopwatch);

        return new NasariModel(synset2vectorOffset, vectors);
    }




    @Deprecated
    private static int Charseq2Int(CharSequence input){
        CharBuffer buffer = CharBuffer.wrap(input);
        StringBuilder sb = new StringBuilder();
        while (buffer.hasRemaining()){
            char chr = buffer.get();
            if(chr > 47 && chr < 58){
                sb.append(chr);
            }
        }
        return Integer.parseInt(sb.toString());
    }

    public static int countTabs(String str) {
        int count = 0;
        for(int idx = 0; (idx = str.indexOf("\t", idx)) != -1; ++idx) {
            ++count;
        }
        return count;
    }

    //use soft copy to make sure the method is thread safe.
    public IntArrayList getVectors(int synset){
        if(!this.synset2vectorOffset.containsKey(synset)){
            return null;
        }
        int position = this.synset2vectorOffset.get(synset);

        //soft copy
        IntBuffer threadSafeShadowVectors = this.vectors.asReadOnlyBuffer();
        threadSafeShadowVectors.position(position);
        int length = threadSafeShadowVectors.get();
        int[] vectors = new int[length];
        threadSafeShadowVectors.get(vectors);

        return new IntArrayList(vectors);
    }

    public static synchronized NasariModel getInstance(File vectorFile){
        if (instance == null){
            try {
                instance = NasariModel.loadVectorFile(vectorFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }



}
