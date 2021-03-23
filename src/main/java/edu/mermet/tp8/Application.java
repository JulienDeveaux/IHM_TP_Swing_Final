package edu.mermet.tp8;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;
import java.util.Random;
import javax.swing.*;

import edu.mermet.tp8.fenetres.*;

/**
 *
 * @author brunomermet
 */
public class Application extends JFrame {
    private JInternalFrame conversion;
    private JInternalFrame texte;
    private JInternalFrame diaporama;
    private JInternalFrame boutons;
    private Action actionAfficherConversion;
    private Action actionAfficherTexte;
    private Action actionAfficherDiaporama;
    private Action actionAfficherBoutons;
    public Application() {
        super("multi-fenêtres");
        this.setContentPane(new JDesktopPane());

        // ****** Barre de menu ******
        JMenuBar barre = new JMenuBar();
        // ------ menu Fichier ------
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setMnemonic(KeyEvent.VK_F);
        JMenuItem quitter = new JMenuItem("Quitter");
        quitter.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aev) {
                System.exit(0);
            }
        });
        quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        menuFichier.add(quitter);
        barre.add(menuFichier);
        this.setJMenuBar(barre);
        // ------ menu Applications ------
        JMenu menuApplication = new JMenu("Applications");
        menuApplication.setMnemonic(KeyEvent.VK_A);
        actionAfficherConversion = new ActionAfficherConversion();
        JMenuItem itemConversion = new JMenuItem(actionAfficherConversion);
        menuApplication.add(itemConversion);
        actionAfficherTexte = new ActionAfficherTexte();
        JMenuItem itemTexte = new JMenuItem(actionAfficherTexte);
        menuApplication.add(itemTexte);
        actionAfficherDiaporama = new ActionAfficherDiaporama();
        JMenuItem itemDiaporama = new JMenuItem(actionAfficherDiaporama);
        menuApplication.add(itemDiaporama);
        actionAfficherBoutons = new ActionAfficherBoutons();
        JMenuItem itemBoutons = new JMenuItem(actionAfficherBoutons);
        menuApplication.add(itemBoutons);
        barre.add(menuApplication);
        // ------ menu Aide ------
        JMenu menuAide = new JMenu("Aide");
        menuAide.setMnemonic(KeyEvent.VK_F1);
        JMenuItem howToMenu = new JMenuItem("Comment faire ?");
        JMenuItem configMenus = new JMenuItem("Configuration des menus");

        howToMenu.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aev) {
                new FenetreAideGenerale();
            }
        });
        howToMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, true));
        Application appli = this;
        configMenus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new FenetreConfigurationAide(menuApplication, appli);
            }
        });
        configMenus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, true));
        menuAide.add(howToMenu);
        menuAide.add(configMenus);
        barre.add(menuAide);
        // ****** Fin barre de menu ******

        // ***** parse et lecture XML *****
        String nom = System.getProperty("user.name");
        File ihmXML = new File(System.getProperty("user.home") + "/.ihm");
        File preference   = new File(ihmXML.getPath() + "/" + nom + ".xml");

        Properties properties = new Properties();
        try
        {
            boolean isExist = ihmXML.exists();

            if(!isExist)
                isExist = ihmXML.mkdir();

            if(!isExist)
                throw new IOException("dossier .ihm non créé");

            isExist = preference.exists();

            if(isExist) {
                properties.loadFromXML(new FileInputStream(preference));
            } else {
                isExist = preference.createNewFile();
                for(int i = 0; i < menuApplication.getItemCount(); i++) {
                    properties.setProperty(menuApplication.getItem(i).getText(), "Auto");
                }
                try{
                    OutputStream o = new FileOutputStream(preference);
                    properties.storeToXML(o, "propriétés");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!isExist)
                throw new IOException("fichier no trouvé et impossible de le créer: " + preference.getPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // ***** fin lecture XML *****

        // ****** Création des fenêtres ******
        // ------ fenêtre conversion ------
        conversion = new FenetreConversion(this, actionAfficherConversion);
        this.add(conversion);
        // ------ fenêtre texte ------
        texte = new FenetreTexte(actionAfficherTexte);
        this.add(texte);
        // ------ fenêtre diaporama ------
        diaporama = new FenetreDiaporama(actionAfficherDiaporama);
        this.add(diaporama);
        // ------ fenêtre boutons ------
        boutons = new FenetreBoutons(this,actionAfficherBoutons);
        this.add(boutons);
        // ------ fenêtre aide du jour ------
        // ***** Récupération d'une aide aléatoire

        JLabel aideText = new JLabel("");
        String[] listeAide = new String[255];
        Properties contextuelAide = new Properties();
        int nbLignes = 0;
        try{
            File contextuel = new File("src/main/resources/HowTo/contextuel.properties");
            InputStream input = new FileInputStream(contextuel);
            if(input != null) {
                contextuelAide.load(input);
                BufferedReader reader = new BufferedReader(new FileReader(contextuel));
                nbLignes = 1;
                while (reader.readLine() != null) nbLignes++;
                reader.close();

                listeAide = new String[nbLignes - 1];
                for(int i = 0; i < nbLignes; i++) {
                    listeAide[i] = contextuelAide.getProperty("" + i);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        Random r = new Random();
        int rand = r.nextInt(nbLignes - 1) + 1;
        while("Caché".equals(properties.getProperty("Aide" + rand))) {      //TODO boucle infinie si tout caché

            rand = r.nextInt(nbLignes - 1) + 1;
        }
        aideText.setText(listeAide[rand]);

        // ***** Fin récupération d'aide aléatoire
        JWindow aideJour = new JWindow(this);
        JButton aideOK = new JButton("OK");
        JButton dontAskAgain = new JButton("Ne plus afficher");
        aideOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aideJour.setVisible(false);
            }
        });
        final int ra = rand;
        dontAskAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                properties.setProperty("Aide" + ra, "Caché");
                try{
                    OutputStream o = new FileOutputStream(preference);
                    properties.storeToXML(o, "propriétés");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                aideJour.setVisible(false);
            }
        });
        ButtonGroup aideBgroup = new ButtonGroup();
        aideBgroup.add(aideOK);
        aideBgroup.add(dontAskAgain);

        JPanel aideDown = new JPanel(new BorderLayout());
        aideDown.add(aideOK, BorderLayout.WEST);
        aideDown.add(dontAskAgain, BorderLayout.EAST);

        aideJour.add(aideText, BorderLayout.NORTH);
        aideJour.add(aideDown, BorderLayout.SOUTH);
        aideJour.setSize(200, 100);
        Point centre=GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        aideJour.setLocation(centre);
        aideJour.setVisible(true);
        // ****** Fin création fenêtres ******
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,300);
        this.setLocationRelativeTo(null);
        setVisible(true);
        enableConversion(!properties.getProperty("Conversion Celsius/Farenheit").equals("Caché"));
        enableTexte(!properties.getProperty("Saisie de texte").equals("Caché"));
        enableDiaporama(!properties.getProperty("Diaporama").equals("Caché"));
        enableBoutons(!properties.getProperty("Boutons").equals("Caché"));
    }

    private class ActionAfficherBoutons extends AbstractAction {
        public ActionAfficherBoutons() {
            super("Boutons");
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_B);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            boutons.setVisible(true);
            enableBoutons(false);
        }
    }

    private class ActionAfficherDiaporama extends AbstractAction {
        public ActionAfficherDiaporama() {
            super("Diaporama");
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_D);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            diaporama.setVisible(true);
            enableDiaporama(false);
        }
    }

    private class ActionAfficherTexte extends AbstractAction {
        public ActionAfficherTexte() {
            super("Saisie de texte");
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_T);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            texte.setVisible(true);
            enableTexte(false);
        }
    }

    public class ActionAfficherConversion extends AbstractAction {
        public ActionAfficherConversion() {
            super("Conversion Celsius/Farenheit");
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY,KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            conversion.setVisible(true);
            enableConversion(false);
        }
    }

    public void enableConversion(boolean b) {
        actionAfficherConversion.setEnabled(b);
    }

    public void enableTexte(boolean b) {
        actionAfficherTexte.setEnabled(b);
    }

    public void enableDiaporama(boolean b) {
        actionAfficherDiaporama.setEnabled(b);
    }

    public void enableBoutons(boolean b) {
        actionAfficherBoutons.setEnabled(b);
    }

    public Action getActionAfficherConversion() {
        return actionAfficherConversion;
    }

    public Action getActionAfficherTexte() {
        return actionAfficherTexte;
    }

    public Action getActionAfficherDiaporama() {
        return actionAfficherDiaporama;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }


}
