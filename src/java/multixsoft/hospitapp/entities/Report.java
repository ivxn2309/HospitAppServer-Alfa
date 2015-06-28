package multixsoft.hospitapp.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan Tovar
 */
@Entity
@Table(name = "report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r"),
    @NamedQuery(name = "Report.findByIdReport", query = "SELECT r FROM Report r WHERE r.idReport = :idReport"),
    @NamedQuery(name = "Report.findByDescription", query = "SELECT r FROM Report r WHERE r.description = :description"),
    @NamedQuery(name = "Report.findByMedicine", query = "SELECT r FROM Report r WHERE r.medicine = :medicine"),
    @NamedQuery(name = "Report.findByIndications", query = "SELECT r FROM Report r WHERE r.indications = :indications"),
    @NamedQuery(name = "Report.findByPatientNss", query = "SELECT r FROM Report r WHERE r.patientNss = :patientNss")})
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_report")
    private Long idReport;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @Size(max = 200)
    @Column(name = "medicine")
    private String medicine;
    @Size(max = 200)
    @Column(name = "indications")
    private String indications;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "patient_nss")
    private Patient patientNss;
    @JoinColumn(name = "id_appointment", referencedColumnName = "id_appointment")
    @ManyToOne(optional = false)
    private Appointment idAppointment;

    public Report() {
    }

    public Report(Long idReport) {
        this.idReport = idReport;
    }

    public Report(Long idReport, Patient patientNss) {
        this.idReport = idReport;
        this.patientNss = patientNss;
    }

    public Long getIdReport() {
        return idReport;
    }

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public Patient getPatientNss() {
        return patientNss;
    }

    public void setPatientNss(Patient patientNss) {
        this.patientNss = patientNss;
    }

    public Appointment getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Appointment idAppointment) {
        this.idAppointment = idAppointment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReport != null ? idReport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.idReport == null && other.idReport != null) || (this.idReport != null && !this.idReport.equals(other.idReport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "multixsoft.hospitapp.entities.Report[ idReport=" + idReport + " ]";
    }
    
}
