package com.game.objets;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.game.personnages.FlappyBird;

public class TuyauHaut {
	
	//**** VARIABLES ****//
	private int largeur, hauteur; //dimensions de l'objet
	private int x, y; //position de l'objet	
	private Image imgTuyauHaut;
	private ImageIcon icoTuyauHaut;
		
		
	//**** CONSTRUCTEUR ****//	
	public TuyauHaut(int x, int y){		
		this.x = x;
		this.y = y;
		this.largeur = 50;
		this.hauteur = 300;
		this.icoTuyauHaut = new ImageIcon(getClass().getResource("/images/tuyauHaut.png"));
        this.imgTuyauHaut = this.icoTuyauHaut.getImage();
	}
		
	//**** GETTERS ****//	
	public int getX() {return x;}

	public int getY() {return y;}

	public int getLargeur() {return largeur;}

	public int getHauteur() {return hauteur;}
		
	public Image getImgObjet() {return imgTuyauHaut;}


	//**** SETTERS ****//	
	public void setX(int x) {this.x = x;}

	public void setY(int y) {this.y = y;}	
		
	//**** METHODES ****//	
	public boolean collisionFlappy(FlappyBird oiseau){
		if(oiseau.getY() <= this.y + this.getHauteur() && 
		   oiseau.getX() + oiseau.getLargeur() >= this.x && 
		   oiseau.getX() <= this.x + this.largeur
		  ){return true;}
		else{return false;}
	}			
}
