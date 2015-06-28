package multixsoft.hospitapp.management;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.utilities.IntervalFilter;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * REST Web Service
 * @author Yonathan Martínez
 * @version 1.0
 * 
 */
@Path("doctormanager")
public class DoctorManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DoctorManager
     */
    public DoctorManager() {
    }
    
    /**
     * POST para crear una instancia de Doctor Manager
     * @param doc corresponde a la representacion del Doctor Manager que se creará
     * @return un String que contiene el nombre de usuario del doctor que se ha creado
    **/
    @GET
    @Path("/savenewdoctor")
    @Produces("text/plain")
    public String postSaveNewDoctor(
        @QueryParam("doctor") String doc){
        
        AdapterRest adapter = new AdapterRest();
        try{
               JSONObject doctorObject = (JSONObject)new JSONParser().parse(doc);
               String path = "doctor/" + doctorObject.get("username");
               JSONObject doctorRequest = (JSONObject) adapter.get(path);
               if(doctorRequest==null || doctorRequest.isEmpty()){
                    path = "doctor";
                    adapter.post(path,doctorObject.toJSONString());
                    return doctorObject.get("username").toString();
        	} else{
                    return null;
        	}
               
    	}catch(org.json.simple.parser.ParseException io){
    		return null;
    	}
    }
    
    /**
    * POST metodo para crear una instancia de Schedule Manager
    * @param sched es la representacion del Schedule Manager que se creará
    * @return Un String que contiene el id del Schedule Manager que se creó
    **/
    @GET
    @Path("/setschedule")
    @Produces("text/plain")
    public String putSetSchedule(
        @QueryParam("schedule") String sched){
        AdapterRest adapter = new AdapterRest();
        try{
                
        	JSONObject scheduleObject = (JSONObject)new JSONParser().parse(sched);
                scheduleObject = parseInterval(scheduleObject);
                String path = "schedule/" + scheduleObject.get("idSchedule");
        	JSONObject scheduleRequest = (JSONObject) adapter.get(path);
                
        	if(scheduleRequest == null || scheduleRequest.isEmpty()){
                    path = "schedule";
                    adapter.post(path, scheduleObject.toJSONString());
                    return scheduleObject.get("idSchedule").toString();
        	}else{
                    path = "schedule/" + scheduleObject.get("idSchedule");
                    adapter.put(path, scheduleObject.toJSONString());
                    return scheduleObject.get("idSchedule").toString();
        	}
        }catch(org.json.simple.parser.ParseException e){
             return null;
        }
    }
    
    private JSONObject parseInterval(JSONObject obj) {
        IntervalFilter filter = new IntervalFilter();
        String monday = filter.getIntervalFromHours((String) obj.get("monday"));
        String tuesday = filter.getIntervalFromHours((String) obj.get("tuesday"));
        String wednesday = filter.getIntervalFromHours((String) obj.get("wednesday"));
        String thursday = filter.getIntervalFromHours((String) obj.get("thursday"));
        String friday = filter.getIntervalFromHours((String) obj.get("friday"));
        
        obj.put("monday", monday);
        obj.put("tuesday", tuesday);
        obj.put("wednesday", wednesday);
        obj.put("thursday", thursday);
        obj.put("friday", friday);
        return obj;
    }
    
    /**
     * Este metodo se encarga de obtener todos los pacientes de un doctor del lado
     * del servidor
     * @param usrn corresponde al doctor del cual se requieren los pacientes
     * @return una variable de tipo String con todos los pacientes de ese doctor
     */
    @GET
    @Path("/patientsfor")
    @Produces("application/json")
    public String getAllPatientsFor(@QueryParam("username") String usrn) {
        AdapterRest adapter = new AdapterRest();
        String path = "patient/patientsof?username=" + usrn;
        JSONArray array = (JSONArray)adapter.get(path);
        if (array.isEmpty()) {
            return null;
        }
        return array.toJSONString();
    }

    /**
     * Este metodo se encarga de obtener todos los pacientes de un doctor del lado
     * del servidor
     * @param idschedule corresponde al doctor del cual se requieren los pacientes
     * @return una variable de tipo String con todos los pacientes de ese doctor
     */
    @GET
    @Path("/removescheduleofdoctor")
    @Produces("text/plain")
    public boolean removeScheduleOfDoctor(@QueryParam("idschedule") String idschedule) {
        AdapterRest adapter = new AdapterRest();
        String path = "schedule/id=" + idschedule;
        if(adapter.delete(path)){
            return true;
        }
        return false;
    }

    /**
     * Este metodo modifica a false la variable isActive de un doctor
     * @param username corresponde al nombre de usuario del doctor que se desea 
     * modificar
     * @return una variable de tipo booleano que indica si se pudo modificar o 
     * no
     */
    @GET
    @Path("/makedoctorinactive")
    @Produces("text/plain")
    public boolean makeDoctorInactive(@QueryParam("doctor") String username) {
        AdapterRest adapter = new AdapterRest();
        JSONObject doctor = (JSONObject) adapter.get("doctor/" + username);
        if(doctor.isEmpty()){
            return false;
        }
        doctor.put("isActive", "false");
        return adapter.put("doctor/" + username, doctor.toJSONString());
    }
}