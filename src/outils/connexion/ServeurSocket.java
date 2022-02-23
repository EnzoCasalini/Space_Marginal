package outils.connexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controleur.Controle;

// On veut que la classe ServeurSocket soit un thread.
public class ServeurSocket extends Thread {

	// On déclare la propriété leRecepteur.
	private Object leRecepteur;
	
	// On déclare la propriété serverSocket.
	private ServerSocket serverSocket;
	
	
	// Création du constructeur ServeurSocket.
	public ServeurSocket(Object leRecepteur, int port) 
	{
		// On valorise la propriété leRecepteur.
		this.leRecepteur = leRecepteur;	
		
		// Création de socket : écoute client.
		try {
			// On crée une instance de la classe ServerSocket (celle de la biblio java) avec port en paramètre.
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// On affiche l'erreur et on quitte le programme.
			System.out.println("erreur grave création du socket serveur :" + e);
			System.exit(0);
		}
		
		// On appelle la méthode start.
		this.start();
	}
	
	
	// On redéfinie la méthode run().
	public void run() {
		
		Socket socket;
		
		// On boucle à l'infini parce qu'il n'y aura pas forcément qu'un seul client.
		while(true)
		{
			try {
				System.out.println("Le serveur attend.");
				// socket va stocker le socket du client qui se connecte (accepté à l'aide de serverSocket).
				socket = serverSocket.accept();
				System.out.println("Un client s'est connecté.");
				new Connection (socket, leRecepteur);
			} catch (IOException e) {
				System.out.println("Erreur sur l'objet serverSocket :" + e);
				System.exit(0);
			}
		}
	}
}