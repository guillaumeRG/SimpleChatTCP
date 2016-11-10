import java.net.*;
import java.util.*;
import java.io.*;

class ChatServer extends Thread {

	static int port = 5217;

	ServerSocket soc = null;

	static boolean fin = false;

	// static int nbClients = 0;

	ChatServer() {

	}

	public void run() {

		InetAddress localeAdresse = null;
		localeAdresse = Ipconfig.ip;
		port = Ipconfig.port;
		// ServerSocket soc = null;
		try {
			soc = new ServerSocket(port, 5, localeAdresse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServeurIhm.afficherDansConsole("Nom serveur : "
				+ localeAdresse.getHostName());
		ServeurIhm.afficherDansConsole("Adresse serveur : " + localeAdresse);
		ServeurIhm.afficherDansConsole("Port : " + port);

		while (!fin) {
			Socket CSoc = null;

			try {
				CSoc = soc.accept();
				System.out.println("Client accepté");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				AcceptClient obClient = new AcceptClient(CSoc);
				obClient.start();
				System.out.println("TESTTTTTTTTTTTTTTTT");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}
	}

	public void stopChatServeur() {
		try {
			this.soc.close();
			AcceptClient.stopAcceptClient();

		} catch (IOException e) {
			ServeurIhm
					.afficherDansConsole("Erreur pendant la fermeture de la socket serveur ");
		}
	}

	static class AcceptClient extends Thread {
		static Vector<Socket> ClientSockets = new Vector<Socket>();
		static Vector<String> LoginNames = new Vector<String>();
		Socket ClientSocket;
		String LoginName;
		String Sendto;
		static String[] membres = new String[1024];
		DataInputStream din;
		DataOutputStream dout;
		boolean finThread = false;

		public AcceptClient() {

		}

		public AcceptClient(Socket CSoc) throws Exception {
			// System.out.println("AcceptClient() debut");
			
			ClientSocket = CSoc;
			
			// System.out.println("AcceptClient() fin");

		}

		public void attendreConnection() throws IOException {

			// System.out.println("attendreConnection() debut");
			din = new DataInputStream(ClientSocket.getInputStream());
			dout = new DataOutputStream(ClientSocket.getOutputStream());

			String message = lireMessage();

			// decryptement du message

			LoginName = message;

			ServeurIhm
					.afficherDansConsole("Utilisateur connecté :" + LoginName);
			LoginNames.add(LoginName);
			ClientSockets.add(ClientSocket);

			acceuilConnection();
			// System.out.println("attendreConnection() fin");
		}

		public String mettreAJourMembres() {
			// System.out.println("mettreAJourMembres() debut");
			String membresLocale = null;
			int iCount = 0;
			for (iCount = 0; iCount < LoginNames.size(); iCount++) {

				Socket tSoc = (Socket) ClientSockets.elementAt(iCount);

				membres[iCount] = (String) LoginNames.elementAt(iCount);

				if (!compare(membresLocale, null)) {
					membresLocale = membresLocale + " " + membres[iCount];
				} // pour membres connectés

				else if (compare(membresLocale, null)) {
					membresLocale = membres[iCount];
				} else {
					membresLocale = "";
				}

			}
			
			// System.out.println("mettreAJourMembres() fin");
			return membresLocale;
		}

		public void envoiMembresBroadcast(String membresLocale)
				throws IOException {
			// System.out.println("envoiMembresBroadcast(String membresLocale) debut");
			int iCount = 0;

			for (iCount = 0; iCount < LoginNames.size(); iCount++) // envois les
																	// membres
																	// connecté
			{

				Socket tSoc = (Socket) ClientSockets.elementAt(iCount);
				DataOutputStream tdout = new DataOutputStream(
						tSoc.getOutputStream());
				tdout.writeUTF("MEMBERS " + membresLocale);
				ServeurIhm
						.afficherDansConsole("Envoi de la liste des membres connécté");
				ServeurIhm.afficherDansConsole(membresLocale);
				ServeurIhm.rafraichirCo(membresLocale);

			}
			// System.out.println("envoiMembresBroadcast(String membresLocale) fin");
		}

		public void acceuilConnection() {
			// System.out.println("acceuilConnection() debut");
			String membresLocale = mettreAJourMembres(); // mettre a jours les
															// membres
			try {

				envoiMembresBroadcast(membresLocale);

			} catch (IOException e) {

				e.printStackTrace();

			}
			// System.out.println("acceuilConnection() fin");

		}

		public static void stopAcceptClient() throws IOException {
			// System.out.println("stopAcceptClient() debut");
			while (LoginNames.size() >= 1) // suppression des clients connecté
			{

				Socket tSoc = (Socket) ClientSockets.elementAt(0);
				DataOutputStream tdout = new DataOutputStream(
						tSoc.getOutputStream());
				tdout.writeUTF("LOGGEDOUT");
				System.out.println("OUT LOGGED!!!");

				String Sendto = null;
				Sendto = LoginNames.elementAt(0);
				LoginNames.removeElementAt(0);
				ClientSockets.removeElementAt(0);
				System.out.println("LoginName : " + LoginNames.size());
				ServeurIhm.afficherDansConsole("Utilisateur " + Sendto
						+ " Déconnecté ...");

			}
		
			// System.out.println("stopAcceptClient() fin");

		}

		public boolean compare(String str1, String str2) {
			return (str1 == null ? str2 == null : str1.equals(str2));
		}

	
		public String lireMessage() throws IOException {
			// System.out.println("lireMessage() debut (fin)");

			return din.readUTF();

		}

		public void agirPourMessage(String message) throws IOException {  //MAIIIIIIIIIIIIIIN !!!!!!!!!!
			// System.out.println("agirPourMessage(String message) debut");
			String membresLocale = null;

			String msgFromClient = message;

			StringTokenizer st = new StringTokenizer(msgFromClient);

			String MsgType = st.nextToken();
			Sendto = st.nextToken();
			int iCount = 0;

			String msg = "";
			while (st.hasMoreTokens()) {
				msg = msg + " " + st.nextToken();
			}
			ServeurIhm.afficherDansConsole("il me dit " + MsgType);
			// dout.writeUTF(Sendto + " " + "DATA" + " " + msg);

			if (MsgType.equals("LOGOUT")) {
				for (iCount = 0; iCount < LoginNames.size(); iCount++) 
				{
					System.out.println("login : "+LoginName);
					System.out.println("iCount : "+iCount);
					System.out.println("LoginNames.elementAt(iCount) : "+LoginNames.elementAt(iCount));
					
					if (LoginNames.elementAt(iCount).equals(LoginName)) 
					{
						System.out.println("LoginNames.elementAt(iCount).equals(LoginName) : "+LoginNames.elementAt(iCount).equals(LoginName));
						System.out.println(LoginNames.size());
						LoginNames.removeElementAt(iCount);
						ClientSockets.removeElementAt(iCount);
						if(LoginNames.size() == 0)
						{
							ServeurIhm.rafraichirCo("");
						}
						System.out.println(LoginNames.size());
						
						ServeurIhm.afficherDansConsole("Utilisateur " + Sendto
								+ " Déconnecté ...");
						finThread = true;

					}
				}

			}

			else if (MsgType.equals("DATA")) {
				for (iCount = 0; iCount < LoginNames.size(); iCount++) 
				{

					Socket tSoc = (Socket) ClientSockets.elementAt(iCount);
					DataOutputStream tdout = new DataOutputStream(
							tSoc.getOutputStream());
					tdout.writeUTF("DATA" + " " + Sendto + " " + msg);

				}
				
			}
			if(LoginNames.size() != 0)
			{
				ServeurIhm.rafraichirCo(mettreAJourMembres());
			
				envoiMembresBroadcast(mettreAJourMembres());
			}
			// System.out.println("agirPourMessage(String message) fin");
		}

		public void run() {
			// System.out.println("run() debut");
			try {
				attendreConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (!fin && !finThread) {

				try {
					agirPourMessage(lireMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finThread = true;
				}
				 //System.out.println("fin : "+finThread);
			}
			
			// System.out.println("run() fin");
		}
	}
}

