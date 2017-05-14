package graphic;

public class Node {
	
	
	public Object racine;
	public int num;
	public double pos_X;
	public double pos_Y;
	
	
	public Node(double x, double y, int num) {
		racine = null;
		this.num = num;
		this.pos_X = x;
		this.pos_Y = y;
	}
}
