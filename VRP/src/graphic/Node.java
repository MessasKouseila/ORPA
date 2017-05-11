package graphic;

public class Node {
	
	
	public Object racine;
	public int num;
	public int pos_X;
	public int pos_Y;
	
	
	public Node(int x, int y, int num) {
		racine = null;
		this.num = num;
		this.pos_X = x;
		this.pos_Y = y;
	}
}
