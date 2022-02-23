package modele;

public abstract class Objet {
	
	// D�claration des propri�t�s.
	protected int posX; // 
	protected int posY; // Ces deux variables contiendront la position du JLabel. 
	protected Label label;
	
	// Cr�ation des getters.
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public Label getLabel() {
		return label;
	}
	
	// Cr�ation des setters pour les positions.
	
	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	/**
	* contr�le si l'objet actuel touche l'objet pass� en param�tre
	* @param objet
	* @return vrai si les 2 objets se touchent
	*/
	
	public boolean toucheObjet (Objet objet) {
		
		if (objet.label==null) {
			return false;
		} 
		else {
			
			if (objet.label.getjLabel()==null) {
				return false;
			} 
			else {
				int l_obj = objet.label.getjLabel().getWidth();
				int h_obj = objet.label.getjLabel().getHeight();
				int l_this = this.label.getjLabel().getWidth();
				int h_this = this.label.getjLabel().getHeight();
				
				return(!((this.posX+l_this<objet.posX ||
						  this.posX>objet.posX+l_obj) ||
						 (this.posY+h_this<objet.posY ||
						  this.posY>objet.posY+h_obj)));
			}
		}
	}


}
