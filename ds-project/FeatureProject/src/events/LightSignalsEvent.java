package events;

import com.bezirk.middleware.messages.Event;

public class LightSignalsEvent extends Event 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int mode;
	
	public LightSignalsEvent (int mode) 
	{
		this.mode = mode;
	}
	
	// -1 -> off, 0 -> flicker, 1 -> on
	public int getMode() {
		return mode;
	}
	
	public String toString(){
		return String.format("Light Signals: %s", mode);
	}
}
