
public class MoveResponse {
	private int status; 
	//0...move accepted
	//1...move failed
	//2...won the game
	//3..found a treasure
	private int x; 
	private int y; 
	
	public MoveResponse()
	{
		
	}

	public int isStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
