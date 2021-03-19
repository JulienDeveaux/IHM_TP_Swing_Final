package edu.mermet.tp8.fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import edu.mermet.tp8.Application;

/**
 *
 * @author brunomermet
 */
public class FenetreBoutons extends AbstractFenetreInterne {
    private JButton boutonTexte;
    private JButton boutonDiaporama;
    private JButton boutonDegres;
    public FenetreBoutons(Application appli, Action action) {
        super(action, "Boutons");
        setLayout(new FlowLayout());
        boutonTexte = new JButton(appli.getActionAfficherTexte());
        boutonDiaporama = new JButton(appli.getActionAfficherDiaporama());
        boutonDegres = new JButton(appli.getActionAfficherConversion());
        add(boutonDegres);
        add(boutonTexte);
        add(boutonDiaporama);
        pack();
    }

}
