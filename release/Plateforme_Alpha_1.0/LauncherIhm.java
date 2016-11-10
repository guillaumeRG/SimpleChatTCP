import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LauncherIhm {
	JTextField input;
	static JTextArea display;
	JFrame frame;
	JFrame secondaire;

	JTextField InO;
	JTextField InG;
	JMenuItem menuItem;
	JMenu menu;
	JMenu menu2;
	JMenuBar menuBar;
	JButton creerPlanning;
	JSlider slider;
	JLabel volume;

	String[] bufferField = new String[3];
	String[] bufferFieldO = new String[3];
	static int id = 0;
	static int recup = 0;

	ChatClient client1;
	String login;
	boolean connectChatIsAlive = false; // si connecté au chat
	
	JFrame frameConnect;
	boolean frameConnectIsAlive = false; // si la frame de connection est affiché
	JTextField fieldLogin;

	public void creerFenetre(String nom, int longueur, int largeur) {
		frame = new JFrame(nom);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int reponse = JOptionPane
						.showConfirmDialog(frame,
								"Voulez-vous quitter l'application ?", "Confirmation",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (reponse == JOptionPane.YES_OPTION) {
					// A LA FERMETURE DE LA FRAME
					if (connectChatIsAlive) {
						client1.envoyer("CLOSE",null);
						connectChatIsAlive = false;
					}
					if(frameConnectIsAlive)
					{
						frameConnect.dispose();
						frameConnectIsAlive = false;
					}
					frame.dispose();
				}
			}
		});
		frame.setSize(longueur, largeur);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.repaint();
	}

	public void connection() throws Exception {
		client1 = new ChatClient(login);
		menu.getItem(1).setEnabled(false);
		connectChatIsAlive = true;
	}

	public void creerTextField(String info, int longueur, int largeur, int x,
			int y, boolean editable) {
		input = new JTextField();
		input.setToolTipText(info);
		input.setEditable(editable);
		input.setBounds(x, y, longueur, largeur);
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE ENTER JTEXTFIELD

				System.out.println(e.getActionCommand());

				if (connectChatIsAlive) {

					client1.envoyer("SEND", e.getActionCommand());
					//display.append("\n" + login + " dit:"	+ input.getText().toString());
					input.setText("");
				}
			}
		});

		frame.getContentPane().add(input);
		frame.setVisible(true);
		frame.repaint();

	}

	public void creerTextArea(String init, int longueur, int largeur, int x,
			int y, boolean editable) {

		display = new JTextArea();
		display.setEditable(editable);
		display.setToolTipText(init);

		JScrollPane scroll = new JScrollPane(display,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setBounds(x, y, longueur, largeur);

		display.setLineWrap(true);
		display.setWrapStyleWord(true);

		frame.getContentPane().add(scroll);

		frame.setVisible(true);

	}

	public void creerJMenuItem(String menu1, String bouton1, String bouton2) {
		menuBar = new JMenuBar();
		menu = new JMenu(menu1);

		menuBar.add(menu);

		menuItem = new JMenuItem(bouton1);
		menuItem.setEnabled(true);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE MENU/BOUTTON1 OUVRIR

				JFileChooser chooser = new JFileChooser();
				File f2 = new File("");
				File file = new File(f2.getAbsolutePath());
				chooser.setCurrentDirectory(file);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();

				}

			}
		});

		menu.add(menuItem);

		menuItem = new JMenuItem(bouton2);
		menuItem.setEnabled(true);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE MENU/BOUTTON2 CONNECTION
				frameConnect = new JFrame(bouton2);
				
				frameConnect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				frameConnect.setSize(300, 100);
				frameConnect.setLocationRelativeTo(null);
				frameConnect.setVisible(true);
				frameConnect.setLayout(null);
				frameConnect.repaint();

				JLabel labelLogin = new JLabel("Pseudonyme : ");
				labelLogin.setBounds(20, 20, 100, 20);
				
				frameConnect.add(labelLogin);
				frameConnect.setVisible(true);
				frameConnect.repaint();

				fieldLogin = new JTextField();
				fieldLogin.setToolTipText("Login");
				fieldLogin.setEditable(true);
				fieldLogin.setBounds(105, 20, 150, 20);
				fieldLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						// SI ON CLIQUE SUR LE ENTER LOGIN
						login = e.getActionCommand();
						frameConnect.dispose();
						try {
							connection();
							
						} catch (Exception e1) {
							
							
							e1.printStackTrace();
							
							JOptionPane.showMessageDialog(
									frame,
									"Erreur à la connection !\n"
											+ e1.getMessage(), null, JOptionPane.WARNING_MESSAGE);
						}
						

					}
				});

				frameConnect.getContentPane().add(fieldLogin);
				frameConnect.setVisible(true);
				frameConnect.repaint();
				frameConnectIsAlive = true;
			

			}

		});

		menu.add(menuItem);

		menuBar.add(menu);

		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.repaint();
	}
}
