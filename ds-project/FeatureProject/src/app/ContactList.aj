package app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import i18n.I18N;
import i18n.Messages;

public aspect ContactList 
{
	// ===== Inject attribute to IoTApp ===== 
	
	AddressBook IoTApp.addressBook = new AddressBook();
	
	// ===== Add command logic to the handler =====
	
	pointcut handle(String command) : execution(* *IoTApp.handle(String)) && 
									  args(command);

	boolean around(String command, IoTApp app) : handle(command) && target(app)
	{
		boolean oldRet = proceed(command, app);

		if (command == null)
		{
			// Does nothing
		}
		else if (command.startsWith("addContact"))
		{
			String[] args = command.split(" ");

			if (args.length < 3)
				return oldRet;
			
			app.addressBook.addContact(args[1], args[2]);
		}
		else if (command.startsWith("removeContact"))
		{
			String[] args = command.split(" ");
			
			if (args.length < 2)
				return oldRet;
			
			app.addressBook.removeContact(args[1]);
		}
		else if (command.startsWith("showContacts"))
		{
			String output = I18N.getString(Messages.COMMAND_HELP_SHOW_CONTACTS_OUTPUT) + ":\n";
			
			for (Contact contact : app.addressBook.getContacts())
				output += "\t" + contact.toString() + "\n";
			
			output += "== " + I18N.getString(Messages.END) + " ==";
			
			
			app.output(output);
		}

		return oldRet;
	}
	
	// ===== Add command description to getCommands ===== 
	
	pointcut getCommands() : execution(String *IoTApp.getCommands());
	
	
	String around() : getCommands()
	{
		String commands = proceed() + "\n" +
				          "\taddContact <name> <contact> - " + I18N.getString(Messages.COMMAND_HELP_ADD_CONTACT) + "\n" +
				          "\tremoveContact <name> - " + I18N.getString(Messages.COMMAND_HELP_REMOVE_CONTACT) +" \n" +
				          "\tshowContacts - " + I18N.getString(Messages.COMMAND_HELP_SHOW_CONTACTS);
		
		return commands;
	}
	
	// ===== Add voice translation to the translator =====
	
	pointcut translate(String voiceInput) : execution(String *VoiceToCommandTranslator.translate(String)) && args(voiceInput);
	
	String around(String voiceInput) : translate(voiceInput)
	{
		String maybeCommand = proceed(voiceInput);
		
		Pattern patternAdd = Pattern.compile(I18N.getString(Messages.VOICE_COMMAND_ADD_CONTACT));
		Matcher matcherAdd = patternAdd.matcher(voiceInput);
		if (matcherAdd.find())
		{
			String contact = matcherAdd.group(1);
			String name = matcherAdd.group(2);
			
			maybeCommand = "addContact " + name + " " + contact;
			return maybeCommand;
		}
		
		
		Pattern patternRem = Pattern.compile(I18N.getString(Messages.VOICE_COMMAND_REMOVE_CONTACT));
		Matcher matcherRem = patternRem.matcher(voiceInput);
		if (matcherRem.find())
		{
			maybeCommand = "removeContact " + matcherRem.group(1);
			return maybeCommand;
		}
				
		return maybeCommand;
	}
	
}
