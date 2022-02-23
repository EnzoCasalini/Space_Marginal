package vue;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controleur.Controle;
import controleur.Global;
import outils.son.Son;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Arene extends JFrame implements Global {

	private JPanel contentPane;
	private JTextField txtSaisie;
	private JPanel jpnMurs;
	private JPanel jpnPieges;
	private JPanel jpnJeu;
	private JTextArea txtChat;
	private JLabel lblFond;
	private JScrollPane jspChat;
	private boolean client;
	private Controle controle;
	private Son[] lessons = new Son[SON.length];

	/**
	 * Create the frame.
	 */
	public Arene(String typeJeu, Controle controle) {
		
		this.controle = controle;
		// On valorise client en fonction de ce que contient typeJeu.
		//client = typeJeu.equals("client") ? true : false;
		// La méthode equals return soit true soit false du coup on peut directement mettre son résultat dans la propriété client.
		client = (typeJeu.equals("client"));
		
		
		setTitle("Arena");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, L_ARENE+3*MARGE, H_ARENE+H_CHAT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		if (client) {
			contentPane.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent arg0) {
					contentPane_keyPressed(arg0);
				}
			});			
		}
		
		jpnJeu = new JPanel();
		jpnJeu.setBounds(0, 0, L_ARENE, H_ARENE);
		contentPane.add(jpnJeu);
		jpnJeu.setLayout(null);
		jpnJeu.setOpaque(false);
		
		jpnMurs = new JPanel();
		jpnMurs.setBounds(0, 0, L_ARENE, H_ARENE);
		contentPane.add(jpnMurs);
		jpnMurs.setLayout(null);
		jpnMurs.setOpaque(false);
		
		jpnPieges = new JPanel();
		jpnPieges.setBounds(0, 0, L_ARENE, H_ARENE);
		contentPane.add(jpnPieges);
		jpnPieges.setLayout(null);
		jpnPieges.setOpaque(false);
		
		lblFond = new JLabel("");
		lblFond.setBounds(0, 0, L_ARENE, H_ARENE);
		contentPane.add(lblFond);
		lblFond.setIcon(new ImageIcon(FONDARENE));
		
		if (client) {
			txtSaisie = new JTextField();
			txtSaisie.setBounds(0, H_ARENE, L_ARENE, H_SAISIE);
			contentPane.add(txtSaisie);
			txtSaisie.setColumns(10);		
			txtSaisie.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent arg0) {
					txtSaisie_keyPressed(arg0);
				}
			});
		}
		
		jspChat = new JScrollPane();
		jspChat.setBounds(0, H_ARENE+H_SAISIE, L_ARENE, H_CHAT-H_SAISIE-7*MARGE);
		jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(jspChat);
		
		txtChat = new JTextArea();
		jspChat.setViewportView(txtChat);
		
		// Gestion des sons.
		
		if (client) {
			(new Son(SONAMBIANCE)).playContinue();
			for (int i = 0; i < SON.length; i++) {
				lessons[i] = new Son(CHEMINSONS + SON[i]);
			}
		}
	}
	
	
	// -- Méthodes pour le t'chat --
	public void txtSaisie_keyPressed(KeyEvent arg0) {
		// Vérification que le client a bien appuyé sur la touche entrée.
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if (txtSaisie.getText() != null) {
				controle.evenementVue(this, CHAT + SEPARE + txtSaisie.getText());
				txtSaisie.setText("");
				contentPane.requestFocus();
			}
		}
	}
	
	public void ajoutChat(String unePhrase) {
		txtChat.setText(unePhrase + "\r\n" + txtChat.getText());
	}
	
	// -- Méthodes pour les déplacements -- 
	public void contentPane_keyPressed(KeyEvent arg0) {
		int valeur = -1;
		
		// On attribue une de nos constantes à valeur en fonction de la touche appuyée par le joueur.
		switch(arg0.getKeyCode()) {
			case KeyEvent.VK_SPACE : valeur = TIRE;
			break;
			case KeyEvent.VK_LEFT : valeur = GAUCHE;
			break;
			case KeyEvent.VK_RIGHT : valeur = DROITE; 
			break;
			case KeyEvent.VK_UP : valeur = HAUT;
			break;
			case KeyEvent.VK_DOWN : valeur = BAS;
			break;
		}
		
		if (valeur != -1) {
			controle.evenementVue(this, ACTION + SEPARE + valeur);
		}
	}
	
	public void ajoutMur(JLabel unMur)
	{
		jpnMurs.add(unMur);
		jpnMurs.repaint();
	}
	
	public void ajoutPiege(JLabel unPiege)
	{
		jpnPieges.add(unPiege);
		jpnPieges.repaint();
	}
	
	// Méthode qui réceptionne un JPanel et transfère son contenu dans le JPanel des murs.
	public void ajoutPanelMurs(JPanel unPanel) {
		jpnMurs.add(unPanel);
		jpnMurs.repaint();
		contentPane.requestFocus();
	}
	
	public void ajoutPanelPieges(JPanel unPanel) {
		jpnPieges.add(unPanel);
		jpnPieges.repaint();
		contentPane.requestFocus();
	}
	
	// On crée la méthode getjpnMurs.
	public JPanel getJpnMurs() {
		return jpnMurs;
	}
	
	
	/**
	 * @return the jpnPieges
	 */
	public JPanel getJpnPieges() {
		return jpnPieges;
	}


	public void ajoutJoueur(JLabel unJoueur) {
		jpnJeu.add(unJoueur);
		jpnJeu.repaint();
		contentPane.requestFocus();
	}
	
	public void ajoutModifJoueur(int num, JLabel unLabel) {
		try {
			// Try catch pour être sûr qu'il ne se passe rien si on tente de supprimer un JLabel qui n'existe pas.
			jpnJeu.remove(num);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		// On met en second paramètre num pour que l'ajout se fasse au bon endroit.
		jpnJeu.add(unLabel, num);
		jpnJeu.repaint();
	}
	
	public void ajoutModifJPiege(int num, JLabel unLabel) {
		try {
			// Try catch pour être sûr qu'il ne se passe rien si on tente de supprimer un JLabel qui n'existe pas.
			jpnPieges.remove(num);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		// On met en second paramètre num pour que l'ajout se fasse au bon endroit.
		jpnPieges.add(unLabel, num);
		jpnPieges.repaint();
	}

	public void remplaceChat(String qqch) {
		txtChat.setText(qqch);
		
	}
	
	// Création de la méthode qui va permettre de jouer le son.
	public void joueSon(int numSon) {
		lessons[numSon].play();
	}

	// Getter de TxtChat.
	public JTextArea getTxtChat() {
		return txtChat;
	}
	
	
}
