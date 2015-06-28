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
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Schedule;
import org.json.simple.JSONObject;

/**
 *
 * @author Ivan Tovar
 */
@Stateless
@Path("doctor")
public class DoctorFacadeREST extends AbstractFacade<Doctor> {
    @PersistenceContext(unitName = "HospitAppServerPU")
    private EntityManager em;

    public DoctorFacadeREST() {
        super(Doctor.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Doctor entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Doctor entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Doctor find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Doctor> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Doctor> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("/doctorschedule")
    @Produces("application/json")
    @Consumes("text/plain")
    public Schedule getDoctorSchedule(@QueryParam("username") String usrn){
        String sql = "SELECT s FROM Schedule s WHERE s.doctorUsername.username = :usrn";
        Query query = getEntityManager().createQuery(sql).setParameter("usrn", usrn);
        Schedule doctorSchedule = (Schedule) query.getSingleResult();
        return doctorSchedule;
    }

    @GET
    @Path("/unfinishedappointments")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<Appointment> getDoctorUnfinishedAppointments(@QueryParam("username") String usrn){
        String sql = "SELECT a FROM Appointment a WHERE a.doctorUsername.username = :usrn AND"
                + " a.isFinished = 0";
        
        Query query = getEntityManager().createQuery(sql).setParameter("usrn", usrn);
        List<Appointment> appointments = query.getResultList();
        return appointments;
    }

    
    
}
