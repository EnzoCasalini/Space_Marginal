package outils.connexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controleur.Controle;

// On veut que la classe ServeurSocket soit un thread.
public class ServeurSocket extends Thread {

	// On d�clare la propri�t� leRecepteur.
	private Object leRecepteur;
	
	// On d�clare la propri�t� serverSocket.
	private ServerSocket serverSocket;
	
	
	// Cr�ation du constructeur ServeurSocket.
	public ServeurSocket(Object leRecepteur, int port) 
	{
		// On valorise la propri�t� leRecepteur.
		this.leRecepteur = leRecepteur;	
		
		// Cr�ation de socket : �coute client.
		try {
			// On cr�e une instance de la classe ServerSocket (celle de la biblio java) avec port en param�tre.
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// On affiche l'erreur et on quitte le programme.
			System.out.println("erreur grave cr�ation du socket serveur :" + e);
			System.exit(0);
		}
		
		// On appelle la m�thode start.
		this.start();
	}
	
	
	// On red�finie la m�thode run().
	public void run() {
		
		Socket socket;
		
		// On boucle � l'infini parce qu'il n'y aura pas forc�ment qu'un seul client.
		while(true)
		{
			try {
				System.out.println("Le serveur attend.");
				// socket va stocker le socket du client qui se connecte (accept� � l'aide de serverSocket).
				socket = serverSocket.accept();
				System.out.println("Un client s'est connect�.");
				new Connection (socket, leRecepteur);
			} catch (IOException e) {
				System.out.println("Erreur sur l'objet serverSocket :" + e);
				System.exit(0);
			}
		}
	}
}