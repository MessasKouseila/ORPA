package graphic;

import java.awt.Color;

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
		this.initPos();
		
	}
	/**
	 * 
	 * @param parent
	 */
	public void initNodes(Object parent) {
		for (int i = 1; i < this.instance.numberOfNode; ++i) {
			this.nodes[i].racine = this.graph.insertVertex(parent, null, this.nodes[i].num, this.nodes[i].pos_X, this.nodes[i].pos_Y, 30, 30, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
		}
	}
	/**
	 * 
	 */
	public void initRelation(Object parent) {
		
		for (int i = 0; i < this.instance.lien.length; ++i) {
			for (int j = 0; j < this.instance.lien.length; ++j) {
				if (this.instance.lien[i][j]) {
					this.graph.insertEdge(parent, null, "", nodes[i].racine, nodes[j].racine);
				}
			}
		}
	}
	/**
	 * 
	 */
	public void initPos() {
		for (int i = 0; i < this.instance.numberOfNode; ++i) {
			this.nodes[i] = new Node(50 * (i + 1), 50 * (i + 1), i);
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
		this.initNodes(parent);
		
		this.nodes[0].pos_X = 10;
		this.nodes[0].pos_Y = framePrint.getWidth() - 70;
		this.nodes[0].racine = null;
		this.nodes[0].racine = this.graph.insertVertex(parent, null, "0", this.nodes[0].pos_X,this.nodes[0].pos_Y, 30, 30, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);

		this.initRelation(parent);
		this.graph.getModel().endUpdate();
		return new mxGraphComponent(this.graph);	
	}


	
	
	
	public static void main(String[] args) {
		JFrame f1 = new JFrame("teste fenetre");
		f1.setSize(600, 600);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.getContentPane().setLayout(null);

		JInternalFrame f2 = new JInternalFrame("interne", true, true, true);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setBounds(10, 10, (int)(f1.getWidth() * 0.8), (int)(f1.getHeight() * 0.8));
		f2.getContentPane().setBackground(Color.LIGHT_GRAY);
		f1.getContentPane().add(f2);
		f1.setVisible(true);
		f2.setVisible(true);
		Instance i = new Instance(8);
		GraphPrinter g1 = new GraphPrinter(f2, i);
		f2.getContentPane().add(g1.graphIt());
		f2.repaint();
		f1.repaint();
		f2.revalidate();
		f1.revalidate();
	}
}
