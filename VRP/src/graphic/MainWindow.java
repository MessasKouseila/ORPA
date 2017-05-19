package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import Statistics.Statistique;
import gurobi.GRBException;
import instance.Instance;
import solver.Vrp_solver;

public class MainWindow {
	
	private Vrp_solver instanceOfSolver;
	private Statistique instanceOfStat;
	private GraphPrinter instanceOfPrint;
	private Instance tmpInst;
	private int clicked = 0;
	private int[] instant;
	private Map<Integer, Integer> solTime; 
	private JFrame frame;
	private JDesktopPane container;
	private JInternalFrame instance;
	private JInternalFrame stat;
	private JInternalFrame solution;
	private JPanel SolutionPage;
	
	JCheckBoxMenuItem checkBoxMenuItem;
	JCheckBoxMenuItem checkBoxMenuItem_1;
	JCheckBoxMenuItem checkBoxMenuItem_2;
	int width = 768;
	int height = 1024;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
					window.container.setVisible(true);
					window.instance.setVisible(true);
					window.stat.setVisible(true);
					window.solution.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * the contructor
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		height = (int)dimension.getHeight();
		width  = (int)dimension.getWidth();
		
		frame = new JFrame();
		frame.setTitle("ORPA");
		frame.setAlwaysOnTop(false);
		frame.setBounds((width - 1024) /  2, (height - 680) / 2, 1024, 680);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(new Color(109, 132, 180));
		
		container = new JDesktopPane();
		container.setSize(frame.getWidth(), frame.getHeight());
		frame.getContentPane().add(container);
		
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				container.setBounds(0, 0, frame.getWidth(), frame.getHeight());
				instance.setBounds(0, 0, (int)(frame.getWidth()*0.60), (int)(frame.getHeight() - 50));
				stat.setBounds(instance.getX() + instance.getWidth() , instance.getY(),(int)(frame.getWidth()*0.40), (int)(frame.getHeight() * 0.48));
				solution.setBounds(instance.getX() + instance.getWidth(), stat.getY() + stat.getHeight(), (int)(frame.getWidth()*0.40), (int)(frame.getHeight() * 0.448));
			}
		});
		
		
		
		instance = new JInternalFrame("INSTANCE", true, true, true);
		instance.setBounds(0, 0, (int)(frame.getWidth()*0.60), (int)(frame.getHeight() - 50));
		instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instance.getContentPane().setBackground(Color.LIGHT_GRAY);
		container.add(instance);
		instance.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				instance.moveToFront();
				solution.moveToBack();
				stat.moveToBack();
			}
		});
		
		

		stat = new JInternalFrame("Statistics", true, true, true);
		stat.setBounds(instance.getX() + instance.getWidth() , instance.getY(),(int)(frame.getWidth()*0.40), (int)(frame.getHeight() * 0.48));
		stat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//stat.getContentPane().setLayout(null);
		stat.getContentPane().setBackground(Color.LIGHT_GRAY);
		container.add(stat);
		stat.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				instance.moveToBack();
				solution.moveToBack();
				stat.moveToFront();
			}
		});
		
		

		solution = new JInternalFrame("Solution", true, true, true);
		solution.setBounds(instance.getX() + instance.getWidth(), stat.getY() + stat.getHeight(), (int)(frame.getWidth()*0.40), (int)(frame.getHeight() * 0.448));
		solution.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		solution.getContentPane().setLayout(null);
		solution.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		SolutionPage = new JPanel();
		SolutionPage.setBounds(3, 25, (int)(solution.getWidth() - 18), (int)(solution.getHeight() - 60));
		
		solution.getContentPane().add(SolutionPage);
		SolutionPage.setLayout(new BorderLayout());
		
		JLabel instantT = new JLabel("instant t = "+clicked);
		instantT.setBounds(12, 0, 120, 25);
		solution.getContentPane().add(instantT);
		
		JButton nextPage = new JButton(">");
		nextPage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (clicked < instant.length - 1) {
					clicked++;
					//instanceOfPrint = new GraphPrinter(instance, tmpInst);
					SolutionPage.removeAll();
					SolutionPage.add(instanceOfPrint.graphSol(solTime.get(instant[clicked])), BorderLayout.CENTER);
					SolutionPage.revalidate();
					SolutionPage.repaint();
					instantT.setText("instant t = " + instant[clicked]);
				}
				
			}
		});
		nextPage.setBounds(216, 0, 50, 25);
		solution.getContentPane().add(nextPage);
		
		JButton previewPage = new JButton("<");
		previewPage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (clicked > 0) {
					clicked--;
					//instanceOfPrint = new GraphPrinter(instance, tmpInst);
					SolutionPage.removeAll();
					SolutionPage.add(instanceOfPrint.graphSol(solTime.get(instant[clicked])), BorderLayout.CENTER);
					SolutionPage.revalidate();
					SolutionPage.repaint();
					instantT.setText("instant t = " + instant[clicked]);
				}	
			}
		});
		previewPage.setBounds(150, 0, 50, 25);
		solution.getContentPane().add(previewPage);
		
		container.add(solution);
		solution.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				instance.moveToBack();
				solution.moveToFront();
				stat.moveToBack();
				SolutionPage.setBounds(3, 25, (int)(solution.getWidth() - 18), (int)(solution.getHeight() - 60));
			}
		});
		
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBackground(new Color(109, 132, 180));
		
		// menu PROBLEM
		JMenu menu_Problem = new JMenu("Problem");
		menuBar.add(menu_Problem);
		JMenuItem Problem_new = new JMenuItem("New");
		JTextField numberNode = new JTextField(5);
	    JTextField timeSimul = new JTextField(5);
		Problem_new.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

			    JPanel myPanel = new JPanel();
			    myPanel.add(new JLabel("stations :"));
			    myPanel.add(numberNode);
			    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			    myPanel.add(new JLabel("duration :"));
			    myPanel.add(timeSimul);
			    int nbr = 0;
			    int timeS = 0;
			    int result = JOptionPane.showConfirmDialog(null, myPanel,
			        "number of stations and Duration of simulation", JOptionPane.OK_CANCEL_OPTION);
			    if (result == JOptionPane.OK_OPTION) {
			      nbr = Integer.parseInt(numberNode.getText());
			      timeS = Integer.parseInt(timeSimul.getText());
			    }
			    
				/*jop = new JOptionPane();
			    int nbr = Integer.parseInt(JOptionPane.showInputDialog(null, "Saisir le nombre de Station", "Generateur d'instance", JOptionPane.QUESTION_MESSAGE));		
				System.out.println("Generation en cours avec "+nbr+" stations");*/
			    
				tmpInst = Instance.getNewInstance(nbr, timeS);
				SolutionPage.removeAll();
				SolutionPage.revalidate();
				SolutionPage.repaint();
				clicked = 0;
				instantT.setText("instant t = " + clicked);
				instanceOfPrint = new GraphPrinter(instance, tmpInst);
				instance.getContentPane().removeAll();
				instance.getContentPane().add(instanceOfPrint.graphIt());
				instance.repaint();
				instance.revalidate();
			}
		});
		menu_Problem.add(Problem_new);
		
		JMenuItem Problem_Open = new JMenuItem("Open");
		menu_Problem.add(Problem_Open);
		JMenuItem Problem_Save = new JMenuItem("Save");
		menu_Problem.add(Problem_Save);
		JMenuItem Problem_Close = new JMenuItem("Close");
		Problem_Close.addMouseListener(new MouseAdapter() {
			//@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		menu_Problem.add(Problem_Close);
		
		// menu METHODS
		JMenu menu_Methods = new JMenu("Methods");
		menuBar.add(menu_Methods);
		//menu_Methods.add(group);
		
		checkBoxMenuItem = new JCheckBoxMenuItem("Modèle", true);
		checkBoxMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkBoxMenuItem.getState()) {
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem_2.setSelected(false);
				}					
			}
		});
		menu_Methods.add(checkBoxMenuItem);
		
		checkBoxMenuItem_1 = new JCheckBoxMenuItem("Heuristique 1", false);
		checkBoxMenuItem_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkBoxMenuItem_1.getState()) {
					checkBoxMenuItem.setSelected(false);
					checkBoxMenuItem_2.setSelected(false);
				}
					
			}
		});
		menu_Methods.add(checkBoxMenuItem_1);
		
		checkBoxMenuItem_2 = new JCheckBoxMenuItem("Heuristique 2", false);
		checkBoxMenuItem_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkBoxMenuItem_2.getState()) {
					checkBoxMenuItem_1.setSelected(false);
					checkBoxMenuItem.setSelected(false);
				}
			}
		});
		menu_Methods.add(checkBoxMenuItem_2);
		
		// menu solution et aide
		JMenu menu_Solution = new JMenu("Solution");
		menuBar.add(menu_Solution);
		
		JMenuItem LaunchSol = new JMenuItem("Launch resolution");
		menu_Solution.add(LaunchSol);
		LaunchSol.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (instanceOfPrint != null && instanceOfPrint.instance != null)
				if (checkBoxMenuItem.isSelected()) {
					try {
						instanceOfSolver = new Vrp_solver(tmpInst.numberOfNode, tmpInst.t, tmpInst.T, tmpInst.M, tmpInst.R, tmpInst.r, tmpInst.d, tmpInst.alpha);
						Thread solveIt = new Thread(instanceOfSolver);
						//instanceOfSolver.resolve();
						solveIt.start();
						//System.out.println("temps d'execution : " + (System.currentTimeMillis() - t) );
						solveIt.join();
						solTime = instanceOfSolver.result();
						instant = new int [solTime.size() + 1];
						int klm = 0;
						solTime.put(0, 0);
						for(Entry<Integer, Integer> entry : solTime.entrySet()) {
						    System.out.println(entry.getKey() + " ==> " + entry.getValue());
						    instant[klm] = entry.getKey();
						    klm++;
						}
						//tmpInst = Instance.getInstance();
						//instanceOfPrint = new GraphPrinter(instance, tmpInst);
						SolutionPage.removeAll();
						SolutionPage.add(instanceOfPrint.graphSol(0), BorderLayout.CENTER);
						SolutionPage.revalidate();
						SolutionPage.repaint();
						
					} catch (GRBException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (checkBoxMenuItem_1.isSelected()) {
					try {
						System.out.println("methode heuristique 1");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (checkBoxMenuItem_2.isSelected()) {
					try {
						System.out.println("methode heuristique 2");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		// Help menu
		JMenu menu_Help = new JMenu("Help");
		menuBar.add(menu_Help);
		
	}
}
