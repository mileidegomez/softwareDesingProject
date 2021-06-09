package app;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AddressBook 
{
	
	private HashMap<String, String> contacts = new HashMap<>();
	
	public AddressBook() { /* Does nothing */ }
	
	public void addContact(String name, String contact)
	{
		contacts.putIfAbsent(name, contact);
	}
	
	public void removeContact(String name)
	{
		contacts.remove(name);
	}
	
	public Contact getContact(String name)
	{
		return new Contact(name, contacts.get(name));
	}
	
	public List<Contact> getContacts()
	{
		
		List<Contact> ret = contacts.entrySet()
				                    .stream()
				                    .map(entry -> new Contact(entry.getKey(), entry.getValue()))
				                    .collect(Collectors.toList());
		
		return ret;
	}
}
