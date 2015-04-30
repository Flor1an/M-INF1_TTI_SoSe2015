package GUI;

import ch.aplu.jgamegrid.Actor;

public class CarTile extends Actor {


	public CarTile(int carId) {
		super(true, getSprite(carId));
	}

	private static String getSprite(int carId) {
		String path=null;
		if (carId==0){
			path = "Sprites/car-red.png";
		}else{
			path = "Sprites/car.png";
		}
		return path;
	}
}
