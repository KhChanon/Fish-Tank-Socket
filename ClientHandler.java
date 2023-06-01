import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler  implements Runnable{
 
    public static ArrayList<ClientHandler>clientHandlers = new ArrayList<>();
    public Socket socket;
    private ObjectInputStream buffReader;
    private ObjectOutputStream buffWriter;
    
    public ClientHandler(Socket socket){
          // Constructors of all the private classes
        try{
	        this.socket = socket;
	        this.buffWriter = new ObjectOutputStream(socket.getOutputStream());
	        this.buffReader = new ObjectInputStream(socket.getInputStream());
	        clientHandlers.add(this);

	    } catch(IOException e){
	    	closeAll(socket, buffReader, buffWriter);
    }
}
// run method override
    @Override
    public void run() {

        Object messageFromClient;
        sendData dataFromClient;

        while(socket.isConnected()){
            try {
				messageFromClient = buffReader.readObject();
				dataFromClient = (sendData) messageFromClient;
				sendMessage(dataFromClient.Fish,dataFromClient.aquariumSendNumber);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    public void sendMessage(Object messageFromClient,int receipient){
    	if(receipient > clientHandlers.size()-1)
    		receipient = 0;
    	
    	if(receipient < 0)
    		receipient = clientHandlers.size()-1;
    	
        try{
        	if(!messageFromClient.equals(null)){
                clientHandlers.get(receipient).buffWriter.writeObject(messageFromClient);
                clientHandlers.get(receipient).buffWriter.flush();
        	}
        } catch(IOException e){
            closeAll(socket,buffReader, buffWriter);

        }
    }
    
    public void broadcastMessage(Object messageFromClient){
        for(ClientHandler clientHandler: clientHandlers){
            try{
            	if(!messageFromClient.equals(null)){
                    clientHandler.buffWriter.writeObject(messageFromClient);
                    clientHandler.buffWriter.flush();
            	}
            } catch(IOException e){
                closeAll(socket,buffReader, buffWriter);

            }
        }
    }
    // notify if the user left the chat
    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    public void closeAll(Socket socket, ObjectInputStream buffReader2, ObjectOutputStream buffWriter2){
      
        // handle the removeClient funciton
        removeClientHandler();
        try{
            if(buffReader2!= null){
                buffReader2.close();
            }
            if(buffWriter2 != null){
                buffWriter2.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.getStackTrace();
        }

    }
    
}