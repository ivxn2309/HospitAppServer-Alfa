package multixsoft.hospitapp.management;

import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.utilities.IntervalFilter;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * REST Web Service
 *
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
@Path("schedulemanager")
public class ScheduleManager {

    private AdapterRest adapter;
    private final String APPOINTMENT_PATH = "appointment/";
    private final String PATIENT_PATH = "patient/";
    private final String DOCTOR_PATH = "doctor/";
    private final String DOCTOR_UNFINISHED_APPS = "doctor/unfinishedappointments?username=";
    private final String PATIENT_UNIFINISHED_APPS = "patient/unfinishedappointments?nss=";
    private final String DOCTOR_SCHEDULE_PATH = "doctor/doctorschedule?username=";

    @Context
    private UriInfo context;

    public ScheduleManager() {
        adapter = new AdapterRest();
    }

    @GET
    @Path("/appointmentsfor")
    @Produces("application/json")
    public String getAllAppointmentsFor(
            @QueryParam("username") String usrn, @QueryParam("date") String date) {
        AdapterRest adapter = new AdapterRest();
        String path = "appointment/appointmentsfor?username=" + usrn + "&date=" + date;
        JSONArray array = (JSONArray) adapter.get(path);
        if (array.isEmpty()) {
            return null;
        }
        return array.toJSONString();
    }

    @GET
    @Path("/appointmentsdoctor")
    @Produces("application/json")
    public String getAllAppointmentsFor(@QueryParam("username") String usrn) {
        AdapterRest adapter = new AdapterRest();
        String path = "appointment/appointmentsdoctor?username=" + usrn;
        JSONArray array = (JSONArray) adapter.get(path);
        if (array.isEmpty()) {
            return null;
        }
        return array.toJSONString();
    }

    @GET
    @Path("/cancelappointment")
    @Produces("text/plain")
    public boolean cancelAppointment(@QueryParam("idAppointment") String id) {
        JSONObject appointment = getAppointmentFromId(id);
        if (appointment.isEmpty()) {
            return false;
        }
        appointment.put("iscanceled", false);
        adapter.put("appointment/" + id, appointment.toJSONString());
        appointment.put("iscanceled", true);
        return adapter.put("appointment/" + id, appointment.toJSONString());
    }

    @GET
    @Path("/finishappointment")
    @Produces("text/plain")
    public boolean setAppointmentFinish(@QueryParam("idAppointment") String id) {
        JSONObject appointment = getAppointmentFromId(id);
        if (appointment.isEmpty()) {
            return false;
        }
        appointment.put("isFinished", false);
        adapter.put("appointment/" + id, appointment.toJSONString());
        appointment.put("isFinished", true);
        return adapter.put("appointment/" + id, appointment.toJSONString());
    }

    @GET
    @Path("/scheduleappointment")
    @Produces("text/plain")
    public long scheduleAppointment(@QueryParam("Appointment") String appointment) {
        JSONObject appointmentToSchedule = (JSONObject) JSONValue.parse(appointment);
        System.out.println("appjson:" + appointmentToSchedule.toJSONString());
        if (isAppointmentValid(appointmentToSchedule) == false) {
            return -1;
        } else {
            if (adapter.post("appointment", appointmentToSchedule.toJSONString())) {
                System.out.println("scheduleJson: " + appointmentToSchedule.toJSONString());
                return (Long) appointmentToSchedule.get("idAppointment");
            }
        }
        return -1;
    }

    @GET
    @Path("/nextappointment")
    @Produces("application/json")
    public String getNextAppointment(@QueryParam("nss") String nss) {
        JSONObject patient = getPatientFromNss(nss);
        JSONArray patientAppointments = getPatientAppointments(nss);

        if (patientAppointments.isEmpty()) {
            return null;
        }
        JSONArray nextAppointment = compareAppointmentsDate(patientAppointments);
        return nextAppointment.toJSONString();
    }

    /**
     * Obtiene los horarios disponibles de un doctor en específico 
     * en una fecha determinada, o bien el horario original si la 
     * bandera lo indica.
     * @param usr Cadena con el nombre de usuario del doctor
     * @param original Bandera para indicar si se espera el horario original o alterado por las citas
     * @param date La fecha en la que se desea verificar el horario
     * @return Un objeto de tipo Schedule 
     */
    @GET
    @Path("/availableschedule")
    @Produces("application/json")
    public String getAvailableSchedule(@QueryParam("username") String usr,
            @QueryParam("original") boolean original, @QueryParam("date") String date) {
        JSONObject doctor = getDoctorFromUsername(usr);
        
        //Doctor no válido
        if (doctor.isEmpty()) {
            return null;
        }
        
        //Horario sin alterar
        JSONObject doctorSchedule = getDoctorSchedule(usr);
        if (original) {
            return doctorSchedule.toJSONString();
        }

        //Todas las citas del médico
        JSONArray doctorAppointments = getDoctorAppointments(usr);
        IntervalFilter intervalFilter = new IntervalFilter();
        for (Object appointment : doctorAppointments) {
            //Verificación por canceladas y finalizadas
            boolean isCanceled = (Boolean)((JSONObject)appointment).get("iscanceled");
            if(isCanceled) continue;
            boolean isFinished = (Boolean)((JSONObject)appointment).get("isFinished");
            if(isFinished) continue;
            
            //Verificación de fecha
            Date today = new Date(date);
            Date appDate = getAppointmentDate((JSONObject) appointment);
            if (!today.equals(appDate)) {
                continue;
            }
            
            //Remoción de fecha
            int time = Integer.parseInt((String) ((JSONObject) appointment).get("time"));
            int day = appDate.getDayOfWeek();
            String scheduleTime = scheduleIntervalByDay(((Long) doctorSchedule.get("idSchedule")).toString(), day);
            String newInterval = intervalFilter.removeInterval(time, scheduleTime);
            doctorSchedule = putScheduleByDay(day, newInterval, doctorSchedule);
        }
        return doctorSchedule.toJSONString();
    }

    @GET
    @Path("/availableschedule2")
    @Produces("application/json")
    public String getAvailableSchedule(@QueryParam("username") String usr, @QueryParam("original") boolean original) {
        System.out.println("Tracking Debug 4");
        JSONObject doctor = getDoctorFromUsername(usr);
        if (doctor.isEmpty()) {
            return null;
        }

        JSONObject doctorSchedule = getDoctorSchedule(usr);
        if (original) {
            return doctorSchedule.toJSONString();
        }

        JSONArray doctorAppointments = getDoctorAppointments(usr);

        IntervalFilter intervalFilter = new IntervalFilter();

        for (Object appointment : doctorAppointments) {
            Date appDate = getAppointmentDate((JSONObject) appointment);
            int time = (Integer) ((JSONObject) appointment).get("time");

            if (appDate.belongsThisWeek()) {
                int day = appDate.getDayOfWeek();
                int appointmentTime = Integer.parseInt((String) ((JSONObject) appointment).get("time"));
                String scheduleTime = scheduleIntervalByDay(((Long) doctorSchedule.get("idSchedule")).toString(), day);
                String newInterval = intervalFilter.removeInterval(appointmentTime, scheduleTime);
                doctorSchedule = putScheduleByDay(day, newInterval, doctorSchedule);
            }
        }
        return doctorSchedule.toJSONString();
    }

    @GET
    @Path("/notificatechange")
    @Produces("text/plain")
    public boolean notificateChange(@QueryParam("appointment") String appointment) {
        //JSONObject obj = ((JSONObject)JSONValue.parse(appointment));
        System.out.println("App: " + appointment);
        JSONObject app = (JSONObject) adapter.get("appointment/" + appointment);//<--
        System.out.println("JSONApp: " + app.toJSONString());
        String path = "patient/" + (String) ((JSONObject) app.get("patientNss")).get("nss");
        System.out.println("Path: " + path);
        JSONObject pat = (JSONObject) adapter.get(path);
        System.out.println("JSONPat: " + pat.toJSONString());
        //TODO
        String email = "esparzamaritza854@gmail.com";
        String pass = "linux125";
        String estatus = ((Boolean) app.get("iscanceled")) ? "Su cita fue cancelada" : "Su cita está activa";
        String recipient = (String) (pat.get("address"));
        String txt = "Estimado derechohabiente del Insituto Mexicano del Seguro Social (IMSS), nos permitimos informarle por este medio que su cita fue actualizada a solicitud de nuestro personal.";
        txt += "\nEstos son los datos de su cita:\n";
        txt += "Folio: " + app.get("idAppointment") + "\n";
        txt += "Paciente: " + pat.get("firstName") + " " + pat.get("lastName") + "\n";
        txt += "E-mail: " + pat.get("address") + "\n";
        txt += "Fecha: " + getAppointmentDate(app) + "\n";
        txt += "Hora: " + app.get("time") + ":00 hrs" + "\n";
        txt += "Estatus: " + estatus + "\n";
        txt += "\n\n IMSS";

        System.out.println("Txt: " + txt);

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = email;
        final String password = pass;
        try {
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));

            msg.setSubject("Notificación de cambio en cita médica");
            msg.setText(txt);
            msg.setSentDate(new java.util.Date());
            Transport.send(msg);
            //System.out.println("Message sent.");
        } catch (MessagingException e) {
            System.err.println("Error: " + e);
            return false;
        }
        return true;
    }

    @GET
    @Path("/updatecita")
    @Produces("text/plain")
    public boolean updateAppointment(@QueryParam("appointment") String appointment) {
        JSONObject obj = (JSONObject) JSONValue.parse(appointment);
        obj.put("date", getAppointmentDate(obj).toFormattedString("YMD"));
        String path = "appointment/" + obj.get("idAppointment");
        System.out.println("path: " + path);
        System.out.println("json: " + obj.toJSONString().replace("\\", ""));
        return new AdapterRest().put(path, obj.toJSONString().replace("\\", ""));
    }

    private JSONArray compareAppointmentsDate(JSONArray appointments) {
        JSONArray nextAppointments = new JSONArray();
        Date actualApp = new Date();

        for (int i = 0; i < appointments.size(); i++) {
            Date nextAppDate = getAppointmentDate((JSONObject) appointments.get(i));
            if (nextAppDate.isAfter(actualApp) || nextAppDate.equals(actualApp)) {
                nextAppointments.add(appointments.get(i));
            }
        }
        return nextAppointments;
    }

    private JSONObject putScheduleByDay(int day, String interval, JSONObject doctorSchedule) {
        if (day == 2 && doctorSchedule.get("monday") != null) {
            doctorSchedule.put("monday", interval);
        } else if (day == 3 && doctorSchedule.get("tuesday") != null) {
            doctorSchedule.put("tuesday", interval);
        } else if (day == 4 && doctorSchedule.get("wednesday") != null) {
            doctorSchedule.put("wednesday", interval);
        } else if (day == 5 && doctorSchedule.get("thursday") != null) {
            doctorSchedule.put("thursday", interval);
        } else if (day == 6 && doctorSchedule.get("friday") != null) {
            doctorSchedule.put("friday", interval);
        }
        return (JSONObject) doctorSchedule;
    }

    private String scheduleIntervalByDay(String idSchedule, int day) {
        JSONObject schedule = getSchedule(idSchedule);
        if (day == 2) {
            return schedule.get("monday").toString();
        } else if (day == 3) {
            return schedule.get("tuesday").toString();
        } else if (day == 4) {
            return schedule.get("wednesday").toString();
        } else if (day == 5) {
            return schedule.get("thursday").toString();
        } else if (day == 6) {
            return schedule.get("friday").toString();
        }
        return null;
    }

    private boolean isAppointmentValid(JSONObject appointment) {
        Date actualDate = new Date();
        Date appointmentDate = getAppointmentDate(appointment);
        String idAppointment = appointment.get("idAppointment").toString();
        boolean appointmentAlreadyExists = comparePatientAndDate(appointment);
        if ((!patientDoctorExists(appointment))
                || appointmentDate.isBefore(actualDate)
                || appointmentAlreadyExists) {
            return false;
        } else {
            return true;
        }
    }

    private boolean patientDoctorExists(JSONObject appointment) {
        JSONObject doctor = (JSONObject) appointment.get("doctorUsername");
        JSONObject patient = (JSONObject) appointment.get("patientNss");
        return !(doctor.isEmpty() || patient.isEmpty());
    }

    private JSONObject getAppointmentFromId(String id) {
        return (JSONObject) adapter.get(APPOINTMENT_PATH + id);
    }

    private Date getAppointmentDate(JSONObject appointment) {
        Calendar calendar = Calendar.getInstance();
        String[] strDate = ((String) appointment.get("date")).split("-");
        int year = Integer.parseInt(strDate[0]);
        int mes = Integer.parseInt(strDate[1]);
        int dia = Integer.parseInt(strDate[2].substring(0, 2));
        return new Date(dia, mes, year);
    }

    private JSONObject getPatientFromNss(String nss) {
        return (JSONObject) adapter.get(PATIENT_PATH + nss);
    }

    private JSONObject getDoctorFromUsername(String usr) {
        return (JSONObject) adapter.get(DOCTOR_PATH + usr);
    }

    private JSONArray getPatientAppointments(String nss) {
        return (JSONArray) adapter.get(PATIENT_UNIFINISHED_APPS + nss);
    }

    private JSONObject getDoctorSchedule(String usr) {
        return (JSONObject) adapter.get(DOCTOR_SCHEDULE_PATH + usr);
    }

    private JSONObject getSchedule(String idSchedule) {
        return (JSONObject) adapter.get("schedule/" + idSchedule);
    }

    private JSONArray getDoctorAppointments(String usr) {
        return (JSONArray) adapter.get(DOCTOR_UNFINISHED_APPS + usr);
    }

    /* compara si dos appointments tienen el mismo paciente y fecha */
    private boolean comparePatientAndDate(JSONObject appointment) {
        if (appointment == null) {
            return false;
        }
        JSONArray appointments = (JSONArray) adapter.get("appointment");
        for (Object app : appointments) {
            if (((JSONObject) app).get("patientNss").equals(appointment.get("patientNss"))
                    && ((JSONObject) app).get("date").equals(appointment.get("date"))) {
                if (Boolean.valueOf(((JSONObject) app).get("iscanceled").toString()) == true) {
                    return false;
                }

                return true;
            }
        }
        return false;
    }

}
