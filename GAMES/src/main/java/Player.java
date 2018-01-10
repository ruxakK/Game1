import javax.persistence.*;

@Entity 
@Table(name="Player")
public class Player {
	
	@Id
	@Column(name="ID")
	private int id; 
	
	@Column(name="name")
	private String name; 
	
	public 	Player(int id, String name)
			{
				this.id=id; 
				this.name=name; 
			}

}
