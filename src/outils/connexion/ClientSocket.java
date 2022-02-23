package outils.connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class ClientSocket {

	// D�claration des propri�t�s.
	
	private boolean connexionOk;
	
	// On cr�e le constructeur. 
	public ClientSocket(String ip, int port, Object leRecepteur) {
		connexionOk = false;
		try {
			// socket recevra le socket client cr�� lors de la tentative de connexion au serveur.
			Socket socket = new Socket (ip, port);
			System.out.println("La connexion au serveur a r�ussi.");
			connexionOk = true;
			new Connection(socket, leRecepteur);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Le serveur n'est pas disponible.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Probl�me lors de la connexion : V�rifiez l'IP entr�e.");
		}
	}

	/**
	 * @return the connexionOk
	 */
	
	// Getter sur la propri�t� connexionOk. 
	public boolean isConnexionOk() {
		return connexionOk;
	}
	
	
	
	
}
