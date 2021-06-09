package app;

import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.EventSet;
import com.bezirk.middleware.messages.EventSet.EventReceiver;

import events.MicrophoneEvent;

/**
 * 
 * This aspect represents the hability to have user input
 * through voice commands. This aspect adds behaviour to the 
 * main of the program in order to properly handle events from 
 * microphone enabled devices.
 *
 */
public aspect VoiceCommands 
{
	pointcut main(String[] args) : execution(* *IoTApp.main(String[])) && args(args);
	
	void around(String[] args) : main(args)
	{
		proceed(args);
		
		// Catch MicrophoneEvent
		final EventSet microphoneEvents = new EventSet(MicrophoneEvent.class);
		
		microphoneEvents.setEventReceiver(new EventReceiver() {
			
			@Override
			public void receiveEvent(Event event, ZirkEndPoint sender) 
			{
				if (event instanceof MicrophoneEvent)
				{
					MicrophoneEvent micEvent = (MicrophoneEvent) event;

					// Parse to a command
					String command = VoiceToCommandTranslator.translate(micEvent.getVoiceInput());
					
					// Pass to IoTApp to handle
					IoTApp.app.handle(command);
				}
			}
		});
		
		IoTApp.app.bezirk.subscribe(microphoneEvents);
	}
}
