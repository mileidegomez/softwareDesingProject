import app.AddressBook;
import app.Contact;
import app.IoTApp;

public aspect EmergencyContact
{
	
	public Contact AddressBook.emergencyContact;
	
	public void AddressBook.setEmergencyContact(String name, String contact)
	{
		emergencyContact = new Contact(name, contact);
	} 
	
	public Contact AddressBook.getEmergencyContact()
	{
		return emergencyContact;
	}
	
	
	
	pointcut main(String[] args) : execution(public static void *.main(String[])) && args(args);
	
	private String name;
	private String contact;
	
	void around(String[] args) : main(args) && within(IoTApp)
	{
//		System.out.println("Args: " + args[0] + "|" + args[1]);
		name = args[0];
		contact = args[1];
		
		proceed(args);
	}
	
	after(AddressBook ab) : initialization(AddressBook.new(..)) && target(ab)
	{
//		System.err.println("Emergency contact is " + name + ":" + contact);
		ab.setEmergencyContact(name, contact);
	}
	
}
