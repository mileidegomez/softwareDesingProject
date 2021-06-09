package events;
import java.time.LocalTime;

import com.bezirk.middleware.messages.Event;

public class MotionSensorEvent extends Event
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String houseCompartment;
	private LocalTime time;
	
	public MotionSensorEvent(String houseCompartment, LocalTime time) {
		this.houseCompartment = houseCompartment;
		this.time = time;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public String getHouseCompartment()
	{
		return houseCompartment;
	}
	
	public String toString() {
		return "Motion Sensor: " + time + " @ " + houseCompartment;
	}
}
