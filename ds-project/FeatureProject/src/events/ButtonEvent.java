package events;

import com.bezirk.middleware.messages.Event;

public class ButtonEvent extends Event
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ButtonEvent() { /* Does nothing */ }
	
	public String toString() {
		return String.format("Button pressed");
	}

}
