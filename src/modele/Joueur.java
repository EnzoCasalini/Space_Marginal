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
	
	// D�claration des propri�t�s. 
	private String pseudo;
	private int numPerso;
	private Label message; 
	private JeuServeur jeuServeur;
	private int vie;
	private int orientation;
	private int etape;
	private Boule boule;
	
	// D�claration des constantes.
	private static final int MAXVIE = 10,
							 GAIN = 1,
							 PERTE = 2;
	
	// Cr�ation du constructeur.
	public Joueur(JeuServeur jeuServeur) {
		// On valorise.
		this.jeuServeur = jeuServeur;
		
		vie = MAXVIE;
		etape = 1;
		orientation = 1;
	}
	
	// Cr�ation de la m�thode initPerso qui valorise les deux propri�t�s priv�es et qui g�re les labels du personnage et des infos du joueur (pseudo/hp).
	public void initPerso(String pseudo, int numPerso, Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		// On valorise.
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		
		// On cr�e la propri�t� label.
		label = new Label(Label.getNbLabel(), new JLabel());

		Label.setNbLabel(Label.getNbLabel()+1);
		
		// On centre horizontalement et verticalement le jLabel de label.
		label.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		label.getjLabel().setVerticalAlignment(SwingConstants.CENTER);
		
		// On ajoute le label � l'ar�ne.
		this.jeuServeur.nouveauLabelJeu(label);
		
		// On fait de m�me avec la propri�t� message.
		message = new Label(Label.getNbLabel(), new JLabel());
		Label.setNbLabel(Label.getNbLabel()+1);
		message.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		// On modifie la police et taille d'�criture du message.
		message.getjLabel().setFont(new Font("Dialog", Font.PLAIN, 8));
		
		this.jeuServeur.nouveauLabelJeu(message);
		
		// On appelle la m�thode premierePosition pour placer le joueur.
		premierePosition(lesJoueurs, lesMurs, lesPieges);
		
		affiche(MARCHE, etape);
		
		boule = new Boule(jeuServeur);
		jeuServeur.envoi(boule.getLabel());
	}

	
	// On cr�e la m�thode toucheJoueur qui v�rifie que l'objet ne rentre pas en collision avec un joueur.	
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
	
	// On cr�e la m�thode toucheJoueur qui v�rifie que l'objet ne rentre pas en collision avec un mur.
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
		// On cr�e une �l�ment de type Countdown.
		Countdown countdown = new Countdown(lesPieges);
		int i = 0;
		for (Piege piege : lesPieges) {
			// On v�rifie si le pi�ge est activ� ou non.
			if (!piege.isActivated())
			{
				// S'il n'est pas activ�, on v�rifie si le joueur touche le pi�ge.
				if (toucheObjet(piege))
				{
					// Si c'est le cas, le pi�ge devient activ� et on lance le timer.
					piege.setActivated(true);
					
					// On lance le timer sur le pi�ge qui vient d'�tre activ�.
					countdown.Timer_Activation(i);
					return true;
				}				
			}
			i++;
		}
		return false;
	}
	
	
	// On cr�e la m�thode premierePosition pour cr�er la position du personnage.
	private void premierePosition(Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		// R�cup�rer le JLabel du Personnage.
		label.getjLabel().setBounds(0, 0, L_PERSO, H_PERSO);
		
		do {
			posX = (int) Math.round(Math.random() * (L_ARENE - L_PERSO - 64)) + 32;
			posY = (int) Math.round(Math.random() * (H_ARENE - H_PERSO - H_MESSAGE - 64)) + 32;	
			
		} while (toucheJoueur(lesJoueurs) || toucheMur(lesMurs) || touchePiege(lesPieges));
	}
	
	public void affiche(String etat, int etape) {
		// On place le personnage.
		label.getjLabel().setBounds(posX, posY, L_PERSO, H_PERSO);
		// On va chercher l'image correspondant � l'�tat et l'�tape dans lesquels se trouvent le personnage.
		label.getjLabel().setIcon(new ImageIcon(PERSO + numPerso + etat + etape + "d" + orientation + EXTIMAGE));
		
		// On place le personnage.
		message.getjLabel().setBounds(posX - 10, posY + H_PERSO, L_PERSO + 10, H_MESSAGE);
		// On rajoute le texte li� au message.
		message.getjLabel().setText(pseudo + " : " + vie);
		
		// On envoie les labels du joueur � tout le monde.
		this.jeuServeur.envoi(label);
		this.jeuServeur.envoi(message);
	}
	
	
	// On cr�e la m�thode deplace 
	private int deplace(int action, int position, int orientation, int lepas, int max, Hashtable<Connection, Joueur> lesJoueurs, ArrayList<Mur> lesMurs, ArrayList<Piege> lesPieges) {
		this.orientation = orientation;
		int ancpos = position;
		
		position += lepas;
		// On v�rifie si on ne sort pas de l'ar�ne.
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
		
		// On v�rifie qu'il n'y a pas de collisions avec un joueur ou un mur.
		if (toucheMur(lesMurs) || toucheJoueur(lesJoueurs)) {
			position = ancpos;
		}

		
		// On met � jour les images en fonction du d�placement du personnage.
		etape = etape + 1 > NBETATSMARCHE ? 1 : etape + 1;
		
		
		// On g�re la collision d'un joueur avec un pi�ge.
		if (touchePiege(lesPieges)) {
			// Si le joueur se prend un pi�ge, il perd 5HP. 
			// Le joueur ne peut pas mourir d'un pi�ge afin de laisser les autres joueurs le tuer pour le mode pr�dator.
			jeuServeur.envoi(HURT);
			this.vie -= PERTEPIEGE;
			this.vie = Math.max(1, vie);
			// On affiche l'image du joueur bless�.
				for (int i = 1; i <= NBETATSBLESSE; i++) {
					this.affiche(BLESSE, i);
					pause(150,0);
				}
		}
		
		return position;
	}
	
	// On cr�e la m�thode action qui g�re toutes les actions du joueur (d�placements/tirs).
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
	
	// Cr�ation de la m�thode gainVie.
	public void gainVie() {
		vie += GAIN;
	}
	
	// Cr�ation de la m�thode perteVie.
	public void perteVie() {
		vie -= PERTE;
		vie = Math.max(vie, 0);
	}
	
	public void perteViePiege() {
		vie -= PERTEPIEGE;
		vie = Math.max(vie, 0);
	}
	
	// Cr�ation de la m�thode estMort.
	public boolean estMort() {
		if (vie == 0) {
			return true;
		}
		return false;
	}
	
	// Cr�ation de la m�thode departJoueur qui g�re la d�connexion d'un joueur.
	public void departJoueur() {
		// On enl�ve tout ce qui concerne le joueur.
		if (label != null) {
			label.getjLabel().setVisible(false);
			message.getjLabel().setVisible(false);
			boule.getLabel().getjLabel().setVisible(false);
			jeuServeur.envoi(label);
			jeuServeur.envoi(message);
			jeuServeur.envoi(boule.getLabel());
		}
	}
	
	
	// M�me m�thode pause que la classe Attaque pour les animations.
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
	
	// Le joueur en mode predator fait 2x plus de d�g�ts.
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
