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
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;
import java.util.Random;
import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;

/**
 * La classe <code>Jeu</code> est la classe principale du jeu qui contient 
 * les méthodes de calcul, d'affichage du plateau et des menus.
 * 
 * @version 1
 * @author david4599
 */
public class Jeu extends MouseAdapter implements ActionListener, WindowListener {

	/**
	 * Taille en hauteur soit le nombre de lignes de la grille.
	 */
	private int tailleH;
	
	/**
	 * Taille en longueur soit le nombre de colonnes de la grille.
	 */
	private int tailleL;
	
	/**
	 * nombre de mines de la grille.
	 */
	private int nbMines;
	
	/**
	 * Taille en hauteur de la fenêtre en px.
	 */
	private int fenetreH;
	
	/**
	 * Taille en longueur de la fenêtre en px.
	 */
	private int fenetreL;
	
	/**
	 * charger permet de ne pas initialiser les tableaux tabCases
	 * et tabCasesFlags si ils sont chargés depuis le fichier de sauvegarde.
	 */
	private boolean charger = false;
	
	/**
	 * etiquette est un tableau à 2 dimensions d'etiquettes définissant toutes les cases du plateau du jeu.
	 */
	private JLabel[][] etiquette;
	
	/**
	 * assetsPath est le chemin relatif des images utilisées dans le jeu.
	 */
	private String assetsPath;
	
	/**
	 * tabNumeros est un tableau contenant les images des numéros des cases de 0 à 8.
	 */
	private ImageIcon[] tabNumeros;
	
	/**
	 * imageMine contient l'image de la mine qui sera affichée sur les cases lorsqu'on gagne ou on perd.
	 */
	private Icon imageMine;
	
	/**
	 * imageDrapeau contient l'image du drapeau qui sera affichée
	 * sur les cases vides lorsqu'on fait un clic droit sur celles-ci.
	 */
	private Icon imageDrapeau;
	
	/**
	 * imagePointInterr contient l'image du point d'interrogation qui remplacera 
	 * l'image du drapeau lorsqu'on fait un clic droit sur les cases marquées d'un drapeau.
	 */
	private Icon imagePointInterr;
	
	/**
	 * Tableau à 2 dimensions des numéros des cases : 
	 *		0-8 pour les cases vides
	 *		9 pour les cases minées
	 */
	private int[][] tabCases;
	
	/**
	 * Tableau à 2 dimensions des cases cliquées : 
	 * 		0 pour les cases non cliquées
	 * 		1 pour les cases cliquées
	 *		2 pour les cases marquées d'un drapeau
	 *		3 pour les cases marquées d'un point d'interrogation
	 *		4 pour la case minée cliquée
	 */
	private int[][] tabCasesFlags;
	
	/**
	 * sauv_i est utilisée pour sauvegarder la valeur de i lors
	 * du clic gauche sur une case pour connaître l'ordonnée de la case.
	 */
	private int sauv_i;
	
	/**
	 * sauv_j est utilisée pour sauvegarder la valeur de j lors
	 * du clic gauche sur une case pour connaître l'abscisse de la case.
	 */
	private int sauv_j;
	
	/**
	 * grilleActive permet de désactiver le clic et le survol de souris lorsqu'on gagne ou on perd.
	 */
	private boolean grilleActive = true;
	
	/**
	 * menu de droite du jeu permettant d'afficher le nombre de mines moins le nombre de drapeaux.
	 */
	private JPanel menu;
	
	/**
	 * nbMinesRestantes est la variable qui stocke le nombre de mines moins le nombre de drapeaux.
	 */
	private int nbMinesRestantes;
	
	/**
	 * l_nbMinesRestantes est l'etiquette qui affiche
	 * le contenu de la variable nbMinesRestantes dans le menu de droite.
	 */
	private JLabel l_nbMinesRestantes = new JLabel();
	
	/**
	 * cases est le panneau de la grille de jeu qui est affiché à l'écran.
	 */
	private JPanel cases;
	
	/**
	 * ssMenu_sauverEtQuitter est un sous-menu du menu Partie activé uniquement en cours de partie.
	 */
	private JMenuItem ssMenu_sauverEtQuitter;
	
	/**
	 * sauver permet de sauvegarder avant de quitter uniquement en cours de partie.
	 */
	private boolean sauver = false;
	
	/**
	 * ssMenu_reprendre est un sous-menu du menu Partie activé si un fichier de sauvegarde existe.
	 */
	private JMenuItem ssMenu_reprendre;
	
	/**
	 * menuPartie est un élément de la barre de menu désactivé uniquement pendant l'animation de 
	 * l'explosion de la mine pour éviter de créer, recommencer ou reprendre une partie pendant qu'on perd.
	 */
	private JMenu menuPartie;
	
	/**
	 * Même commentaire que pour menuPartie mais désactive le menu Aide.
	 */
	private JMenu menuAide;
	
	/**
	 * Fenêtre principale du jeu.
	 */
	private JFrame fenetre;
	
	/**
	 * Timer permettant de laisser le temps de voir l'explosion de la mine avant d'afficher la boîte de dialogue.
	 */
	private Timer timerPerdre;
	
	/**
	 * Timer permettant d'exécuter un listener toutes les 0.2s pour augmenter la valeur de tempsMs.
	 */
	private Timer timerTemps;
	
	/**
	 * SimpleDateFormat décrit le format de l'horloge à afficher en l'occurrence HH:mm:ss ici.
	 */
	private SimpleDateFormat formatDate;
	
	/**
	 * calendrier l'horloge.
	 */
	private Calendar calendrier;
	
	/**
	 * horloge est l'étiquette de l'horloge qui sera affichée à l'écran et actualisée toutes les secondes.
	 */
	private JLabel horloge;
	
	/**
	 * tempsMs est le compteur de l'horloge en ms.
	 */
	private long tempsMs;
	
	/**
	 * timerPause permet de savoir quand l'horloge doit se mettre en pause (perte du focus et réduction de la fenêtre).
	 */
	private boolean timerPause;
	
	/**
	 * Constructeur Jeu pour commencer une nouvelle partie.
	 *
	 * @param tailleH Taille en hauteur soit le nombre de lignes de la grille.
	 * @param tailleL Taille en longueur soit le nombre de colonnes de la grille.
	 * @param nbMines Nombre de mines de la grille.
	 * @param fenetreH Taille en hauteur de la fenêtre en px.
	 * @param fenetreL Taille en longueur de la fenêtre en px.
	 */
	public Jeu(int tailleH, int tailleL, int nbMines, int fenetreH, int fenetreL) {
		this.tailleH = tailleH;
		this.tailleL = tailleL;
		this.nbMines = nbMines;
		
		this.fenetreH = fenetreH;
		this.fenetreL = fenetreL;
	}
	
	
	/**
	 * Constructeur Jeu pour reprendre la dernière partie.
	 *
	 * @param tabCases_lecture Même tableau que tabCases mais contient les valeurs enregistrées du fichier de sauvegarde.
	 * @param tabCasesFlags_lecture Même tableau que tabCasesFlags mais contient les valeurs enregistrées du fichier de sauvegarde.
	 * @param tailleH_lecture Taille en hauteur soit le nombre de lignes de la grille de la sauvegarde.
	 * @param tailleL_lecture Taille en longueur soit le nombre de colonnes de la grille de la sauvegarde.
	 * @param fenetreH_lecture Taille en hauteur de la fenêtre en px de la sauvegarde.
	 * @param fenetreL_lecture Taille en longueur de la fenêtre en px de la sauvegarde.
	 * @param tempsMs_lecture Compteur de l'horloge en ms.
	 */
	public Jeu(int[][] tabCases_lecture, int[][] tabCasesFlags_lecture, int tailleH_lecture, int tailleL_lecture, int fenetreH_lecture, int fenetreL_lecture, long tempsMs_lecture) {
		this.tabCases = tabCases_lecture;
		this.tabCasesFlags = tabCasesFlags_lecture;
		
		this.tailleH = tailleH_lecture;
		this.tailleL = tailleL_lecture;
		
		this.fenetreH = fenetreH_lecture;
		this.fenetreL = fenetreL_lecture;
		
		this.tempsMs = tempsMs_lecture;
		
		// Compte le nombre de mines du tableau tabCases lors de la restauration de la partie
		nbMines = 0;
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				if(this.tabCases[i][j] == 9) {
					nbMines++;
				}
			}
		}
		
		charger = true; // Ne pas initialiser les tableaux et découvrir les cases déjà cliquées
		timerPause = false;
	}
	
	
	
	
	/**
	 * Initialise tout le jeu et l'affiche à l'écran.
	 */
	public void afficher() {
		fenetre = new JFrame("Démineur");
		fenetre.setSize(fenetreL, fenetreH);
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setMinimumSize(new Dimension(350, 268));
		
		Demineur demineur = new Demineur();
		assetsPath = demineur.assetsPath;
		
		fenetre.setIconImage(new ImageIcon(assetsPath + "mine120.png").getImage());
		
		initBarreMenu();
		
		initPlateauJeu();
		
		initMenuMinesRestantes();

		fenetre.add(cases, BorderLayout.CENTER);
		fenetre.add(menu, BorderLayout.EAST);
		
		fenetre.addWindowListener(this);
		
		fenetre.setVisible(true); 
	}
	
	
	
	
	/**
	 * Initialise les etiquettes et les tableaux du jeu.
	 */
	private void initPlateauJeu() {
		cases = new JPanel();
		cases.setBackground(Color.BLACK);
		etiquette = new JLabel[tailleH][tailleL];
		
		imageMine = new ImageIcon(assetsPath + "mine_petit.png");
		imageDrapeau = new ImageIcon(assetsPath + "drapeau_petit.png");
		imagePointInterr = new ImageIcon(assetsPath + "point_interrogation_petit.png");
		
		tabNumeros = new ImageIcon[9]; // Création d'un tableau d'icones contenant les icones des chiffres de 0 à 8
		for (int i = 0; i < 9; i++) {
			tabNumeros[i] = new ImageIcon(assetsPath + i + ".png");
		}
		
		// Gestionnaire pour générer la grille
		GridLayout gestionnaire = new GridLayout(tailleH, tailleL);
		cases.setLayout(gestionnaire);
		
		if(!charger) {
			initTableaux();
		}
		
		// Création des cases graphiquement
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				etiquette[i][j] = new JLabel();
				etiquette[i][j].setHorizontalAlignment(JLabel.CENTER);
				etiquette[i][j].setVerticalAlignment(JLabel.CENTER);
				Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
				etiquette[i][j].setBorder(border);
				etiquette[i][j].setOpaque(true);
				etiquette[i][j].setBackground(new Color(0, 100, 200));
				etiquette[i][j].addMouseListener(this);
				
				if (tabCasesFlags[i][j] == 2) {
					etiquette[i][j].setIcon(imageDrapeau);
				}
				else if (tabCasesFlags[i][j] == 3) {
					etiquette[i][j].setIcon(imagePointInterr);
				}
				
				cases.add(etiquette[i][j]);
			}
		}
		
		// Découverte des cases cliquées lors de la restauration de la dernière partie
		if(charger) {
			for(int i = 0; i < tailleH; i++) {
				for(int j = 0; j < tailleL; j++) {
					if (tabCasesFlags[i][j] == 1) {
						etiquette[i][j].setBackground(new Color(150, 150, 150)); // Case grise
						etiquette[i][j].setIcon(tabNumeros[tabCases[i][j]]); // Ajout icone du numéro correspondant
					}
				}
			}
			ssMenu_sauverEtQuitter.setEnabled(true);
			sauver = true;
			File fichierSauvegarde = new File("sauvegarde.bin");
			fichierSauvegarde.delete(); // Supprimer la sauvegarde une fois que la partie est chargée
			ssMenu_reprendre.setEnabled(false);
		}
	}
	
	
	
	
	/**
	 * Crée et Initialise la barre de menu.
	 */
	private void initBarreMenu() {
		JMenuBar barreMenu = new JMenuBar();
		menuPartie = new JMenu("Partie");
		menuAide = new JMenu("?");
		JMenuItem ssMenu_recommencer = new JMenuItem("Recommencer");
		JMenuItem ssMenu_nvellePartie = new JMenuItem("Nouvelle partie");
		ssMenu_reprendre = new JMenuItem("Reprendre la dernière partie sauvegardée");
		ssMenu_sauverEtQuitter = new JMenuItem("Sauver et quitter");
		ssMenu_sauverEtQuitter.setEnabled(false);
		JMenuItem ssMenu_quitter = new JMenuItem("Quitter le jeu sans sauvegarder");
		JMenuItem ssMenu_aPropos = new JMenuItem("A propos...");

		menuPartie.add(ssMenu_recommencer);
		menuPartie.add(ssMenu_nvellePartie);
		menuPartie.add(ssMenu_reprendre);
		menuPartie.addSeparator();
		menuPartie.add(ssMenu_sauverEtQuitter);
		menuPartie.add(ssMenu_quitter);
		menuAide.add(ssMenu_aPropos);
		
		ssMenu_recommencer.addActionListener(this);
		ssMenu_nvellePartie.addActionListener(this);
		ssMenu_reprendre.addActionListener(this);
		ssMenu_sauverEtQuitter.addActionListener(this);
		ssMenu_quitter.addActionListener(this);
		ssMenu_aPropos.addActionListener(this);
		
		barreMenu.add(menuPartie);
		barreMenu.add(menuAide);
		fenetre.setJMenuBar(barreMenu);
		
		
		File fichierSauvegarde_bouton = new File("sauvegarde.bin");
		if(fichierSauvegarde_bouton.exists()) { // Activation du bouton "Reprendre la dernière partie sauvegardée" si une sauvegarde existe
			ssMenu_reprendre.setEnabled(true);
		}
		else {
			ssMenu_reprendre.setEnabled(false);
		}
	}
	
	
	
	
	/**
	 * Initialise le menu de droite du jeu.
	 */
	private void initMenuMinesRestantes() {
		menu = new JPanel();
		menu.setLayout(new GridLayout(0,1));
		menu.setBackground(new Color(200, 200, 200));
		l_nbMinesRestantes.setHorizontalAlignment(JLabel.CENTER);
		l_nbMinesRestantes.setFont(new Font("Lucida Grande", 1, 35));
		JLabel imageMineCompte = new JLabel();
		Icon imageMineMenu = new ImageIcon(assetsPath + "mine128.png");
		imageMineCompte.setIcon(imageMineMenu);
		
		JLabel separateur = new JLabel();
		
		formatDate = new SimpleDateFormat("HH:mm:ss");
		
		JLabel imageHorloge = new JLabel();
		imageHorloge.setIcon(new ImageIcon(assetsPath + "horloge.png"));
		
		horloge = new JLabel();
		
		timerTemps = new Timer(200, this); // Timer de 0.2s pour compter le temps passé
		timerTemps.setRepeats(true);
		
		if (!charger) {
			tempsMs = -3600000; // 01:00:00 si tempsMs = 0 moins 3600000ms pour commencer à 00:00:00
		}
		else {
			/* S'il n'y a pas de drapeau sur la grille ni de point d'interrogation ou 
			s'il y a au moins une case découverte, on active le timer */
			if ((!valeurExisteDansTableau2(tabCasesFlags, 2) && !valeurExisteDansTableau2(tabCasesFlags, 3)) || valeurExisteDansTableau2(tabCasesFlags, 1)) {
				timerTemps.start();
			}
		}
		
		calendrier = Calendar.getInstance();
		calendrier.setTimeInMillis(tempsMs);
		horloge.setText(formatDate.format(calendrier.getTime()));
		horloge.setFont(new Font("Lucida Grande", 1, 25));
		horloge.setHorizontalAlignment(JLabel.CENTER);
		
		
		menu.add(imageMineCompte);
		menu.add(l_nbMinesRestantes);
		menu.add(separateur);
		menu.add(imageHorloge);
		menu.add(horloge);
		
		initNbMinesRestantes();
	}
	
	
	
	
	/**
	 * Initialise les tableaux tabCases et tabCasesFlags du jeu.
	 */
	private void initTableaux() {
		tabCases = new int[tailleH][tailleL];
		tabCasesFlags = new int[tailleH][tailleL];
		
		for(int i = 0; i < tailleH; i++) { // Initialisation du tableau à 0;
			for(int j = 0; j < tailleL; j++) {
				tabCasesFlags[i][j] = 0;
			}
		}
		
		int[] tabMines = new int[nbMines]; // Tableau contenant les mines symbolisées par les numéros des cases de 0 au nombre max de cases 
		boolean valExiste = false;
		int caseMine;
		
		for (int i = 0; i < tabMines.length; i++) { 
			// Initialisation de tabMines à -1 pour éviter que les 0 soient comptés comme des mines
			tabMines[i] = -1;
		}
		
		for(int i = 0; i < tabMines.length; i++) { // Remplissage du tableau tabMines 
			// Boucle tant que la valeur tirée existe dans le tableau pour ne pas avoir plusieurs mines dans la même case
			do {
				caseMine = new Random().nextInt(tailleH*tailleL);
				valExiste = valeurExisteDansTableau(tabMines, caseMine);
			} while(valExiste);
			
			tabMines[i] = caseMine;
		}
		
		// Remplissage du tableau tabCases avec des 0 pour une case non minée et 9 pour une case minée
		int compteCases = 0;
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				tabCases[i][j] = 0;
				
				for(int k = 0; k < tabMines.length; k++) {
					if(compteCases == tabMines[k]) {
						tabCases[i][j] = 9;
						break;
					}
				}
				compteCases++;
			}
		}
		
		// Remplissage du tableau tabCases avec les numéros correspondant au nombre de mines adjacentes
		int compteMine;
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				if(tabCases[i][j] != 9) {
					// Si case non minée, on regarde pour chaque case adjacente si elle est minée et on incrémente alors compteMine
					compteMine = 0;
					if(i-1 >= 0) { if(tabCases[i-1][j] == 9) compteMine++; }
					if(i+1 < tailleH) { if(tabCases[i+1][j] == 9) compteMine++; }
					
					if (i-1 >= 0 && j-1 >= 0) { if(tabCases[i-1][j-1] == 9) compteMine++; }
					if (j-1 >= 0) { if(tabCases[i][j-1] == 9) compteMine++; }
					if (i+1 < tailleH && j-1 >= 0) { if(tabCases[i+1][j-1] == 9) compteMine++; }
					
					if (i-1 >= 0 && j+1 < tailleL) { if(tabCases[i-1][j+1] == 9) compteMine++; }
					if (j+1 < tailleL) { if(tabCases[i][j+1] == 9) compteMine++; }
					if (i+1 < tailleH && j+1 < tailleL) { if(tabCases[i+1][j+1] == 9) compteMine++; }
					
					/* Le nombre de mines adjacentes est alors mis dans le tableau tabCases
					contenant maintenant les cases minées notées 9 et non minées notées de 0 à 8 */
					tabCases[i][j] = compteMine;
					
				}
			}
		}
		
		initNbMinesRestantes();
	}
	
	
	
	
	/**
	 * La méthode valeurExisteDansTableau() vérifie si une valeur existe dans un tableau.
	 * 
	 * @param tab[] Le tableau à parcourir.
	 * @param valeur La valeur à comparer aux valeurs du tableau.
	 * @return vrai si la valeur est dans le tableau ou faux si elle n'y est pas.
	 */
	private boolean valeurExisteDansTableau(int tab[], int valeur){
		for(int i = 0; i < tab.length; i++){
			if(valeur == tab[i]) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * La méthode valeurExisteDansTableau2() vérifie si une valeur existe dans un tableau à 2 dimensions.
	 * 
	 * @param tab[][] Le tableau à parcourir.
	 * @param valeur La valeur à comparer aux valeurs du tableau.
	 * @return vrai si la valeur est dans le tableau ou faux si elle n'y est pas.
	 */
	private boolean valeurExisteDansTableau2(int tab[][], int valeur){
		for(int i = 0; i < tab.length; i++){
			for(int j = 0; j < tab[0].length; j++){
				if(valeur == tab[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * La méthode actionPerformed() est appelée lors d'un clic sur un des 6 boutons de la barre de menu.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Recommencer") {
			Jeu recommencer = new Jeu(tailleH, tailleL, nbMines, fenetreH, fenetreL);
			recommencer.afficher();
			fenetre.dispose();
		}
		else if(e.getActionCommand() == "Nouvelle partie") {
			ChoixPartie partie = new ChoixPartie(fenetre);
			partie.parametres();
		}
		else if(e.getActionCommand() == "Reprendre la dernière partie sauvegardée") {
			Sauvegarde partie = new Sauvegarde();
			Object tab[] = partie.charger(); // Charge le tableau d'objets contenant la taille de la grille et les 2 tableaux
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
			fenetre.dispose();
			nouveauJeu.afficher();
		}
		else if(e.getActionCommand() == "Sauver et quitter") {
			Sauvegarde partie = new Sauvegarde();
			partie.sauvegarder(tabCases, tabCasesFlags, tailleH, tailleL, tempsMs);
			
			System.exit(0);
		}
		else if(e.getActionCommand() == "Quitter le jeu sans sauvegarder") {
			System.exit(0);
		}
		else if(e.getActionCommand() == "A propos...") {
			APropos aPropos = new APropos();
			aPropos.afficher();
		}
		
		
		else if (e.getSource().equals(timerPerdre)) {
			dialogueGagnerPerdre("Partie perdue", "Vous avez perdu cette partie. Vous aurez peut-être plus de chance la prochaine fois. :)");
				
			menuPartie.setEnabled(true);
			menuAide.setEnabled(true);
		}
		else if (e.getSource().equals(timerTemps)) {
			if (!timerPause) {
				if (tempsMs >= 82799000) { // Correspond à 23:59:59
					ActionListener[] timerListener = timerTemps.getActionListeners();
					timerTemps.removeActionListener(timerListener[0]); // Supprime le listener lorsque l'horloge est à 23:59:59
				}
				tempsMs += 200;
				
				calendrier.setTimeInMillis(tempsMs);
				horloge.setText(formatDate.format(calendrier.getTime()));
			}
		}
	}
	
	
	
	
	/**
	 * La méthode mousePressed() est appelée lors d'un clic sur une case du jeu.
	 * 		Clic gauche : découvre la case cliquée si elle n'est pas minée.
	 *		Clic droit : marque la case d'un drapeau ou d'un point d'interrogation.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void mousePressed(MouseEvent e) {
		if (grilleActive) {
			ssMenu_sauverEtQuitter.setEnabled(true);
			sauver = true;
			JLabel etiquetteSource = (JLabel) e.getSource(); // Obtient l'étiquette qui a été cliquée
			for(int i = 0; i < tailleH; i++) { // Recherche des coordonnées de l'étiquette cliquée
				for(int j = 0; j < tailleL; j++) {
					if (etiquette[i][j].equals(etiquetteSource)) {
						sauv_i = i;
						sauv_j = j;
					}
				}
			}

			if (e.getModifiers() == InputEvent.BUTTON3_MASK) { // Si clic droit
				if(etiquetteSource.getIcon() == null) {
					etiquetteSource.setIcon(imageDrapeau);
					tabCasesFlags[sauv_i][sauv_j] = 2;
					nbMinesRestantes--; // Décrémente le nombre de mines restantes
				}
				else if(etiquetteSource.getIcon().equals(imageDrapeau)) {
					etiquetteSource.setIcon(imagePointInterr);
					tabCasesFlags[sauv_i][sauv_j] = 3;
					nbMinesRestantes++; // Incrémente le nombre de mines restantes
				}
				else if(etiquetteSource.getIcon().equals(imagePointInterr)) {
					tabCasesFlags[sauv_i][sauv_j] = 0;
					etiquetteSource.setIcon(null);
				}
				l_nbMinesRestantes.setText(Integer.toString(nbMinesRestantes));
			}
			
			if (e.getModifiers() == InputEvent.BUTTON1_MASK) { // Si clic gauche et case non marquée
				if (!timerTemps.isRunning()) {
					timerTemps.start();
				}
				if (tabCasesFlags[sauv_i][sauv_j] != 2 && tabCasesFlags[sauv_i][sauv_j] != 3) {
					etiquetteSource.setBackground(new Color(150, 150, 150)); // Case devient grise
					tabCasesFlags[sauv_i][sauv_j] = 1;
					
					if (tabCases[sauv_i][sauv_j] == 9) { // Si clic sur une bombe
						tabCasesFlags[sauv_i][sauv_j] = 4;
						perdre(sauv_i, sauv_j);
					}
					else {
						etiquetteSource.setIcon(tabNumeros[tabCases[sauv_i][sauv_j]]); // Affichage du numéro correspondant
						
						decouvrirCases(sauv_i, sauv_j); // Découverte des cases dans le tableau tabCasesFlags
						
						for(int k = 0; k < tailleH; k++) { // Découverte des cases graphiquement
							for(int l = 0; l < tailleL; l++) {
								if (tabCasesFlags[k][l] == 1) {
									etiquette[k][l].setBackground(new Color(150, 150, 150)); // Cases adjacentes découvertes deviennent grises
									etiquette[k][l].setIcon(tabNumeros[tabCases[k][l]]); // Affichage du numéro correspondant
								}
							}
						}
						testGagner();
					}
				}
			}
		}
	}
	
	
	
	
	/**
	 * La méthode mouseEntered() est appelée lors de l'entrée du curseur
	 * sur une case du jeu et modifie la couleur de la case survolée.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void mouseEntered(MouseEvent e) {
		if (grilleActive) {
			JLabel etiquetteSource = (JLabel) e.getSource();
			if(etiquetteSource.getIcon() == null || etiquetteSource.getIcon().equals(imageDrapeau) || etiquetteSource.getIcon().equals(imagePointInterr)) {
				etiquetteSource.setBackground(new Color(0, 150, 200)); // Case bleu clair si survolée
			}
			else {
				etiquetteSource.setBackground(new Color(150, 150, 150)); // Case grise si découverte
			}
		}
	}
	
	
	
	
	/**
	 * La méthode mouseEntered() est appelée lors de la sortie du curseur
	 * sur une case du jeu et réinitialise la couleur de la case survolée.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	public void mouseExited(MouseEvent e) {
		if (grilleActive) {
			JLabel etiquetteSource = (JLabel) e.getSource();
			if(etiquetteSource.getIcon() == null || etiquetteSource.getIcon().equals(imageDrapeau) || etiquetteSource.getIcon().equals(imagePointInterr)) {
				etiquetteSource.setBackground(new Color(0, 100, 200)); // Case bleue si non survolée
			}
			else {
				etiquetteSource.setBackground(new Color(150, 150, 150)); // Case grise si découverte
			}
		}
	}
	
	
	
	
	/**
	 * La méthode initNbMinesRestantes() calcule le nombre de mines restantes lors du début ou à la restauration d'une partie.
	 */
	private void initNbMinesRestantes() {
		nbMinesRestantes = 0;
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				if(tabCases[i][j] == 9) {
					nbMinesRestantes++;
				}
				if(tabCasesFlags[i][j] == 2) {
					nbMinesRestantes--;
				}
			}
		}
		
		l_nbMinesRestantes.setText(Integer.toString(nbMinesRestantes));
	}
	
	
	
	
	/**
	 * La méthode decouvrirCases() permet de découvrir les 8 cases adjacentes à la case cliquée et aux cases vides.
	 * 
	 * @param sauv_i l'ordonnée de la case cliquée
	 * @param sauv_j l'abscisse de la case cliquée
	 */
	private void decouvrirCases(int sauv_i, int sauv_j) {
		if (tabCases[sauv_i][sauv_j] == 0) { // Si la case cliquée n'a pas de mine autour d'elle, on découvre les 8 cases adjacentes
			if (sauv_i-1 >= 0 && tabCasesFlags[sauv_i-1][sauv_j] == 0) {
				tabCasesFlags[sauv_i-1][sauv_j] = 1;//Découverte case du haut
				decouvrirCases(sauv_i-1, sauv_j);
			}
			if (sauv_j+1 < tailleL && sauv_i-1 >= 0 && tabCasesFlags[sauv_i-1][sauv_j+1] == 0) {
				tabCasesFlags[sauv_i-1][sauv_j+1] = 1;//Découverte case du haut droit
				decouvrirCases(sauv_i-1, sauv_j+1);
			}
			if (sauv_j+1 < tailleL && tabCasesFlags[sauv_i][sauv_j+1] == 0) {
				tabCasesFlags[sauv_i][sauv_j+1] = 1;//Découverte case de droite
				decouvrirCases(sauv_i, sauv_j+1);
			}
			if (sauv_j+1 < tailleL && sauv_i+1 < tailleH && tabCasesFlags[sauv_i+1][sauv_j+1] == 0) {
				tabCasesFlags[sauv_i+1][sauv_j+1] = 1;//Découverte case du bas droit
				decouvrirCases(sauv_i+1, sauv_j+1);
			}
			if (sauv_i+1 < tailleH && tabCasesFlags[sauv_i+1][sauv_j] == 0) {
				tabCasesFlags[sauv_i+1][sauv_j] = 1;//Découverte case du bas
				decouvrirCases(sauv_i+1, sauv_j);
			}
			if (sauv_j-1 >= 0 && sauv_i+1 < tailleH && tabCasesFlags[sauv_i+1][sauv_j-1] == 0) {
				tabCasesFlags[sauv_i+1][sauv_j-1] = 1;//Découverte case du bas gauche
				decouvrirCases(sauv_i+1, sauv_j-1);
			}
			if (sauv_j-1 >= 0 && tabCasesFlags[sauv_i][sauv_j-1] == 0) { 
				tabCasesFlags[sauv_i][sauv_j-1] = 1; //Découverte case de gauche
				decouvrirCases(sauv_i, sauv_j-1);
			}
			if (sauv_j-1 >= 0 && sauv_i-1 >= 0 && tabCasesFlags[sauv_i-1][sauv_j-1] == 0) {
				tabCasesFlags[sauv_i-1][sauv_j-1] = 1;//Découverte case du haut gauche
				decouvrirCases(sauv_i-1, sauv_j-1);
			}
		}
	}
	
	
	
	
	/**
	 * La méthode perdre() révèle toutes les cases de la grille en affichant en rouge les cases minées, en noir la case minée cliquée,
	 * en bleu pâle les cases non découvertes et en affichant une croix rouge sur les cases non minées marquées.
	 * Puis elle affiche une boîte de dialogue permettant de créer une nouvelle partie, recommencer une partie ou quitter le jeu.
	 * 
	 * @param i_mineCliquee l'ordonnée de la case minée cliquée
	 * @param j_mineCliquee l'abscisse de la case minée cliquée
	 */
	private void perdre(int i_mineCliquee, int j_mineCliquee) {
		timerTemps.stop();
		grilleActive = false;
		ssMenu_sauverEtQuitter.setEnabled(false);
		sauver = false;
		
		ImageIcon[] tabNumerosCroix = new ImageIcon[9];
		for (int i = 0; i < 9; i++) {
			tabNumerosCroix[i] = new ImageIcon(assetsPath + i + "croix.png");
		}
		
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {		
				if (tabCases[i][j] == 9) {
					etiquette[i][j].setBackground(new Color(200, 50, 50)); // Case minée en rouge et affichage d'une mine
					etiquette[i][j].setIcon(imageMine);
				}
				else if (tabCases[i][j] != 9 && (tabCasesFlags[i][j] == 2 || tabCasesFlags[i][j] == 3)) {
					etiquette[i][j].setBackground(new Color(150, 150, 150)); // Case marquée en gris et affichage d'une croix sur le numéro
					etiquette[i][j].setIcon(tabNumerosCroix[tabCases[i][j]]);
				}
				else if (tabCasesFlags[i][j] == 0) {
					etiquette[i][j].setBackground(new Color(120, 120, 255)); // Case non découverte en bleu pâle et affichage du numéro
					etiquette[i][j].setIcon(tabNumeros[tabCases[i][j]]);
				}
				else {
					etiquette[i][j].setBackground(new Color(150, 150, 150)); // Case découverte en gris et affichage du numéro
					etiquette[i][j].setIcon(tabNumeros[tabCases[i][j]]);
				}
			}
		}
		
		Image mineExplosion_gif = Toolkit.getDefaultToolkit().createImage(assetsPath + "mine_explosion.gif");
		ImageIcon mineExplosion = new ImageIcon(mineExplosion_gif);
		etiquette[i_mineCliquee][j_mineCliquee].setIcon(mineExplosion); // Case minée cliquée en noir et affichage de l'explosion de la mine
		etiquette[i_mineCliquee][j_mineCliquee].setBackground(new Color(0, 0, 0));
		
		// Désactive les menus pendant le temps d'attente du timer
		menuPartie.setEnabled(false);
		menuAide.setEnabled(false);
		
		timerPerdre = new Timer(1200, this); // Timer d'1,2s pour voir l'explosion de la mine avant d'afficher la boîte de dialogue
		timerPerdre.setRepeats(false);
		timerPerdre.start();
		
	}
	
	
	
	
	/**
	 * La méthode testGagner() parcours le tableau tabCasesFlags et compte le nombre de 1 dans chaque case du tableau.
	 * Si le nombre de 1 correspond au nombre de cases du jeu moins le nombre de mines, alors on gagne.
	 */
	private void testGagner() {
		int nbCasesRevelees = 0;
		for(int k = 0; k < tailleH; k++) {
			for(int l = 0; l < tailleL; l++) {
				if (tabCasesFlags[k][l] == 1) {
					nbCasesRevelees++;
				}
			}
		}
		
		if (nbCasesRevelees == tailleH*tailleL-nbMines) {
			gagner();
		}
	}
	
	
	
	
	/**
	 * La méthode gagner() révèle toutes les cases de la grille en affichant en vert les cases minées
	 * puis elle affiche une boîte de dialogue permettant de créer une nouvelle partie, recommencer une partie,
	 * revenir au menu ou quitter le jeu.
	 */
	private void gagner() {
		timerTemps.stop();
		grilleActive = false;
		ssMenu_sauverEtQuitter.setEnabled(false);
		sauver = false;
		l_nbMinesRestantes.setText("0");
		for(int i = 0; i < tailleH; i++) {
			for(int j = 0; j < tailleL; j++) {
				tabCasesFlags[sauv_i][sauv_j] = 1;
				if (tabCases[i][j] == 9) {
					etiquette[i][j].setBackground(new Color(50, 200, 50)); // Case minée en vert et affichage de la mine
					etiquette[i][j].setIcon(imageMine);
				}
				else {
					etiquette[i][j].setBackground(new Color(150, 150, 150)); // Case non minée en gris et affichage du numéro
					etiquette[i][j].setIcon(tabNumeros[tabCases[i][j]]);
				}
			}
		}
		
		try {
			calendrier.setTime(formatDate.parse(horloge.getText()));
			String messageGagner = "Félicitations, vous avez gagné en ";
			
			if (calendrier.get(Calendar.HOUR_OF_DAY) == 0 && calendrier.get(Calendar.MINUTE) == 0) {
				dialogueGagnerPerdre("Partie gagnée", messageGagner + calendrier.get(Calendar.SECOND) + "s !");
			}
			else if (calendrier.get(Calendar.HOUR_OF_DAY) == 0 && calendrier.get(Calendar.MINUTE) != 0) {
				dialogueGagnerPerdre("Partie gagnée", messageGagner + calendrier.get(Calendar.MINUTE) + "m " + calendrier.get(Calendar.SECOND) + "s !");
			}
			else {
				dialogueGagnerPerdre("Partie gagnée", messageGagner + calendrier.get(Calendar.HOUR_OF_DAY) + "h " + calendrier.get(Calendar.MINUTE) + "m " + calendrier.get(Calendar.SECOND) + "s !");
			}
		}
		catch (ParseException e) {
			System.err.println("Erreur conversion string vers SimpleDateFormat : " + e.getMessage());
			dialogueGagnerPerdre("Partie gagnée", "Félicitations, vous avez gagné !");
		}
		
		
		
		
	}
	
	
	
	
	/**
	 * La méthode dialogueGagnerPerdre() affiche la boîte de dialogue lorsqu'on gagne ou on perd.
	 * 
	 * @param titre le titre de la boîte de dialogue
	 * @param message le message qui sera affiché en cas de réussite ou d'échec de la partie
	 */
	private void dialogueGagnerPerdre(String titre, String message) {
		JPanel panneau = new JPanel();
		JLabel l_message = new JLabel(message);
		l_message.setHorizontalAlignment(JLabel.CENTER);
		
		panneau.add(l_message);
		
		Object[] choix = {"Nouvelle partie", "Recommencer", "Revenir au menu", "Quitter le jeu"};
		int reponse = JOptionPane.showOptionDialog(
			null,
			panneau,
			titre,
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE,
			null,
			choix,
			null
		);
		
		if (reponse == 0) {
			ChoixPartie partie = new ChoixPartie(fenetre);
			partie.parametres();
		}
		else if (reponse == 1) {
			Jeu recommencer = new Jeu(tailleH, tailleL, nbMines, fenetreH, fenetreL);
			recommencer.afficher();
			fenetre.dispose();
		}
		else if (reponse == 2) {
			fenetre.dispose();
			Menu menuPartie = new Menu();
		}
		else if (reponse == 3) {
			System.exit(0);
		}
	}
	
	
	
	
	/**
	 * La méthode windowClosing() est appelée lors de la fermeture de la fenêtre
	 * avec la croix et permet de sauvegarder la partie avant de quitter le jeu.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// Sauvegarde automatique lors du clic sur la croix avant de fermer la fenêtre
		if (sauver) {
			Sauvegarde partie = new Sauvegarde();
			partie.sauvegarder(tabCases, tabCasesFlags, tailleH, tailleL, tempsMs);
		}
		System.exit(0);
	}
	
	/**
	 * La méthode windowDeactivated() est appelée lors de la perte du focus de la fenêtre et permet de mettre en pause le chronomètre.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowDeactivated(WindowEvent e) { if (sauver) timerPause = true; } // Ici, sauver permet de savoir si on est en cours de partie
	
	/**
	 * La méthode windowIconified() est appelée lorsque la fenêtre se minimise et permet de mettre en pause le chronomètre.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowIconified(WindowEvent e) { if (sauver) timerPause = true; }
	
	/**
	 * La méthode windowActivated() est appelée lorsque la fenêtre revient au premier plan et permet de réactiver le chronomètre.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowActivated(WindowEvent e) { if (sauver) timerPause = false; }
	
	/**
	 * La méthode windowDeiconified() est appelée lorsque la fenêtre revient
	 * au premier plan alors qu'elle était minimisée et permet de réactiver le chronomètre.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowDeiconified(WindowEvent e) { if (sauver) timerPause = false; }
	
	/**
	 * La méthode windowClosed() est appelée lorsque la fenêtre est fermée mais elle n'est pas utilisée ici.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowClosed(WindowEvent e) {}
	
	/**
	 * La méthode windowOpened() est appelée lorsque la fenêtre est ouverte mais elle n'est pas utilisée ici.
	 * 
	 * @param e Ce paramètre décrit la source et les circonstances de l'évènement.
	 */
	@Override
	public void windowOpened(WindowEvent e) {}
}