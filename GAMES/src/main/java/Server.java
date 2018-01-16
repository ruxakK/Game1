import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


import com.google.gson.Gson;




public class Server {
	
	static FullMap fullmap = new FullMap();
	static boolean mcnt1 = false;
	static boolean mcnt2 = false;
	
	static boolean hasT1 = false; 
	static boolean hasT2 = false; 
	
	private static SessionFactory sessionFactory = null; 
	private static SessionFactory configureSessionFactory() throws HibernateException {
		sessionFactory = new Configuration()
				.configure()
				.buildSessionFactory();
		
		return sessionFactory; 
	}

	public static void main(String[] args) throws IOException
	{

		try {
		
			//register clients
			ServerSocket serverSocket = new ServerSocket(9999); 
			
			System.out.println("Server ready");
			Socket splayer1 = serverSocket.accept();
			
			RegisterThread thread1 = new RegisterThread(splayer1, 1); 
			thread1.run(); 
			
			Socket splayer2 = serverSocket.accept();
			RegisterThread thread2 = new RegisterThread(splayer2, 2); 
			thread2.run();
			
			//send whole map to other client
			
			Player player1 = thread1.getPlayer(); 
			Player player2 = thread2.getPlayer(); 
			
			sendToDB_Player(player1);
			sendToDB_Player(player2);
			
			MessMap m1 = thread1.getPlayer_map();
			MessMap m2 = thread2.getPlayer_map();

			
			fullmap.setTreasure_x1(7-m1.getMap().getTX());
			fullmap.setTreasure_x2(m2.getMap().getTX());
			fullmap.setTreasure_y1(3-m1.getMap().getTY());
			fullmap.setTreasure_y2(4+m2.getMap().getTY());
			fullmap.setCastle_x1(7-m1.getMap().getCX());
			fullmap.setCastle_y1(3-m1.getMap().getCY());
			fullmap.setCastle_x2(m2.getMap().getCX());
			fullmap.setCastle_y2(4+m2.getMap().getCY());
			

			fullmap.insertInto(m1.getNumb(), true);
			fullmap.insertInto(m2.getNumb(), false);
			
			
			OutputStreamWriter writer1 = new OutputStreamWriter(splayer1.getOutputStream(), "UTF-8");
			OutputStreamWriter writer2 = new OutputStreamWriter(splayer2.getOutputStream(), "UTF-8");
			BufferedReader reader1 = new BufferedReader (new InputStreamReader(splayer1.getInputStream(), "UTF-8"));
			BufferedReader reader2 = new BufferedReader (new InputStreamReader(splayer2.getInputStream(), "UTF-8"));
			
			Gson gson = new Gson(); 
			
			fullmap.setCurrentx(fullmap.getCastle_x1());
			fullmap.setCurrenty(fullmap.getCastle_y1());
			
			String line = gson.toJson(fullmap);
			writer1.write(line + "\n");
			writer1.flush();
			
			fullmap.setCurrentx(fullmap.getCastle_x2());
			fullmap.setCurrenty(fullmap.getCastle_y2());
			
			line = gson.toJson(fullmap); 
			writer2.write(line + "\n");
			writer2.flush();
			
			int turns = 0; 
			boolean turn = false; 
			int gameStatus = 1; 
			while(turns < 200)
			{
				
				turn = !turn; 
				if(turn)
				{
					gameStatus=gameHandler(writer1, reader1, 1);
					if(gameStatus==0)
					{	
						MoveResponse movere = new MoveResponse();
						movere.setStatus(2);
						reader2.readLine(); 
						writer2.write(gson.toJson(movere)+ "\n");
						writer2.flush();
						break; 
					}
					else if(gameStatus==2)
					{
						MoveResponse movere = new MoveResponse();
						movere.setStatus(1);
						reader2.readLine(); 
						writer2.write(gson.toJson(movere)+ "\n");
						writer2.flush();
						break; 
					}
				}	
				else
				{
					gameStatus=gameHandler(writer2, reader2, 2);
					if(gameStatus==0)
					{	
						MoveResponse movere = new MoveResponse();
						movere.setStatus(2);
						reader1.readLine(); 
						writer1.write(gson.toJson(movere) + "\n");
						writer1.flush();
						break; 
					}
					else if(gameStatus==2)
					{
						MoveResponse movere = new MoveResponse();
						movere.setStatus(1);
						reader1.readLine(); 
						writer1.write(gson.toJson(movere)+ "\n");
						writer1.flush();
						break; 
					}
					
				}	
				++turns;
			}
			
			splayer1.close();
			splayer2.close();
			
			System.out.println("Game terminated");
			//sendToDB(); 

		}catch(IOException e)
		{
			
		}
		finally
		{
			
		}
	}
	
	public static void sendToDB_Player(Player p)
	{
		configureSessionFactory();
		Session session = null; 
		Transaction tx = null; 
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(p); 
			tx.commit(); 
			
			
		} catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	public static void sendToDB_Move(Move m)
	{
		configureSessionFactory();
		Session session = null; 
		Transaction tx = null; 
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(m); 
			tx.commit(); 
			
			
		} catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(session != null)
				session.close();
		}
	}
	
	static int gameHandler(OutputStreamWriter writer, BufferedReader reader, int player) throws IOException //if true player lost game
	{
		MoveResponse movere = new MoveResponse();
		Gson gson = new Gson(); 
		Move move = gson.fromJson(reader.readLine(), Move.class); 
		
		switch(move.getDIR())
		{
			case 0:
				if(move.getX()==7 || fullmap.getFieldVal(move.getX()+1, move.getY())==0)
				{	
					movere.setStatus(1);
				}
				else 
				{
					if(fullmap.getFieldVal(move.getX()+1, move.getY())==1 || mcnt1 && player ==1 || mcnt2 && player == 2 )
					{
						movere.setX(move.getX()+1);
						if(player==1)
							mcnt1 = false;
						else
							mcnt2 = false; 
					}
					else
					{
						movere.setX(move.getX());
						if(player==1)
							mcnt1 = true;
						else
							mcnt2 = true; 
					}
					movere.setY(move.getY());
					if(player==1&&movere.getX()==fullmap.getTreasure_x1()&&movere.getY()==fullmap.getTreasure_y1() || player==2&&movere.getX()==fullmap.getTreasure_x2()&&movere.getY()==fullmap.getTreasure_y2())
						movere.setStatus(3);
					else
						movere.setStatus(0);
				}
				break; 
			case 1:
				if(move.getX()==0 || fullmap.getFieldVal(move.getX()-1, move.getY())==0)
				{	
					movere.setStatus(1);
		
				}
				else 
				{
					if(fullmap.getFieldVal(move.getX()-1, move.getY())==1 || mcnt1 && player == 1 ||  mcnt2 && player == 2 )
					{
						movere.setX(move.getX()-1);
						if(player==1)
							mcnt1 = false;
						else
							mcnt2 = false; 
					}
					else
					{
						movere.setX(move.getX());
						if(player==1)
							mcnt1 = true;
						else
							mcnt2 = true; 
					}
					movere.setY(move.getY());
					if(player==1&&movere.getX()==fullmap.getTreasure_x1()&&movere.getY()==fullmap.getTreasure_y1() || player==2&&movere.getX()==fullmap.getTreasure_x2()&&movere.getY()==fullmap.getTreasure_y2())
						movere.setStatus(3);
					else
						movere.setStatus(0);
				}
				break;
			case 2:
				if(move.getY()==0 || fullmap.getFieldVal(move.getX(), move.getY()-1)==0)
				{	
					movere.setStatus(1);
				}
				else 
				{
					if(fullmap.getFieldVal(move.getX(), move.getY()-1)==1 || mcnt1 && player == 1 || mcnt2 && player == 2)
					{
						movere.setY(move.getY()-1);
						if(player==1)
							mcnt1 = false;
						else
							mcnt2 = false;  
					}
					else
					{
						movere.setY(move.getY());
						if(player==1)
							mcnt1 = true;
						else
							mcnt2 = true;   
					}
					movere.setX(move.getX());
					if(player==1&&movere.getX()==fullmap.getTreasure_x1()&&movere.getY()==fullmap.getTreasure_y1() || player==2&&movere.getX()==fullmap.getTreasure_x2()&&movere.getY()==fullmap.getTreasure_y2())
						movere.setStatus(3);
					else
						movere.setStatus(0);
				}
				break; 
			case 3:
				if(move.getY()==7 || fullmap.getFieldVal(move.getX(), move.getY()+1)==0)
				{	
					movere.setStatus(1);
				}
				else 
				{
					if(fullmap.getFieldVal(move.getX(), move.getY()+1)==1 || mcnt1 || mcnt2 && player == 2 )
					{
						movere.setY(move.getY()+1);
						if(player==1)
							mcnt1 = false;
						else
							mcnt2 = false; 
					}
					else
					{
						movere.setY(move.getY());
						if(player==1)
							mcnt1 = true;
						else
							mcnt2 = true;
					}
					movere.setX(move.getX());
					if(player==1&&movere.getX()==fullmap.getTreasure_x1()&&movere.getY()==fullmap.getTreasure_y1() || player==2&&movere.getX()==fullmap.getTreasure_x2()&&movere.getY()==fullmap.getTreasure_y2())
						movere.setStatus(3);
					else
						movere.setStatus(0);
				}
				break;
			default:
				return 1; 
		}
		
		System.out.println(gson.toJson(movere));
		
		if(movere.isStatus()==3)
		{
			if(player == 1)
			{
				hasT1=true; 
				fullmap.setTreasure_x1(8);
				fullmap.setTreasure_y1(8);
			}
			else 
			{
				hasT2=true; 
				fullmap.setTreasure_x2(8);
				fullmap.setTreasure_y2(8);
			}
		}

		if(movere.isStatus()==0)
		{
			if(player==1)
			{
				if(hasT1&&movere.getX()==fullmap.getCastle_x2()&&movere.getY()==fullmap.getCastle_y2())
					movere.setStatus(2);
			}
			else
			{
				if(hasT2&&movere.getX()==fullmap.getCastle_x1()&&movere.getY()==fullmap.getCastle_y1())
					movere.setStatus(2);
			}
		}
		
		writer.write(gson.toJson(movere) + "\n");
		writer.flush();
		
		if(movere.isStatus()==1)
			return 0;
		else if(movere.isStatus()==2)
			return 2; 
		else 
		{
			sendToDB_Move(move);
			System.out.println(gson.toJson(movere));
			return 1; 
			
		}
		
	}

}
