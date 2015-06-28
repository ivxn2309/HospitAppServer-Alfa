/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multixsoft.hospitapp.webservice;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Ivan Tovar
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(multixsoft.hospitapp.management.DoctorManager.class);
        resources.add(multixsoft.hospitapp.management.PatientDataRecorder.class);
        resources.add(multixsoft.hospitapp.management.PatientManager.class);
        resources.add(multixsoft.hospitapp.management.PatientMapper.class);
        resources.add(multixsoft.hospitapp.management.ScheduleManager.class);
        resources.add(multixsoft.hospitapp.receiver.PrivacyControl.class);
        resources.add(multixsoft.hospitapp.webservice.AdminFacadeREST.class);
        resources.add(multixsoft.hospitapp.webservice.AppointmentFacadeREST.class);
        resources.add(multixsoft.hospitapp.webservice.DoctorFacadeREST.class);
        resources.add(multixsoft.hospitapp.webservice.PatientFacadeREST.class);
        resources.add(multixsoft.hospitapp.webservice.ReportFacadeREST.class);
        resources.add(multixsoft.hospitapp.webservice.ScheduleFacadeREST.class);
    }
    
}
