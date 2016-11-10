import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import javax.management.timer.Timer;
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



public class ServeurIhm {
	JTextField input;
	static JTextArea displayC;
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
	JLabel connecter;
	JLabel chat;
	

	String[] bufferField = new String[3];
	String[] bufferFieldO = new String[3];
	static int id = 0;
	static int recup = 0;

	JTextField horloge;
	JTextField voyant = new JTextField("Off");
	
	String login;
	boolean connectChatIsAlive = false; // si connecté au chat
	
	JFrame frameConnect;
	boolean frameConnectIsAlive = false; // si la frame de connection est affiché
	JTextField fieldLogin;
	
	JButton boutonDemarre;
	JButton boutonArette;
	
	ChatServer serveur;
	
	public ServeurIhm()
	{
		
	}
	public void creerVoyant(int longueur, int largeur, int x,
			int y)
	{
		voyant.setForeground(new Color(255, 0, 0));
		voyant.setEditable(false);
		voyant.setFont(new Font("sansserif", Font.PLAIN, 22));
		voyant.setBounds(x, y, longueur, largeur);
		frame.getContentPane().add(voyant);
		frame.setVisible(true);
		frame.repaint();
	}
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
					serveur.fin=true;
					try
					{serveur.stopChatServeur();}
					catch(NullPointerException e1)
					{
						
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

				//System.out.println(e.getActionCommand());
				System.out.println("connectChatIsAlive :"+connectChatIsAlive);
				if (connectChatIsAlive) {

					
					//display.append("\n" + login + " dit:"	+ input.getText().toString());
					if(e.getActionCommand().split(":")[0].equals("info"))
					{
						if(e.getActionCommand().split(":")[1].equals("perso"))
						{
							try {
								ChatServer.envoyerInfoPersonnelle(e.getActionCommand().split(":")[2],e.getActionCommand().split(":")[3]);
							} catch (IOException e1) {
								afficherDansConsole("erreur à la commande : "+e.getActionCommand());
								
								e1.printStackTrace();
							}
						}
						else if (e.getActionCommand().split(":")[1].equals("broadcast"))
						{
							try {
								ChatServer.envoyerInfoBroadcast(e.getActionCommand().split(":")[2]);
							} catch (IOException e1) {
								afficherDansConsole("erreur à la commande : "+e.getActionCommand());
						
								e1.printStackTrace();
							}
						}
						else
							afficherDansConsole("erreur syntaxe : "+e.getActionCommand());
					}
					else if(e.getActionCommand().split(":")[0].equals("msg"))
					
					{
						
						if(e.getActionCommand().split(":")[1].equals("perso"))
						{
							try {
								ChatServer.envoyerMessagePersonnel(e.getActionCommand().split(":")[2], e.getActionCommand().split(":")[3], "NOIR");
							} catch (IOException e1) {
								afficherDansConsole("erreur à la commande : "+e.getActionCommand());
								
								e1.printStackTrace();
							}
						}
						else
							afficherDansConsole("erreur syntaxe : "+e.getActionCommand());
						
						
					}
					else if(e.getActionCommand().split(":")[0].equals("kick"))
					{
						if(e.getActionCommand().split(":")[1].equals("perso"))
						{
							try {
								ChatServer.kick(e.getActionCommand().split(":")[2]);
							} catch (IOException e1) {
								afficherDansConsole("erreur à la commande : "+e.getActionCommand());
								e1.printStackTrace();
							}
						}
						
						else
							afficherDansConsole("erreur syntaxe : "+e.getActionCommand());
					}
					else
					{
						try {
							ChatServer.envoyerMessageBroadcast(e.getActionCommand(),"NOIR");
							afficherDansConsole("Info all : "+e.getActionCommand());
						} catch (IOException e1) {
							afficherDansConsole("erreur à la commande : "+e.getActionCommand());
						
							e1.printStackTrace();
						}
					}
					
					
					
					
					
					input.setText("");
				}
				else
				{display.append(e.getActionCommand()+"\n");
				input.setText("");}
			}
		});

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
	public void creerTextArea2(String init, int longueur, int largeur, int x,
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
	public void creerBouton(String init, int longueur, int largeur, int x, int y)
	{
		boutonDemarre = new JButton(init);
		boutonDemarre.setBounds(x, y, longueur, largeur);
		boutonDemarre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE BOUTON DEMARRER
				serveur = new ChatServer();
				serveur.fin=false; //sécurité
				serveur.setDaemon(true);
				serveur.start();
				voyant.setForeground(new Color(0, 255, 0));
				voyant.setText("On");
				boutonDemarre.setEnabled(false);
				boutonArette.setEnabled(true);
				connectChatIsAlive = true;
			}
		});
		frame.add(boutonDemarre);
		frame.setVisible(true);
		frame.repaint();
	}
	public static void afficherDansConsole(String message)
	{
		display.append(getHeur(recupDateActuelle()) + " : " + message + "\n");
	}
	public static void rafraichirCo(String membres)
	{
		String msg = "";
		StringTokenizer st = new StringTokenizer(membres);
		while (st.hasMoreTokens()) {
			msg = msg + st.nextToken()+"\n";

		}
		System.out.println("connecté : "+msg);
		displayC.setText(msg);
		
	}
	public void creerBouton2(String init, int longueur, int largeur, int x, int y)
	{
		boutonArette = new JButton(init);
		boutonArette.setBounds(x, y, longueur, largeur);
		boutonArette.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				// SI ON CLIQUE SUR LE BOUTON ARRETER
				
				serveur.fin=true;
				serveur.stopChatServeur();
				
				voyant.setForeground(new Color(255, 0, 0));
				voyant.setText("Off");
				boutonDemarre.setEnabled(true);
				boutonArette.setEnabled(false);
				connectChatIsAlive = false;
				rafraichirCo("");
			}
		});
		boutonArette.setEnabled(false);
		frame.add(boutonArette);
		frame.setVisible(true);
		frame.repaint();
	}
	public static String getHeur(int[] maDate) {
		return maDate[3] + ":" + maDate[4] + ":" + maDate[5];
	}
	public static int[] recupDateActuelle() {

		int[] maDate = new int[7];
		int[] maDate12Heur = new int[7];
		Date laDate = new Date();
		Date date12Heur = new Date(laDate.getTime() + 12L * Timer.ONE_HOUR);

		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss: dd/MM/yy/");

		maDate12Heur = traductionDate(dateFormat.format(date12Heur));

		maDate = traductionDate(dateFormat.format(laDate));
		// format jj/mm/aa/hh/mm/ss
		// System.out.println("date/heure : "+maDate[0]+"/"+maDate[1]+"/"+maDate[2]+" "+maDate[3]+":"+maDate[4]);
		// System.out.println(date12Heur);

		
			
			
			return maDate;
		
	}
	public static int[] traductionDate(String dateDefault) {
		int[] dateInt = new int[7];

		String[] dateDebutString = new String[2];
		String[] dateString = new String[3];
		String[] heureString = new String[3];

		dateDebutString = dateDefault.split(" ");
		// System.out.println("date/heureS : "+dateDefault);

		heureString = dateDebutString[0].split(":");
		dateString = dateDebutString[1].split("/");

		// System.out.println("test"+dateString[0]);
		// format jj/mm/aa/hh/mm
		dateInt[0] = Integer.parseInt(dateString[0]);

		dateInt[1] = Integer.parseInt(dateString[1]);
		dateInt[2] = Integer.parseInt(dateString[2]);
		dateInt[3] = Integer.parseInt(heureString[0]);
		dateInt[4] = Integer.parseInt(heureString[1]);
		dateInt[5] = Integer.parseInt(heureString[2]);

		return dateInt;
	}
			
	public void creerHorloge(String init, int longueur, int largeur, int x,
			int y) {
		horloge = new JTextField();
		horloge.setEditable(false);
		horloge.setToolTipText(init);
		horloge.setBounds(x, y, longueur, largeur);
		horloge.setFont(new Font("sansserif", Font.PLAIN, 22));
		javax.swing.Timer t = new javax.swing.Timer(1000, new ClockListener());
		t.start();

		frame.getContentPane().add(horloge);
		frame.repaint();

		frame.setVisible(true);
	}

	class ClockListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			horloge.setText(df.format(Calendar.getInstance().getTime()));
		}
	}
}