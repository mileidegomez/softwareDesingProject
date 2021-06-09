package devices;

import java.time.LocalTime;
import java.util.Scanner;

import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.java.proxy.BezirkMiddleware;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.EventSet;
import com.bezirk.middleware.messages.EventSet.EventReceiver;

import events.MicrophoneEvent;
import events.MotionSensorEvent;
import events.SpeakerEvent;


/**
 * 
 * Capabilities:
 *   - Microphone (voice commands)
 *   - Speaker (voice synthesizer)
 *   - Movement/Detection Sensor
 *
 */

public class HomeAssistant 
{
	private static Bezirk bezirk;
 	
	public HomeAssistant() {
		 BezirkMiddleware.initialize();
	     bezirk = BezirkMiddleware.registerZirk("Home Assistant Zirk");
	     setSpeaker();
	}
	
	private void setSpeaker() 
    {
    	final EventSet speakerEvents = new EventSet(SpeakerEvent.class);

    	speakerEvents.setEventReceiver(new EventReceiver() {

    		@Override
    		public void receiveEvent(Event event, ZirkEndPoint sender) 
    		{
    			if (event instanceof SpeakerEvent)
    			{
    				final SpeakerEvent speakerEvent = (SpeakerEvent) event;
    				
    				System.out.println("SPEAKER > " + speakerEvent.getVoiceOutput());
    			}
    		}
    	});

    	bezirk.subscribe(speakerEvents);
    }
	  
    public void sendMicrophoneEvent(String input) 
    {
    	MicrophoneEvent microphoneEvent = new MicrophoneEvent(input);
    	
    	bezirk.sendEvent(microphoneEvent);
        System.out.println("Published Microfone: " + microphoneEvent.toString());
    }
    
    public void sendMotionSensorEvent() 
    {
    	MotionSensorEvent motionSensorEvent = new MotionSensorEvent("kitchen", LocalTime.now());

    	bezirk.sendEvent(motionSensorEvent);
    	System.out.println("Sent MotionSensorEvent: " + motionSensorEvent.toString());
    }
    
	public static void main(String args[]) 
	{
		HomeAssistant homeA = new HomeAssistant();
		
		System.out.println("Commands -> exit | mic <input> | motion");
		
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
			else if (s.startsWith("mic"))
			{
				// Little hack to send the rest of the string as mic input
				homeA.sendMicrophoneEvent(s.substring(4));
			}
			else if (s.startsWith("motion"))
			{
				homeA.sendMotionSensorEvent();
			}
		}
		
		sc.close();
	}
}
