package events;

import java.time.LocalTime;

import com.bezirk.middleware.messages.Event;

public class HeartBeatMonitorEvent extends Event
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String houseCompartment;
	private LocalTime time;
	private  int heartBeat;

    public HeartBeatMonitorEvent(String houseCompartment, LocalTime time, int heartBeat)
    {
    	this.houseCompartment = houseCompartment;
		this.time = time;
    	this.heartBeat = heartBeat;
    }
    
    public LocalTime getTime() {
		return time;
	}
	
	public String getHouseCompartment()
	{
		return houseCompartment;
	}
  
    public int getHeartBeat() 
    {
        return heartBeat;
    }
    
    public String toString() 
    {
    	return String.format("Heart Beat : %s", heartBeat);
    }

}
