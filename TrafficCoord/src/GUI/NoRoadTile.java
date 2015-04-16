package GUI;

import ch.aplu.jgamegrid.Actor;

public class NoRoadTile extends Actor {

	public NoRoadTile() {
		super(getSprite());
	}

	private static String getSprite() {
		String path = "Sprites/green.png";
		return path;
	}
}
