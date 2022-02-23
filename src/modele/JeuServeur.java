package modele;

import java.util.ArrayList;
import java.util.Hashtable;

import controleur.Controle;
import controleur.Global;
import outils.connexion.Connection;

public class JeuServeur extends Jeu implements Global {

	// Déclaration des propriétés.
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>();
	private ArrayList<Piege> lesPieges = new ArrayList<Piege>();
	// Hashtable gère un dictionnaire avec une clé (ici de type Connection).
	private Hashtable<Connection, Joueur> lesJoueurs = new Hashtable<Connection, Joueur>();
	// Copie des joueurs pour les avoir dans leur ordre d'arrivée.
	private ArrayList<Joueur> lesJoueursDansLordre = new ArrayList<Joueur>();
	
	public JeuServeur(Controle controle)
	{
		// On valorise controle, on utilise super pour récupérer la propriété de la mère.
		super.controle = controle;
		// Initialisation du rang du dernier label mémorisé.
		Label.setNbLabel(0);
	}
	
	@Override
	public void setConnection(Connection connection) {
		// On ajoute un nouveau joueur dans le dictionnaire.
		lesJoueurs.put(connection, new Joueur(this));
		// On met en premier paramètre le JeuServeur pour savoir si c'est le client ou le serveur, 
		// Le second paramètre est un ordre pour pouvoir rentrer dans le else if de la méthode evenementJeuServeur (Controle),
		// Le troisième paramètre est l'info qu'on doit envoyer, ici, la connexion.
	}

	@Override
	public void reception(Connection connection, Object info) {
		// On décompose la chaîne de caractère en fonction du caractère de séparation (ici : "~").
		String[] infos = ((String)info).split(SEPARE);
		
		switch (Integer.parseInt(infos[0])) {
			case PSEUDO : 
				// On envoies les JLabels des autres joueurs au joueur qui vient de se connecter.
				for (Joueur joueur : lesJoueursDansLordre) {
					super.envoi(connection, joueur.getLabel());
					super.envoi(connection, joueur.getMessage());
					super.envoi(connection, joueur.getBoule().getLabel());
				}
				// On crée les JLabels nécessaires en fonction du joueur avec la méthode initPerso.
				lesJoueurs.get(connection).initPerso(infos[1], Integer.parseInt(infos[2]), lesJoueurs, lesMurs, lesPieges);
				// On stock le joueur qui vient d'arriver dans notre tableau copie.
				this.lesJoueursDansLordre.add(this.lesJoueurs.get(connection));
				controle.evenementModele(this, "envoi panel murs", connection);
				controle.evenementModele(this, "envoi panel pieges", connection);
				// Message d'arrivée du joueur.
				String laPhrase = "*** " + infos[1] + " vient de se connecter ! ***";
				controle.evenementModele(this, "ajout phrase", laPhrase);
				break;
			case CHAT : 
				// Concaténation qui récupère le pseudo et la phrase pour avoir un affichage tel que : Enzo > Bonjour !
				laPhrase = lesJoueurs.get(connection).getPseudo() + " > " + infos[1];
				controle.evenementModele(this, "ajout phrase", laPhrase);
				break;
			case ACTION : 
				// Appelle de la méthode action sur le joueur concerné afin de gérer ses actions (déplacements/tirs).
				if (!lesJoueurs.get(connection).estMort()) {
					lesJoueurs.get(connection).action(Integer.parseInt(infos[1]), lesJoueurs, lesMurs, lesPieges);
				}
		}
	}

	@Override
	public void deconnection(Connection connection) {
		// On déconnecte correctement le joueur qui ferme la fenêtre.
		lesJoueurs.get(connection).departJoueur();
		lesJoueurs.remove(connection);
		
	}
	
	// Création de la méthode constructionMurs.
	public void constructionMurs()
	{
		// On boucle pour ajouter le NBMURS qu'on veut (ici 20).
		for (int k = 0; k < NBMURS; k++)
		{
			lesMurs.add(new Mur());
			// On envoie JeuServeur pour que controle sâche si c'est le serveur ou le client.
			// On envoie également l'ordre "ajout mur" pour que la condition dans la classe Controle soit remplie (deuxième partie).
			// On envoie le jLabel du dernier mur (troisième paramètre). 
			controle.evenementModele(this, "ajout mur", lesMurs.get(lesMurs.size()-1).getLabel().getjLabel());
		}
	}
	
	// Création des pièges.
	public void constructionPieges()
	{
		// On fixe les positions des pièges.
		lesPieges.add(new Piege(100, 80, this));
		lesPieges.add(new Piege(180, H_ARENE - H_PIEGE - 32, this));
		lesPieges.add(new Piege(L_ARENE - L_PIEGE - 100, 120, this));
		lesPieges.add(new Piege(L_ARENE - L_PIEGE - 240, H_ARENE - H_PIEGE - 70, this));
		
		// On parcourt le tableau de pièges pour les ajouter au jeu.
		for (int k = 1; k <= 4; k++)
		{
			controle.evenementModele(this, "ajout piege", lesPieges.get(lesPieges.size() - k).getLabel().getjLabel());			
		}
	}
	
	public void nouveauLabelJeu(Label label) {
		controle.evenementModele(this, "ajout joueur", label.getjLabel());
	}

	// On récupère la clé de tous les clients pour leur envoyer, à tous, les informations.
	public void envoi(Object info) {
		for (Connection key : lesJoueurs.keySet()) {
			super.envoi(key, info);
		}
	}
	
	

}
