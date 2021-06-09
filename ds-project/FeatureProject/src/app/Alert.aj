package app;

import i18n.I18N;
import i18n.Messages;
import app.alerts.AlertCentral;
import com.bezirk.middleware.messages.EventSet;
import app.alerts.AlertHandler;
import events.MotionSensorEvent;
import events.HeartBeatMonitorEvent;
import events.ButtonEvent;
import com.bezirk.middleware.messages.EventSet.EventReceiver;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.addressing.ZirkEndPoint;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import app.alerts.ActivityAlert;
import app.alerts.InactivityAlert;

public aspect Alert 
{
	
	// ========== Add handler to Bezirk ==========
	pointcut main(String[] args) : execution(* *IoTApp.main(String[])) && args(args);
	
	public AlertCentral IoTApp.alertCentral = new AlertCentral(new AlertHandler() {
		@Override
		public void handleAlert(String alert)
		{
			Alert.handleAlert(alert);
		}
	});
	
	after(IoTApp app) : initialization(IoTApp.new(..)) && target(app)
	{
		
		// Catch MicrophoneEvent
		final EventSet buttonEvents = new EventSet(ButtonEvent.class, MotionSensorEvent.class, HeartBeatMonitorEvent.class);
		
		buttonEvents.setEventReceiver(new EventReceiver() {
			
			@Override
			public void receiveEvent(Event event, ZirkEndPoint sender) 
			{
				if (event instanceof ButtonEvent)
				{
					// This raises a false error, beacause 
					//   Alert -> EmergencyCantact -> ContactList
					// and therefore, there is always an AddressBook instance 
					// with an emergency contact.
					handleAlert("# Alert # I need help");
				}
				else if (event instanceof MotionSensorEvent)
				{
					MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
					
					app.alertCentral.notifyActivity(motionSensorEvent.getHouseCompartment(), motionSensorEvent.getTime());
				}
				else if (event instanceof HeartBeatMonitorEvent)
				{
					HeartBeatMonitorEvent heartBeatMonitorEvent = (HeartBeatMonitorEvent) event;
					
					app.alertCentral.notifyActivity(heartBeatMonitorEvent.getHouseCompartment(), heartBeatMonitorEvent.getTime());
				}
			}
		});
		
		app.bezirk.subscribe(buttonEvents);
	}
	
	static void handleAlert(String alert)
	{
		Contact emergencyContact = IoTApp.app.addressBook.getEmergencyContact();
		//Contact emergencyContact = new Contact("name", "contact");
		System.out.println(I18N.getString(Messages.WARNING_SMS_SENT, emergencyContact.toString(), alert));
		IoTApp.app.output(alert);
	}
	
	
	// ========== Add handler logic ==========
	pointcut handle(String command) : execution(* *IoTApp.handle(String)) && 
	                                  args(command) ;
	
	boolean around(String command, IoTApp app) : handle(command) && target(app)
	{
		boolean oldRet = proceed(command, app);
		
		if (command == null)
		{
			// Does nothing
		}
		else if (command.startsWith("setAlert"))
		{
			String[] args = command.split(" ");
			
			if (args.length < 5)
				return oldRet;
			
			String alertName = args[1];
			String isActiveS = args[2];
			
			String[] periodS = args[4].split("-");
			LocalTime periodStart = parseLocalTime(periodS[0]);
			LocalTime periodEnd = parseLocalTime(periodS[1]);
			
			
			if (isActiveS.equals("active"))
			{
				String houseCompartment = args[3];
				
				app.alertCentral.addAlert(new ActivityAlert(houseCompartment, alertName, periodStart, periodEnd));
			}
			else if (isActiveS.equals("inactive"))
			{
				long duration = Long.parseLong(args[3]);
				
				app.alertCentral.addAlert(new InactivityAlert(duration, alertName, periodStart, periodEnd));
			}
			
		}
		else if (command.startsWith("deleteAlert"))
		{
			String[] args = command.split(" ");
			
			if (args.length < 2)
				return oldRet;
			
			String alertName = args[1];
			
			app.alertCentral.removeAlert(alertName);
			IoTApp.app.output(I18N.getString(Messages.COMMAND_HELP_DELETE_ALERT_OUTPUT));
		}
		else if (command.startsWith("showAlerts"))
		{
			String output = I18N.getString(Messages.COMMAND_HELP_SHOW_ALERTS_OUTPUT) + "\n";
			
			for (app.alerts.Alert alert : app.alertCentral.getAlerts())
				output += "\t" + alert.toString() + "\n";
			
			output += "== " + I18N.getString(Messages.END) + " ==";
			
			
			IoTApp.app.output(output);
		}
		 
		return oldRet;
	}
	
	
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
	private LocalTime parseLocalTime(String timeString)
	{
		return LocalTime.parse(timeString, format);
	}
	
	// ========== Add command information ==========
	pointcut getCommands() : execution(String *IoTApp.getCommands());
	
	
	String around() : getCommands()
	{
		String commands = proceed() + "\n" +
				          "\tsetAlert <alertName> \n" + 
				          	"\t\tactive <houseCompartment> <period> - " + I18N.getString(Messages.COMMAND_HELP_SETALERT_ACTIVITY) + "\n" +
				          	"\t\tinactive <duration> <period> - " + I18N.getString(Messages.COMMAND_HELP_SETALERT_INACTIVITY) + "\n" +
				          "\tshowAlerts - " + I18N.getString(Messages.COMMAND_HELP_SHOW_ALERTS) + " \n" +
				          "\tdeleteAlert <alertName> - " + I18N.getString(Messages.COMMAND_HELP_DELETE_ALERT);
		
		return commands;
	}
	
}
