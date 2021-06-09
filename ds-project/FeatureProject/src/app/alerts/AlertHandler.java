package app.alerts;

@FunctionalInterface
public interface AlertHandler 
{
	public void handleAlert(String alert);
}
