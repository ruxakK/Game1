
public class FullMap {
	private int treasure_x1; 
	private int treasure_y1; 
	private int castle_x1; 
	private int castle_y1; 
	
	private int [] numb = new int [64]; 
	
	private int treasure_x2; 
	private int treasure_y2; 
	private int castle_x2; 
	private int castle_y2;
	
	private int currentx; 
	private int currenty; 
	
	public FullMap()
	{
		
	}
	
	public FullMap(int treasure_x1, int treasure_y1, int castle_x1, int castle_y1, int[] numb, int treasure_x2,
			int treasure_y2, int castle_x2, int castle_y2) {
		super();
		this.treasure_x1 = treasure_x1;
		this.treasure_y1 = treasure_y1;
		this.castle_x1 = castle_x1;
		this.castle_y1 = castle_y1;
		this.numb = numb;
		this.treasure_x2 = treasure_x2;
		this.treasure_y2 = treasure_y2;
		this.castle_x2 = castle_x2;
		this.castle_y2 = castle_y2;
	}


	public int getTreasure_x1() {
		return treasure_x1;
	}


	public void setTreasure_x1(int treasure_x1) {
		this.treasure_x1 = treasure_x1;
	}


	public int getTreasure_y1() {
		return treasure_y1;
	}


	public void setTreasure_y1(int treasure_y1) {
		this.treasure_y1 = treasure_y1;
	}


	public int getCastle_x1() {
		return castle_x1;
	}


	public void setCastle_x1(int castle_x1) {
		this.castle_x1 = castle_x1;
	}


	public int getCastle_y1() {
		return castle_y1;
	}


	public void setCastle_y1(int castle_y1) {
		this.castle_y1 = castle_y1;
	}


	public int[] getNumb() {
		return numb;
	}


	public void setNumb(int[] numb) {
		this.numb = numb;
	}


	public int getTreasure_x2() {
		return treasure_x2;
	}


	public void setTreasure_x2(int treasure_x2) {
		this.treasure_x2 = treasure_x2;
	}


	public int getTreasure_y2() {
		return treasure_y2;
	}


	public void setTreasure_y2(int treasure_y2) {
		this.treasure_y2 = treasure_y2;
	}


	public int getCastle_x2() {
		return castle_x2;
	}


	public void setCastle_x2(int castle_x2) {
		this.castle_x2 = castle_x2;
	}


	public int getCastle_y2() {
		return castle_y2;
	}


	public void setCastle_y2(int castle_y2) {
		this.castle_y2 = castle_y2;
	} 
	
	public int getCurrentx() {
		return currentx;
	}

	public void setCurrentx(int currentx) {
		this.currentx = currentx;
	}

	public int getCurrenty() {
		return currenty;
	}

	public void setCurrenty(int currenty) {
		this.currenty = currenty;
	}

	public void insertInto(int arr[], boolean which)
	{
		if(which)
		{
			for(int i = 0; i<32; i++)
			{
				numb[i]=arr[31-i]; 
			}
		}
		else
		{

			for(int i = 32; i<64; i++)
			{
				numb[i]=arr[i-32]; 
			
			}
		}	
	}
	
	public int getFieldVal(int x, int y)
	{
		if(x<0 || x >7 || y<0 || y>7)
			throw new IllegalArgumentException("Coordinate out of range (FullMap)");
		
		return numb[y*8+x];
	}
}
