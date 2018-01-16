import javax.persistence.*;

@Entity 
@Table(name="Map")
public class Map {
	
	@Id
	@Column(name="id")
	private int id; 

	@Column(name="castle_x")
	private int castle_x; 
	@Column(name="castle_y")
	private int castle_y; 
	
	@Column(name="treasure_x")
	private int treasure_x; 
	@Column(name="treasure_y")
	private int treasure_y; 
	
	private boolean check_xy(int x, int y)
	{
		if(x<0 || x>7 || y<0 || y>3)
			return false; 
		return true; 
		
	}
	
	public Map(int id, int castle_x, int castle_y, int treasure_x, int treasure_y)
	{
		if(id<0)
			throw new IllegalArgumentException("ID is negative"); 
		this.id=id; 
		
		if(!check_xy(castle_x, castle_y))
			throw new IllegalArgumentException("Castle coordinate out of range");
		
		if(!check_xy(treasure_x, treasure_y))
			throw new IllegalArgumentException("Treasure Coordinate out of range");
		
		this.castle_x=castle_x; 
		this.castle_y=castle_y;
		
		this.treasure_x=treasure_x; 
		this.treasure_y=treasure_y; 
		
	}
	
	public Map(int id, int treasure_x, int treasure_y)
	{
		if(id<0)
			throw new IllegalArgumentException("ID is negative"); 
		this.id=id; 
		
		if(!check_xy(treasure_x, treasure_y))
			throw new IllegalArgumentException("Treasure Coordinate out of range");
		
		this.treasure_x=treasure_x; 
		this.treasure_y=treasure_y; 
		
	}
	
	/**
	 * Returns x coordinate of the castle
	 * @return
	 */
	public int getCX()
	{
		return castle_x;
	}
	
	/**
	 * Returns y coordinate of castle 
	 * @return
	 */
	public int getCY()
	{
		return castle_y;
	}
	
	/**
	 * Returns x coordinate of treasure
	 * @return
	 */
	public int getTX()
	{
		return treasure_x;
	}
	
	
	/**
	 * Returns y coordinate of treasure
	 * @return
	 */
	public int getTY()
	{
		return treasure_y;
	}
	
	/**
	 * Returns id
	 * @return
	 */
	public int getID()
	{
		return id; 
	}
	
	public void setCX(int cx)
	{
		if(cx<0 || cx>7)
			throw new IllegalArgumentException("Coordinate X out of range");
		castle_x=cx; 
	}
	
	public void setCY(int cy)
	{
		if(cy<0 || cy>3)
			throw new IllegalArgumentException("Coordinate Y out of range");
		castle_y=cy; 
	}
	
	
	
}
