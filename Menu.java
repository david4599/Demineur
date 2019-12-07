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
import javax.swing.*;
import java.io.*;

/**
 * La classe <code>Menu</code> permet de créer le menu principal lors du démarrage du jeu.
 *  
 * @version 1
 * @author david4599
 */
public class Menu extends JFrame implements ActionListener {
	
	/**
	 * assetsPath est le chemin relatif des images utilisées dans le jeu.
	 */
	private String assetsPath;
	
	/**
	 * Le constructeur menu crée et affiche la fenêtre du menu où sont implémentés 3 boutons :
	 * Commencer une nouvelle partie ; Reprendre la dernière partie sauvegardée ; Quitter le jeu.
	 */
	public Menu() {
		this.setTitle("Démineur");
		Dimension dimension = new Dimension(350, 315);
		this.setSize(dimension);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Demineur demineur = new Demineur();
		assetsPath = demineur.assetsPath;
		
		this.setIconImage(new ImageIcon(assetsPath + "mine120.png").getImage());
		this.setResizable(false);
		
		JPanel menu = new JPanel();
		menu.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		ImageIcon imageMineMenu = new ImageIcon(assetsPath + "mine_menu.png");
		JLabel l_imageMineMenu = new JLabel();
		l_imageMineMenu.setIcon(imageMineMenu);
		l_imageMineMenu.setHorizontalAlignment(JLabel.CENTER);
		menu.add(l_imageMineMenu);
		
		JLabel l_demineur = new JLabel("Démineur");
		l_demineur.setFont(new Font("Lucida Grande", 1, 30));
		l_demineur.setHorizontalAlignment(JLabel.CENTER);
		menu.add(l_demineur);
		
		JButton b_nvellePartie = new JButton("Commencer une nouvelle partie");
		JButton b_reprendrePartie = new JButton("Reprendre la dernière partie sauvegardée");
		JButton b_quitterJeu = new JButton("Quitter le jeu");
		
		JLabel copyright = new JLabel("<html>Copyright © 2018 david4599 Tous droits réservés</html>");
		copyright.setFont(new Font("Lucida Grande", 1, 10));
		
		b_nvellePartie.addActionListener(this);
		b_reprendrePartie.addActionListener(this);
		b_quitterJeu.addActionListener(this);
		
		menu.add(b_nvellePartie);
		menu.add(b_reprendrePartie);
		menu.add(b_quitterJeu);
		menu.add(copyright);
		
		this.add(menu);
		
		b_reprendrePartie.setEnabled(false);
		File fichierSauvegarde = new File("sauvegarde.bin");
		if(fichierSauvegarde.exists()) {
			b_reprendrePartie.setEnabled(true);
		}
		
		this.setVisible(true);
	}
	
	/**
	 * La méthode actionPerformed() est appelée lors d'un clic sur un des 3 boutons du menu.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Commencer une nouvelle partie") {
			ChoixPartie partie = new ChoixPartie(this);
			partie.parametres();
		}
		else if(e.getActionCommand() == "Reprendre la dernière partie sauvegardée") {
			Sauvegarde partie = new Sauvegarde();
			Object tab[] = partie.charger();
			int tailleH_lecture = (int) tab[0];
			int tailleL_lecture = (int) tab[1];
			int[][] tabCases_lecture = (int[][]) tab[2];
			int[][] tabCasesFlags_lecture = (int[][]) tab[3];
			long tempsMs_lecture = (long) tab[4];
			
			int fenetreH_lecture = tailleH_lecture * 42; // Ajustement automatique de la fenêtre en fonction du nombre de cases
			int fenetreL_lecture = tailleL_lecture * 55;
			
			// Obtenir la définition de l'écran
			Dimension definitionEcran = Toolkit.getDefaultToolkit().getScreenSize();
			int largeurEcran = (int) definitionEcran.getWidth();
			int hauteurEcran = (int) definitionEcran.getHeight();
			
			if(fenetreH_lecture > hauteurEcran - 50) fenetreH_lecture = hauteurEcran - 50;
			if(fenetreL_lecture > largeurEcran - 50) fenetreL_lecture = largeurEcran - 50;
			Jeu nouveauJeu = new Jeu(tabCases_lecture,
									tabCasesFlags_lecture, 
									tailleH_lecture, 
									tailleL_lecture, 
									fenetreH_lecture, 
									fenetreL_lecture,
									tempsMs_lecture);
			this.dispose();
			nouveauJeu.afficher();
		}
		else if(e.getActionCommand() == "Quitter le jeu") {
			System.exit(0);
		}
	}
	
}