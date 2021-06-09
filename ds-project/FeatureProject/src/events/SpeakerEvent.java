package events;
import com.bezirk.middleware.messages.Event;

public class SpeakerEvent extends Event
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String voiceOutput;
	
	public SpeakerEvent(String voiceOutput) {
		this.voiceOutput = voiceOutput;
	}
	
	public String getVoiceOutput() {
		return voiceOutput;
	}

}
