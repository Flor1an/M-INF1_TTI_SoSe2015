package Tiles;

import ch.aplu.jgamegrid.Actor;

public class CarTile extends Actor {
	private Integer carId;

	public CarTile(int carId) {
		super(true, getSprite(carId));
		this.carId = carId;
	}

	public CarTile() {
		// TODO Auto-generated constructor stub
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
	
	Integer getCarId() {
		return this.carId;
	}
}
