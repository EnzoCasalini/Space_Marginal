package modele;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;
import outils.connexion.Connection;

public class Boule extends Objet implements Global {

	// Déclaration des propriétés.
	private JeuServeur jeuServeur;
	
	
	// Création du constructeur.
	public Boule (JeuServeur jeuServeur) {
		
		this.jeuServeur = jeuServeur;
		
		// Création du JLabel de la boule.
		super.label = new Label(Label.getNbLabel(), new JLabel());
		Label.setNbLabel(Label.getNbLabel()+1);
		
		// On donne les caractéristiques au JLabel de la boule.
		super.label.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		super.label.getjLabel().setVerticalAlignment(SwingConstants.CENTER);
		super.label.getjLabel().setBounds(0, 0, L_BOULE, H_BOULE);
		super.label.getjLabel().setIcon(new ImageIcon(BOULE));
		super.label.getjLabel().setVisible(false);
		
		// On ajoute le JLabel au JPanel du serveur.
		jeuServeur.nouveauLabelJeu(super.label);
	}
	
	// Création de la méthode tireBoule.
	public void tireBoule(Joueur attaquant, ArrayList<Mur> lesMurs, Hashtable<Connection, Joueur> lesJoueurs) {
		// On initialise les positions de départ de la boule.
		if (attaquant.getOrientation() == GAUCHE) {
			attaquant.getBoule().setPosX(attaquant.getPosX() - L_BOULE - 1);
		}
		else {
			attaquant.getBoule().setPosX(attaquant.getPosX() + L_PERSO + 1);	
		}
		
		attaquant.getBoule().setPosY(attaquant.getPosY() + (H_PERSO/2 - 8));
		
		new Attaque(attaquant, jeuServeur, lesMurs, lesJoueurs);
	}
	
}
