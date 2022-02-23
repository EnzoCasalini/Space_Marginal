package modele;

import java.io.Serializable;

import javax.swing.JLabel;

public class Label implements Serializable {

	// Déclaration des propriétés :
	private static int nbLabel; // Mémorise le numéro du dernier label ajouté.
	private int numLabel; // Numéro du JLabel.
	private JLabel jLabel; 
	
	
	// Création du constructeur.
	public Label(int numLabel, JLabel jLabel) {
		// On valorise les deux propriétés privées.
		this.numLabel = numLabel;
		this.jLabel = jLabel;
	}


	// Création des getters et des setters.
	public static int getNbLabel() {
		return nbLabel;
	}

	
	public static void setNbLabel(int nbLabel) {
		Label.nbLabel = nbLabel;
	}


	public int getNumLabel() {
		return numLabel;
	}


	public JLabel getjLabel() {
		return jLabel;
	}
	
}
