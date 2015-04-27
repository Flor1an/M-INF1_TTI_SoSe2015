package traffic;

import logger.Logger;

import org.openspaces.core.GigaSpace;

import GUI.MapElementUpdate;
import roxel.MapElement;

public class Car {

	private final GigaSpace gigaSpace;
	private final Integer carId;
	private final Logger log = new Logger();
	private int currentXPosition;
	private int currentYPosition;

	/**
	 * Initialise a car for the map environment
	 * 
	 * @param gigaSpace
	 *            Initialised tuple space.
	 * @param carId
	 *            unique number for the car as a unique identifier
	 */
	public Car(GigaSpace gigaSpace, Integer carId) {
		this.gigaSpace = gigaSpace;
		this.carId = carId;
		setCarToRandomPosition();
	}

	/**
	 * Find a free mapElement and start driving.
	 */
	private void setCarToRandomPosition() {
		MapElement roadMapElem = new MapElement();
		roadMapElem.setType(MapElement.Type.ROAD);

		boolean foundFreeMapElement = false;
		while (!foundFreeMapElement) {
			MapElement randomRoadMapElementFromSpace = gigaSpace.read(roadMapElem);
			log.read(randomRoadMapElementFromSpace.toString() + " \t\t\t (random road element from space)");
			if (!randomRoadMapElementFromSpace.hasCar()) {
				randomRoadMapElementFromSpace = gigaSpace.take(randomRoadMapElementFromSpace);
				foundFreeMapElement = true;
				randomRoadMapElementFromSpace.setCurrentCarId(carId);
				currentXPosition = randomRoadMapElementFromSpace.getX();
				currentYPosition = randomRoadMapElementFromSpace.getY();
				gigaSpace.write(randomRoadMapElementFromSpace);
				log.write(randomRoadMapElementFromSpace.toString() + "   \t\t\t (with setted carId)");

				MapElementUpdate meu = new MapElementUpdate(carId, -1, -1, randomRoadMapElementFromSpace.getX(), randomRoadMapElementFromSpace.getY(), randomRoadMapElementFromSpace.getArrow());
				gigaSpace.write(meu);
				log.write(meu.toString());
			}
		}
		log.log("Assigning of Car " + carId + " to [" + currentXPosition + ";" + currentYPosition + "] completed.");
	}
}
