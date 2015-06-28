package multixsoft.hospitapp.utilities;

public class Interval {
	private int start;
	private int end;
	
	public Interval(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public Interval(String cadenaIntervalos){
		String[] hoursInterval = cadenaIntervalos.split("-");
		if(hoursInterval.length > 0){
			this.start = Integer.parseInt(hoursInterval[0]);
			this.end = Integer.parseInt(hoursInterval[1]);
		}
	}
	
	public String getHoursBetween(){
		String hoursBetween="";
		for(int i=this.start; i<this.end;i++){
			hoursBetween+=i+",";
		}
		return hoursBetween;
	}
	
	public boolean isInsideInterval(int hour){
		return ((hour >= this.start) && (hour <= this.end));
	}
	
	public String breakIntervals(int hour){
		String timeInterval = start+"-"+hour+" ";
		
		if(!((hour+1) >= end)){
			 timeInterval += (hour+1)+"-"+end;
		}
		return timeInterval;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public String toString(){
		return this.start+"-"+this.end;
	}
}