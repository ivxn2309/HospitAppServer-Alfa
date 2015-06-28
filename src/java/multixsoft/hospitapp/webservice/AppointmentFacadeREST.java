/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multixsoft.hospitapp.webservice;

import java.util.ArrayList;
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
import multixsoft.hospitapp.utilities.Date;

/**
 *
 * @author Ivan Tovar
 */
@Stateless
@Path("appointment")
public class AppointmentFacadeREST extends AbstractFacade<Appointment> {
    @PersistenceContext(unitName = "HospitAppServerPU")
    private EntityManager em;

    public AppointmentFacadeREST() {
        super(Appointment.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Appointment entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Appointment entity) {
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
    public Appointment find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Appointment> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Appointment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("/appointmentsfor")
    @Produces("application/json")
    public List<Appointment> getAllAppointmentsFor(
            @QueryParam("username") String usrn, @QueryParam("date") String date) {
        int dia = Integer.parseInt(date.split("/")[0]);
        int mes = Integer.parseInt(date.split("/")[1]);
        int year = Integer.parseInt(date.split("/")[2]);
        Date fecha = new Date(dia, mes, year);
        String sql = "SELECT a FROM Appointment a WHERE a.doctorUsername.username = :usrn AND a.date = :fecha";
        Query query = getEntityManager().createQuery(sql).setParameter("usrn", usrn).setParameter("fecha", fecha.getTime());
        List<Appointment> apps = query.getResultList();
        return apps;
    }
    
    @GET
    @Path("/appointmentsdoctor")
    @Produces("application/json")
    public List<Appointment> getAllAppointmentsFor(@QueryParam("username") String usrn) {
        //String sql = "SELECT a FROM Appointment a WHERE a.doctorUsername.username = :usrn";
        //Query query = getEntityManager().createQuery(sql).setParameter("usrn", usrn);
        //List<Appointment> apps = query.getResultList();
        List<Appointment> all = this.findAll();
        List<Appointment> apps = new ArrayList<>();
        for(Appointment a : all){
            if(a.getDoctorUsername().getUsername().equals(usrn)) {
                apps.add(a);
            }
        }
        
        
        return apps;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
