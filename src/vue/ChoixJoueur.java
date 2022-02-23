package vue;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controleur.Controle;
import controleur.Global;
import outils.son.Son;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ChoixJoueur extends JFrame implements Global {

	
	// D�claration des propri�t�s.
	private JPanel contentPane;
	private JTextField txtPseudo;
	private int numPerso;
	private JLabel lblPersonnage;
	private Controle controle;
	
	// Propri�t� li�es au son.
	private Son precedent;
	private Son suivant;
	private Son go;
	private Son welcome;

	/**
	 * Create the frame.
	 */
	public ChoixJoueur(Controle controle) {
		setTitle("Choice");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 416, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// --------- LABEL PR�C�DENT ---------
		
		JLabel lblPrecedent = new JLabel("");
		lblPrecedent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblPrecedent_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				souris_doigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				souris_normale();
			}
		});
		lblPrecedent.setBounds(96, 159, 35, 39);
		contentPane.add(lblPrecedent);
		
		// --------- LABEL SUIVANT ---------
		
		JLabel lblSuivant = new JLabel("");
		lblSuivant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblSuivant_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				souris_doigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				souris_normale();
			}
		});
		lblSuivant.setBounds(272, 159, 35, 39);
		contentPane.add(lblSuivant);
		
		// --------- LABEL GO ---------
		
		JLabel lblGo = new JLabel("");
		lblGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblGo_clic();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				souris_doigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				souris_normale();
			}
		});
		lblGo.setBounds(303, 195, 76, 71);
		contentPane.add(lblGo);
		
		// --------- txtPseudo ---------
		
		txtPseudo = new JTextField();
		txtPseudo.setBounds(144, 247, 119, 19);
		contentPane.add(txtPseudo);
		txtPseudo.setColumns(10);
		
		// --------- LABEL PERSONNAGE ---------
		
		lblPersonnage = new JLabel("");
		lblPersonnage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonnage.setBounds(141, 115, 122, 122);
		contentPane.add(lblPersonnage);
		
		// --------- LABEL FOND ---------
		
		JLabel lblFond = new JLabel("New label");
		lblFond.setBounds(0, 0, 400, 275);
		
		lblFond.setIcon(new ImageIcon(FONDCHOIX));
		contentPane.add(lblFond);
		
		txtPseudo.requestFocus();
		
		// Gestion de l'affichage du personnage.
		numPerso = 1;
		affichePerso();
		
		// On valorise controle.
		this.controle = controle;
		
		// Valorisation des propri�t�s li�es au son.
		precedent = new Son(SONPRECEDENT);
		suivant = new Son(SONSUIVANT);
		go = new Son(SONGO);
		welcome = new Son(SONWELCOME);
		welcome.play();
		
	}
	
	// Cr�ation des m�thodes en fonction du clic.
	private void lblPrecedent_clic() {
		precedent.play();
		numPerso = ((numPerso+2)%NBPERSOS) + 1;
		affichePerso();
	}
	
	private void lblSuivant_clic() {
		suivant.play();
		numPerso = (numPerso % NBPERSOS) + 1;
		affichePerso();
	}
	
	private void lblGo_clic() {
		if((txtPseudo.getText()).equals(""))
		{
			JOptionPane.showMessageDialog(null, "Votre pseudo est invalide.");
			txtPseudo.requestFocus();
		}
		else
		{
			go.play();
			controle.evenementVue(this, PSEUDO + SEPARE + txtPseudo.getText() + SEPARE + numPerso);
		}
	}
	
	// On cr�e la m�thode souris_normale.
	private void souris_normale() {
		contentPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	// On cr�e la m�thode souris_doigt.
	private void souris_doigt() {
		contentPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	// On cr�e la m�thode affichePerso qui permet d'affecter au JLabel, la bonne image en fonction du numPerso.
	private void affichePerso() {
		// Chemin : media/personnages/perso1marche1d1.
		lblPersonnage.setIcon(new ImageIcon(PERSO + numPerso + MARCHE + "1d" + DROITE + EXTIMAGE));
	}
	
}
