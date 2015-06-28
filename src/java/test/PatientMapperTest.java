
package test;

import java.util.List;
import org.junit.*;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
/**
 *
 * @author maritza
 */
public class PatientMapperTest {
    private JSONObject actualDoctor;
    private JSONObject expectedDoctor;
    private JSONObject patient;
    
    private AdapterRest adapter;
    
    @BeforeClass
    public void setUpClass(){
        adapter = new AdapterRest();
        
        actualDoctor = new JSONObject();
        actualDoctor.put("username", "mario");
        actualDoctor.put("password", "pass");
        actualDoctor.put("firstName", "Mario");
        actualDoctor.put("lastName", "Rodriguez");
        actualDoctor.put("license", "KKD93849432");
        actualDoctor.put("specialty", "odontologia");
        
        patient = new JSONObject();
        patient.put("nss", "123456789");
        patient.put("firstName","Juan");
        patient.put("lastName","Perez");
        patient.put("password", "password");
        patient.put("address","Adolfo Lopez Mateos");
        patient.put("isActive", true);
        patient.put("doctorUsername", actualDoctor.toJSONString());
        
        expectedDoctor = new JSONObject();
        expectedDoctor.put("username", "jose");
        expectedDoctor.put("password", "pass");
        expectedDoctor.put("firstName", "Jose");
        expectedDoctor.put("lastName", "Dominguez");
        expectedDoctor.put("license", "34O34KDSFJ");
        expectedDoctor.put("specialty", "nutricion");
        
        
        adapter.post("doctor", expectedDoctor.toJSONString());
        adapter.post("doctor", actualDoctor.toJSONString());
        adapter.post("patient", patient.toJSONString());
    }
    
    @Test
    public void testMapPatient() {
         boolean isMapped = (Boolean) adapter
				.get("patientmapper/mappatient?nss="
						+ patient.get("nss")
                                +"&username="+expectedDoctor.get("username"));

        assertEquals(isMapped, true);
        
    }
    
    @AfterClass
     public void tearDownClass() {
        adapter.delete("patient/"+(String)patient.get("nss"));
        adapter.delete("doctor/"+(String)actualDoctor.get("username"));
        adapter.delete("doctor/"+(String)expectedDoctor.get("username"));
     }
}
