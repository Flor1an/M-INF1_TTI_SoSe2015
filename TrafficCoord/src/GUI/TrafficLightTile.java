package GUI;

import ch.aplu.jgamegrid.Actor;

public class TrafficLightTile extends Actor {

	public TrafficLightTile(Boolean east, Boolean south) {
		super(getSprite(east,south));
	}

	private static String getSprite(Boolean east, Boolean south) {

			if (east) {
				return "Sprites/junctionWest.png";
			} else if (south) {
				return "Sprites/junctionSouth.png";
			} else {
				return "Sprites/junctionOrange.png";
			}
	
	}
}
