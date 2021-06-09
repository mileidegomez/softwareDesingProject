package app.warnings;

import java.time.Duration;
import java.time.LocalDateTime;

import app.Contact;

public class Warning 
{
	
	private String name;
	private String message;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Duration duration;
	private Contact contact = null;
	
	public Warning(String name, String message, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration duration) 
	{
		this.name = name;
		this.message = message;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.duration = duration;
	}

	public Warning(String name, String message, LocalDateTime startDateTime, LocalDateTime endDateTime, Duration duration, Contact contact) 
	{
		this(name, message, startDateTime, endDateTime, duration);
		this.contact = contact;
	}

	public String getName() {
		return name;
	}
	
	public String getMessage() {
		return message;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public Duration getDuration() {
		return duration;
	}

	public Contact getContact() {
		return contact;
	}

	@Override
	public String toString() {
		return "Warning [name=" + name + ", message=" + message + ", startDateTime=" + startDateTime + ", endDateTime="
				+ endDateTime + ", duration=" + duration + ", contact=" + contact + "]";
	}

}
