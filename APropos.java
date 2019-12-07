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
import javax.swing.*;

/**
 * La classe <code>APropos</code> permet d'ajouter une boîte de dialogue mentionnant les crédits.
 * 
 * @version 1
 * @author david4599
 */
public class APropos {
	
	/**
	 * assetsPath est le chemin relatif des images utilisées dans le jeu.
	 */
	private String assetsPath;
	
	/**
	 * La méthode afficher() crée et affiche la boîte de dialogue contenant les crédits.
	 */
	public void afficher() {	
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(0, 1));
		
		Demineur demineur = new Demineur();
		assetsPath = demineur.assetsPath;
		
		ImageIcon imageMineAPropos = new ImageIcon(assetsPath + "mine_apropos.png");
		JLabel l_imageMineAPropos = new JLabel();
		l_imageMineAPropos.setIcon(imageMineAPropos);
		l_imageMineAPropos.setHorizontalAlignment(JLabel.CENTER);
		menu.add(l_imageMineAPropos);
		
		JLabel l_demineur = new JLabel("Démineur");
		l_demineur.setFont(new Font("Lucida Grande", 1, 30));
		l_demineur.setHorizontalAlignment(JLabel.CENTER);
		menu.add(l_demineur);
		
		JLabel l_copyright = new JLabel("<html><center>Copyright © 2018 david4599 Tous droits réservés</center></html>", SwingConstants.CENTER);
		l_copyright.setVerticalAlignment(SwingConstants.CENTER);
		l_copyright.setFont(new Font("Lucida Grande", 1, 14));
		menu.add(l_copyright);
		
		
		int reponse = JOptionPane.showConfirmDialog(
			null,
			menu,
			"A propos...",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.PLAIN_MESSAGE);
		
	}
}