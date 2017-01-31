package graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GenerateInstance extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3006689418498095673L;
	JLabel lblNewLabel;
	JButton btnGenerate;
	private JTextField textField;
	
	public GenerateInstance() {
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)dimension.getHeight();
		int width  = (int)dimension.getWidth();	
		this.setTitle("Generator");
		this.setAlwaysOnTop(false);
		this.setBounds((width - 450) /  2, (height - 300) / 2, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.getContentPane().setBackground(new Color(109, 132, 180));
		
		textField  = new JTextField();
		textField.setBounds(164, 105, 114, 37);
		this.getContentPane().add(textField);
		textField.setColumns(10);
		
		lblNewLabel = new JLabel("Densite : ");
		lblNewLabel.setBounds(72, 111, 80, 25);
		this.getContentPane().add(lblNewLabel);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(164, 222, 117, 25);
		this.getContentPane().add(btnGenerate);
		this.addEvent(this);
	}
	public void addEvent(GenerateInstance instance) {
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					this.generate();
					System.out.println("instances generé avec succés !!");
					instance.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			public void generate() throws IOException {
				String[] cmd = new String[]{"/bin/sh", "/mnt/06A68C79A68C6B4F/master/projet/VRP/rs.sh"};
				Process p = Runtime.getRuntime().exec(cmd);
				p.getInputStream();
				BufferedReader reader = null;
		        reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line;
		        while ((line = reader.readLine()) != null) {
		            System.out.println(line);
	
		        }
			}
		});
	}	
}
