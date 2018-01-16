
public class MessMap {
	
	private int [] numb = new int [32]; 
	private Map map; 
	
	public MessMap()
	{
		
	}
	
	public MessMap(int [] numb, Map map)
	{
		this.numb=numb; 
		this.map=map; 
	}

	public MessMap(int [] numb)
	{
		this.numb=numb; 
	}
	
	public int[] getNumb() {
		return numb;
	}

	public void setNumb(int[] numb) {
		this.numb = numb;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public int getFieldVal(int x, int y)
	{
		if(x<0 || x >7 || y<0 || y>3)
			throw new IllegalArgumentException("Coordinate out of range"); 
		int xy = y*8+x; 
		return numb[xy]; 
	}
	
	public void showNumb()
	{
		for(int i = 0; i<32; i++)
		{
			if(i%8==0)
				System.out.print("\n");
			
			System.out.print(numb[i] + " ");
		
		}
	}

}
