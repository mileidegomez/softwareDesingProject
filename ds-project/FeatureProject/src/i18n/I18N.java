package i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {

	private final ResourceBundle bundle;

	I18N(String language, String country) {
		Locale locale = new Locale(language, country);
		this.bundle = ResourceBundle.getBundle("i18n/Messages", locale);
	}

	private String getString(Messages key) {
		return bundle.getString(key.toString());
	}
	
	/***  SINGLETON ***/
	
	private static I18N instance;

	static void setInstance(I18N i18n) {
		 instance = i18n;
	}
	
	public static I18N getInstance() {
		return instance;
	}

	/***  SERVICES  ***/

	public static String getString(Messages key, String... args) {
		return MessageFormat.format(instance.getString(key), (Object[]) args);
	}
}
