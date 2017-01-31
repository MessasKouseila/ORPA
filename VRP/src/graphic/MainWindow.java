package graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import Statistics.Statistique;
import solver.*;

public class MainWindow {
	
	private Solver instanceOfSolver;
	private Statistique instanceOfStat;
	private GraphPrinter instanceOfPrint;
	private GenerateInstance instanceOfGenerator;
	
	private JFrame frame;
	private JDesktopPane container;
	private JInternalFrame instance;
	private JInternalFrame stat;
	private JInternalFrame solution;
	int width = 768;
	int height = 1024; 
	private final ButtonGroup buttonGroup = new ButtonGroup();
	

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
		instanceOfGenerator = new GenerateInstance();
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
		instance.getContentPane().setLayout(null);
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
		stat.getContentPane().setLayout(null);
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
		container.add(solution);
		solution.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				instance.moveToBack();
				solution.moveToFront();
				stat.moveToBack();
			}
		});
		
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBackground(new Color(109, 132, 180));
		
		// menu PROBLEM
		JMenu menu_Problem = new JMenu("Problem");
		menuBar.add(menu_Problem);
		JMenuItem Problem_new = new JMenuItem("New");
		Problem_new.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				instanceOfGenerator.setVisible(true);
				System.out.println("Generation en cours ...");
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
		JCheckBoxMenuItem methode_1 = new JCheckBoxMenuItem("Methode 1");
		buttonGroup.add(methode_1);
		menu_Methods.add(methode_1);
		JCheckBoxMenuItem methode_2 = new JCheckBoxMenuItem("Methode 2");
		buttonGroup.add(methode_2);
		menu_Methods.add(methode_2);
		JCheckBoxMenuItem methode_3 = new JCheckBoxMenuItem("Methode 3");
		buttonGroup.add(methode_3);
		menu_Methods.add(methode_3);
		
		// menu solution et aide
		JMenu menu_Solution = new JMenu("Solution");
		menuBar.add(menu_Solution);
		JMenu menu_Help = new JMenu("Help");
		menuBar.add(menu_Help);
		
	}
}
