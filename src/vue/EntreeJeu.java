package vue;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controleur.Controle;

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class EntreeJeu extends JFrame {

	
	private JPanel contentPane;
	private JTextField txtIp;
	private final JLabel lblStartAServer = new JLabel("Start a server : ");
	
	// On déclare la propriété controle.
	private Controle controle;

	
	/**
	 * Launch the application.
	 */
	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					EntreeJeu frame = new EntreeJeu();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	
	/**
	 * Create the frame.
	 * @param controle 
	 */
	
	public EntreeJeu(Controle controle) {
		setForeground(SystemColor.text);
		setTitle("Urban Marginal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 349, 236);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setBorder(new EmptyBorder(10, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// On initialise le bouton Start et on lui donne un évènement.
		
		JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnStart_clic();
			}
		});
		btnStart.setForeground(SystemColor.activeCaptionText);
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnStart.setBackground(SystemColor.activeCaption);
		btnStart.setBounds(210, 38, 115, 25);
		contentPane.add(btnStart);
		
		txtIp = new JTextField();
		txtIp.setFont(new Font("Arial", Font.PLAIN, 14));
		txtIp.setText("127.0.0.1");
		txtIp.setBounds(99, 120, 100, 24);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
		
		// On initialise le bouton Connect et on lui donne un évènement.
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnConnect_clic();
			}
		});
		btnConnect.setForeground(SystemColor.activeCaptionText);
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnConnect.setBackground(SystemColor.activeCaption);
		btnConnect.setBounds(210, 118, 115, 25);
		contentPane.add(btnConnect);
		
		// On initialise le bouton Exit et on lui donne un évènement.
		
		JButton btnExit = new JButton("Exit");
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnExit_clic();
			}
		});
		btnExit.setForeground(SystemColor.activeCaptionText);
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExit.setBackground(SystemColor.activeCaption);
		btnExit.setBounds(210, 157, 115, 25);
		contentPane.add(btnExit);
		lblStartAServer.setForeground(SystemColor.controlDkShadow);
		lblStartAServer.setFont(new Font("Arial", Font.BOLD, 15));
		lblStartAServer.setBounds(10, 33, 139, 36);
		contentPane.add(lblStartAServer);
		
		JLabel lblConnectAnExisting = new JLabel("Connect an existing server :");
		lblConnectAnExisting.setForeground(SystemColor.controlDkShadow);
		lblConnectAnExisting.setFont(new Font("Arial", Font.BOLD, 15));
		lblConnectAnExisting.setBounds(10, 74, 221, 36);
		contentPane.add(lblConnectAnExisting);
		
		JLabel lblIpServer = new JLabel("IP server :");
		lblIpServer.setForeground(SystemColor.controlDkShadow);
		lblIpServer.setFont(new Font("Arial", Font.BOLD, 15));
		lblIpServer.setBounds(10, 113, 89, 36);
		contentPane.add(lblIpServer);
		
		// On valorise la propriété controle.
		
		this.controle = controle;	
	}
	
	
	/**
	 * Clic sur le bouton Start pour lancer le serveur.
	 */
	
	private void btnStart_clic() {
		//System.out.println("Using start button");
		controle.evenementVue(this, "serveur");
	}

	/**
	 * Clic sur le bouton Connect pour se connecter à un serveur.
	 */
	
	private void btnConnect_clic() {
		//System.out.println("Using connect button");
		controle.evenementVue(this, txtIp.getText());
	}
	
	/**
	 * Clic sur le bouton Exit pour quitter le serveur.
	 */
	
	private void btnExit_clic() {
		System.exit(0);
	}
	
}
