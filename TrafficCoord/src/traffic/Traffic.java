package traffic;

import java.util.HashSet;
import java.util.Set;

import logger.Logger;

import org.openspaces.core.GigaSpace;

public class Traffic {
	private final Set<Car> cars = new HashSet<Car>();
	private final Logger log = new Logger();

	/**
	 * responsible for the traffic on the map especial initialise the cars for
	 * the map
	 * 
	 * @param gigaSpace
	 *            Initialised tuple space.
	 * @param amountOfCars
	 *            the amount of cars for the map
	 */
	public Traffic(GigaSpace gigaSpace, int amountOfCars) {
		log.log("Adding " + amountOfCars + " cars...");

		for (int id = 0; id < amountOfCars; id++) {
			cars.add(new Car(gigaSpace, id));
		}

		log.log("Adding of cars completed!");
		log.logLine();
	}

	/**
	 * adds the cars to the map and starts them
	 */
	public void moveCars() {
		for (Car car : cars) {
			car.moveForward();
		}
	}
}
