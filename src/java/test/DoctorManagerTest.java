/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author maritza
 */
public class DoctorManagerTest {
    
    private JSONObject doctor;
  
    private JSONObject actualSchedule;
    
    private AdapterRest adapter;
     
     
    @BeforeClass
    public void setUpClass(){
        doctor = new JSONObject();
        doctor.put("username", "mario");
        doctor.put("password", "pass");
        doctor.put("firstName", "Mario");
        doctor.put("lastName", "Rodriguez");
        doctor.put("license", "KKD93849432");
        doctor.put("specialty", "odontologia");
        
        actualSchedule = new JSONObject();
        actualSchedule.put("idSchedule", 2938342L);
        actualSchedule.put("monday","10-14");
        actualSchedule.put("tuesday", "10-15");
        actualSchedule.put("thursday","10-17");
        actualSchedule.put("friday", "9-16");
        actualSchedule.put("doctorUsername", doctor);
        
    }
    
    @Test
    public void testSaveNewDoctor() {
      
         String savedDoctor = (String) adapter
				.get("doctormanager/savenewdoctor?doctor="
						+ doctor.toJSONString());

        assertEquals(savedDoctor, doctor.get("username").toString());
        
    }
    
    @Test
    public void testFailSaveNewDoctor() {
      
         String savedDoctor = (String) adapter
				.get("doctormanager/savenewdoctor?doctor="
						+ doctor.toJSONString());

        assertEquals(savedDoctor, null);
        
    }
    
    @Test
    public void testSetSchedule() {
         String idSchedule = (String) adapter
				.get("doctormanager/setschedule?schedule="
						+ actualSchedule.toJSONString());

        assertEquals(idSchedule, actualSchedule.get("idSchedule").toString());
        
    }
    
     
    @Test
    public void testFailSetSchedule() {
         String idSchedule = (String) adapter
				.get("doctormanager/setschedule?schedule="
						+ actualSchedule.toJSONString());

        assertEquals(idSchedule, -1);
        
    }
}
