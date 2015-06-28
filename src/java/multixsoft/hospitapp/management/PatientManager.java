package multixsoft.hospitapp.management;

/**
 *
 * @author maritza
 */
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("patientmanager")
public class PatientManager {

    @Context
    private UriInfo context;

    public PatientManager() {
    }

    @GET
    @Path("/savepatient")
    @Produces("text/plain")
    public String saveNewPatient(@QueryParam("patient") String patient) {
        AdapterRest adapter = new AdapterRest();
        try {
            JSONObject patientObj = (JSONObject) new JSONParser().parse(patient);
            String path = "patient/" + patientObj.get("nss");
            JSONObject doctorRequest = (JSONObject) adapter.get(path);
            if (doctorRequest == null || doctorRequest.isEmpty()) {
                path = "patient";
                adapter.post(path, patientObj.toJSONString());
                return patientObj.get("nss").toString();
            } else {
                return null;
            }
        } catch (ParseException e) {
            return null;
        }
    }

    @GET
    @Path("/makepatientinactive")
    @Produces("text/plain")
    public boolean makePatientInactive(@QueryParam("patient") String nss) {
        AdapterRest adapter = new AdapterRest();
        JSONObject patientRequest = new JSONObject();
//            JSONObject patientObj = (JSONObject) new JSONParser().parse(nss);
            String path = "patient/" + nss;
            patientRequest = (JSONObject) adapter.get(path);
            if (patientRequest.isEmpty()) {
                return false;
            }
            patientRequest.put("isActive", "false");

        return adapter.put("patient/" + nss, patientRequest.toJSONString());
    }
}
