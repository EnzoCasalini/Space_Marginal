package outils.connexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import controleur.Controle;

public class Connection extends Thread {
	
	// D�claration des propri�t�s. //
	
	private Object leRecepteur;
	
	// Canal d'entr�e qui recevra les messages du client.
	private ObjectInputStream in;
	
	// Canal de sortie qui enverra les messages vers le client.
	private ObjectOutputStream out;
	
	
	// Cr�ation du constructeur : il re�oit le socket du client et leRecepteur qui re�oit l'objet leRecepteur de la classe ServeurSocket ou ClientSocket.
	public Connection(Socket socket, Object leRecepteur) {
		
		this.leRecepteur = leRecepteur;
		
		// On cr�e le canal de sortie.
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Erreur de cr�ation du canal de sortie.");
			System.exit(0);
		}
		
		// On cr�e le canal d'entr�e.
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Erreur de cr�ation du canal d'entr�e.");
			System.exit(0);
		}
		
		// On appelle la m�thode start.
		this.start();
		
		// Ici on transtype (cast) directement avec controleur.Controle pour �viter de faire un import.
		((controleur.Controle)this.leRecepteur).setConnection(this);
	}
	
	public void run() {
		
		// NB : un type est �crit tout en minuscule alors qu'une classe commence par une Majuscule
		
		// inOk m�morise la d�connexion du client.
		boolean inOk = true;
		
		// R�ception du message client.
		Object reception;
		
		while (inOk) 
		{
			try {
				// On attend la r�ception d'un message de la part du client, il est ensuite stock� dans la propri�t� reception.
				reception = in.readObject();
				((controleur.Controle)this.leRecepteur).receptionInfo(this, reception);
			} catch (ClassNotFoundException e) {
				System.out.println("Erreur sur la r�ception.");
				System.exit(0);
			} catch (IOException e) {
				// Si le client se d�connecte, on ne doit plus attendre de messages de sa part, donc on met inOk � false.
				JOptionPane.showMessageDialog(null, "L'ordinateur distant s'est d�connect�");
				inOk = false;
				((Controle)leRecepteur).deconnection(this);
				try {
					// On ferme le canal d'entr�e.
					in.close();
				} catch (IOException e1) {
					System.out.println("Le canal ne s'est pas ferm� correctement.");
				}
			}
		}
	}

	// On cr�e la m�thode envoi qui permet aux filles d'envoyer des informations � l'ordinateur distant.
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
