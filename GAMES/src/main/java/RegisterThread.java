import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;



public class RegisterThread extends Thread {
	
	private final Socket socket;
	private int playerId;
	private Player player;
	
	public RegisterThread(final Socket socket, int playerId) {
	        this.socket = socket;
	        this.playerId = playerId;
	}
	    
	

	@Override
	public void run() {
		
		try{
			 OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	         BufferedReader reader = new BufferedReader (new InputStreamReader(socket.getInputStream(), "UTF-8"));
	         
	         String line = reader.readLine();
	         
	         Gson register_req = new Gson(); 
	         
		}catch(IOException e)
		{
			
		}
		
	}

}
