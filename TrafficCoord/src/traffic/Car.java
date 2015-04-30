package traffic;

import logger.Logger;

import org.openspaces.core.GigaSpace;

import roxel.MapElement;
import updates.NotifyGuiAboutCarMovement;

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
		roadMapElem.setRoad(true);

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

				NotifyGuiAboutCarMovement meu = new NotifyGuiAboutCarMovement(carId, -1, -1, randomRoadMapElementFromSpace.getX(), randomRoadMapElementFromSpace.getY(), randomRoadMapElementFromSpace.isEastAllowed(), randomRoadMapElementFromSpace.isSouthAllowed());
				gigaSpace.write(meu);
				log.write(meu.toString());
			}
		}
		log.log("Assigning of Car " + carId + " to [" + currentXPosition + ";" + currentYPosition + "] completed.");
	}
}
