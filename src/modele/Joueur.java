package modele;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;
import outils.connexion.Connection;

public class Joueur extends Objet implements Global {
	
	// Déclaration des propriétés. 
	private String pseudo;
	private int numPerso;
	private Label message; 
	private JeuServeur jeuServeur;
	private int vie;
	private int orientation;
	private int etape;
	private Boule boule;
	
	// Déclaration des constantes.
	private static final int MAXVIE = 10,
							 GAIN = 1,
							 PERTE = 2;
	
	// Création du constructeur.
	public Joueur(JeuServeur jeuServeur) {
		// On valorise.
		this.jeuServeur = jeuServeur;
		
		vie = MAXVIE;
		etape = 1;
		orientation = 1;
	}
	
	// Création de la méthode initPerso qui valorise les deux propriétés privées et qui gère les labels du personnage et des infos du joueur (pseudo/hp).
	public void initPerso(String pseudo, int numPerso, Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		// On valorise.
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		
		// On crée la propriété label.
		label = new Label(Label.getNbLabel(), new JLabel());

		Label.setNbLabel(Label.getNbLabel()+1);
		
		// On centre horizontalement et verticalement le jLabel de label.
		label.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		label.getjLabel().setVerticalAlignment(SwingConstants.CENTER);
		
		// On ajoute le label à l'arène.
		this.jeuServeur.nouveauLabelJeu(label);
		
		// On fait de même avec la propriété message.
		message = new Label(Label.getNbLabel(), new JLabel());
		Label.setNbLabel(Label.getNbLabel()+1);
		message.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		// On modifie la police et taille d'écriture du message.
		message.getjLabel().setFont(new Font("Dialog", Font.PLAIN, 8));
		
		this.jeuServeur.nouveauLabelJeu(message);
		
		// On appelle la méthode premierePosition pour placer le joueur.
		premierePosition(lesJoueurs, lesMurs, lesPieges);
		
		affiche(MARCHE, etape);
		
		boule = new Boule(jeuServeur);
		jeuServeur.envoi(boule.getLabel());
	}

	
	// On crée la méthode toucheJoueur qui vérifie que l'objet ne rentre pas en collision avec un joueur.	
	private boolean toucheJoueur(Hashtable<Connection, Joueur> lesJoueurs) {
		for (Joueur joueur : lesJoueurs.values()) {
			if (!joueur.equals(this)) {
				if (toucheObjet(joueur)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// On crée la méthode toucheJoueur qui vérifie que l'objet ne rentre pas en collision avec un mur.
	private boolean toucheMur(ArrayList<Mur> lesMurs) {
		for (Mur mur : lesMurs) {
			if (toucheObjet(mur))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean touchePiege(ArrayList<Piege> lesPieges) {
		// On crée une élément de type Countdown.
		Countdown countdown = new Countdown(lesPieges);
		int i = 0;
		for (Piege piege : lesPieges) {
			// On vérifie si le piège est activé ou non.
			if (!piege.isActivated())
			{
				// S'il n'est pas activé, on vérifie si le joueur touche le piège.
				if (toucheObjet(piege))
				{
					// Si c'est le cas, le piège devient activé et on lance le timer.
					piege.setActivated(true);
					
					// On lance le timer sur le piège qui vient d'être activé.
					countdown.Timer_Activation(i);
					return true;
				}				
			}
			i++;
		}
		return false;
	}
	
	
	// On crée la méthode premierePosition pour créer la position du personnage.
	private void premierePosition(Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		// Récupérer le JLabel du Personnage.
		label.getjLabel().setBounds(0, 0, L_PERSO, H_PERSO);
		
		do {
			posX = (int) Math.round(Math.random() * (L_ARENE - L_PERSO - 64)) + 32;
			posY = (int) Math.round(Math.random() * (H_ARENE - H_PERSO - H_MESSAGE - 64)) + 32;	
			
		} while (toucheJoueur(lesJoueurs) || toucheMur(lesMurs) || touchePiege(lesPieges));
	}
	
	public void affiche(String etat, int etape) {
		// On place le personnage.
		label.getjLabel().setBounds(posX, posY, L_PERSO, H_PERSO);
		// On va chercher l'image correspondant à l'état et l'étape dans lesquels se trouvent le personnage.
		label.getjLabel().setIcon(new ImageIcon(PERSO + numPerso + etat + etape + "d" + orientation + EXTIMAGE));
		
		// On place le personnage.
		message.getjLabel().setBounds(posX - 10, posY + H_PERSO, L_PERSO + 10, H_MESSAGE);
		// On rajoute le texte lié au message.
		message.getjLabel().setText(pseudo + " : " + vie);
		
		// On envoie les labels du joueur à tout le monde.
		this.jeuServeur.envoi(label);
		this.jeuServeur.envoi(message);
	}
	
	
	// On crée la méthode deplace 
	private int deplace(int action, int position, int orientation, int lepas, int max, Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		this.orientation = orientation;
		int ancpos = position;
		
		position += lepas;
		// On vérifie si on ne sort pas de l'arène.
		if (position < 28 ) {
			position = 28;
		}
		if (position > max) {
			position = max;
		}
		
		if (action == GAUCHE || action == DROITE) {
			posX = position;
		}
		else {
			posY = position;
		}
		
		// On vérifie qu'il n'y a pas de collisions avec un joueur ou un mur.
		if (toucheMur(lesMurs) || toucheJoueur(lesJoueurs)) {
			position = ancpos;
		}

		
		// On met à jour les images en fonction du déplacement du personnage.
		etape = etape + 1 > NBETATSMARCHE ? 1 : etape + 1;
		
		
		// On gère la collision d'un joueur avec un piège.
		if (touchePiege(lesPieges)) {
			// Si le joueur se prend un piège, il perd 5HP. 
			// Le joueur ne peut pas mourir d'un piège afin de laisser les autres joueurs le tuer pour le mode prédator.
			jeuServeur.envoi(HURT);
			this.vie -= PERTEPIEGE;
			this.vie = Math.max(1, vie);
			// On affiche l'image du joueur blessé.
				for (int i = 1; i <= NBETATSBLESSE; i++) {
					this.affiche(BLESSE, i);
					pause(150,0);
				}
		}
		
		return position;
	}
	
	// On crée la méthode action qui gère toutes les actions du joueur (déplacements/tirs).
	public void action (int action, Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		switch (action) {
			case GAUCHE : posX = deplace(action, posX, GAUCHE, -LEPAS, L_ARENE - L_PERSO - 28, lesJoueurs, lesMurs, lesPieges);
			break;
			case DROITE :posX = deplace(action, posX, DROITE, LEPAS, L_ARENE - L_PERSO - 28, lesJoueurs, lesMurs, lesPieges);
			break;
			case HAUT : posY = deplace(action, posY, orientation, -LEPAS, H_ARENE - H_PERSO - H_MESSAGE - 28, lesJoueurs, lesMurs, lesPieges);
			break;
			case BAS :posY = deplace(action, posY, orientation, LEPAS, H_ARENE - H_PERSO - H_MESSAGE - 28, lesJoueurs, lesMurs, lesPieges);
			break;
			case TIRE : if (!boule.getLabel().getjLabel().isVisible()) { 
							jeuServeur.envoi(FIGHT);
							boule.tireBoule(this, lesMurs, lesJoueurs);
						}
			break;
		}
		
		affiche(MARCHE, etape);
	}
	
	// Création de la méthode gainVie.
	public void gainVie() {
		vie += GAIN;
	}
	
	// Création de la méthode perteVie.
	public void perteVie() {
		vie -= PERTE;
		vie = Math.max(vie, 0);
	}
	
	public void perteViePiege() {
		vie -= PERTEPIEGE;
		vie = Math.max(vie, 0);
	}
	
	// Création de la méthode estMort.
	public boolean estMort() {
		if (vie == 0) {
			return true;
		}
		return false;
	}
	
	// Création de la méthode departJoueur qui gère la déconnexion d'un joueur.
	public void departJoueur() {
		// On enlève tout ce qui concerne le joueur.
		if (label != null) {
			label.getjLabel().setVisible(false);
			message.getjLabel().setVisible(false);
			boule.getLabel().getjLabel().setVisible(false);
			jeuServeur.envoi(label);
			jeuServeur.envoi(message);
			jeuServeur.envoi(boule.getLabel());
		}
	}
	
	
	// Même méthode pause que la classe Attaque pour les animations.
	public void pause(long milli, int nano) {
		try {
			Thread.sleep(milli, nano);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Gestion du mode predator.
	public void modePredator() {
		vie = Math.max(MAXVIE * 2, vie);
		numPerso = 5;
	}
	
	// Le joueur en mode predator fait 2x plus de dégâts.
	public void dpsPredator() {
		vie -= PERTE * 2;
		vie = Math.max(vie, 0);
	}
	
	// Getter de message.
	public Label getMessage() {
		return message;
	}

	// Getter du pseudo.
	public String getPseudo() {
		return pseudo;
	}

	// Getter de la boule.
	public Boule getBoule() {
		return boule;
	}

	// Getter de l'orientation.
	public int getOrientation() {
		return orientation;
	}

	/**
	 * @return the numPerso
	 */
	public int getNumPerso() {
		return numPerso;
	}

	/**
	 * @param numPerso the numPerso to set
	 */
	public void setNumPerso(int numPerso) {
		this.numPerso = numPerso;
	}	
	
}
