package controleur;

public interface Global {
	
	// Constantes de type int.
	public static final int 
	
	// Serveur 
	PORT = 6666,
	
	// Personnages 
	NBPERSOS = 4,
	H_PERSO = 44,
	L_PERSO = 39,
	TIRE = 4,
	ACTION = 2,
	NBETATSBLESSE = 4,
	NBETATSMORT = 5,
	
	// Déplacements 
	GAUCHE = 0,
	DROITE = 1,
	HAUT = 2,
	BAS = 3,
	NBETATSMARCHE = 6,
	LEPAS = 10,
	
	// Joueur
	PSEUDO = 0,
	H_MESSAGE = 8,
	
	// Arène
	H_ARENE = 632,
	L_ARENE = 1000,
	H_CHAT = 200,
	H_SAISIE = 25,
	MARGE = 5,
	
	// Murs
	NBMURS = 10,
	H_MUR = 35,
	L_MUR = 34,
	
	// Boules 
	L_BOULE = 17,
	H_BOULE = 17,
	NBETATSBOULE = 6,
	
	// Pièges
	L_PIEGE = 32,
	H_PIEGE = 32,
	PERTEPIEGE = 5,
	
	// Chat
	CHAT = 1,
	
	// Sons
	FIGHT = 0,
	HURT = 1,
	DEATH = 2;
	
	
	// Constantes de type String.
	public static final String 
	
	// Serveur
	SEPARATOR = "//",
	CHEMIN = "media" + SEPARATOR,
	CHEMINFONDS = CHEMIN + "fonds" + SEPARATOR,
	FONDCHOIX = CHEMINFONDS + "fondchoix.png",
	
	// Personnages
	CHEMINPERSOS = CHEMIN + "personnages" + SEPARATOR,
	PERSO = CHEMINPERSOS + "perso",
	EXTIMAGE = ".png",
	MARCHE = "marche",
	BLESSE = "touche",
	MORT = "mort",
	ATTAQUE = "attaque",
	
	// Joueur
	SEPARE = "~",
	
	// Arène 
	FONDARENE = CHEMINFONDS + "fondarene.png",
	
	// Murs 
	CHEMINMURS = CHEMIN + "murs" + SEPARATOR,
	MUR = CHEMINMURS + "mur.png",
	
	// Boules 
	CHEMINBOULES = CHEMIN + "boules" + SEPARATOR,
	BOULE = CHEMINBOULES + "boule.png",
	
	// Potion
	CHEMINPIEGE = CHEMIN + "pieges" + SEPARATOR,
	PIEGE = CHEMINPIEGE + "piege.png",
	
	// Sons
	CHEMINSONS = CHEMIN + "sons/",
	SONPRECEDENT = CHEMINSONS + "precedent.wav",
	SONSUIVANT = CHEMINSONS + "suivant.wav",
	SONGO = CHEMINSONS + "go.wav",
	SONWELCOME = CHEMINSONS + "welcome.wav",
	SONAMBIANCE = CHEMINSONS + "ambiance.wav";
	
	// Constante spécifique aux sons.
	
	public static final String[] 
			
	SON = {"fight.wav", "hurt.wav", "death.wav"};
}
