package app;

public class Contact 
{
	
	private final String name;
	private final String contact;

	public Contact(String name, String contact) 
	{
		this.name = name;
		this.contact = contact;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}

	@Override
	public String toString() {
		return name + ": " + contact;
	}
	
	
	
}
