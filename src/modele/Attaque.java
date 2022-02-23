package modele;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import controleur.Global;
import outils.connexion.Connection;

public class Attaque extends Thread implements Global {
	
	// Déclaration des propriétés.
	private Joueur attaquant;
	private JeuServeur jeuServeur;
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>();
	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>();
	
	// Création du constructeur.
	public Attaque(Joueur attaquant, JeuServeur jeuServeur, ArrayList<Mur> lesMurs, Hashtable<Connection, Joueur> lesJoueurs) {
		this.attaquant = attaquant;
		this.jeuServeur = jeuServeur;
		this.lesMurs = lesMurs;
		this.lesJoueurs = lesJoueurs;
		
		super.start();
	}
	
	public void run() {
		Boule laboule = attaquant.getBoule();
		int orientation = attaquant.getOrientation();
		laboule.getLabel().getjLabel().setVisible(true);
		Joueur victime = null;
		int temp = 0;
		
		attaquant.affiche(MARCHE, 1);
		
		// Animation pour l'attaque.
		for (int i = 1; i <= NBETATSBOULE; i++) {
			attaquant.affiche(ATTAQUE, i);
			pause(80,0);
		}
		
		do {
			if (orientation == GAUCHE) {
				laboule.setPosX(laboule.getPosX() - LEPAS);
			}
			else {
				laboule.setPosX(laboule.getPosX() + LEPAS);				
			}
			// On affiche la boule aux bonnes coordonnées.
			laboule.getLabel().getjLabel().setBounds(laboule.getPosX(), laboule.getPosY(), L_BOULE, H_BOULE);
			// On met en pause l'affichage.
			pause(15, 0);
			// On envoie la boule à tous les joueurs.
			jeuServeur.envoi(laboule.getLabel());
			
			// On vérifie si la boule a touché un joueur.
			victime = toucheJoueur();
		} 
		while(laboule.getPosX() > 28 && laboule.getPosX() < L_ARENE - 56 && !toucheMur() && victime == null);
		
		// On gère l'affichage des images lorsqu'un joueur est touché.
		if (victime != null && !victime.estMort()) {
			jeuServeur.envoi(HURT);
			if (attaquant.getNumPerso() == 5)
			{
				victime.dpsPredator();
			}
			else
			{
				victime.perteVie();				
			}
			attaquant.gainVie();
			for (int i = 1; i <= NBETATSBLESSE; i++) {
				victime.affiche(BLESSE, i);
				pause(80,0);
			}
			// On vérifie si la victime est morte, si c'est le cas on affiche l'image du personnage mort.
			if (victime.estMort()) {
				jeuServeur.envoi(DEATH);
				
				// On vérifie que l'attaquant n'est pas déjà en mode prédator.
				if (attaquant.getNumPerso() != 5)
				{
					// Si la victime qui est morte n'est pas un prédator, on parcourt le tableau de joueurs pour vérifier qu'il n'y a pas un autre prédator.
					if (victime.getNumPerso() != 5)
					{
						Enumeration<Connection> e = lesJoueurs.keys();
						while (e.hasMoreElements())
						{
							Connection key = e.nextElement();
							// S'il y a un autre prédator, on incrémente notre variable temp qui sert de compteur.
							if (lesJoueurs.get(key).getNumPerso() == 5)
							{
								temp++;
							}
						}
						// Si temp vaut 0, alors il n'y a pas encore de prédator, le tueur peut passer en mode prédator.
						if (temp == 0)
						{
							attaquant.modePredator();
						}
						else temp = 0;
					}
					// Si la victime qui vient de mourir était un prédator, alors celui qui l'a tuée prend sa place en tant que prédator.
					else attaquant.modePredator();
				}
				
				for (int i = 1; i <= NBETATSMORT; i++) {
					victime.affiche(MORT, i);
					pause(80,0);
				}
			}
			else {
				victime.affiche(MARCHE, 1);				
			}
			attaquant.affiche(MARCHE, 1);
		}
		laboule.getLabel().getjLabel().setVisible(false);
		jeuServeur.envoi(laboule.getLabel());
	}
	
	// Méthode permettant de gérer la vitesse de la boule.
	// On fait en sorte de mettre en pause l'affichage de la boule sur un thread à part qui ne s'exécutera que sur le processus qui affiche la boule.
	public void pause(long milli, int nano) {
		try {
			Thread.sleep(milli, nano);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Méthode qui gère la collision entre la boule et les murs.
	private boolean toucheMur() {
		for (Mur mur : lesMurs) {
			if (attaquant.getBoule().toucheObjet(mur))
			{
				return true;
			}
		}
		return false;
	}
	
	// Méthode qui gère la collision entre la boule et les joueurs. Elle renvoie soit le joueur touché, soit null.
	private Joueur toucheJoueur() {
		for (Joueur joueur : lesJoueurs.values()) {
				if (attaquant.getBoule().toucheObjet(joueur)) {
					return joueur;
				}
		}
		return null;
	}
	

}
