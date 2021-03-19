package edu.mermet.tp8.fenetres;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Properties;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FenetreAide {

	public FenetreAide() {
		JFrame f = new JFrame("Fenêtre d'aide");
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.generateAide(f);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private void generateAide(JFrame frame) {
		String listeElements[] = new String[255];
		String listeAide[] = new String[255];
		int lines = -1;
		try {
			Properties properties = new Properties();
			String texteLink = "src/main/resources/HowTo/text.properties";
			String titreLink = "src/main/resources/HowTo/titre.properties";
			File titre = new File(titreLink);
			File texte = new File(texteLink);
			InputStream inputTitre = new FileInputStream(titre);
			InputStream inputTexte = new FileInputStream(texte);
			if(inputTitre != null && inputTexte != null) {
				// ------ ajout des titres d'aide ------
				properties.load(inputTitre);
				BufferedReader reader = new BufferedReader(new FileReader(titreLink));
				lines = 1;
				while (reader.readLine() != null) lines++;
				reader.close();

				for(int i = 0; i < lines; i++) {
					listeElements[i] = properties.getProperty("titre" + i);
				}
				// ------ ajout de l'aide html ------
				properties.load(inputTexte);
				reader = new BufferedReader(new FileReader(titreLink));
				lines = 1;
				while (reader.readLine() != null) lines++;
				reader.close();

				for(int i = 0; i < lines; i++) {
					listeAide[i] = properties.getProperty("text" + i);
				}
			} else {
				throw new FileNotFoundException("property file '" + titreLink + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		JList liste = new JList(listeElements);
		JEditorPane zoneExplications = new JEditorPane();
		zoneExplications.setContentType("text/html");
		zoneExplications.setEditable(false);

		int finalLines = lines;
		liste.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selected = liste.getSelectedIndex();
				if(selected < finalLines) {
					zoneExplications.setText(listeAide[selected]);
				} else {
					zoneExplications.setText("Aide non trouvée");
				}
			}
		});

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(liste, BorderLayout.WEST);
		mainPanel.add(zoneExplications, BorderLayout.EAST);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, liste, zoneExplications);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		liste.setMinimumSize(new Dimension(100, 100));
		zoneExplications.setMinimumSize(new Dimension(100, 100));

		frame.add(splitPane);
	}
}
