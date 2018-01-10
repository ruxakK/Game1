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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class Server {
	
	private static SessionFactory sessionFactory = null; 
	private static SessionFactory configureSessionFactory() throws HibernateException {
		sessionFactory = new Configuration()
				.configure()
				.buildSessionFactory();
		
		return sessionFactory; 
	}

	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(9999); 
		
		System.out.println("Server ready");
		Socket splayer1 = serverSocket.accept();	
		
		OutputStreamWriter writer = new OutputStreamWriter(splayer1.getOutputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(splayer1.getInputStream(), "UTF-8"));
		
		String line = reader.readLine(); 
		
		Gson gson = new Gson(); 
		
		Map m = gson.fromJson(line, Map.class);
		System.out.println(m.getCX());
		
		splayer1.close();
	}
	
	public void sendToDB()
	{
		configureSessionFactory();
		Session session = null; 
		Transaction tx = null; 
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Map m1 = new Map(1, 0 ,0, 0, 0); 
			session.save(m1); 
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

}
