package app.alerts;

import java.time.LocalTime;

public abstract class Alert 
{
	protected String name;
	protected LocalTime periodStart;
	protected LocalTime periodEnd;
	
	public Alert(String name, LocalTime periodStart, LocalTime periodEnd) 
	{
		this.name = name;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
	}
	
	public String getName()
	{
		return name;
	}

	protected boolean isTriggered(LocalTime time) 
	{
		return time.isAfter(periodStart) && time.isBefore(periodEnd);
	}

	@Override
	public String toString() {
		return "Alert [name=" + name + ", periodStart=" + periodStart + ", periodEnd=" + periodEnd + "]";
	}
	
	
	
}
