package modele;

import javax.swing.*;

import controleur.Global;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class Countdown implements Global {
	
	private ArrayList<Piege> lesPieges;
	
	public Countdown(ArrayList<Piege> lesPieges) {
		this.lesPieges = lesPieges;
	}

	public void Timer_Activation(int num) {
		// On cr�e le Timer.
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			// On fixe le timer � 20s.
            int i = 5;

            public void run() {
            	// Chaque seconde, on r�duit le temps restant.
                i--;

                // Si le temps est �coul�, on r�active le pi�ge qui a �t� activ�.
                if (i < 0) {
                	lesPieges.get(num).setActivated(false);
                    timer.cancel();
                }
            }
        // Le 1000, correspond � 1s.
        }, 0, 1000);
	}
    
}
