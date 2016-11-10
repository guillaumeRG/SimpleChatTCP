import java.awt.Color;
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
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class LauncherIhm {
	JTextField input;
	static JTextArea displayC;
	static JTextArea display;
	static JFrame frame;
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
	static boolean connectChatIsAlive = false; // si connecté au chat
	
	JFrame frameConnect;
	boolean frameConnectIsAlive = false; // si la frame de connection est affiché
	JTextField fieldLogin;
	
	static JTextPane textPane;
	StringBuilder sb;
	static StyledDocument doc;
	//SimpleAttributeSet style_normal;
	static SimpleAttributeSet styleMessage;
	static SimpleAttributeSet styleSimpleMessage;
	static SimpleAttributeSet styleMembre;
	

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
						display.setText(""); //A CHANGER
						input.setText("");
					}
				}
				else
				{
					bufferFieldD[idD] = e.getActionCommand();
					if(!e.getActionCommand().equals(".clear"))	
					{
						try {
							append(e.getActionCommand(), "NOIR");
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
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
	public boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}
	/*public void creerStringBuilder()
	{
		sb = new StringBuilder();
		sb.append("textPane !").append("\n");
		
	}*/
	public static void append(String msg, String style) throws BadLocationException
	{
		doc = textPane.getStyledDocument();
		
		styleSimpleMessage = new SimpleAttributeSet();
		
		if (style.equals("BLACK"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.BLACK);
		}
		else if (style.equals("ROUGE"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.RED);
		}
		else if (style.equals("VERT"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.GREEN);
		}
		else if (style.equals("JAUNE"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.YELLOW);
		}
		else if (style.equals("BLEU"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.BLUE);
		}
		else if (style.equals("CYAN"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.CYAN);
		}
		else if (style.equals("DARK_GRAY"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.DARK_GRAY);
		}
		else if (style.equals("GRAY"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.GRAY);
		}
		else if (style.equals("LIGHT_GRAY"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.LIGHT_GRAY);
		}
		else if (style.equals("MAGENTA"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.MAGENTA);
		}
		else if (style.equals("ORANGE"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.ORANGE);
		}
		else if (style.equals("PINK"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.PINK);
		}
		else if (style.equals("WHITE"))
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.WHITE);
		}
		else 
		{
			StyleConstants.setForeground(styleSimpleMessage, Color.BLACK);
		}
		doc.insertString(doc.getLength(), msg+"\n", styleSimpleMessage);
		
	}
	public static void append(String envoyeur, String styleEnvoyeur, String texte, String styleTexte)
	{
		//sb.append(texte+"\n");
		try {
			/*
			 * Récupération du style du document 
			 */
			doc = textPane.getStyledDocument();
			
			/*
			 * Insertion d'une chaine de caractères dans le document
			 * insertString :
			 * 	position de départ dans le document (doc.getLength ajoute à la fin
			 *  texte à ajouter
			 *  style pour le texte à ajouter
			 */
			styleMembre = new SimpleAttributeSet();
			styleMessage = new SimpleAttributeSet();
			//styleEnvoyeur = "ROUGE";
			//styleTexte = "NOIR";
			if (styleEnvoyeur.equals("BLACK"))
			{
				StyleConstants.setForeground(styleMembre, Color.BLACK);
			}
			else if (styleEnvoyeur.equals("ROUGE"))
			{
				StyleConstants.setForeground(styleMembre, Color.RED);
			}
			else if (styleEnvoyeur.equals("VERT"))
			{
				StyleConstants.setForeground(styleMembre, Color.GREEN);
			}
			else if (styleEnvoyeur.equals("JAUNE"))
			{
				StyleConstants.setForeground(styleMembre, Color.YELLOW);
			}
			else if (styleEnvoyeur.equals("BLEU"))
			{
				StyleConstants.setForeground(styleMembre, Color.BLUE);
			}
			else if (styleEnvoyeur.equals("CYAN"))
			{
				StyleConstants.setForeground(styleMembre, Color.CYAN);
			}
			else if (styleEnvoyeur.equals("DARK_GRAY"))
			{
				StyleConstants.setForeground(styleMembre, Color.DARK_GRAY);
			}
			else if (styleEnvoyeur.equals("GRAY"))
			{
				StyleConstants.setForeground(styleMembre, Color.GRAY);
			}
			else if (styleEnvoyeur.equals("LIGHT_GRAY"))
			{
				StyleConstants.setForeground(styleMembre, Color.LIGHT_GRAY);
			}
			else if (styleEnvoyeur.equals("MAGENTA"))
			{
				StyleConstants.setForeground(styleMembre, Color.MAGENTA);
			}
			else if (styleEnvoyeur.equals("ORANGE"))
			{
				StyleConstants.setForeground(styleMembre, Color.ORANGE);
			}
			else if (styleEnvoyeur.equals("PINK"))
			{
				StyleConstants.setForeground(styleMembre, Color.PINK);
			}
			else if (styleEnvoyeur.equals("WHITE"))
			{
				StyleConstants.setForeground(styleMembre, Color.WHITE);
			}		
			else 
			{
				StyleConstants.setForeground(styleMembre, Color.BLACK);
			}
			
			
			doc.insertString(doc.getLength(), envoyeur, styleMembre);
			
			
			
			if (styleTexte.equals("BLACK"))
			{
				StyleConstants.setForeground(styleMessage, Color.BLACK);
			}
			else if (styleTexte.equals("ROUGE"))
			{
				StyleConstants.setForeground(styleMessage, Color.RED);
			}
			else if (styleTexte.equals("VERT"))
			{
				StyleConstants.setForeground(styleMessage, Color.GREEN);
			}
			else if (styleTexte.equals("JAUNE"))
			{
				StyleConstants.setForeground(styleMessage, Color.YELLOW);
			}
			else if (styleTexte.equals("BLEU"))
			{
				StyleConstants.setForeground(styleMessage, Color.BLUE);
			}
			else if (styleTexte.equals("CYAN"))
			{
				StyleConstants.setForeground(styleMessage, Color.CYAN);
			}
			else if (styleTexte.equals("DARK_GRAY"))
			{
				StyleConstants.setForeground(styleMessage, Color.DARK_GRAY);
			}
			else if (styleTexte.equals("GRAY"))
			{
				StyleConstants.setForeground(styleMessage, Color.GRAY);
			}
			else if (styleTexte.equals("LIGHT_GRAY"))
			{
				StyleConstants.setForeground(styleMessage, Color.LIGHT_GRAY);
			}
			else if (styleTexte.equals("MAGENTA"))
			{
				StyleConstants.setForeground(styleMessage, Color.MAGENTA);
			}
			else if (styleTexte.equals("ORANGE"))
			{
				StyleConstants.setForeground(styleMessage, Color.ORANGE);
			}
			else if (styleTexte.equals("PINK"))
			{
				StyleConstants.setForeground(styleMessage, Color.PINK);
			}
			else if (styleTexte.equals("WHITE"))
			{
				StyleConstants.setForeground(styleMessage, Color.WHITE);
			}
			else 
			{
				StyleConstants.setForeground(styleMessage, Color.BLACK);
			}
			
			doc.insertString(doc.getLength(), " : "+texte+"\n", styleMessage);
			
			
			/*doc.insertString(doc.getLength(), envoyeur, styleMembre);
			doc.insertString(doc.getLength(), titrePoeme, style_titre);
			int fin_titre=doc.getLength();
			doc.insertString(doc.getLength(), poeme+"\n", style_normal);
			int fin_poeme=doc.getLength();
			doc.insertString(doc.getLength(), auteur+"\n", style_citation);
			doc.insertString(doc.getLength(), livre, style_citation);
			
			/*
			 * Centrage du titre
			 
			doc.setParagraphAttributes(0, fin_titre, centrer, false);
			
			/*
			 * Centrage des références
			 
			doc.setParagraphAttributes(fin_poeme, doc.getLength(), centrer, false);*/
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	public void creerTextPane1(String init, int longueur, int largeur, int x, int y)
	{
		/*
		 * Création du JTextPane
		 */
		textPane = new JTextPane();
		/*style_normal = new SimpleAttributeSet();
		StyleConstants.setFontFamily(style_normal, "Calibri");
		StyleConstants.setFontSize(style_normal, 16);

		/*
		 * Création du style pour l'affichage du titre
		 
		
		SimpleAttributeSet style_titre = new SimpleAttributeSet();
		style_titre.addAttributes(style_normal);
		StyleConstants.setForeground(style_titre, Color.BLUE);
		StyleConstants.setUnderline(style_titre, true);
		StyleConstants.setFontSize(style_titre, 18);
		StyleConstants.setBold(style_titre, true);
		
		/*
		 * Création du style qui permet de centrer le texte
		 
		SimpleAttributeSet centrer = new SimpleAttributeSet();
		StyleConstants.setAlignment(centrer, StyleConstants.ALIGN_CENTER);
		
		/*
		 * Création du style qui permet d'afficher les référénces
		 
		SimpleAttributeSet style_citation = new SimpleAttributeSet();
		style_citation.addAttributes(style_normal);
		StyleConstants.setItalic(style_citation, true);*/
		
		textPane.setToolTipText(init);
		JScrollPane scroll = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setBounds(longueur, largeur, x, y);

		

		frame.getContentPane().add(scroll);
		
		frame.setVisible(true);
		frame.repaint();
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
				//display.append("Déconnecté du serveur\n");
				
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