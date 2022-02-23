package modele;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controleur.Global;

public class Piege extends Objet implements Global {
	
	private JeuServeur jeuServeur;
	private boolean activated;
	
	// Création du constructeur.
	public Piege(int num1, int num2, JeuServeur jeuServeur) {

		this.jeuServeur = jeuServeur;
		
		// Cette fois-ci, je mets les pièges en position fixe.
		posX = (int) num1;
		posY = (int) num2;
		
		label = new Label(-1, new JLabel());
		
		// Caractéristiques : Centrage, position et image.
		label.getjLabel().setHorizontalAlignment(SwingConstants.CENTER);
		label.getjLabel().setVerticalAlignment(SwingConstants.CENTER);
		label.getjLabel().setBounds(posX, posY, L_PIEGE, H_PIEGE);
		label.getjLabel().setIcon(new ImageIcon(PIEGE));
	}
	
	
	/**
	 * @return the state
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param state the state to set
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
