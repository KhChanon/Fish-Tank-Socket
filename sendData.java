import java.io.Serializable;

public class sendData implements Serializable {
    private static final long serialVersionUID = 1L;
	Fish Fish;
	int aquariumSendNumber;
	
	sendData(Fish Fish,int aquariumSendNumber){
		this.Fish = Fish;
		this.aquariumSendNumber = aquariumSendNumber - 1;
	}
}