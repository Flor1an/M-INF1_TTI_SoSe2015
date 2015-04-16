package GUI;

import ch.aplu.jgamegrid.Actor;

public class CarTile extends Actor {


	public CarTile(int carId) {
		super(true, getSprite());
	}

	private static String getSprite() {
		String path = "Sprites/car.png";
		return path;
	}
}
