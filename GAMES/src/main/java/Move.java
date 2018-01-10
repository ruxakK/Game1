import javax.persistence.*;

@Entity
@Table(name="Move")
public class Move {
	
	@Id
	@Column(name="id")
	private int id; 
	@Column(name="direction")
	private String direction; 
	
	@Column(name="x")
	private int x; 
	
	@Column(name="y")
	private int y; 
	
	private boolean check_xy(int x, int y)
	{
		if(x<0 || x>7 || y<0 || y>7)
			return false; 
		return true; 
		
	}
	
	public Move(int id, String direction, int x, int y) {
		
		if(id<0)
			throw new IllegalArgumentException("ID is negative"); 
		this.id=id; 
		
		if(direction!="right"&&direction!="left"&&direction!="up"&&direction!="down")
			throw new IllegalArgumentException("Direction is invalid"); 
		
		if(!check_xy(x, y))
			throw new IllegalArgumentException("Coordinate out of range");
		
		this.x=x;
		this.y=y;
		
	}
	
	/**
	 * Return the direction 
	 * @return
	 */
	public String getDIR()
	{
		return direction; 
	}


	/**
	 * Returns the x coordinate
	 * @return
	 */
	public int getX()
	{
		return x; 
	}
	
	/**
	 * Returns the y coordinate
	 * @return
	 */
	public int getY()
	{
		return y; 
	}

	/**
	 * Returns the id
	 * @return
	 */
	public int id()
	{
		return id; 
	}

}
