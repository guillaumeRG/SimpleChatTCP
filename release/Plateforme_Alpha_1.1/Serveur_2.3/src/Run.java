
public class Run {
	static Ipconfig instanceIpconfig;
	public static void main(String args[]) throws Exception
    {
        
		
        ServeurIhm serveurIhm = new ServeurIhm();
        serveurIhm.creerFenetre("Serveur Plateforme", 700, 700);
        serveurIhm.creerTextArea("Console", 500, 500, 20, 100, false);
        serveurIhm.creerTextArea2("Connectés",120 , 590, 540, 10, false);
        serveurIhm.creerTextField("Entrer", 500, 20, 20, 605, true);
        serveurIhm.creerHorloge("Horloge", 205, 30, 315, 10);
        serveurIhm.creerBouton("Démarrer", 100, 30, 20, 50);
        serveurIhm.creerBouton2("Arrêter", 100, 30, 155, 50);
        serveurIhm.creerVoyant(100, 25, 20, 10);
        instanceIpconfig = new Ipconfig();
        
		String[] contenuFichierIpconfig = new String[Ipconfig
                                   				.getLigneFichierIpconfig()];
        contenuFichierIpconfig = Ipconfig.ouvrirFichierIpconfig();
        
        
    }
}
