package graphic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import instance.Instance;
/**
 * 
 * @author kouceila
 *
 */
public class GraphPrinter {
	
	public JInternalFrame framePrint;
	public Instance instance;
	public mxGraph graph;
	public Node[] nodes;
	private int hight;
	private Map<Integer, Position>positions;
	
	/**
	 * 
	 * @param frame
	 * @param insttance
	 */
	public GraphPrinter(JInternalFrame frame, Instance instance) {
		
		this.framePrint = frame;
		this.graph = new mxGraph();
		this.instance = instance;
		this.nodes = new Node[instance.numberOfNode];
		this.hight = frame.getHeight();
		this.positions = new HashMap<Integer, Position>();
		this.initPos();
	}
	/**
	 * création du graph
	 * @param parent
	 */
	public void initNodes(mxGraph graph, Object parent) {
		for (int i = 1; i < this.instance.numberOfNode; ++i) {
			this.nodes[i].racine = graph.insertVertex(parent, null, this.nodes[i].num, this.nodes[i].pos_X, this.nodes[i].pos_Y, 40, 40, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
		}
	}
	public void color(Object parent, int node) {
		String styleBase = mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE +";"+ mxConstants.STYLE_STROKECOLOR + "=black;"+ mxConstants.STYLE_FILLCOLOR + "=red";
			this.nodes[node].racine = this.graph.insertVertex(parent, null, this.nodes[node].num, this.nodes[node].pos_X, this.nodes[node].pos_Y, 40, 40, styleBase);
	}
	/**
	 * génération des connexion entre nodes
	 */
	public void initRelation(mxGraph graph, Object parent) {
		
		for (int i = 0; i < this.instance.lien.length; ++i) {
			for (int j = 0; j < this.instance.lien.length; ++j) {
				if (this.instance.lien[i][j]) {
					graph.insertEdge(parent, null, "", nodes[i].racine, nodes[j].racine);
				}
			}
		}
	}
	/**
	 * génération des positions sur le graph
	 */
	public void initPos() {
		
		this.positions.put(0, new Position(30, this.hight * 0.80));
		this.positions.put(1, new Position(this.positions.get(0).x + 20, this.positions.get(0).y - Instance.Rand(100, 120)));
		this.positions.put(2, new Position(this.positions.get(0).x + 100, this.positions.get(0).y - 30));
		
		for (int i = 3; i < this.instance.lien.length; ++i) {	
			this.positions.put(i, new Position(this.positions.get(i - 2).x + Instance.Rand(35, 50), this.positions.get(i - 2).y - Instance.Rand(60, 90)));
		}	
		
		/*double lastX = ( this.positions.get(this.instance.lien.length - 2).x 
				+ this.positions.get(this.instance.lien.length - 3).x ) /2;
		double lastY = ( this.positions.get(this.instance.lien.length - 2).y 
				+ this.positions.get(this.instance.lien.length - 3).y ) /2;*/
		
		//this.positions.put(this.instance.lien.length - 1, new Position(lastX + 30, lastY));
		
		for (int i = 0; i < this.instance.numberOfNode; ++i) {
			this.nodes[i] = new Node(this.positions.get(i).x,this.positions.get(i).y, i);
		}
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public mxGraphComponent graphIt() {
		
		Object parent = this.graph.getDefaultParent();
		this.graph.getModel().beginUpdate();
		this.initNodes(this.graph, parent);
		this.nodes[0].racine = null;
		String styleBase = mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE +";"+ mxConstants.STYLE_STROKECOLOR + "=black;"+ mxConstants.STYLE_FILLCOLOR + "=green";
		
		this.nodes[0].racine = this.graph.insertVertex(parent, null, "0", this.nodes[0].pos_X,this.nodes[0].pos_Y, 40, 40, styleBase);
		this.initRelation(this.graph, parent);
		this.graph.getModel().endUpdate();
		return new mxGraphComponent(this.graph);	
	}
	
	public mxGraphComponent graphSol(int node) {
		
		mxGraph tmp_graph = new mxGraph();
		Object parent = tmp_graph.getDefaultParent();
		tmp_graph.getModel().beginUpdate();
		this.initNodes(tmp_graph, parent);
		this.nodes[0].racine = null;
		String styleBase = mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE +";"+ mxConstants.STYLE_STROKECOLOR + "=black;"+ mxConstants.STYLE_FILLCOLOR + "=green";
		this.nodes[0].racine = tmp_graph.insertVertex(parent, null, "0", this.nodes[0].pos_X,this.nodes[0].pos_Y, 40, 40, styleBase);
		this.color(parent, node);
		this.initRelation(tmp_graph, parent);
		tmp_graph.getModel().endUpdate();
		return new mxGraphComponent(tmp_graph);
	}
	
	public static void main(String[] args) {
		JFrame f1 = new JFrame("teste fenetre");
		f1.setSize(600, 600);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.getContentPane().setLayout(null);

		JInternalFrame f2 = new JInternalFrame("interne", true, true, true);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setBounds(0, 0, (int)(f1.getWidth() * 0.45), (int)(f1.getHeight()));
		f2.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		JInternalFrame f3 = new JInternalFrame("interne", true, true, true);
		f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f3.setBounds( (int) (f1.getWidth() * 0.55), 0, (int)(f1.getWidth() * 0.45), (int)(f1.getHeight()));
		f3.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		f1.getContentPane().add(f2);
		f1.getContentPane().add(f3);
		
		f1.setVisible(true);
		f2.setVisible(true);
		f3.setVisible(true);
		
		Instance i = Instance.getNewInstance(15, 10);
		GraphPrinter g1 = new GraphPrinter(f3, i);
		f3.getContentPane().add(g1.graphIt());
		f3.repaint();
		f1.repaint();
		f3.revalidate();
		f1.revalidate();
	}
}
