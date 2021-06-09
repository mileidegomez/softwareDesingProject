package app.warnings;

import app.Contact;

@FunctionalInterface
public interface WarningHandler {

	public void handleWarning(String warning, Contact contact);
	
}
