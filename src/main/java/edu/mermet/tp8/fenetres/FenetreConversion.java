package edu.mermet.tp8.fenetres;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import edu.mermet.tp8.Application;

/**
 *
 * @author brunomermet
 */
public class FenetreConversion extends AbstractFenetreInterne {
    private JTextField champCelsius;
    private JTextField champFarenheit;
    private JButton boutonConvertir;
    private Action actionConvertir;
    private boolean celsiusAFocus;
    public FenetreConversion(Application appli, Action action) {
        super(action,"Conversion celsius/Farenheit");
        this.setSize(new Dimension(100,50));
        this.setLayout(new GridLayout(3,2));

        JPanel ligneCelsius = new JPanel();
        ligneCelsius.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ligneCelsius.setToolTipText("Valeur en degré de Celsius");
        JPopupMenu popupCelcius = new JPopupMenu();
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showInternalMessageDialog(appli.getRootPane(), "Entrez la valeur en Celsius", "Aide", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        JMenuItem element;
        popupCelcius.add(element = new JMenuItem("Aide"));
        element.addActionListener(menuListener);
        ligneCelsius.add(popupCelcius);
        ligneCelsius.setComponentPopupMenu(popupCelcius);

        JButton helpCelsius = new JButton(new ImageIcon("src/main/resources/question.png"));
        helpCelsius.setToolTipText("Aide");
        helpCelsius.setBorder(BorderFactory.createEmptyBorder());
        helpCelsius.setContentAreaFilled(false);
        helpCelsius.setPreferredSize(new Dimension(20, 20));
        helpCelsius.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showInternalMessageDialog(appli.getRootPane(), "Entrez la valeur en Celcius", "Aide", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel labCelsius = new JLabel("Celsius :");
        champCelsius = new JTextField(15);
        champCelsius.setToolTipText("Valeur en degré de Celsius");
        champCelsius.add(popupCelcius);
        champCelsius.setComponentPopupMenu(popupCelcius);
        labCelsius.setLabelFor(champCelsius);

        ligneCelsius.add(labCelsius);
        ligneCelsius.add(champCelsius);
        ligneCelsius.add(helpCelsius);
        this.add(ligneCelsius);

        celsiusAFocus = true;
        champCelsius.addFocusListener(new EcouteurFocus(true));

        JPanel ligneFarenheit = new JPanel();
        ligneFarenheit.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ligneFarenheit.setToolTipText("Valeur en degré de Farhenheit");
        JPopupMenu popupFarhenheit = new JPopupMenu();
        ActionListener menuListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showInternalMessageDialog(appli.getRootPane(), "Entrez la valeur en Farhenheit", "Aide", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        popupFarhenheit.add(element = new JMenuItem("Aide"));
        element.addActionListener(menuListener2);
        ligneFarenheit.add(popupFarhenheit);
        ligneFarenheit.setComponentPopupMenu(popupFarhenheit);

        JButton helpFrarenheit = new JButton(new ImageIcon("src/main/resources/question.png"));
        helpFrarenheit.setToolTipText("Aide");
        helpFrarenheit.setBorder(BorderFactory.createEmptyBorder());
        helpFrarenheit.setContentAreaFilled(false);
        helpFrarenheit.setPreferredSize(new Dimension(20, 20));
        helpFrarenheit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showInternalMessageDialog(appli.getRootPane(), "Entrez la valeur en Farhenheit", "Aide", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel labFarenheit = new JLabel("Farenheit :");
        champFarenheit = new JTextField(15);
        champFarenheit.setToolTipText("Valeur en degré de Farhenheit");
        champFarenheit.add(popupFarhenheit);
        champFarenheit.setComponentPopupMenu(popupFarhenheit);
        labFarenheit.setLabelFor(champFarenheit);

        ligneFarenheit.add(labFarenheit);
        ligneFarenheit.add(champFarenheit);
        ligneFarenheit.add(helpFrarenheit);
        this.add(ligneFarenheit);

        champFarenheit.addFocusListener(new EcouteurFocus(false));

        JPanel ligneValider = new JPanel();
        ligneValider.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionConvertir = new ActionConvertir();
        boutonConvertir = new JButton(actionConvertir);
        boutonConvertir.setToolTipText("Convertir");
        ligneValider.add(boutonConvertir);
        this.add(ligneValider);
        
        pack();
        getRootPane().setDefaultButton(boutonConvertir);
    }

    private class EcouteurFocus implements FocusListener {
        private boolean aStocker;

        public EcouteurFocus(boolean b) {
            aStocker = b;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            celsiusAFocus = aStocker;
        }

        @Override
        public void focusLost(FocusEvent fe) {
            return;
        }
    }

    private class ActionConvertir extends AbstractAction {

        public ActionConvertir() {
            super("Convertir");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            double tempCelsius = 0;
            double tempFarenheit = 0;
            if (celsiusAFocus) {
                try {
                    tempCelsius = Double.parseDouble(champCelsius.getText());
                tempFarenheit = 9./5*tempCelsius+32;
                champFarenheit.setText(""+tempFarenheit);
                }
                catch (NumberFormatException nfe) {
                    champFarenheit.setText("Format incorrect");
                }
                }
            else {
                try {
                    tempFarenheit = Double.parseDouble(champFarenheit.getText());
                    tempCelsius = (tempFarenheit - 32) *5./9;
                    champCelsius.setText(""+tempCelsius);
                }
                catch (NumberFormatException nfe) {
                    champCelsius.setText("Format incorrect");
                }
                
            }
        }
    }
    
}
