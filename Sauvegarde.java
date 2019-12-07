/* 
 * D�mineur
 * Copyright (C) 2018 david4599
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */


import java.io.*;

/**
 * La classe <code>Sauvegarde</code> permet la sauvegarde et la restauration d'une partie en cours.
 * 
 * @version 1
 * @author david4599
 */
public class Sauvegarde {
	
	/**
	 * Taille en hauteur soit le nombre de lignes de la grille charg�e depuis le fichier de sauvegarde.
	 */
	private int tailleH_lecture;
	
	/**
	 * Taille en longueur soit le nombre de colonnes de la grille charg�e depuis le fichier de sauvegarde.
	 */
	private int tailleL_lecture;
	
	/**
	 * M�me tableau que tabCases mais contient les valeurs enregistr�es du fichier de sauvegarde.
	 */
	private int[][] tabCases_lecture;
	
	/**
	 * M�me tableau que tabCasesFlags mais contient les valeurs enregistr�es du fichier de sauvegarde.
	 */
	private int[][] tabCasesFlags_lecture;
	
	/**
	 * tempsMs_lecture est le compteur de l'horloge en ms charg� depuis le fichier de sauvegarde.
	 */
	private long tempsMs_lecture;
	
	/**
	 * La m�thode Sauvegarder() ouvre et �crit dans le fichier de sauvegarde
	 * les entiers contenus dans tailleH, tailleL, tabCases et tabCasesFlags.
	 *
	 * @param tabCases Le tableau des num�ros des cases.
	 * @param tabCasesFlags Le tableau des num�ros des cases cliqu�es.
	 * @param tailleH Taille en hauteur soit le nombre de lignes de la grille.
	 * @param tailleL Taille en longueur soit le nombre de colonnes de la grille.
	 * @param tempsMs Compteur de l'horloge en ms.
	 */
	public void sauvegarder(int[][] tabCases, int[][] tabCasesFlags, int tailleH, int tailleL, long tempsMs) {
		try {
			DataOutputStream flux = new DataOutputStream(new FileOutputStream(new File("sauvegarde.bin")));
			try {
				flux.writeInt(tailleH);
				flux.writeInt(tailleL);
				
				for(int i = 0; i < tailleH; i++) {
					for(int j = 0; j < tailleL; j++) {
						flux.writeInt(tabCases[i][j]);
					}
				}
				
				for(int i = 0; i < tailleH; i++) {
					for(int j = 0; j < tailleL; j++) {
						flux.writeInt(tabCasesFlags[i][j]);
					}
				}
				
				flux.writeLong(tempsMs);
				
				try {
					flux.close();
				}
				catch(IOException e) {
					System.err.println("Erreur de fermeture du fichier de sauvegarde");
				}
			}
			catch(IOException e) {
				System.err.println("Erreur d'�criture dans le fichier de sauvegarde");
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("Erreur d'ouverture du fichier de sauvegarde");
		}
	}
	
	/**
	 * La m�thode Sauvegarder() ouvre et �crit dans le fichier de sauvegarde
	 * les entiers contenus dans tailleH, tailleL, tabCases, tabCasesFlags et tempsMs.
	 *
	 * @return un objet contenant les variables tailleH, tailleL, tempsMs et les tableaux tabCases, tabCasesFlags.
	 */
	public Object[] charger() {
		try {
			DataInputStream flux = new DataInputStream(new FileInputStream(new File("sauvegarde.bin")));
			try {
				tailleH_lecture = flux.readInt();
				tailleL_lecture = flux.readInt();
				
				tabCases_lecture = new int[tailleH_lecture][tailleL_lecture];
				tabCasesFlags_lecture = new int[tailleH_lecture][tailleL_lecture];
				
				for(int i = 0; i < tailleH_lecture; i++) {
					for(int j = 0; j < tailleL_lecture; j++) {
						tabCases_lecture[i][j] = flux.readInt();
					}
				}
				
				for(int i = 0; i < tailleH_lecture; i++) {
					for(int j = 0; j < tailleL_lecture; j++) {
						tabCasesFlags_lecture[i][j] = flux.readInt();
					}
				}
				
				tempsMs_lecture = flux.readLong();
				
				try {
					flux.close();
				}
				catch(IOException e) {
					System.err.println("Erreur de fermeture du fichier de sauvegarde");
				}
				
			}
			catch(IOException e) {
				System.err.println("Erreur de lecture du fichier de sauvegarde");
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("Erreur d'ouverture du fichier de sauvegarde");
		}

		Object tab[] = new Object[] {tailleH_lecture, tailleL_lecture, tabCases_lecture, tabCasesFlags_lecture, tempsMs_lecture}; 
		
		return tab;
	}
}