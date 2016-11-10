import javax.swing.JTextPane;


public class Run {
	static Ipconfig instanceIpconfig;
	
	public static void main(String[] args) throws Exception {
		
		

		
		LauncherIhm launcher = new LauncherIhm();
		launcher.creerFenetre("Plateforme", 800, 500);
		launcher.creerTextAreaConnecter("Membres", 300, 300, 330, 20, false);
		//launcher.creerTextArea("Conversation", 300, 300, 20, 20, false);
		launcher.creerTextField("Message", 300, 20, 20, 325, true);
		launcher.creerJMenuItem("Fonctions", "Ouvrir", "Connection", "Déconnection");
		launcher.creerJLabelConnecter(150, 20, 330, 0);
		launcher.creerJLabelChat(150, 20, 20, 0);
		  instanceIpconfig = new Ipconfig();
	        
			String[] contenuFichierIpconfig = new String[Ipconfig
	                                   				.getLigneFichierIpconfig()];
	        contenuFichierIpconfig = Ipconfig.ouvrirFichierIpconfig();
	        
	       
	       
	        
	      
			
			
			
			launcher.creerTextPane1("Conversation", 20, 20, 300, 300);
			//launcher.append("Gman", "ROUGE", "Bonjour", "NOIR");
	}
	
	public void append()
	{
		
	}

}
