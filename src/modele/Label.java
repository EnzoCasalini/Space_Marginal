package modele;

import java.io.Serializable;

import javax.swing.JLabel;

public class Label implements Serializable {

	// D�claration des propri�t�s :
	private static int nbLabel; // M�morise le num�ro du dernier label ajout�.
	private int numLabel; // Num�ro du JLabel.
	private JLabel jLabel; 
	
	
	// Cr�ation du constructeur.
	public Label(int numLabel, JLabel jLabel) {
		// On valorise les deux propri�t�s priv�es.
		this.numLabel = numLabel;
		this.jLabel = jLabel;
	}


	// Cr�ation des getters et des setters.
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
