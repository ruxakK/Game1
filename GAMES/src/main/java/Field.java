import javax.persistence.*;

@Entity 
@Table(name="Field")
public class Field {
	
	@Id
	@Column(name="id")
	private int id; 
	
	@Column(name="val")
	private int val; 
	
	@Column(name="x")
	private int x; 
	
	@Column(name="y")
	private int y; 
	
	public Field(int val, int x, int y)
	{
		if(val<0 || val >2)
			throw new IllegalArgumentException("Value of field not allowed"); 
		if(x<0 || x>7 || y<0 || y>7)
			throw new IllegalArgumentException("Field coordinates out of range"); 
		
		this.val=val; 
		this.x=x; 
		this.y=y; 
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getVal() {
		return val;
	}


	public void setVal(int val) {
		this.val = val;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	
	

}
