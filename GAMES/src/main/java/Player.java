import java.io.Serializable;

import javax.persistence.*;

@Entity 
@Table(name="Player")
public class Player implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id; 
	
	@Column(name="name")
	private String name; 
	
	public 	Player(int id, String name)
			{
				this.id=id; 
				this.name=name; 
			}
	
	public 	Player(String name)
	{
		this.name=name; 
	}
	
	@Id
	@GeneratedValue(generator="sqlite")
		@TableGenerator(name="sqlite", table="id_gen",
	    pkColumnName="name", valueColumnName="value",
	    pkColumnValue="SEQUENCE",  allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
