package Tiles;

import haw.common.MapElement;
import ch.aplu.jgamegrid.Actor;

public class JunctionTile extends Actor {

	public JunctionTile(MapElement junctionElem) {
		super(getSprite(junctionElem));
	}

	private static String getSprite(MapElement junctionElem) {
		return "Sprites/junction.png";
	}
}
