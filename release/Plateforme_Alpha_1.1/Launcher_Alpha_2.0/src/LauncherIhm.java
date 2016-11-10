import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	static JTextArea displayC;
	static JTextArea display;
	JFrame frame;
	JMenuItem menuItem;
	static JMenu menu;
	
	JMenuBar menuBar;
	JButton creerPlanning;
	JSlider slider;
	JLabel connecter;
	JLabel chat;

	String[] bufferFieldD = new String[3];
	String[] bufferFieldC = new String[3];
	static int idD = 0;
	static int idC = 0;
	static int recupD = 0;
	static int recupC = 0;

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
		menu.getItem(2).setEnabled(true);
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
					if(!e.getActionCommand().equals(".clear"))
					{
						bufferFieldC[idC] = e.getActionCommand();
					
					
						client1.envoyer("SEND", e.getActionCommand());
						//display.append("\n" + login + " dit:"	+ input.getText().toString());
						input.setText("");
						idC++;
						recupC = 0;
						if (idC > 2)
							idC = 0;
					}
					else
					{
						display.setText("");
						input.setText("");
					}
				}
				else
				{
					bufferFieldD[idD] = e.getActionCommand();
					if(!e.getActionCommand().equals(".clear"))	
					{
						display.append(e.getActionCommand()+"\n");
					
						input.setText("");
					}
					else
					{
						display.setText("");
						input.setText("");
					}
					
					input.setText("");
					idD++;
					recupD = 0;
					if (idD > 2)
						idC = 0;
				}
			}
		});
		KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {

				// SI ON APPUI SUR HAUT
				//printIt("Pressed", keyEvent);
				
				if((keyEvent.getKeyCode() == 38) && (connectChatIsAlive))
				{
					input.setText(bufferFieldC[recupC]);
					recupC++;
					if (recupC > 2)
						recupC = 0;
				}
				else if((keyEvent.getKeyCode() == 38) && (!connectChatIsAlive))
				{
					input.setText(bufferFieldD[recupD]);
					recupD++;
					if (recupD > 2)
						recupD = 0;
				}
				

			}

			

			public void keyReleased(KeyEvent keyEvent) {
				//printIt("Released", keyEvent);
			}

			public void keyTyped(KeyEvent keyEvent) {
				//printIt("Typed", keyEvent);
			}

			/*private void printIt(String title, KeyEvent keyEvent) {
				int keyCode = keyEvent.getKeyCode();
				// System.out.println("code :"+keyCode);
				String keyText = KeyEvent.getKeyText(keyCode);
				System.out.println(title + " : " + keyText + " / "
						+ keyEvent.getKeyChar());
			}*/
		};
		input.addKeyListener(keyListener);

		frame.getContentPane().add(input);
		frame.setVisible(true);
		frame.repaint();

	}
	public void creerJLabelChat( int longueur, int largeur, int x,
			int y)
	{
		chat = new JLabel("Chat");
		
		chat.setBounds(x,y,longueur,largeur);
		
		frame.getContentPane().add(chat);
		frame.setVisible(true);
		frame.repaint();
	}
	public void creerJLabelConnecter( int longueur, int largeur, int x,
			int y)
	{
		connecter = new JLabel("Membres connectés");
		
		connecter.setBounds(x,y,longueur,largeur);
		
		frame.getContentPane().add(connecter);
		frame.setVisible(true);
		frame.repaint();
	}
	public void creerTextAreaConnecter(String init, int longueur, int largeur, int x,
			int y, boolean editable) {

		displayC = new JTextArea();
		displayC.setEditable(editable);
		displayC.setToolTipText(init);

		JScrollPane scroll = new JScrollPane(displayC,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setBounds(x, y, longueur, largeur);

		displayC.setLineWrap(true);
		displayC.setWrapStyleWord(true);

		frame.getContentPane().add(scroll);

		frame.setVisible(true);

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

	public void creerJMenuItem(String menu1, String bouton1, String bouton2,String bouton3) {
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
		
		menuItem = new JMenuItem(bouton3);
		menuItem.setEnabled(true);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE MENU/BOUTTON1 DECONNECTION
				ChatClient.envoyer("CLOSE", "");
				display.append("Déconnecté du serveur\n");
				displayC.setText("");
				menu.getItem(1).setEnabled(true);
				menu.getItem(2).setEnabled(false);
				connectChatIsAlive=false;

				

			}
		});

		menu.add(menuItem);
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		frame.repaint();
		menu.getItem(1).setEnabled(true);
		menu.getItem(2).setEnabled(false);
	}
}