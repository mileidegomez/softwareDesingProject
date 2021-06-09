package app;

import java.util.Scanner;

import i18n.I18N;
import i18n.Messages;

/**
 * 
 * This aspect represents the hability to have user input through
 * the console. It works by running after the main of the program
 * and passing all user input to it.
 *
 */
public aspect KeyboardMouse 
{
	
	pointcut main() : execution(* *IoTApp.main(..));
	
	after() : main()
	{	
		
		System.out.println(I18N.getString(Messages.KEYBOARD_MOUSE_FIRST_MESSAGE));
		
		Scanner sc = new Scanner(System.in);
		String input;
		boolean active = true;
		
		while (active)
		{
			
			input = sc.nextLine();
			
			
			if (IoTApp.app.handle(input))
				active = false;
		}
		
		sc.close();
	}
	
	
}
