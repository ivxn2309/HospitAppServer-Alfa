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
import multixsoft.hospitapp.entities.Report;

/**
 *
 * @author Ivan Tovar
 */
@Stateless
@Path("report")
public class ReportFacadeREST extends AbstractFacade<Report> {
    @PersistenceContext(unitName = "HospitAppServerPU")
    private EntityManager em;

    public ReportFacadeREST() {
        super(Report.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Report entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Report entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Report find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Report> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Report> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findbyappointment")
    @Produces({"application/xml", "application/json"})
    public Report findreportByAppointment(@QueryParam("idAppointment") String appointment) {
        Report r = null;
        String sql = "SELECT r FROM Report r WHERE r.idAppointment.idAppointment = :appointment";
        Query query = getEntityManager().createQuery(sql).setParameter("appointment", appointment);
        r = (Report)query.getSingleResult();
        return r;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
