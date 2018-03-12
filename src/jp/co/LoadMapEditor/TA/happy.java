package jp.co.LoadMapEditor.TA;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class happy {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			happy window = new happy();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//EventQueue.invokeLater(new Runnable() {
			//public void run() {
				
			//}
		//});
	
	}

	/**
	 * Create the application.
	 */
	public happy() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		device.setFullScreenWindow(frame);
		
		//frame.getContentPane().setLayout(null);
		
		
		
		
		
		
		
		frame.setVisible(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(d.width/2);
		
		JLabel label = new JLabel("瀬田先生");
		Font font = new Font("Arial", Font.BOLD, 100);
		label.setForeground(Color.RED);
		label.setFont(font);
		label.setBounds(d.width/2, d.height/2-500, 1000, 500);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("<html><h1 style=\"color:red;size:500px;\">お誕生日</h1></html>");
		label_1.setBounds(d.width/2, d.height/2-300, 500, 500);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("<html><h1 style=\"color:red;size:500px;\">おめでとうございます</h1></html>");
		label_2.setBounds(d.width/2-60, d.height/2-100, 500, 500);
		frame.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("");
		label_3.setBounds(d.width/2+20, d.height/2+20, 0, 0);
		frame.getContentPane().add(label_3);
		
	}
}
