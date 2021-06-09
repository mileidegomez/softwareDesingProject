package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import app.warnings.WarningCentral;
import app.warnings.WarningHandler;
import i18n.I18N;
import i18n.Messages;

public aspect Warning 
{
	
	public WarningCentral IoTApp.warningCentral = new WarningCentral(new WarningHandler() {
		@Override
		public void handleWarning(String warning, Contact contact)
		{
			Warning.handleWarning(warning, contact);
		}
	});
	
	static void handleWarning(String warning, Contact contact)
	{
		IoTApp.app.output(warning);
		
		if (contact != null)
			System.out.println(I18N.getString(Messages.WARNING_SMS_SENT, contact.toString(), warning));
	}
	
	
	
	
	
	// ========== Add handler logic ==========
		pointcut handle(String command, IoTApp app) : execution(* *IoTApp.handle(String)) && 
		                                  			  args(command) &&
		                                  			  target(app);
		
		boolean around(String command, IoTApp app) : handle(command, app)
		{
			boolean oldRet = proceed(command, app);
			
			if (command == null)
			{
				// Does nothing
			}
			else if (command.startsWith("setWarning"))
			{
				
				
				String[] split = command.split("#");
				String[] args = split[0].split(" ");
				
				if (args.length < 5)
					return oldRet;
				
				
				String warningName = args[1];
				LocalDateTime startDateTime = parseLocalDateTime(args[2]);
				LocalDateTime endDateTime = parseLocalDateTime(args[3]);
				Duration duration = parseDuration(args[4]);
				String message = split[1];
				
				if(args.length < 6)
				{
					// No contact
					app.warningCentral.addWarning(warningName, message, startDateTime, endDateTime, duration);
				}
				else
				{
					// With contact
					String contactName = args[5];
					
					app.warningCentral.addWarning(warningName, message, startDateTime, endDateTime, duration, app.addressBook.getContact(contactName)); 
				}
				
				
			}
			else if (command.startsWith("deleteWarning"))
			{
				String[] args = command.split(" ");
				
				if (args.length < 2)
					return oldRet;
				
				String warningName = args[1];
				
				app.warningCentral.removeWarning(warningName);
				app.output(I18N.getString(Messages.WARNING_REMOVED));
			}
			else if (command.startsWith("showWarnings"))
			{
				String output = I18N.getString(Messages.COMMAND_HELP_SHOW_WARNINGS_OUTPUT) + "\n";
				
				for (app.warnings.Warning warning : app.warningCentral.getWarnings())
					output += "\t" + warning.toString() + "\n";
				
				output += "== " + I18N.getString(Messages.END) + " ==";
				
				
				app.output(output);
			}
			 
			return oldRet;
		}
		
		
		private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm");
		private LocalDateTime parseLocalDateTime(String timeString)
		{
			return LocalDateTime.parse(timeString, format);
		}
		
		private Duration parseDuration(String durationString)
		{
			String[] args = durationString.split(":");
			
			int hours = Integer.parseInt(args[0]);
			int minutes = Integer.parseInt(args[1]);
			
			Duration duration = Duration.ZERO;
			duration = duration.plus(Duration.ofHours(hours));
			duration = duration.plus(Duration.ofMinutes(minutes));
			
			return duration;
		}
		
		// ========== Add command information ==========
		pointcut getCommands() : execution(String *IoTApp.getCommands());
		
		String around() : getCommands()
		{
			String commands = proceed() + "\n" +
					          "\tsetWarning <warningName> <startDateTime> <endDateTime> <duration> [<contactName>] #<message> - " + I18N.getString(Messages.COMMAND_HELP_SET_WARNING) +" \n" +
					          "\tshowWarnings - " + I18N.getString(Messages.COMMAND_HELP_SHOW_WARNINGS) + "\n" +
					          "\tdeleteWarnings <warningName> - " + I18N.getString(Messages.COMMAND_HELP_DELETE_WARNING);
			
			return commands;
		}
}
