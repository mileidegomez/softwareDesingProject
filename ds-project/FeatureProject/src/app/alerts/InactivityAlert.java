package app.alerts;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import i18n.I18N;
import i18n.Messages;

public class InactivityAlert extends Alert
{
	// Formatter for the times (HH:mm)
	private static final DateTimeFormatter LOCALTIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	private long duration;
	
	public InactivityAlert(long duration, String name, LocalTime periodStart, LocalTime periodEnd) {
		super(name, periodStart, periodEnd);
		this.duration = duration;
	}
	
	public boolean isTriggered(long duration, LocalTime time) 
	{
		return duration >= this.duration &&
			   super.isTriggered(time);
	}
	
	public String getMessage(long duration, LocalTime time)
	{
		return I18N.getString(Messages.INACTIVITY_DETECTION, "" + duration, time.format(LOCALTIME_FORMAT));
	}
	
	@Override
	public String toString() {
		return I18N.getString(Messages.INACTIVITY_ALERT, name, "" + duration, periodStart.format(LOCALTIME_FORMAT), periodEnd.format(LOCALTIME_FORMAT));
	}
	
}
