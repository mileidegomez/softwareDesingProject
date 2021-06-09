package i18n;
public aspect English {
	before() : execution(* *.main(..)) {
		I18N.setInstance(new I18N("en","US"));
		System.err.println("This product speaks english.");
	}
}
