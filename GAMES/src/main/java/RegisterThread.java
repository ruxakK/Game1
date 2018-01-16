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
	private MessMap player_map; 
	
	public RegisterThread(final Socket socket, int playerId) {
	        this.socket = socket;
	        this.playerId = playerId;
	}
	    
	

	@Override
	public void run() {
		
		try{
			 OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	         BufferedReader reader = new BufferedReader (new InputStreamReader(socket.getInputStream(), "UTF-8"));
	         
	         //----------------general connection w client----------------- 
	         String line = reader.readLine();
	         Gson register_play = new Gson(); 

	         Player p = register_play.fromJson(line, Player.class);
	         System.out.println("Client" + playerId + " named: " + p.getName() + " connected");
	         
	         Message m = new Message("OK", 0);
	         line = register_play.toJson(m); 
	         
	         writer.write(line + "\n");
	         writer.flush();
	         
	         //-----------------receiving map--------------------------------
	         line = reader.readLine(); 
	         MessMap messMap = register_play.fromJson(line, MessMap.class); 
	         System.out.println("Map from Client" + playerId + " received!");
	        
	         if(checkMap(messMap))
	         {
		         player_map=messMap;
	         }
	         else
	         {
	        	 m.setM("Your castle is not placed correctly, you lost");
		         m.setStatus(1);
		         line = register_play.toJson(m); 
		         
		         writer.write(line + "\n");
		         writer.flush();
		         
	         }	
	         
		}catch(IOException e)
		{
			System.out.println(e.toString());
		}

	}
	
	public static boolean checkMap(MessMap messMap)
	{
		if(messMap.getFieldVal(messMap.getMap().getCX(), messMap.getMap().getCY())==0)
			return false; 
		else 
			return true; 
	}



	public int getPlayerId() {
		return playerId;
	}



	public Player getPlayer() {
		return player;
	}



	public MessMap getPlayer_map() {
		return player_map;
	}
	
	

}
