package modele;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;

public class Mur extends Objet implements Global {
	
	// Déclaration des propriétés.
	
	// Création du constructeur.
	public Mur() {
		// Calcul de positions aléatoires.
		// -64 et +32 afin que le mur ne puisse pas apparaître sur les murs qui bordent l'arène.
		posX = (int) Math.round(Math.random() * (L_ARENE - L_MUR - 64)) + 32;
		posY = (int) Math.round(Math.random() * (H_ARENE - H_MUR - 64)) + 32;
		
		// Création du label pour ce mur.
		// Pas d'importance pour le rang donc on met -1.
		label = new Label(-1, new JLabel());
		// Caractéristiques : Centrage, position et image.
		label.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		label.getjLabel().setVerticalAlignment(SwingConstants.CENTER);
		label.getjLabel().setBounds(posX, posY, L_MUR, H_MUR);
		label.getjLabel().setIcon(new ImageIcon(MUR));
	}

}
