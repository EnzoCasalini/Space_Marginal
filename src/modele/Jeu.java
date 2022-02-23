package modele;

import controleur.Controle;
import outils.connexion.Connection;

public abstract class Jeu {

	// On d�clare controle en protected pour que les deux filles puissent y acc�der.
	protected Controle controle;
	
	// On cr�e la m�thode setConnection en abstraite pour forcer les filles � avoir une m�thode avec la m�me signature.
	public abstract void setConnection(Connection connection);
	
	// On cr�e la m�thode reception qui traite la r�ception des messages des ordinateurs distants.
	// connection permet de savoir qui a envoy� le message.
	// info contient le message envoy� par l'ordinateur distant.
	public abstract void reception(Connection connection, Object info);
	
	// On cr�e la m�thode envoi.
	public void envoi(Connection connection, Object info) {
		// L'objet info sera envoy� � l'ordinateur distant.
		connection.envoi(info);
	}
	
	// On cr�e la m�thode d�connection.
	public abstract void deconnection(Connection connection);
	
}
