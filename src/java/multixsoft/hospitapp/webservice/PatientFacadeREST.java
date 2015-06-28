package multixsoft.hospitapp.webservice;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.entities.Patient;

/**
 *
 * @author Ivan Tovar
 */
@Stateless
@Path("patient")
public class PatientFacadeREST extends AbstractFacade<Patient> {
    @PersistenceContext(unitName = "HospitAppServerPU")
    private EntityManager em;

    public PatientFacadeREST() {
        super(Patient.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Patient entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Patient entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Patient find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Patient> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Patient> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("/unfinishedappointments")
    @Produces("application/json")
    public List<Appointment> getUnfinishedAppointments(@QueryParam("nss") String nss){
        String sql = "SELECT a FROM Appointment a WHERE a.patientNss.nss=:nss AND"
                + " a.isFinished=false";
        Query query = getEntityManager().createQuery(sql).setParameter("nss", nss);

        List<Appointment> appointments = query.getResultList();
        return appointments;
    }
    /**
     * Este metodo se encarga de obtener todos los pacientes de un doctor en especifico
     * @param usrn corresponde al nombre del doctor del cual se consultaran los pacientes
     * @return una lista de tipo Patient que ocntiene todos los pacientes de ese doctor
     */
    @GET
    @Path("/patientsof")
    @Produces("application/json")
    public List<Patient> getAllAppointmentsFor(
            @QueryParam("username") String usrn) {
        String sql = "SELECT p FROM Patient p WHERE p.doctorUsername.username = :usrn";
        Query query = getEntityManager().createQuery(sql).setParameter("usrn", usrn);
        List<Patient> apps = query.getResultList();
        return apps;
    }
}
