package GUI;

import roxel.MapElement;
import ch.aplu.jgamegrid.Actor;

public class RoadTile extends Actor {

	public RoadTile(MapElement roadElem) {
		super(getSprite(roadElem));
	}

	private static String getSprite(MapElement roadElem) {
		if (roadElem.isJunction() == false) {
			if (roadElem.isEastAllowed()) {
				return "Sprites/road.png";
			} else if (roadElem.isSouthAllowed()) {
				return "Sprites/road90.png";
			} else {
				return "Sprites/unk.png";
			}
		} else {
			return "Sprites/unk.png";
		}
	}
}
