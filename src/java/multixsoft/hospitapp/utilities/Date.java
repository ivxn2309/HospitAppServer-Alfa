package multixsoft.hospitapp.utilities;

import java.util.Calendar;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
public class Date {
    private int dia;
    private int mes;
    private int year;
    private Calendar calendar;

    public Date() {
        this.calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DATE);
        mes = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        this.calendar = calendar;
    }
    
    public Date(Calendar calendar) {
        dia = calendar.get(Calendar.DATE);
        mes = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        this.calendar = calendar;
    }
    
    public Date(String date) {
        String [] strD = date.split("[/-]");
        this.dia = Integer.parseInt(strD[0]);
        this.mes = Integer.parseInt(strD[1]);
        this.year = Integer.parseInt(strD[2]);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, dia);
        calendar.set(Calendar.MONTH, mes-1);
        calendar.set(Calendar.YEAR, year);
    }

    public Date(int dia, int mes, int year) {
        this.dia = dia;
        this.mes = mes;
        this.year = year;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, dia);
        calendar.set(Calendar.MONTH, mes-1);
        calendar.set(Calendar.YEAR, year);
    }

    public boolean isBefore(Date date) {
        if (year != date.getYear()) {
            return year < date.getYear();
        }

        if (mes != date.getMes()) {
            return mes < date.getMes();
        }

        if (dia != date.getDia()) {
            return dia < date.getDia();
        }

        return false;
    }

    public boolean isAfter(Date date) {
        if (year != date.getYear()) {
            return year > date.getYear();
        }

        if (mes != date.getMes()) {
            return mes > date.getMes();
        }

        if (dia != date.getDia()) {
            return dia > date.getDia();
        }

        return false;
    }

    private String getSpanishMonth(int indexMonth) {
        switch (indexMonth) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
        }
        return null;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public java.util.Date getTime() {
        return calendar.getTime();
    }
    
    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public String toString() {
        return dia + "/" + getSpanishMonth(mes) + "/" + year;
    }
    
    public boolean belongsThisWeek() {
        Calendar beginWeek = Calendar.getInstance();
        Calendar endWeek = Calendar.getInstance();
        beginWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        endWeek.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        
        if(this.equals(new Date(beginWeek)) || this.equals(new Date(endWeek))){
            return true;
        }
        if(this.isAfter(new Date(beginWeek))) {
            return this.isBefore(new Date(endWeek));
        }
        return false;
    }
    
    public String toFormattedString(String format) {
        StringBuilder str = new StringBuilder();
        if(format.charAt(0) == 'Y') {
            str.append(year).append("/");
        }
        else if(format.charAt(0) == 'M') {
            str.append(mes).append("/");
        }
        else {
            str.append(dia).append("/");
        }
        
        if(format.charAt(1) == 'Y') {
            str.append(year).append("/");
        }
        else if(format.charAt(1) == 'M') {
            str.append(mes).append("/");
        }
        else {
            str.append(dia).append("/");
        }
        
        if(format.charAt(2) == 'Y') {
            str.append(year);
        }
        else if(format.charAt(2) == 'M') {
            str.append(mes);
        }
        else {
            str.append(dia);
        }           
            
        return str.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.dia;
        hash = 97 * hash + this.mes;
        hash = 97 * hash + this.year;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Date other = (Date) obj;
        if (this.dia != other.dia) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        return true;
    }
    
}
