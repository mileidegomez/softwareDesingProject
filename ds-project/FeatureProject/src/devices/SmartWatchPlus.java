package devices;

import java.util.Scanner;

import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.EventSet;
import com.bezirk.middleware.messages.EventSet.EventReceiver;

import events.LightSignalsEvent;
import events.MicrophoneEvent;

/**
 * 
 * Capabilities:
 *   - Heartbeat monitor
 *   - Button
 *   - Light Signals
 *   - Microphone (voice commands)
 *
 */
public class SmartWatchPlus extends SmartWatch
{
	
	public SmartWatchPlus() {
		super();
		setLightSignals();
	}
	
	public void sendMicrophoneEvent(String input) 
    {
    	MicrophoneEvent microphoneEvent = new MicrophoneEvent(input);
    	
    	bezirk.sendEvent(microphoneEvent);
        System.out.println("Published Microfone: " + microphoneEvent.toString());
    }
    
    public void setLightSignals() {
    	final EventSet speakerEvents = new EventSet(LightSignalsEvent.class);

    	speakerEvents.setEventReceiver(new EventReceiver() {

    		@Override
    		public void receiveEvent(Event event, ZirkEndPoint sender) 
    		{
    			if (event instanceof LightSignalsEvent)
    			{
    				final LightSignalsEvent lightEvent = (LightSignalsEvent) event;
    				
    				switch (lightEvent.getMode()) {
    				case -1:
    					System.out.println("Lights > OFF");
    					break;
    				case 0:
    					System.out.println("Lights > Flicker");
    					break;
    				case 1:
    					System.out.println("Lights > ON");
    					break;
					}
    			}
    		}
    	});

    	bezirk.subscribe(speakerEvents);
    }
    
	
    public static void main (String args[]) 
    {
    	SmartWatchPlus smartWP = new SmartWatchPlus();
    	
    	System.out.println("Commands -> exit | button | mic");
    	
    	Scanner sc = new Scanner(System.in);
		boolean active = true;
		String s;
		
		while(active)
		{
			s = sc.nextLine();
			
			
			if (s.startsWith("exit"))
			{
				active = false;
			}
			else if (s.startsWith("button"))
			{
				smartWP.sendButtonEvent();
			}
			else if (s.startsWith("mic"))
			{
				// Little hack to send the rest of the string as mic input
				smartWP.sendMicrophoneEvent(s.substring(4));
			}
		}
		
		sc.close();
   }

}
