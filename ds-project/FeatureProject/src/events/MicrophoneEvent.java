package events;
import com.bezirk.middleware.messages.Event;

public class MicrophoneEvent extends Event
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String voiceInput;
	
	public MicrophoneEvent(String voiceInput) {
		this.voiceInput = voiceInput;
	}
	
	public String getVoiceInput() {
		return voiceInput;
	}
	
	public String toString() {
		return String.format("Voz: %s", voiceInput);
	}

}
