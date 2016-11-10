import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OuvrirFichier {
	public OuvrirFichier() {

	}

	public int compterPatternParLigne(String filePath, String pattern)
			throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(new File(filePath));
		String[] decouper = { " " };
		int nbPattern = 0;
		while (scanner.hasNextLine()) {
			int dernierPattern = 0;

			String line = scanner.nextLine();
			decouper = line.split(pattern);
			/*
			 * for(int i=0;i<decouper.length;i++) {
			 * System.out.println(decouper[i]); }
			 */
			dernierPattern = decouper.length;
			if (nbPattern < dernierPattern)
				nbPattern = dernierPattern;
		}

		// System.out.println(nbPattern);

		return nbPattern;
	}

	public int compterPatternLigne(int ligne, String filePath, String pattern)
			throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(new File(filePath));
		String[] decouper = { " " };
		int nbPattern = 0;
		int currentLine = 0;
		while (scanner.hasNextLine()) {
			int dernierPattern = 0;

			String line = scanner.nextLine();
			decouper = line.split(pattern);
			dernierPattern = decouper.length;
			if (currentLine == (ligne - 1)) {
				nbPattern = dernierPattern;
			}
			currentLine++;

		}
		return nbPattern;
	}

	public String[] lireFichier(String filePath) throws FileNotFoundException {
		String[] fichier = new String[compterLigne(filePath)];
		int nbLine = 0;

		Scanner scanner = new Scanner(new File(filePath));
		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();
			fichier[nbLine] = line;
			// System.out.println(nbLine+" : "+line);
			nbLine++; 
		}

		scanner.close();
		return fichier;
	}

	public static int compterLigne(String filePath)
			throws FileNotFoundException {
		int nbLine = 0;
		Scanner scanner = new Scanner(new File(filePath));
		while (scanner.hasNextLine()) {
			scanner.nextLine();
			nbLine++;
		}
		scanner.close();
		return nbLine;
	}

}