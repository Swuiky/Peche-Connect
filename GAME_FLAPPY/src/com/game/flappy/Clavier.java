package com.game.flappy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.game.audio.Audio;

public class Clavier implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
	    if(e.getKeyCode() == KeyEvent.VK_SPACE){
	    	Audio.playSound("/audio/battementAile.wav");
	    	Main.scene.flappyBird.monte();	    	
	    }
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}