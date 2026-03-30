package com.game.objets;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.game.personnages.FlappyBird;

public class TuyauBas {
	//**** VARIABLES ****//
	private int largeur, hauteur; //dimensions de l'objet
	private int x, y; //position de l'objet	
	private Image imgTuyauBas;
	private ImageIcon icoTuyauBas;
			
			
	//**** CONSTRUCTEUR ****//	
	public TuyauBas(int x, int y){		
			this.x = x;
			this.y = y;
			this.largeur = 50;
			this.hauteur = 300;
			this.icoTuyauBas = new ImageIcon(getClass().getResource("/images/tuyauBas.png"));
	        this.imgTuyauBas = this.icoTuyauBas.getImage();
		}
			
		//**** GETTERS ****//	
		public int getX() {return x;}

		public int getY() {return y;}

		public int getLargeur() {return largeur;}

		public int getHauteur() {return hauteur;}
			
		public Image getImgObjet() {return imgTuyauBas;}


		//**** SETTERS ****//	
		public void setX(int x) {this.x = x;}

		public void setY(int y) {this.y = y;}	
			
		//**** METHODES ****//	
		public boolean collisionFlappy(FlappyBird oiseau){
			if(oiseau.getY() + oiseau.getHauteur() >= this.y && 
			   oiseau.getX() + oiseau.getLargeur() >= this.x && 
			   oiseau.getX() <= this.x + this.largeur
			  ){return true;}
			else{return false;}
		}
	}
