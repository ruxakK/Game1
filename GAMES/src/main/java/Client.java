import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Player p1 = new Player(1, "Seth"); 
		Map m = new Map(1, 2, 3, 4, 3); 
		Gson gson = new Gson(); 
		
		//connection w server 
		Socket socket = new Socket("127.0.0.1", 9999);
		OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		
		String str = gson.toJson(m);
		System.out.println(str);

		writer.write(str + "\n"); 
		writer.flush();
		writer.close();
		
		socket.close();	
		
	}
	
	

}
