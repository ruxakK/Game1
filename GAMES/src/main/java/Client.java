import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import com.google.gson.Gson;



public class Client {
	
	private static int currentx = 0;
	private static int currenty = 0;
	private static boolean playerNum = false; 
	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		try
		{	
			Gson gson = new Gson(); 
			
			//connection w server setup
			Socket socket = new Socket("127.0.0.1", 9999);
			OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			System.out.println("Hello Player, please enter your name: ");
			
			Scanner scanner = new Scanner(System.in); 
		
			Player p1 = new Player(scanner.nextLine()); 
			
			
			//send your player as first message
			String str = gson.toJson(p1);
			System.out.println(str);

			writer.write(str + "\n"); 
			writer.flush();
			
			String line = reader.readLine();
			System.out.println(line);
			
			Message m = gson.fromJson(line, Message.class); 
		
			
			if(m.getStatus() == 0)
				System.out.println("Connection to server successful");
			else 
			{
				writer.close();
				socket.close();
				throw new IOException("Connection failed"); 
			}
			
			
			//---------generating and sending Map---------------------
		
			MessMap messMap = generateMap(); 
			
			System.out.println("Where do you want to place your castle?: ");
			System.out.println("X-Coordinate: ");
			currentx = scanner.nextInt(); 
			messMap.getMap().setCX(currentx);
		
			System.out.println("Y-Coordinate: ");
			currenty = scanner.nextInt();
			
			
			messMap.getMap().setCY(currenty);
			
			str = gson.toJson(messMap); 
			
			writer.write(str + "\n"); 
			writer.flush();
			
			line = reader.readLine();
			//System.out.println(line);
			
			FullMap fullmap = gson.fromJson(line, FullMap.class); 
			
			currentx = fullmap.getCurrentx();
			currenty = fullmap.getCurrenty();
			
			if(currenty > 3)
			{
				playerNum = true; 
			}
			
			showplayinMap(fullmap);
			
			Move move = new Move();
			
			str = scanner.nextLine();
			
			boolean end= false; 
			
			while(!end)
			{

				boolean validdir = false; 
				
				while(!validdir)
				{
					System.out.println("Which direction?: ");
					str = scanner.nextLine();
					
					switch (str)
					{
						case "right": 
							move.setDirection(0);
							validdir = true; 
							break; 
						case "left":
							move.setDirection(1);
							validdir = true; 
							break;
						case "up":
							move.setDirection(2);
							validdir = true; 
							break; 
							
						case "down":
							move.setDirection(3);
							validdir = true; 
							break; 
						default: 
							System.out.println("Wrong direction!");
						
					}
				}
				
				move.setX(currentx);
				move.setY(currenty);
				
				writer.write(gson.toJson(move) + "\n"); 
				writer.flush();
				
				MoveResponse movere = gson.fromJson(reader.readLine(), MoveResponse.class); 
				
				switch(movere.isStatus())
				{
					case 0: 
						currentx=movere.getX();
						currenty=movere.getY();
						showplayinMap(fullmap);
						break;
					case 1:
						System.out.println("You lost the game");
						end = true; 
						break;
					case 2: 
						System.out.println("You won the game");
						end = true; 
						break; 
					case 3: 
						System.out.println("Treasure found!");
						currentx=movere.getX();
						currenty=movere.getY();
						showplayinMap(fullmap);
						break; 
						
				}
				
			}
			
			
			System.out.println("GAME over!");
			
			writer.close();
			socket.close();	
			
		}catch(IOException e)
		{
			System.out.println(e.toString());
		}
		finally
		{

		}
	}
	
	
	public static MessMap generateMap()
	{
		int [] numb = new int [32]; 
		Random rand = new Random();
		
		int wcnt = rand.nextInt((11 - 4) + 1) + 4;					//wcnt - water count : number of water fields
		int gcnt = rand.nextInt(((32-wcnt-3) - 5) + 1) + 5;			//gcnt - grass count : number of grass fields
		int mcnt = 32-gcnt-wcnt; 									//mcnt - mount count : number of mountain fields

			/*
		System.out.println("Water: " + wcnt);
		System.out.println("Grass: " + gcnt);
		System.out.println("Mount: " + mcnt);
		*/
		
		int wnumb=wcnt; 											//wcnt number of total water fields; wnumb number of water field left to insert

		int val = 0; 
		
		for(int i=0; i<32; i++)
		{
			val =  rand.nextInt((2 - 0) + 1) + 0;
			
			switch(val)
			{
				case 0: 
					if(wnumb>0)										//their are waterfields left
					{
						if(!(i<=7 && (wcnt - wnumb) == 3))			//remove possibility of more than 3 water fields on the border
						{		
							if(!((i==8 && numb[1]==0)||(i==30 && numb[23]==0)||(i==15 && numb[6]==0)||(i==25&&numb[16]==0)))	//remove possibility of island on the corners	
							{
								if(!(i>=8&&numb[i-7]==0)) 					//if false in second row
								{
									if(!(i>=9&&numb[i-9]==0))				
									{
										if(!(i>=25&&numb[i-25]==0&&numb[i-13]==0))	 //if false last row
										{
											numb[i] = 0; 
											--wnumb; 
											break; 
										}
									}
									
								}
								
							}
						}
					}
				case 1: 
					if(gcnt>0)
					{
						numb[i] = 1; 
						--gcnt;
						break; 
					}
				case 2:
					if(mcnt>0)
					{
						numb[i] = 2; 
						--mcnt;
						break; 
					}
				default: 
					if(gcnt>0)
					{
						numb[i] = 1; 
						--gcnt;
						break; 
					}
					else
					{
						numb[i] = 0; 
						--wnumb;
						break; 
					}
			
			}
		}
		
		
		//------------show map--------------
		wcnt = 0; 
		gcnt = 0; 
		mcnt = 0; 
		
		System.out.println("This is your Map: " + "\n");
		for(int i = 0; i<32; i++)
		{
			if(i%8==0)
				System.out.print("\n");
			switch(numb[i])
			{
				case 0:
					wcnt++; 
					break;
				case 1: 
					gcnt++;
					break; 
				case 2: 
					mcnt++;
			}
			
			System.out.print(numb[i]);
		
		}
		
		System.out.print("\n");
		System.out.print("\n");
		
		System.out.println("Water: " + wcnt);
		System.out.println("Grass: " + gcnt);
		System.out.println("Mount: " + mcnt);
		
		
		int ranXY = rand.nextInt((31 - 0) + 1) + 0;							//random coordinate of treasure
		
		while(numb[ranXY]==0 || numb[ranXY]==2)												//while coordinate is on water check next coordinate
		{
			
			if(ranXY == 31)
				ranXY = 0; 
			else
				ranXY++; 
		}

		
		//System.out.println("Random Coordinate: "+ ranXY);											
		
		int x, y; 
		
		x=ranXY%8; 
		y=ranXY/8; 
		System.out.println(x + " " + y);
		Map map = new Map(1, x, y); 
		MessMap messMap = new MessMap(numb, map); 
		
		return messMap; 
		
		
	}
	
	public static void showplayinMap(FullMap m)
	{
		for(int i = 0; i<64; i++)
		{
			if(i%8==0)
				System.out.print("\n");
			if(playerNum)
			{
				if((currentx+currenty*8)==i)
				{
					System.out.print("(" + m.getNumb()[i] + ")");
			
				}
				else if((m.getCastle_x1()+m.getCastle_y1()*8)==i)
				{
					System.out.print("|" + m.getNumb()[i] + "|");
				}
				else if((m.getCastle_x1()+m.getCastle_y1()*8)==i-1 || (currentx+currenty*8)==i-1)
				{
						if(i%8==0)
							System.out.print(" ");	
						System.out.print(m.getNumb()[i]);
				}
				
				else
				{	
					System.out.print(" "+ m.getNumb()[i]);
				}
			}
			else {
				
				if((currentx+currenty*8)==i)
				{
					System.out.print("(" + m.getNumb()[i] + ")");
			
				}
				else if((m.getCastle_x2()+m.getCastle_y2()*8)==i-1 || (currentx+currenty*8)==i-1)
				{
					if(i%8==0)
						System.out.print(" ");	
						System.out.print(m.getNumb()[i]);
				}
				else if((m.getCastle_x2()+m.getCastle_y2()*8)==i)
				{
					System.out.print("|" + m.getNumb()[i] + "|");
				}
				else
				{	
					System.out.print(" "+ m.getNumb()[i]);
				}
				
			}
		
		}
		System.out.println("\n");
	}
}
