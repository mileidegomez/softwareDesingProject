package app.alerts;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import i18n.I18N;
import i18n.Messages;

public class ActivityAlert extends Alert 
{
	// Formatter for the times (HH:mm)
	private static final DateTimeFormatter LOCALTIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
	
	private String houseCompartment;
	
	public ActivityAlert(String houseCompartment, String name, LocalTime periodStart, LocalTime periodEnd) 
	{
		super(name, periodStart, periodEnd);
		this.houseCompartment = houseCompartment;
	}
	
	public boolean isTriggered(String houseCompartment, LocalTime time) 
	{
		return this.houseCompartment.equals(houseCompartment) &&
			   super.isTriggered(time);
	}
	
	public String getMessage(String houseCompartment, LocalTime time)
	{
		return I18N.getString(Messages.ACTIVITY_DETECTION, houseCompartment, time.format(LOCALTIME_FORMAT));
	}

	@Override
	public String toString() {
		return I18N.getString(Messages.ACTIVITY_ALERT, name, houseCompartment, periodStart.format(LOCALTIME_FORMAT), periodEnd.format(LOCALTIME_FORMAT));
	}
	
}
