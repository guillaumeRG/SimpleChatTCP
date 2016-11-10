import java.net.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.*;
import java.awt.*;

import javax.swing.JFrame;

class ChatClient extends Frame implements Runnable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Socket soc;
	
	TextField tf;
	TextArea ta;
	
	String sendTo;
	static String LoginName;
	Thread t = null;
	static DataOutputStream dout;
	DataInputStream din;
	String[] membresCo = null;
	static boolean isConnect = false;
	static int port = 0;
	
	ChatClient(String LoginName) throws Exception {
		super(LoginName);
		
		InetAddress serveur ;
		serveur = Ipconfig.ipCible;
		String adresseLocale = new String(); 
		InetAddress locale;
		
		if(!Ipconfig.areThereDhcp)
		{
			locale = Ipconfig.ipCible;
		}
		else
		{
			locale = InetAddress.getLocalHost();
		}
		port = Ipconfig.port;
		this.LoginName = LoginName;
		System.out.println("Cible : "+serveur);
		System.out.println("Port ciblé : "+port);
		System.out.println("Adresse locale : "+ locale);
		
		
		soc = new Socket(serveur,port);

		din = new DataInputStream(soc.getInputStream());
		dout = new DataOutputStream(soc.getOutputStream());
		dout.writeUTF(LoginName);
		isConnect = true;

		t = new Thread(this);
		t.start();

	}
	
	public static boolean envoyer(String ordre, String message) {

		if (ordre.equals("SEND")) {
			try {
				// System.out.println(LoginName);
				dout.writeUTF("DATA" + " " +LoginName + " " +  message);

				return true;
			} catch (Exception ex) {
			}
		} else if (ordre.equals("CLOSE")) {
			try {
				dout.writeUTF("LOGOUT "+LoginName );
				isConnect = false;
				try {
					soc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception ex) {
			}

		}
		return false;

	}
	
	public void run() {
		while (isConnect) {
			String msgFromClient = new String();
			try {
				msgFromClient = din.readUTF();
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			
			StringTokenizer st = new StringTokenizer(msgFromClient);
			String MsgType = st.nextToken();
			
			//System.out.println("message " + msgFromClient);
			
			String msg = "";
			
			if (MsgType.equals("DATA")) {
				msg = "";
				String envoyeur = st.nextToken();
				while (st.hasMoreTokens()) {
					msg = msg + " " + st.nextToken();
				}
				try {
					LauncherIhm.display.append(envoyeur + " : "+ msg+"\n");

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			
			else if (MsgType.equals("LOGOUT")) {
				break;
			}
			else if (MsgType.equals("MEMBERS")) {
				
				while (st.hasMoreTokens()) {
					msg = msg + st.nextToken()+"\n";

				}
				LauncherIhm.displayC.setText(msg);
			}
			else if (MsgType.equals("LOGGEDOUT"))
			{
				LauncherIhm.display.append("Déconnecté du serveur\n");
				LauncherIhm.displayC.setText("");
				LauncherIhm.menu.getItem(1).setEnabled(true);
				LauncherIhm.menu.getItem(2).setEnabled(false);
				try {
					soc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
			
		}
	}
}

