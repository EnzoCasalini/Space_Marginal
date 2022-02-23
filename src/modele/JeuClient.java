package modele;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controleur.Controle;
import outils.connexion.Connection;

public class JeuClient extends Jeu {

	// D�claration des propri�t�s 
	private Connection connection;
	
	public JeuClient(Controle controle)
	{
		// On valorise controle, on utilise super pour r�cup�rer la propri�t� de la m�re.
		super.controle = controle;
	}
	
	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void reception(Connection connection, Object info) {
		if (info instanceof JPanel) {
			// On met en premier param�tre le JeuClient pour savoir si c'est le client ou le serveur, 
			// Le second param�tre est un ordre pour rentrer dans le if de la m�thode evenementJeuClient (Controle),
			// Le troisi�me param�tre est l'info � envoyer, ici, info qui est le JPanel des murs.
			controle.evenementModele(this, "ajout panel murs", info);
			controle.evenementModele(this, "ajout panel pieges", info);
		}
		else if (info instanceof Label) {
			controle.evenementModele(this, "ajout joueur", info);
		}
		else if (info instanceof String) {
			controle.evenementModele(this,  "remplace chat", info);
		}
		else if (info instanceof Integer) {
			controle.evenementModele(this, "son", info);
		}
	}

	@Override
	// D�connexion du serveur.
	public void deconnection(Connection connection) {
		System.exit(0);
	}
	
	// On surcharge la m�thode envoi.
	public void envoi(Object info) {
		// Envoi de l'info vers l'ordinateur distant.
		super.envoi(connection, info);
	}

}
