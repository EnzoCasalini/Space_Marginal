package modele;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import controleur.Global;
import outils.connexion.Connection;

public class Attaque extends Thread implements Global {
	
	// D�claration des propri�t�s.
	private Joueur attaquant;
	private JeuServeur jeuServeur;
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>();
	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>();
	
	// Cr�ation du constructeur.
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
			// On affiche la boule aux bonnes coordonn�es.
			laboule.getLabel().getjLabel().setBounds(laboule.getPosX(), laboule.getPosY(), L_BOULE, H_BOULE);
			// On met en pause l'affichage.
			pause(15, 0);
			// On envoie la boule � tous les joueurs.
			jeuServeur.envoi(laboule.getLabel());
			
			// On v�rifie si la boule a touch� un joueur.
			victime = toucheJoueur();
		} 
		while(laboule.getPosX() > 28 && laboule.getPosX() < L_ARENE - 56 && !toucheMur() && victime == null);
		
		// On g�re l'affichage des images lorsqu'un joueur est touch�.
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
			// On v�rifie si la victime est morte, si c'est le cas on affiche l'image du personnage mort.
			if (victime.estMort()) {
				jeuServeur.envoi(DEATH);
				
				// On v�rifie que l'attaquant n'est pas d�j� en mode pr�dator.
				if (attaquant.getNumPerso() != 5)
				{
					// Si la victime qui est morte n'est pas un pr�dator, on parcourt le tableau de joueurs pour v�rifier qu'il n'y a pas un autre pr�dator.
					if (victime.getNumPerso() != 5)
					{
						Enumeration<Connection> e = lesJoueurs.keys();
						while (e.hasMoreElements())
						{
							Connection key = e.nextElement();
							// S'il y a un autre pr�dator, on incr�mente notre variable temp qui sert de compteur.
							if (lesJoueurs.get(key).getNumPerso() == 5)
							{
								temp++;
							}
						}
						// Si temp vaut 0, alors il n'y a pas encore de pr�dator, le tueur peut passer en mode pr�dator.
						if (temp == 0)
						{
							attaquant.modePredator();
						}
						else temp = 0;
					}
					// Si la victime qui vient de mourir �tait un pr�dator, alors celui qui l'a tu�e prend sa place en tant que pr�dator.
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
	
	// M�thode permettant de g�rer la vitesse de la boule.
	// On fait en sorte de mettre en pause l'affichage de la boule sur un thread � part qui ne s'ex�cutera que sur le processus qui affiche la boule.
	public void pause(long milli, int nano) {
		try {
			Thread.sleep(milli, nano);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// M�thode qui g�re la collision entre la boule et les murs.
	private boolean toucheMur() {
		for (Mur mur : lesMurs) {
			if (attaquant.getBoule().toucheObjet(mur))
			{
				return true;
			}
		}
		return false;
	}
	
	// M�thode qui g�re la collision entre la boule et les joueurs. Elle renvoie soit le joueur touch�, soit null.
	private Joueur toucheJoueur() {
		for (Joueur joueur : lesJoueurs.values()) {
				if (attaquant.getBoule().toucheObjet(joueur)) {
					return joueur;
				}
		}
		return null;
	}
	

}
