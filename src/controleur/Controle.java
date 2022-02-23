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

// 'implements Global' permet d'impl�menter l'interface Global pour avoir acc�s � ses membres.
public class Controle implements Global {
	
	private EntreeJeu frmEntreeJeu;
	private Jeu leJeu;
	private Arene frmArene;
	private ChoixJoueur frmChoixJoueur;
	private Connection connection;

	public static void main(String[] args) {
		// On cr�e l'objet Controle.
		new Controle();
	}
	
	// On d�taille les instructions qu'ex�cutera le constructeur.
	public Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}
	
	
	// Cr�ation de la m�thode evenementVue qui permettra de d�marrer le jeu � l'aide de la m�thode evenementEntreeJeu.
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
		// M�thode qui permettra de d�marrer un jeu en mode serveur ou client en fonction du bouton cliqu�.
		/* On caste (transtype) le param�tre de type Objet en String.
		System.out.println((String)info);*/
		if ((String)info == "serveur")
		{
			// On cr�e un socket serveur.
			new ServeurSocket(this, PORT);
			
			// On cr�e l'objet JeuServeur qui nous permet d'acc�der le jeu, soit en mode serveur, soit en mode client (ici en mode serveur).
			leJeu = new JeuServeur(this);
			
			// On enl�ve la fen�tre de choix de serveur.
			frmEntreeJeu.dispose();
			
			// On cr�e et affiche la fen�tre Arena en mdoe serveur.
			frmArene = new Arene("serveur", this);
			((JeuServeur)leJeu).constructionMurs();
			((JeuServeur)leJeu).constructionPieges();
			frmArene.setVisible(true);
		}
		else 
		{
			// On cr�e un socket client.
			(new ClientSocket((String)info, PORT, this)).isConnexionOk();
			
			// On cr�e l'objet JeuServeur qui nous permet d'acc�der le jeu, soit en mode serveur, soit en mode client (ici en mode client).
			leJeu = new JeuClient(this);
			
			// Le controleur informe le JeuClient.
			leJeu.setConnection(connection);
			
			// On enl�ve la fen�tre de choix de serveur.
			frmEntreeJeu.dispose();
			
			// On cr�e la fen�tre Arena.
			frmArene = new Arene("client", this);
			
			// On cr�e et affiche la fen�tre ChoixJoueur.
			frmChoixJoueur = new ChoixJoueur(this);
			frmChoixJoueur.setVisible(true);
		}
	}
	
	
	// M�thode qui fonctionne comme la m�thode evenementEntreeJeu.
	private void evenementChoixJoueur (Object info) {
		// On transtype le jeu pour pouvoir utiliser sa m�thode envoi et pas celle de la classe Jeu.
		((JeuClient)leJeu).envoi(info); // Envoi de l'info vers le serveur.
		frmChoixJoueur.dispose();
		frmArene.setVisible(true);
	}
	
	// M�me fonctionnement que les deux pr�c�dentes.
	private void evenementArene (Object info) {
		((JeuClient)leJeu).envoi(info); 
	}
	
	// Cr�ation de la m�thode setConnection.
	public void setConnection(Connection connection) {
		// On valorise connection.
		this.connection = connection;
		
		// On v�rifie que leJeu est une instance de type JeuServeur pour pouvoir appeler la m�thode d�s qu'un client se connecte.
		if (leJeu instanceof JeuServeur) {
			leJeu.setConnection(connection);
		}
	}
	
	// Cr�ation de la m�thode receptionInfo.
	public void receptionInfo (Connection connection, Object info) {
		leJeu.reception(connection, info);
	}
	
	// Cr�ation de la m�thode evenementModele.
	public void evenementModele (Object unJeu, String ordre, Object info) {
		if(unJeu instanceof JeuServeur)
		{
			evenementJeuServeur(ordre, info);
		}
		else if (unJeu instanceof JeuClient) {
			evenementJeuClient(ordre, info);
		}
	}

	
	// Cr�ation de la m�thode evenementJeuServeur.
	public void evenementJeuServeur(String ordre, Object info) {
		// On utilise la m�thode equals pour comparer des cha�nes de caract�res.
		if (ordre.equals("ajout mur"))
		{
			frmArene.ajoutMur((JLabel)info);
		}
		else if (ordre.equals("envoi panel murs")) {
			// On envoie l'objet info (qui contient l'objet connection pour contacter le client) et le JPanel des murs de l'ar�ne.
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
	
	
	// Cr�ation de la m�thode evenementJeuClient.
	public void evenementJeuClient(String ordre, Object info) {
		// M�me fonctionnement que evenementJeuServeur.
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
