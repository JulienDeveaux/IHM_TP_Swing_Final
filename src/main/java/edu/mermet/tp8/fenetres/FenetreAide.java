package edu.mermet.tp8.fenetres;

import edu.mermet.tp8.Application;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class FenetreAide extends AbstractFenetreInterne {
	String message = "Aide vide";

	public FenetreAide(Application appli, Action action) {
		super(action, "Aide");
		setLayout(new FlowLayout());
		JOptionPane aide = new JOptionPane(message,INFORMATION_MESSAGE);
		add(aide);
		pack();
	}
}
