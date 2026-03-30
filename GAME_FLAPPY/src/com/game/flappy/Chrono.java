package com.game.flappy;

public class Chrono implements Runnable{

	private final int PAUSE = 10;
	
	@Override
	public void run() {
		while(true) {
			Main.scene.xFond--;
			Main.scene.repaint();
			try {
				Thread.sleep(this.PAUSE);
			} catch (InterruptedException e) {}
		}
	}

}
