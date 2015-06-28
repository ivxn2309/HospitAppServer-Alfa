package multixsoft.hospitapp.utilities;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import multixsoft.hospitapp.utilities.Interval;

public class IntervalFilter {

    private List<Interval> makeIntervals(String intervalHours) {
        String[] intervals = intervalHours.split(",");

        if (intervals.length <= 0) {
            return null;
        }

        List<Interval> listIntervals = new ArrayList<Interval>();
        for (String hours : intervals) {
            listIntervals.add(new Interval(hours));
        }
        return listIntervals;
    }

    private String validateInterval(Interval interval, int timeApp) {
        String intervalValidation = "";
        if (interval.isInsideInterval(timeApp)) {
            intervalValidation = interval.breakIntervals(timeApp) + " ";
        } else {
            intervalValidation = interval.getStart() + "-" + interval.getEnd() + " ";
        }
        return intervalValidation;
    }

    public String removeInterval(int timeApp, String timeSchedule) {
        List<Interval> listIntervals = makeIntervals(timeSchedule);
        String normalIntervals = "";
        for (Interval interval : listIntervals) {
            normalIntervals += validateInterval(interval, timeApp);
        }
        return getFormatInterval(normalIntervals, ",");
    }

    private boolean isSameHour(String interval) {
        String[] hour = interval.split("-");
        if (hour.length == 2) {
            return hour[0].equals(hour[1]);
        } else {
            return false;
        }
    }

    public String getHours(String intervals) {
        List<Interval> intervalList = makeIntervals(intervals);
        String hours = "";
        for (Interval interval : intervalList) {
            hours += interval.getHoursBetween();
        }
        if (hours.endsWith(",")) {
            hours = hours.substring(0, hours.length() - 1);
        }
        return hours;

    }

    public String getIntervalFromHours(String hours) {
        String [] array = hours.split(",");
        if(array[0].equals(" ")) return " ";
        if(array[0].equals("")) return " ";
        int temp = Integer.parseInt(array[0]);
        String str = temp + "";
        for (int i = 0; i < array.length; i++) {
            if(temp < Integer.parseInt(array[i])){
                str += "-" + temp + ",";
                temp = Integer.parseInt(array[i]);
                str += temp;
            }
            temp++;
        }
        return str += "-" + temp;
    }

    private String getFormatInterval(String interval, String token) {
        String[] formatInterval = interval.split(" ");
        String format = "";

        for (int i = 0; i < formatInterval.length; i++) {

            if (isSameHour(formatInterval[i])) {
                continue;
            }
            if (i != formatInterval.length - 1) {
                format += formatInterval[i] + token;
            } else {
                format += formatInterval[i];
            }

        }
        return format;
    }

}
