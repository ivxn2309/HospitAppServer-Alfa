package multixsoft.hospitapp.management;

import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.entities.Report;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author YONY
 */
@Path("patientdatarecorder")
public class PatientDataRecorder {

    @Context
    private UriInfo context;
    private AdapterRest adapter = new AdapterRest();

    /**
     * Creates a new instance of PatientDataRecorder
     */
    public PatientDataRecorder() {
    }

    /**
     * Este metodo se encarga de guardar un nuevo reporte
     *
     * @param report conrresponde a la variable de tipo reporte que contiene al
     * reporte
     * @return una variable de tipo booleana que indica si se pudo realizar la
     * acción
     */
    @GET
    @Path("/savereport")
    @Produces("text/plain")
    public boolean saveNewReport(@QueryParam("report") String report) {
        String path = "report";
        System.out.println("report: " + report);
        return adapter.post(path, report);
    }

    /**
     * Este metodo obtiene un reporte específico a partir de una cita
     *
     * @param idAppointment corresponde al id de la cita de la cual se requiere
     * el reporte
     * @return un String que contiene al reporte asociado a la cita indicada
     */
    @GET
    @Path("/historyfromappointment")
    @Produces("application/json")
    public String obtainHistoryFromAppointment(@QueryParam("appointment") String idAppointment) {
        String path = "report/findbyappointment?idAppointment" + idAppointment;
        JSONObject report = (JSONObject) adapter.get(path);
        return report.toJSONString();
    }

    /**
     * Este metodo se encarga de obtener el total de reportes guardados y
     * sumarle uno. No requiere parámetros.
     *
     * @return un String que contiene el numero de reportes más uno
     */
    @GET
    @Path("/idreportplusone")
    @Produces("text/plain")
    public String getIdReportPlusOne() {
        String path = "report/count";
        if (adapter.get(path) == null) {
            //El primer reporte
            return "1";
        }
        Long idReport = (Long) adapter.get(path);
        idReport += 1L;
        return idReport.toString();
    }

}
