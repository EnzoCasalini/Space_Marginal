package outils.connexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import controleur.Controle;

public class Connection extends Thread {
	
	// Déclaration des propriétés. //
	
	private Object leRecepteur;
	
	// Canal d'entrée qui recevra les messages du client.
	private ObjectInputStream in;
	
	// Canal de sortie qui enverra les messages vers le client.
	private ObjectOutputStream out;
	
	
	// Création du constructeur : il reçoit le socket du client et leRecepteur qui reçoit l'objet leRecepteur de la classe ServeurSocket ou ClientSocket.
	public Connection(Socket socket, Object leRecepteur) {
		
		this.leRecepteur = leRecepteur;
		
		// On crée le canal de sortie.
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Erreur de création du canal de sortie.");
			System.exit(0);
		}
		
		// On crée le canal d'entrée.
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Erreur de création du canal d'entrée.");
			System.exit(0);
		}
		
		// On appelle la méthode start.
		this.start();
		
		// Ici on transtype (cast) directement avec controleur.Controle pour éviter de faire un import.
		((controleur.Controle)this.leRecepteur).setConnection(this);
	}
	
	public void run() {
		
		// NB : un type est écrit tout en minuscule alors qu'une classe commence par une Majuscule
		
		// inOk mémorise la déconnexion du client.
		boolean inOk = true;
		
		// Réception du message client.
		Object reception;
		
		while (inOk) 
		{
			try {
				// On attend la réception d'un message de la part du client, il est ensuite stocké dans la propriété reception.
				reception = in.readObject();
				((controleur.Controle)this.leRecepteur).receptionInfo(this, reception);
			} catch (ClassNotFoundException e) {
				System.out.println("Erreur sur la réception.");
				System.exit(0);
			} catch (IOException e) {
				// Si le client se déconnecte, on ne doit plus attendre de messages de sa part, donc on met inOk à false.
				JOptionPane.showMessageDialog(null, "L'ordinateur distant s'est déconnecté");
				inOk = false;
				((Controle)leRecepteur).deconnection(this);
				try {
					// On ferme le canal d'entrée.
					in.close();
				} catch (IOException e1) {
					System.out.println("Le canal ne s'est pas fermé correctement.");
				}
			}
		}
	}

	// On crée la méthode envoi qui permet aux filles d'envoyer des informations à l'ordinateur distant.
	public synchronized void envoi(Object unObjet) {
		try {
			this.out.reset();
			out.writeObject(unObjet);
			out.flush();
		} catch (IOException e) {
			System.out.println("Erreur sur l'objet out !");
		}
	}
	
	
}
