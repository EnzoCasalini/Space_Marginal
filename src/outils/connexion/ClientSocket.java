package outils.connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class ClientSocket {

	// Déclaration des propriétés.
	
	private boolean connexionOk;
	
	// On crée le constructeur. 
	public ClientSocket(String ip, int port, Object leRecepteur) {
		connexionOk = false;
		try {
			// socket recevra le socket client créé lors de la tentative de connexion au serveur.
			Socket socket = new Socket (ip, port);
			System.out.println("La connexion au serveur a réussi.");
			connexionOk = true;
			new Connection(socket, leRecepteur);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Le serveur n'est pas disponible.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problème lors de la connexion : Vérifiez l'IP entrée.");
		}
	}

	/**
	 * @return the connexionOk
	 */
	
	// Getter sur la propriété connexionOk. 
	public boolean isConnexionOk() {
		return connexionOk;
	}
	
	
	
	
}
