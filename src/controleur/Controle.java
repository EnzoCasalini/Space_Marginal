package controleur;

import vue.Arene;
import vue.ChoixJoueur;
import vue.EntreeJeu;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.Jeu;
import modele.JeuClient;
import modele.JeuServeur;
import modele.Label;
import outils.connexion.ClientSocket;
import outils.connexion.Connection;
import outils.connexion.ServeurSocket;

// 'implements Global' permet d'implémenter l'interface Global pour avoir accès à ses membres.
public class Controle implements Global {
	
	private EntreeJeu frmEntreeJeu;
	private Jeu leJeu;
	private Arene frmArene;
	private ChoixJoueur frmChoixJoueur;
	private Connection connection;

	public static void main(String[] args) {
		// On crée l'objet Controle.
		new Controle();
	}
	
	// On détaille les instructions qu'exécutera le constructeur.
	public Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}
	
	
	// Création de la méthode evenementVue qui permettra de démarrer le jeu à l'aide de la méthode evenementEntreeJeu.
	public void evenementVue (JFrame uneFrame, Object info) {
		if (uneFrame instanceof EntreeJeu) 
		{
			evenementEntreeJeu(info);
		}
		else if (uneFrame instanceof ChoixJoueur) 
		{
			evenementChoixJoueur(info);
		}
		else if (uneFrame instanceof Arene) {
			evenementArene(info);
		}
	}

	private void evenementEntreeJeu(Object info) {
		// Méthode qui permettra de démarrer un jeu en mode serveur ou client en fonction du bouton cliqué.
		/* On caste (transtype) le paramètre de type Objet en String.
		System.out.println((String)info);*/
		if ((String)info == "serveur")
		{
			// On crée un socket serveur.
			new ServeurSocket(this, PORT);
			
			// On crée l'objet JeuServeur qui nous permet d'accéder le jeu, soit en mode serveur, soit en mode client (ici en mode serveur).
			leJeu = new JeuServeur(this);
			
			// On enlève la fenêtre de choix de serveur.
			frmEntreeJeu.dispose();
			
			// On crée et affiche la fenêtre Arena en mdoe serveur.
			frmArene = new Arene("serveur", this);
			((JeuServeur)leJeu).constructionMurs();
			((JeuServeur)leJeu).constructionPieges();
			frmArene.setVisible(true);
		}
		else 
		{
			// On crée un socket client.
			(new ClientSocket((String)info, PORT, this)).isConnexionOk();
			
			// On crée l'objet JeuServeur qui nous permet d'accéder le jeu, soit en mode serveur, soit en mode client (ici en mode client).
			leJeu = new JeuClient(this);
			
			// Le controleur informe le JeuClient.
			leJeu.setConnection(connection);
			
			// On enlève la fenêtre de choix de serveur.
			frmEntreeJeu.dispose();
			
			// On crée la fenêtre Arena.
			frmArene = new Arene("client", this);
			
			// On crée et affiche la fenêtre ChoixJoueur.
			frmChoixJoueur = new ChoixJoueur(this);
			frmChoixJoueur.setVisible(true);
		}
	}
	
	
	// Méthode qui fonctionne comme la méthode evenementEntreeJeu.
	private void evenementChoixJoueur (Object info) {
		// On transtype le jeu pour pouvoir utiliser sa méthode envoi et pas celle de la classe Jeu.
		((JeuClient)leJeu).envoi(info); // Envoi de l'info vers le serveur.
		frmChoixJoueur.dispose();
		frmArene.setVisible(true);
	}
	
	// Même fonctionnement que les deux précédentes.
	private void evenementArene (Object info) {
		((JeuClient)leJeu).envoi(info); 
	}
	
	// Création de la méthode setConnection.
	public void setConnection(Connection connection) {
		// On valorise connection.
		this.connection = connection;
		
		// On vérifie que leJeu est une instance de type JeuServeur pour pouvoir appeler la méthode dès qu'un client se connecte.
		if (leJeu instanceof JeuServeur) {
			leJeu.setConnection(connection);
		}
	}
	
	// Création de la méthode receptionInfo.
	public void receptionInfo (Connection connection, Object info) {
		leJeu.reception(connection, info);
	}
	
	// Création de la méthode evenementModele.
	public void evenementModele (Object unJeu, String ordre, Object info) {
		if(unJeu instanceof JeuServeur)
		{
			evenementJeuServeur(ordre, info);
		}
		else if (unJeu instanceof JeuClient) {
			evenementJeuClient(ordre, info);
		}
	}

	
	// Création de la méthode evenementJeuServeur.
	public void evenementJeuServeur(String ordre, Object info) {
		// On utilise la méthode equals pour comparer des chaînes de caractères.
		if (ordre.equals("ajout mur"))
		{
			frmArene.ajoutMur((JLabel)info);
		}
		else if (ordre.equals("envoi panel murs")) {
			// On envoie l'objet info (qui contient l'objet connection pour contacter le client) et le JPanel des murs de l'arène.
			((JeuServeur)leJeu).envoi((Connection)info, frmArene.getJpnMurs());
		}
		else if (ordre.equals("ajout joueur")) {
			frmArene.ajoutJoueur((JLabel)info);
		}
		else if (ordre.equals("ajout phrase")) {
			frmArene.ajoutChat((String)info);
			((JeuServeur)leJeu).envoi(frmArene.getTxtChat().getText());
		}
		else if (ordre.equals("ajout piege")) {
			frmArene.ajoutPiege((JLabel)info);
		}
		else if (ordre.equals("envoi panel pieges")) {
			((JeuServeur)leJeu).envoi((Connection)info, frmArene.getJpnPieges());
		}
	}
	
	
	// Création de la méthode evenementJeuClient.
	public void evenementJeuClient(String ordre, Object info) {
		// Même fonctionnement que evenementJeuServeur.
		if (ordre.equals("ajout panel murs")) {
			frmArene.ajoutPanelMurs((JPanel)info);
		}
		else if (ordre.equals("ajout joueur")) {
			frmArene.ajoutModifJoueur(((Label)info).getNumLabel(), ((Label)info).getjLabel());
		}
		else if (ordre.equals("remplace chat")) {
			frmArene.remplaceChat((String)info);
		}
		else if (ordre.equals("son")) {
			frmArene.joueSon((Integer)info);
		}
		else if (ordre.equals("ajout panel pieges")) {
			frmArene.ajoutPanelPieges((JPanel)info);
		}
	}
	
	public void deconnection(Connection connection) {
		leJeu.deconnection(connection);
	}
	
}
