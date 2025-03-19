import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.*;

public class Client extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private ObjectInputStream buffReader;
	private ObjectOutputStream buffWriter;
	public int aquariumNumber;
	ArrayList<Fish> Fishs = new ArrayList<Fish>();
	
	public Client(Socket socket, int aquariumNumber){
		super("Aquarium " + aquariumNumber);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aquariumNumber = aquariumNumber;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		getContentPane().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					System.out.printf("Left Clicked: x=%d y=%d\n", arg0.getX(), arg0.getY());
					Fish fish = new Fish(arg0.getX() - 30, arg0.getY() - 30);
					while(fish.speedX == 0 && fish.speedY == 0) {
						fish.speedX = Fish.randomSpeed();
						fish.speedY = Fish.randomSpeed();
					}
					fish.setBounds(fish.getX(), fish.getY(), fish.imgSize, fish.imgSize);
					add(fish);
					Fishs.add(fish);
				}
				
				if(arg0.getButton() == MouseEvent.BUTTON2) {
					System.out.printf("Middle Clicked\n");
					for(Fish fish:Fishs){
						fish.setVisible(false);
						remove(fish);
					}
					Fishs.removeAll(Fishs);
				}

				if (arg0.getButton() == MouseEvent.BUTTON3) {
					System.out.printf("Right Clicked\n");
					if (Fishs.size() >= 1) {
						Fish firstFish = Fishs.remove(0);
						firstFish.setVisible(false);
						remove(firstFish);
					}
				}
			}
		});
		
		setLayout(null);//using no layout managers 
		setVisible(true);//making the frame visible
		
		
		ActionListener listener = new ActionListener() {
			
			sendData sendData;
			
			public void actionPerformed(ActionEvent e) {
				Dimension size = getContentPane().getSize();

				int w = size.width;
				int h = size.height;
				ArrayList<Fish> FishsCopy = new ArrayList<>(Fishs);
				for (Fish fish : FishsCopy) {
					if (fish.getX() <= 0 || fish.getX() >= w - fish.imgSize) {
						if (fish.getX() <= 0) {
							fish.setLocation(w - (fish.imgSize+1),fish.getY());
							sendData = new sendData(fish,aquariumNumber-1);
						}
						if (fish.getX() >= w - fish.imgSize)  {
							fish.setLocation(1,fish.getY());
							sendData = new sendData(fish,aquariumNumber+1);
						}
						
						try {
							if (!socket.isClosed()) {
							    buffWriter.writeObject(sendData);
							    buffWriter.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Fishs.remove(fish);
						fish.setVisible(false);
						remove(fish);
					}

					if (fish.getY() <= 0 || fish.getY() >= h - fish.imgSize) {
						if(fish.getY() >= h - fish.imgSize) 
							fish.setLocation(fish.getX(), h - (fish.imgSize));
						else if (fish.getY() <= 0) 
							fish.setLocation(fish.getX(), 0);
						fish.speedY = -fish.speedY;
					}

					fish.setLocation((fish.getX() + fish.speedX), (fish.getY() + fish.speedY));
				}
			}
		};
		
		Timer displayTimer = new Timer(1, listener);
		displayTimer.start();
		
		try{
            // Constructors of all the private classes
              this.socket = socket;
              this.buffWriter = new ObjectOutputStream(socket.getOutputStream());
              this.buffReader = new ObjectInputStream(socket.getInputStream());
          
      }catch (IOException e){
          closeAll(socket, buffReader, buffWriter);
      }
	}
	
	public void readMessage(){
	       new Thread( new Runnable() {

	           @Override
	           public void run() {
	        	   Object msfFromGroupChat;

	               while(socket.isConnected()){
		               try{
		                   try {
							msfFromGroupChat = buffReader.readObject();
							Fish fish = (Fish) msfFromGroupChat;
							add(fish);
							fish.setImage("Fish/Fish"+fish.imgStyle+".png");
							fish.setBounds(fish.getX(), fish.getY(), fish.imgSize, fish.imgSize);
							Fishs.add(fish);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
		               } catch (IOException e){
		                   closeAll(socket, buffReader, buffWriter);
		               }

	               }
	               
	           }
	           
	       }).start();
	   }
	
	 public void closeAll(Socket socket, ObjectInputStream buffReader2, ObjectOutputStream buffWriter2){
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
	
	public static void main(String args[]) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 5000);
		Client client = new Client(socket,1);
 	   	client.readMessage();
	}
	
}
