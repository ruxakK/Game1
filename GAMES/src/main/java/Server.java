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
			
			while(turns < 200)
			{
				
				turn = !turn; 
				if(turn)
				{
					if(!gameHandler(writer1, reader1, player1))
					{	
						MoveResponse movere = new MoveResponse();
						movere.setStatus(2);
						reader2.readLine(); 
						writer2.write(gson.toJson(movere)+ "\n");
						writer2.flush();
						break; 
					}
						
				}	
				else
				{
					if(!gameHandler(writer2, reader2, player2))
					{	
						MoveResponse movere = new MoveResponse();
						movere.setStatus(2);
						reader1.readLine(); 
						writer1.write(gson.toJson(movere) + "\n");
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
	
	public static void sendToDB()
	{
		configureSessionFactory();
		Session session = null; 
		Transaction tx = null; 
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Map m = new Map(1,1,1);
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
	
	static boolean gameHandler(OutputStreamWriter writer, BufferedReader reader, Player player) throws IOException //if true player lost game
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
					movere.setX(move.getX()+1);
					movere.setY(move.getY());
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
					movere.setX(move.getX()-1);
					movere.setY(move.getY());
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
					movere.setX(move.getX());
					movere.setY(move.getY()-1);
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
					movere.setX(move.getX());
					movere.setY(move.getY()+1);
					movere.setStatus(0);
				}
				break;
			default:
				return true; 
		}
		
		System.out.println(gson.toJson(movere));
		
		writer.write(gson.toJson(movere) + "\n");
		writer.flush();
		
		if(movere.isStatus()==0)
			return true;
		else 
			return false; 
	}

}
