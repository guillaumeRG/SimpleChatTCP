import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Ipconfig {
	static OuvrirFichier fichierIpconfig = new OuvrirFichier();
	static File f = new File("");
	static String ipconfigPath = f.getAbsolutePath()
			+ "/fichiers_config/ipconfig.txt";
	static InetAddress ip = null;
	static int port = 0;
	static boolean areThereDhcp = false;

	// static String prioritesPath =
	// "/Ordonnanceur/Ordonnanceur_Beta1.1/bin/fichiers_config/priorites.txt";

	public Ipconfig() {

	}

	public static String[] ouvrirFichierIpconfig() throws FileNotFoundException, UnknownHostException {

		String[] contenu = new String[OuvrirFichier
				.compterLigne(ipconfigPath)];
		String type = null;
		contenu = fichierIpconfig.lireFichier(ipconfigPath);

		
		for (int i = 0; i < OuvrirFichier.compterLigne(ipconfigPath); i++) {
			
			System.out.println(contenu[i]);
			
			type = contenu[i].split(" : ")[0];
			if(type.equals("ip"))
			{
				ip = InetAddress.getByName(contenu[i].split(" : ")[1]);
			}
			if(type.equals("port"))
			{
				port = Integer.parseInt(contenu[i].split(" : ")[1]);
			}
			if(type.equals("dhcp"))
			{
				if(contenu[i].split(" : ")[1].equals("false"))
				{areThereDhcp = false;}
				else if(contenu[i].split(" : ")[1].equals("true"))
				{areThereDhcp = true;}
				else
				{areThereDhcp = false;}
			}
			
		}
		return contenu;
	}

	public static int getLigneFichierIpconfig() throws FileNotFoundException {
		return OuvrirFichier.compterLigne(ipconfigPath);
	}

	public static int getPatternParLigne() throws FileNotFoundException {
		return fichierIpconfig.compterPatternParLigne(ipconfigPath, " : ");
	}
	public static InetAddress getIp() throws UnknownHostException
	{
		return InetAddress.getLocalHost();
	}
}