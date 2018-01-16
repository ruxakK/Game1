import javax.persistence.*;

@Entity
@Table(name="Move")
public class Move {
	
	@Id
	@GeneratedValue(generator="sqlite")
		@TableGenerator(name="sqlite", table="id_gen",
	    pkColumnName="name", valueColumnName="value",
	    pkColumnValue="SEQUENCE1",  allocationSize=1)
	private int id; 
	@Column(name="direction")
	private int direction; 
	
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
	
	public Move(int id, int direction, int x, int y) {
		
		if(id<0)
			throw new IllegalArgumentException("ID is negative"); 
		this.id=id; 
		
		if(direction<0 || direction>3)
			throw new IllegalArgumentException("Direction is invalid"); 
		
		if(!check_xy(x, y))
			throw new IllegalArgumentException("Coordinate out of range");
		
		this.x=x;
		this.y=y;
		
	}
	
	public Move(int direction)
	{
		if(direction<0 || direction>3)
			throw new IllegalArgumentException("Direction is invalid"); 
		this.direction=direction; 
	}
	
	public Move()
	{
		
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Return the direction 
	 * 0 is right
	 * 1 is left
	 * 2 is up 
	 * 3 is down 
	 * @return
	 */
	public int getDIR()
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
