import com.bezirk.middleware.messages.Event;

import app.IoTApp;
import events.LightSignalsEvent;

public aspect LightSignals 
{
	
	pointcut p(String output, IoTApp app) : execution(* *IoTApp.output(String)) && args(output) && target(app);
	
	after(String output, IoTApp app) : p(output, app)
	{	
		Event event = new LightSignalsEvent(0);
		
		app.bezirk.sendEvent(event);
	}
	
}
