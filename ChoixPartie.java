/* 
 * Démineur
 * Copyright (C) 2018 david4599
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import javax.swing.*;

/**
 * La classe <code>ChoixPartie</code> permet de choisir le nombre de lignes, le nombre de colonnes
 * et le nombre de mines de la nouvelle partie.
 * 
 * @version 1
 * @author david4599
 */
public class ChoixPartie extends FocusAdapter implements ActionListener {
	
	/**
	 * nombre de lignes de la grille par défaut.
	 */
	private int nbLignes = 10;
	
	/**
	 * nombre de colonnes de la grille par défaut.
	 */
	private int nbColonnes = 10;
	
	/**
	 * nombre de mines de la grille par défaut.
	 */
	private int nbMines = 10;
	
	/**
	 * Champ de texte permettant de saisir le nombre de lignes de la grille.
	 */
	private JTextField tf_nbLignes;
	
	/**
	 * Champ de texte permettant de saisir le nombre de colonnes de la grille.
	 */
	private JTextField tf_nbColonnes;
	
	/**
	 * Champ de texte permettant de saisir le nombre de mines de la grille.
	 */
	private JTextField tf_nbMines;
	
	/**
	 * Etiquette affichant le nombre de mines de la grille dynamiquement.
	 */
	private JLabel afficheNbMines;
	
	/**
	 * reponse enregistre la valeur de retour de la boîte de dialogue - contenant
	 * les 3 champs de texte - en fonction du bouton cliqué.
	 */
	private int reponse;
	
	/**
	 * reponse enregistre la valeur de retour de la méthode isTextfieldValueNumber().
	 */
	private boolean isNumber;
	
	/**
	 * Fenêtre de la classe menu permettant l'affichage des paramètres de la grille à entrer dans une boîte de dialogue.
	 */
	private JFrame fenetre;
	
	/**
	 * Constructeur ChoixPartie définissant la récupération de la fenêtre de la classe menu pour l'affichage
	 * des paramètres de la grille à entrer dans une boîte de dialogue.
	 *
	 * @param fenetre Fenêtre de la classe menu.
	 */
	public ChoixPartie(JFrame fenetre) {
		this.fenetre = fenetre;
	}
	
	/**
	 * La méthode Parametres() affiche la boîte de dialogue
	 * et fait des vérifications sur la validité des valeurs remplies.
	 */
	public void parametres() {
		tf_nbLignes = new JTextField(Integer.toString(nbLignes));
		tf_nbColonnes = new JTextField(Integer.toString(nbColonnes));
		tf_nbMines = new JTextField(Integer.toString(nbMines));
		
		
		
		JPanel p_radio = new JPanel();
		p_radio.setLayout(new GridLayout(0,2));
		
		JRadioButton r_facile = new JRadioButton("Débutant");
		JRadioButton r_moyen = new JRadioButton("Intermédiaire");
		JRadioButton r_difficile = new JRadioButton("Avancé");
		JRadioButton r_perso = new JRadioButton("Paramètres personnalisés");
		
		r_facile.addActionListener(this);
		r_moyen.addActionListener(this);
		r_difficile.addActionListener(this);
		r_perso.addActionListener(this);
		
		ButtonGroup g_difficulte = new ButtonGroup();
		
		g_difficulte.add(r_facile);
		g_difficulte.add(r_moyen);
		g_difficulte.add(r_difficile);
		g_difficulte.add(r_perso);
		
		r_facile.setSelected(true);
		
		p_radio.add(r_facile);
		p_radio.add(r_moyen);
		p_radio.add(r_difficile);
		p_radio.add(r_perso);
		
		JPanel p_perso = new JPanel();
		p_perso.setLayout(new GridLayout(0,2));
		p_perso.add(new JLabel("Nombre de lignes (4 - 30) : "));
		p_perso.add(tf_nbLignes);
		p_perso.add(new JLabel("Nombre de colonnes (4 - 30) : "));
		p_perso.add(tf_nbColonnes);
		afficheNbMines = new JLabel("Nombre de mines (1 - " + nbLignes*nbColonnes + ") : ");
		p_perso.add(afficheNbMines);
		p_perso.add(tf_nbMines);
		
		tf_nbLignes.setEditable(false);
		tf_nbColonnes.setEditable(false);
		tf_nbMines.setEditable(false);
		
		tf_nbLignes.addFocusListener(this);
		tf_nbColonnes.addFocusListener(this);
		tf_nbMines.addFocusListener(this);
		
		JPanel panneau = new JPanel();
		panneau.setLayout(new GridLayout(2,0));
		panneau.add(p_radio);
		panneau.add(p_perso);
		
		do {
			reponse = JOptionPane.showConfirmDialog(
				null,
				panneau,
				"Paramètres",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE);
			
			isNumber = isTextfieldValueNumber();
			if (reponse == -1) { // Toutes les conditions sont vraies pour sortir de la boucle quand on ferme la boite de dialogue

				isNumber = true;
				nbLignes = 10;
				nbColonnes = 10;
				nbMines = 10;
			}
		} while (!isNumber || nbLignes < 4 || nbLignes > 30 || nbColonnes < 4 || nbColonnes > 30 || nbMines < 1 || nbMines > nbLignes*nbColonnes);
		
		if (reponse == JOptionPane.OK_OPTION) {
			valider();
		}
	}
	
	/**
	 * La méthode isTextfieldValueNumber() vérifie que les valeurs 
	 * remplies dans les champs de texte soient des nombres.
	 */
	private boolean isTextfieldValueNumber() {
		try {
			nbLignes = Integer.parseInt(tf_nbLignes.getText());
			nbColonnes = Integer.parseInt(tf_nbColonnes.getText());
			nbMines = Integer.parseInt(tf_nbMines.getText());
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * La méthode Valider() est appelée une fois que toutes les vérifications
	 * sont validées et appelle la méthode Jeu pour démarrer la nouvelle partie.
	 */
	private void valider() {
		int fenetreH = nbLignes * 42; // Ajustement automatique de la fenêtre en fonction du nombre de cases
		int fenetreL = nbColonnes * 55;
		
		// Obtenir la définition de l'écran
		Dimension definitionEcran = Toolkit.getDefaultToolkit().getScreenSize();
		int largeurEcran = (int) definitionEcran.getWidth();
		int hauteurEcran = (int) definitionEcran.getHeight();
		
		if(fenetreH > hauteurEcran - 50) fenetreH = hauteurEcran - 50;
		if(fenetreL > largeurEcran - 50) fenetreL = largeurEcran - 50;
		Jeu nouveauJeu = new Jeu(nbLignes, nbColonnes, nbMines, fenetreH, fenetreL);
		fenetre.dispose();
		nouveauJeu.afficher();
	}
	
	
	/**
	 * La méthode actionPerformed() est appelée lors d'un clic sur un des 3 boutons du menu.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Débutant") {
			parametresDifficulte("10", "10", "10");
		}
		else if(e.getActionCommand() == "Intermédiaire") {
			parametresDifficulte("18", "18", "50");
		}
		else if(e.getActionCommand() == "Avancé") {
			parametresDifficulte("22", "26", "120");
		}
		else if(e.getActionCommand() == "Paramètres personnalisés") {
			tf_nbLignes.setEditable(true);
			tf_nbColonnes.setEditable(true);
			tf_nbMines.setEditable(true);
		}
	}
	
	
	/**
	 * La méthode parametresDifficulte() affiche le nombre de lignes, 
	 * de colonnes et de mines dans les champs de texte selon la difficulté.
	 * 
	 * @param nbLignes le nombre de lignes
	 * @param nbColonnes le nombre de colonnes
	 * @param nbMines le nombre de mines
	 */
	private void parametresDifficulte(String nbLignes, String nbColonnes, String nbMines) {
		tf_nbLignes.setText(nbLignes);
		tf_nbColonnes.setText(nbColonnes);
		tf_nbMines.setText(nbMines);
		
		tf_nbLignes.setEditable(false);
		tf_nbColonnes.setEditable(false);
		tf_nbMines.setEditable(false);
	}
	
	
	/**
	 * La méthode focusLost() est appelée lors d'une perte de focus d'un des champs.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void focusLost(FocusEvent e) {
		isNumber = isTextfieldValueNumber();
		if (isNumber && nbLignes >= 4 && nbLignes <= 30 && nbColonnes >= 4 && nbColonnes <= 30 && nbMines >= 1 && nbMines <= nbLignes*nbColonnes) {
			afficheNbMines.setText("Nombre de mines (1 - " + nbLignes*nbColonnes + ") : ");
		}
		else {
			afficheNbMines.setText("Nombre de mines (1 - *) : ");
		}
	}
}