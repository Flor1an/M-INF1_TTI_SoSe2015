package GUI;

import java.util.Random;

import ch.aplu.jgamegrid.Actor;

public class NoRoadTile extends Actor {

	public NoRoadTile() {
		super(getSprite());
	}

	private static String getSprite() {
		
		if (new Random().nextInt(5)==0){
			return "Sprites/green1.png";
		}else{
			return "Sprites/green.png";

		}
	}
}
