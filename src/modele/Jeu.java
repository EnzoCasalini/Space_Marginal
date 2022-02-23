package modele;

import controleur.Controle;
import outils.connexion.Connection;

public abstract class Jeu {

	// On déclare controle en protected pour que les deux filles puissent y accéder.
	protected Controle controle;
	
	// On crée la méthode setConnection en abstraite pour forcer les filles à avoir une méthode avec la même signature.
	public abstract void setConnection(Connection connection);
	
	// On crée la méthode reception qui traite la réception des messages des ordinateurs distants.
	// connection permet de savoir qui a envoyé le message.
	// info contient le message envoyé par l'ordinateur distant.
	public abstract void reception(Connection connection, Object info);
	
	// On crée la méthode envoi.
	public void envoi(Connection connection, Object info) {
		// L'objet info sera envoyé à l'ordinateur distant.
		connection.envoi(info);
	}
	
	// On crée la méthode déconnection.
	public abstract void deconnection(Connection connection);
	
}
