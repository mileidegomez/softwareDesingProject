package app.warnings;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import app.Contact;

public class WarningCentral 
{
	
	class WarningCheckpoint
	{
		// Formatter for the times (HH:mm)
		private final DateTimeFormatter LOCALDATETIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
		
		private Warning warning;
		private LocalDateTime checkpoint;
		
		public WarningCheckpoint(Warning warning) 
		{
			this.warning = warning;
			
			this.checkpoint = warning.getStartDateTime();
			
			while (checkpoint.isBefore(LocalDateTime.now()) &&
					checkpoint.until(LocalDateTime.now(), ChronoUnit.MINUTES) > warning.getDuration().toMinutes())
			{
				checkpoint = checkpoint.plus(warning.getDuration());
			}
			
		}
		
		public boolean isTriggered()
		{
			return Duration.between(checkpoint, LocalDateTime.now()).compareTo(warning.getDuration()) >= 0;
		}
		
		public String trigger()
		{
			checkpoint = checkpoint.plus(warning.getDuration());
			return checkpoint.format(LOCALDATETIME_FORMAT) + " - " + warning.getMessage();
		}
		
		public Warning getWarning()
		{
			return warning;
		}
	}
	
	
	
	private CopyOnWriteArrayList<WarningCheckpoint> warnings = new CopyOnWriteArrayList<>();
	
	public WarningCentral(WarningHandler warningHandler) 
	{
		
		// Start timer to check for inactivity events (every 1min)
		TimerTask timerTask = new TimerTask() 
		{	
			@Override
			public void run() 
			{
				warnings.forEach(warning ->
				{
					if (warning.isTriggered())
						warningHandler.handleWarning(warning.trigger(), warning.getWarning().getContact());
					
					if(warning.getWarning().getEndDateTime().isBefore(LocalDateTime.now()))
						warnings.remove(warning);
				});
			}
		};

		Timer timer = new Timer();
		timer.schedule(timerTask, 0, 60 * 1000);
		
	}
	
	public void addWarning(String name, String message, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration duration, Contact contact)
	{
		warnings.add(new WarningCheckpoint(new Warning(name, message, startDateTime, endDateTime, duration, contact)));
	}
	
	public void addWarning(String name, String message, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration duration)
	{
		warnings.add(new WarningCheckpoint(new Warning(name, message, startDateTime, endDateTime, duration)));
	}
	
	public void removeWarning(String name)
	{
		warnings.removeIf(warningCheckpoint -> warningCheckpoint.getWarning().getName().equals(name));
	}
	
	public List<Warning> getWarnings()
	{
		ArrayList<Warning> ret = new ArrayList<>();
		
		warnings.forEach(warningCheckpoint -> ret.add(warningCheckpoint.getWarning()));
		
		return ret;
	}
}
