package app;

import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.java.proxy.BezirkMiddleware;

import i18n.I18N;
import i18n.Messages;

public class IoTApp 
{
	
	static IoTApp app;
	
	public static void main(String[] args) 
	{
		BezirkMiddleware.initialize();
		Bezirk bezirk = BezirkMiddleware.registerZirk("IoTApp");
        
        app = new IoTApp(bezirk);
	}

	
	
	
	
	
	public Bezirk bezirk;
	
	public IoTApp(Bezirk bezirk) {
		this.bezirk = bezirk;
	}
	
	
	
	/**
	 * 
	 * @param command
	 * @return true if the command was to exit, false otherwise
	 */
	boolean handle(String command)
	{
		// This method should only handle features that are available
		//   in all products. Other features are programmed in aspects
		//   *around/after* this method.
		
		if (command == null)
		{
			// Do nothing
		}
		else if(command.startsWith("exit"))
		{
			// Exit command
			output(I18N.getString(Messages.COMMAND_EXIT_OUTPUT));
			return true;
		}
		else if (command.startsWith("help"))
		{
			// Help command
			output(getCommands());
		}
		else
		{
			// Default behaviour
			//   The default behaviour should be to ignore.
			//   Unknown commands here can be known by aspects
			//     built around this method
			//output("Command not found '" + command + "'");
		}
		
		return false;
	}
	
	
	void output(String output)
	{
		// Does nothing
		// This method is only here to serve as a join point 
		//   for aspects
	}
	
	String getCommands()
	{
		//   This method should be appended by aspects
		
		String commands = 
				"Available commands:\n" +
				"\thelp - " + I18N.getString(Messages.COMMAND_HELP_HELP) + "\n" +
				"\texit - " + I18N.getString(Messages.COMMAND_EXIT_HELP);
		
		return commands;
	}
}
