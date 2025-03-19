import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MyApp implements ActionListener {

	ArrayList<Client> clients = new ArrayList<Client>();
	JFrame frame = new JFrame("Add Client");
    JButton addClientButton = new JButton("Add a Client");
    int aquariumNumber = 1;
    
    public MyApp() {
    	frame.setBounds(700,400,250, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setLayout(new GridLayout(1, 1));
        
        frame.add(addClientButton);
        
        addClientButton.addActionListener(this);
        
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) { 
    	if (e.getSource() == addClientButton) {
			try {
				Socket socket = new Socket("localhost", 3000);
	    		Client client = new Client(socket,aquariumNumber);
	    		aquariumNumber++;
	     	   	client.readMessage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }
    
    public static void main(String[] args) {
        new MyApp();
    }
}

