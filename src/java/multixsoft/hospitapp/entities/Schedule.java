/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ivan Tovar
 */
@Entity
@Table(name = "schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM Schedule s"),
    @NamedQuery(name = "Schedule.findByIdSchedule", query = "SELECT s FROM Schedule s WHERE s.idSchedule = :idSchedule"),
    @NamedQuery(name = "Schedule.findByMonday", query = "SELECT s FROM Schedule s WHERE s.monday = :monday"),
    @NamedQuery(name = "Schedule.findByTuesday", query = "SELECT s FROM Schedule s WHERE s.tuesday = :tuesday"),
    @NamedQuery(name = "Schedule.findByWednesday", query = "SELECT s FROM Schedule s WHERE s.wednesday = :wednesday"),
    @NamedQuery(name = "Schedule.findByThursday", query = "SELECT s FROM Schedule s WHERE s.thursday = :thursday"),
    @NamedQuery(name = "Schedule.findByFriday", query = "SELECT s FROM Schedule s WHERE s.friday = :friday")})
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_schedule")
    private Long idSchedule;
    @Size(max = 150)
    @Column(name = "monday")
    private String monday;
    @Size(max = 150)
    @Column(name = "tuesday")
    private String tuesday;
    @Size(max = 150)
    @Column(name = "wednesday")
    private String wednesday;
    @Size(max = 150)
    @Column(name = "thursday")
    private String thursday;
    @Size(max = 150)
    @Column(name = "friday")
    private String friday;
    @JoinColumn(name = "doctor_username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Doctor doctorUsername;

    public Schedule() {
    }

    public Schedule(Long idSchedule) {
        this.idSchedule = idSchedule;
    }

    public Long getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(Long idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public Doctor getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(Doctor doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSchedule != null ? idSchedule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) object;
        if ((this.idSchedule == null && other.idSchedule != null) || (this.idSchedule != null && !this.idSchedule.equals(other.idSchedule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "multixsoft.hospitapp.entities.Schedule[ idSchedule=" + idSchedule + " ]";
    }
    
}
