package edu.mermet.tp8.fenetres;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class FenetreConfigurationAide {
	Properties properties;
	File preference;

	public  FenetreConfigurationAide(JMenu menuApplication){
		JFrame f = new JFrame("Configuration des menus");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				// ------ écriture XML  à la fermeture de fenêtre ------
				try{
					OutputStream o = new FileOutputStream(preference);
					properties.storeToXML(o, "propriétés");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		f.setDefaultCloseOperation(HIDE_ON_CLOSE);
		generateConfig(f, menuApplication);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public void generateConfig(JFrame f, JMenu menuApplication){
		// ------ parse et lecture XML ------
		String nom = System.getProperty("user.name");
		File ihmXML = new File(System.getProperty("user.home") + "/.ihm");
        preference   = new File(ihmXML.getPath() + "/" + nom + ".xml");

        properties = new Properties();
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

            if( !isExist )
                throw new IOException("fichier no trouvé et impossible de le créer: " + preference.getPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

		// ------ récupération des entêtes des items du menu application ------
		String[] menuItems = new String[menuApplication.getItemCount()];
		for(int i = 0; i < menuApplication.getItemCount(); i++) {
			menuItems[i] = menuApplication.getItem(i).getText();
		}
		f.setLayout(new GridLayout(menuApplication.getItemCount(), 4));
		ButtonGroup[] bGroup = new ButtonGroup[menuApplication.getItemCount()];
		JLabel[] labels = new JLabel[menuApplication.getItemCount()];
		JRadioButton[] boutonsConfig = new JRadioButton[menuApplication.getItemCount() * 3];
		for(int i = 0; i < menuApplication.getItemCount(); i++) {
			labels[i] = new JLabel(menuItems[i]);
			f.add(labels[i]);
			bGroup[i] = new ButtonGroup();
			int it = 0;
			for(int j = i * 3; j < (i * 3 + 3); j++) {
				final int pos = i;
				if(it == 0) {
					boutonsConfig[j] = new JRadioButton("Affiché");
					boutonsConfig[j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							properties.setProperty(menuItems[pos], "Affiché");
						}
					});
					if(properties.getProperty(menuItems[pos]).equals("Affiché")) {
						boutonsConfig[j].setSelected(true);
					}
				}else if(it == 1) {
					boutonsConfig[j] = new JRadioButton("Auto");
					boutonsConfig[j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							properties.setProperty(menuItems[pos], "Auto");
						}
					});
					if(properties.getProperty(menuItems[pos]).equals("Auto")) {
						boutonsConfig[j].setSelected(true);
					}
				} else if(it == 2) {
					boutonsConfig[j] = new JRadioButton("Caché");
					boutonsConfig[j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							properties.setProperty(menuItems[pos], "Caché");
						}
					});
					if(properties.getProperty(menuItems[pos]).equals("Caché")) {
						boutonsConfig[j].setSelected(true);
					}
				}
				bGroup[i].add(boutonsConfig[j]);
				it++;
				f.add(boutonsConfig[j]);
			}
		}
	}
}
