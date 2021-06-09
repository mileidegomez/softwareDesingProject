package devices;

import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.java.proxy.BezirkMiddleware;

import events.ButtonEvent;
import events.HeartBeatMonitorEvent;

/**
 * 
 * Capabilities:
 *   - Heartbeat monitor
 *   - Button
 *
 */
public class SmartWatch 
{

	protected  Bezirk bezirk;
    public SmartWatch() {

        BezirkMiddleware.initialize();
        bezirk = BezirkMiddleware.registerZirk("Smart Watch ");
        System.out.println("Got Bezirk instance");
    }

	
    public void sendHeartBeatMonitorUpdate() 
    {
    	TimerTask timerTask = new TimerTask() 
    	{	
    		@Override
    		public void run() 
    		{
    			Random r = new Random();
    			HeartBeatMonitorEvent heartBeatMonitorEvent = new HeartBeatMonitorEvent("kitchen", LocalTime.now(), 60 + r.nextInt(60));

    			bezirk.sendEvent(heartBeatMonitorEvent);
    		}

    	};

    	Timer timer = new Timer();
    	timer.schedule(timerTask, 60 * 1000);
    }
	 
    
    public void sendButtonEvent() {
    	ButtonEvent buttonEvent = new ButtonEvent();
    	
    	bezirk.sendEvent(buttonEvent);
    }
   
    public static void main (String args[]) 
    {
    	SmartWatch smartW = new SmartWatch();
    	
    	System.out.println("Commands -> exit | button");
    	
    	Scanner sc = new Scanner(System.in);
		boolean active = true;
		String s;
		
		while(active)
		{
			s = sc.nextLine();
			
			
			if (s.startsWith("exit"))
			{
				active = false;
			}
			else if (s.startsWith("button"))
			{
				smartW.sendButtonEvent();
			}
		}
		
		sc.close();
   }
	
}
