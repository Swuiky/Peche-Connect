package com.game.personnages;

import java.awt.Image;

import javax.swing.ImageIcon;

public class FlappyBird implements Runnable{

	// VARIABLES
	private int largeur;
	private int hauteur;
	private int x;
	private int y;
	private int dy;
	private String strImage;
	private ImageIcon icoOiseau;
	private Image imgOiseau;
	
	private final int PAUSE = 10;
	
	// CONSTRUCTEUR
	public FlappyBird(int x, int y, String strImage){
		
		this.largeur = 34;
		this.hauteur = 24;
		this.x = x;
		this.y = y;
		this.strImage = strImage;
		this.icoOiseau = new ImageIcon(getClass().getResource(this.strImage));
		this.imgOiseau = this.icoOiseau.getImage();
		
		Thread chronoAiles = new Thread(this);
		chronoAiles.start();
	}

	
	// GETTERS
	public int getX() {return x;}
	
	public int getY() {return y;}
	
	public int getLargeur() {return largeur;}

	public int getHauteur() {return hauteur;}

	public Image getImgOiseau() {return imgOiseau;}
	
	
	// SETTERS
	public void setX(int x) {this.x = x;}

	public void setY(int y) {this.y = y;}
	
	
	// METHODES
	public void monte(){this.dy = 50;}

	
	private void batDesAiles(int dy){
		if(dy >10){
			this.icoOiseau = new ImageIcon(getClass().getResource("/images/oiseau2.png"));
			this.imgOiseau = this.icoOiseau.getImage();
			this.y = this.y - 3;
		}else if (dy > 5){this.y = this.y - 2;}
		else if (dy > 1){this.y = this.y - 1;}
		else if (dy == 1){
			this.icoOiseau = new ImageIcon(getClass().getResource("/images/oiseau1.png"));
			this.imgOiseau = this.icoOiseau.getImage();
		}
	}

	@Override
	public void run() {
		while(true){
			this.batDesAiles(dy);
			this.dy--;
			try {Thread.sleep(PAUSE);} catch (InterruptedException e) {}
		}
		
	}
}
