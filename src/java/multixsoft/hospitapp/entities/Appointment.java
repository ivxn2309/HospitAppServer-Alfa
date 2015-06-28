package multixsoft.hospitapp.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ivan Tovar
 */
@Entity
@Table(name = "appointment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a"),
    @NamedQuery(name = "Appointment.findByIdAppointment", query = "SELECT a FROM Appointment a WHERE a.idAppointment = :idAppointment"),
    @NamedQuery(name = "Appointment.findByDate", query = "SELECT a FROM Appointment a WHERE a.date = :date"),
    @NamedQuery(name = "Appointment.findByIsFinished", query = "SELECT a FROM Appointment a WHERE a.isFinished = :isFinished"),
    @NamedQuery(name = "Appointment.findByIscanceled", query = "SELECT a FROM Appointment a WHERE a.iscanceled = :iscanceled")})
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_appointment")
    private Long idAppointment;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "is_finished")
    private Boolean isFinished;
    @Column(name = "iscanceled")
    private Boolean iscanceled;
    @Column(name = "time")
    private String time;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAppointment")
    private Collection<Report> reportCollection;
    @JoinColumn(name = "doctor_username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Doctor doctorUsername;
    @JoinColumn(name = "patient_nss", referencedColumnName = "nss")
    @ManyToOne(optional = false)
    private Patient patientNss;

    public Appointment() {
    }

    public Appointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Boolean getIscanceled() {
        return iscanceled;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIscanceled(Boolean iscanceled) {
        this.iscanceled = iscanceled;
    }

    @XmlTransient
    public Collection<Report> getReportCollection() {
        return reportCollection;
    }

    public void setReportCollection(Collection<Report> reportCollection) {
        this.reportCollection = reportCollection;
    }

    public Doctor getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(Doctor doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public Patient getPatientNss() {
        return patientNss;
    }

    public void setPatientNss(Patient patientNss) {
        this.patientNss = patientNss;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAppointment != null ? idAppointment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) object;
        if ((this.idAppointment == null && other.idAppointment != null) || (this.idAppointment != null && !this.idAppointment.equals(other.idAppointment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "multixsoft.hospitapp.entities.Appointment[ idAppointment=" + idAppointment + " ]";
    }
    
}
