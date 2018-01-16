import java.io.IOException;
import java.util.Random;

import com.google.gson.Gson;

public class test {

	public static void main(String[] args) throws IOException {
		int numb [] = new int [64]; 
		
		
		for(int i=0; i<32; i++)
		{
			numb[i]=i; 
		}
		
		FullMap f = new FullMap(1,1,1,1,numb,1,1,1,1); 
		
		f.insertInto(numb, true);
		f.insertInto(numb,false);
	
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(f));
		
	}

}
