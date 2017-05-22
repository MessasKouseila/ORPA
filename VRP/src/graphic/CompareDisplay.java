package graphic;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class CompareDisplay extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTextArea solution1;
	public JTextArea solution2;
	public JTextArea solution3;
	JTabbedPane tabbedPane;
	
	public CompareDisplay() {
		super("Comparaion");
		setBounds(250, 250, 600, 475);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		solution1 = new JTextArea();
		solution1.setEditable(false);
		
		solution2 = new JTextArea();
		solution2.setEditable(false);
		
		solution3 = new JTextArea();
		solution3.setEditable(false);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane panelSol1 = new JScrollPane(solution1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabbedPane.addTab("Modèle", null, solution1, null);
		
		JScrollPane panelSol2 = new JScrollPane(solution2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabbedPane.addTab("Heuristique1", null, solution2, null);
		
		JScrollPane panelSol3 = new JScrollPane(solution3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tabbedPane.addTab("Heuristique2", null, solution3, null);
		
	}

}
