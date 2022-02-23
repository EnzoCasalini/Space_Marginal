package modele;

import java.util.ArrayList;
import java.util.Hashtable;

import controleur.Controle;
import controleur.Global;
import outils.connexion.Connection;

public class JeuServeur extends Jeu implements Global {

	// D�claration des propri�t�s.
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>();
	private ArrayList<Piege> lesPieges = new ArrayList<Piege>();
	// Hashtable g�re un dictionnaire avec une cl� (ici de type Connection).
	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>();
	// Copie des joueurs pour les avoir dans leur ordre d'arriv�e.
	private ArrayList<Joueur> lesJoueursDansLordre = new ArrayList<Joueur>();
	
	public JeuServeur(Controle controle)
	{
		// On valorise controle, on utilise super pour r�cup�rer la propri�t� de la m�re.
		super.controle = controle;
		// Initialisation du rang du dernier label m�moris�.
		Label.setNbLabel(0);
	}
	
	@Override
	public void setConnection(Connection connection) {
		// On ajoute un nouveau joueur dans le dictionnaire.
		lesJoueurs.put(connection, new Joueur(this));
		// On met en premier param�tre le JeuServeur pour savoir si c'est le client ou le serveur, 
		// Le second param�tre est un ordre pour pouvoir rentrer dans le else if de la m�thode evenementJeuServeur (Controle),
		// Le troisi�me param�tre est l'info qu'on doit envoyer, ici, la connexion.
	}

	@Override
	public void reception(Connection connection, Object info) {
		// On d�compose la cha�ne de caract�re en fonction du caract�re de s�paration (ici : "~").
		String[] infos = ((String)info).split(SEPARE);
		
		switch (Integer.parseInt(infos[0])) {
			case PSEUDO : 
				// On envoies les JLabels des autres joueurs au joueur qui vient de se connecter.
				for (Joueur joueur : lesJoueursDansLordre) {
					super.envoi(connection, joueur.getLabel());
					super.envoi(connection, joueur.getMessage());
					super.envoi(connection, joueur.getBoule().getLabel());
				}
				// On cr�e les JLabels n�cessaires en fonction du joueur avec la m�thode initPerso.
				lesJoueurs.get(connection).initPerso(infos[1], Integer.parseInt(infos[2]), lesJoueurs, lesMurs, lesPieges);
				// On stock le joueur qui vient d'arriver dans notre tableau copie.
				this.lesJoueursDansLordre.add(this.lesJoueurs.get(connection));
				controle.evenementModele(this, "envoi panel murs", connection);
				controle.evenementModele(this, "envoi panel pieges", connection);
				// Message d'arriv�e du joueur.
				String laPhrase = "*** " + infos[1] + " vient de se connecter ! ***";
				controle.evenementModele(this, "ajout phrase", laPhrase);
				break;
			case CHAT : 
				// Concat�nation qui r�cup�re le pseudo et la phrase pour avoir un affichage tel que : Enzo > Bonjour !
				laPhrase = lesJoueurs.get(connection).getPseudo() + " > " + infos[1];
				controle.evenementModele(this, "ajout phrase", laPhrase);
				break;
			case ACTION : 
				// Appelle de la m�thode action sur le joueur concern� afin de g�rer ses actions (d�placements/tirs).
				if (!lesJoueurs.get(connection).estMort()) {
					lesJoueurs.get(connection).action(Integer.parseInt(infos[1]), lesJoueurs, lesMurs, lesPieges);
				}
		}
	}

	@Override
	public void deconnection(Connection connection) {
		// On d�connecte correctement le joueur qui ferme la fen�tre.
		lesJoueurs.get(connection).departJoueur();
		lesJoueurs.remove(connection);
		
	}
	
	// Cr�ation de la m�thode constructionMurs.
	public void constructionMurs()
	{
		// On boucle pour ajouter le NBMURS qu'on veut (ici 20).
		for (int k = 0; k < NBMURS; k++)
		{
			lesMurs.add(new Mur());
			// On envoie JeuServeur pour que controle s�che si c'est le serveur ou le client.
			// On envoie �galement l'ordre "ajout mur" pour que la condition dans la classe Controle soit remplie (deuxi�me partie).
			// On envoie le jLabel du dernier mur (troisi�me param�tre). 
			controle.evenementModele(this, "ajout mur", lesMurs.get(lesMurs.size()-1).getLabel().getjLabel());
		}
	}
	
	// Cr�ation des pi�ges.
	public void constructionPieges()
	{
		// On fixe les positions des pi�ges.
		lesPieges.add(new Piege(100, 80, this));
		lesPieges.add(new Piege(180, H_ARENE - H_PIEGE - 32, this));
		lesPieges.add(new Piege(L_ARENE - L_PIEGE - 100, 120, this));
		lesPieges.add(new Piege(L_ARENE - L_PIEGE - 240, H_ARENE - H_PIEGE - 70, this));
		
		// On parcourt le tableau de pi�ges pour les ajouter au jeu.
		for (int k = 1; k <= 4; k++)
		{
			controle.evenementModele(this, "ajout piege", lesPieges.get(lesPieges.size() - k).getLabel().getjLabel());			
		}
	}
	
	public void nouveauLabelJeu(Label label) {
		controle.evenementModele(this, "ajout joueur", label.getjLabel());
	}

	// On r�cup�re la cl� de tous les clients pour leur envoyer, � tous, les informations.
	public void envoi(Object info) {
		for (Connection key : lesJoueurs.keySet()) {
			super.envoi(key, info);
		}
	}
	
	

}
