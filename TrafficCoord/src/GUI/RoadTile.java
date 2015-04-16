package GUI;

import roxel.MapElement;
import ch.aplu.jgamegrid.Actor;

public class RoadTile extends Actor {

	public RoadTile(MapElement.Arrow style) {
		super(getSprite(style));
	}

	private static String getSprite(MapElement.Arrow style) {
	if(style.isRoad()){

		if (style.isStraight()) {
			switch (style) {
			case WEST:
				return "Sprites/road.png";
			case SOUTH:
				return "Sprites/road90.png";

			default:
				return "Sprites/unk.png";
			}
		} else if (style.isJunction()) {
			return "Sprites/junction.png";
		} else {
			return "Sprites/unk.png";
		}
	}else{
		throw new UnsupportedOperationException("map element is not an road! (no generation of an road tile possible)");
	}}
}
