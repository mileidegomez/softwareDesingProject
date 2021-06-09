package app;

import com.bezirk.middleware.messages.Event;

import events.SpeakerEvent;

public aspect VoiceSynthesizer 
{
	pointcut p(String output) : execution(* *.output(String)) && args(output);
	
	after(String output) : p(output)
	{	
		Event event = new SpeakerEvent(output);
		
		IoTApp.app.bezirk.sendEvent(event);
	}
}
