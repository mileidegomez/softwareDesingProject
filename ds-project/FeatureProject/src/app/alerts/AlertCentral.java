package app.alerts;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * This object keeps track of all alerts and checks if
 *   a given set of conditions trigger an alert.
 *
 */
public class AlertCentral 
{
	
	private ArrayList<ActivityAlert> activityAlerts = new ArrayList<>();
	private ArrayList<InactivityAlert> inactivityAlerts = new ArrayList<>();
	
	private AlertHandler alertHandler;
	
	private LocalTime lastActivityTime = LocalTime.now();
	
	public AlertCentral(AlertHandler alertHandler) 
	{
		this.alertHandler = alertHandler;
		
		// Start timer to check for inactivity events (every 1min)
		TimerTask timerTask = new TimerTask() 
		{	
			@Override
			public void run() 
			{
				LocalTime now = LocalTime.now();
				long duration = Duration.between(lastActivityTime, now).toMinutes();
				
				inactivityAlerts.forEach(inactivityAlert ->
				{
					if (inactivityAlert.isTriggered(duration, now))
						alertHandler.handleAlert(inactivityAlert.getMessage(duration, now));
				});
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(timerTask, 0, 60 * 1000);
	}
	
	public void notifyActivity(String houseCompartment, LocalTime time)
	{
		lastActivityTime = time;
		
		activityAlerts.forEach(activityAlert -> 
		{
			if(activityAlert.isTriggered(houseCompartment, time))
				// TODO: This should be an interface for handling such events, not
				//         a static reference to the class
				alertHandler.handleAlert(activityAlert.getMessage(houseCompartment, time));
		});
	}
	
	public void addAlert(Alert alert)
	{
		if (alert instanceof ActivityAlert)
		{
			activityAlerts.add((ActivityAlert) alert);
		}
		else if (alert instanceof InactivityAlert)
		{
			inactivityAlerts.add((InactivityAlert) alert);
		}
	}
	
	public void removeAlert(String alertName)
	{
		activityAlerts.removeIf(activityAlert -> activityAlert.getName().equals(alertName));
		inactivityAlerts.removeIf(inactivityAlert -> inactivityAlert.getName().equals(alertName));
	}
	
	public List<Alert> getAlerts()
	{
		ArrayList<Alert> list = new ArrayList<>();
		
		list.addAll(activityAlerts);
		list.addAll(inactivityAlerts);
		
		return list;
	}
}
