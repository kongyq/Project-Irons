package edu.udel.irl.irons.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/27/18.
 */
public class CompressionUtilTest {
    @Test
    public void compress() throws Exception {
        byte[] input = new String("neighborhood_69.22      neighbourhood_26.45     city_21.21      preindustrial_18.14     mukhtar_14.8    sublevel_13.97  social_12.83    neighbourhood watch_11.55       official_9.59   social control_6.8      urban_6.35      seniors_6.33    face-to-face_5.82       community_5.74  subdistrict_5.52        block parent program_5.46       komshi_5.4      administrative district_5.25    nonwestern_5.24 interaction_5.12        residential_5.11        burough_5.06    resident_5.0    commonwealth english_4.94       committees for the defense of the revolution_4.88\n" +
                "       t’ang_4.78      block parties_4.74      homeowners' association_4.72    residential community_4.7       kiez_4.65       neighborhood association_4.58   administrative divisions of the people's republic of china_4.54 aglandjia_4.45  self-chosen_4.42        district_4.35   mahalle_4.3\n" +
                "     local_4.27      jefferson county, colorado_4.27 neighborhood watch_4.25 meatpacking district_4.24       inchoate_4.03   kingston-upon-thames_4.01       社区_3.93       social unit_3.89\n" +
                "        status_3.83     quartiere_3.8   new urbanism_3.79       historical_3.65 causally_3.64   bairro_3.54     record-keeping_3.5      administrator_3.49      past_3.47       semi-official_3.41\n" +
                "      function_3.31   distinctiveness_3.29    socialise_3.25  sub-divisions_3.2       considerable_3.18       social action_3.1       mumford_3.07    specialisation_3.05     frazione_3.04   spatially_3.04  quarters_2.98   historical document_2.95        regulation_2.95 adversity_2.93  tend_2.92       turkey_2.91     pluralism_2.89  american english_2.88   statistic_2.88  preoccupation_2.88      geographic area_2.87    addition_2.85   congregate_2.8  boundary_2.76   functionally_2.75\n" +
                "       suburbs_2.73    cohesive_2.71   nicosia_2.7     wards_2.7       refer_2.7       cohesion_2.69   rural_2.67      administrative unit_2.65        upkeep_2.64     organization_2.63       direct_2.6      municipal government_2.6        plurality_2.57  localize_2.54   continual_2.54  units_2.54      large_2.52      level_2.49      balkan_2.47     typically_2.46  united kingdom_2.44     term_2.42       barrio_2.39     document_2.37   cypriot_2.37    parish_2.37     cleaning_2.36   differentiation_2.34    council member_2.33     people's republic of china_2.31 informally_2.31 human being_2.29        old english_2.28        geographically_2.27     purpose_2.26    administrative division_2.26    buffer_2.24     acquaintance_2.24       lawn_2.16       terminology_2.1 taxation_2.09   chang_2.07      umbrella_2.06   area_2.06       family_2.05     compensate_2.04 mental health_2.04      generally_2.04  excavate_2.03   exist_2.03      urban area_2.02 migrant_2.02    subdivide_2.01").getBytes();
        System.out.println(input.length);
        System.out.println(CompressionUtil.compress(input).length);

    }

    @Test
    public void decompress() throws Exception {
    }

}