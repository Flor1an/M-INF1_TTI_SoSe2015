package traffic;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import logger.Logger;

import org.openspaces.core.GigaSpace;

import GUI.MapElementUpdate;
import roxel.MapDimension;
import roxel.MapElement;

public class Car {

	private final GigaSpace gigaSpace;
	private final Integer carId;
	private final Logger log = new Logger();
	private MapDimension mapDimension;
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
		mapDimension = gigaSpace.read(new MapDimension());

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

	/**
	 * moves the car one element forward (or turn on junctions) bases on the
	 * game logic
	 */
	public void moveForward() {
		MapElement plainMapElemWithCarId = new MapElement();
		plainMapElemWithCarId.setCurrentCarId(carId);
		MapElement currentMapElementFromSpace = gigaSpace.take(plainMapElemWithCarId);
		log.read(currentMapElementFromSpace.toString() + "   \t\t\t\t (found the element which car " + carId + " is currently using)");

		if (currentMapElementFromSpace != null) {
			currentXPosition = currentMapElementFromSpace.getX();
			currentYPosition = currentMapElementFromSpace.getY();

			List<int[]> successorPositions = getNextPosition(currentMapElementFromSpace);
			Set<MapElement> successors = getNextMapElementsByPositions(successorPositions);

			MapElement randomSucessor = successors.iterator().next();

			if (!randomSucessor.hasCar()) {
				randomSucessor = gigaSpace.take(randomSucessor);
				currentMapElementFromSpace.removeCar();

				randomSucessor.setCurrentCarId(carId);
				currentXPosition = randomSucessor.getX();
				currentYPosition = randomSucessor.getY();

				MapElementUpdate meu = new MapElementUpdate(carId, currentMapElementFromSpace.getX(), currentMapElementFromSpace.getY(), randomSucessor.getX(), randomSucessor.getY(), randomSucessor.getArrow());

				gigaSpace.write(meu);
				log.write(meu.toString() + "   \t\t (notification that change was made)");

			} else {
				log.log("[car=" + carId + " NOT moved (due to mapElement is blocked by car=" + randomSucessor.getCurrentCarId() + ") => 'TRAFFIC JAM']");
				// next try to move, on new call of this method.
			}

			// update "old" element
			gigaSpace.write(currentMapElementFromSpace);
			log.write(currentMapElementFromSpace.toString() + "   \t\t\t\t (old mapElement [now free])");

			// update "new" element
			gigaSpace.write(randomSucessor);
			log.write(randomSucessor.toString() + "   \t\t\t\t (new mapElement [now blocked with carId=" + carId + "])\n");
		}
	}

	/**
	 * from a given mapElement this method calculates the next possible x and y
	 * coordinate (successor) to get moved on. This element have to be an road
	 * element by definition of the game.
	 * 
	 * @param mapElem
	 *            the MapElement to get an successor position
	 * @return a list of x and y positions of successor elements
	 */
	private List<int[]> getNextPosition(MapElement mapElem) {
		currentXPosition = mapElem.getX();
		currentYPosition = mapElem.getY();
		MapElement.Arrow arrow = mapElem.getArrow();

		List<int[]> successorPositions = new ArrayList<int[]>();

		if (arrow.isStraight()) {
			successorPositions.add(getNextPositionOfAnStraightElementByArrowDirection(arrow));
		} else if (arrow.isTwoWayJunction()) {
			switch (arrow) {
			case TWO_WAY_JUNCTION_TO_WEST_OR_TO_SOUTH:
				successorPositions.add(getNextPositionOfAnStraightElementByArrowDirection(MapElement.Arrow.WEST));
				successorPositions.add(getNextPositionOfAnStraightElementByArrowDirection(MapElement.Arrow.SOUTH));
				break;
			
	
			default:
				throw new UnsupportedOperationException("Unknown two way junction type arrow");
			}
		} else if (arrow.isThreeWayJunction()) {
			switch (arrow) {

			default:
				throw new UnsupportedOperationException("Unknown three way junction type arrow");
			}
		} else if (arrow.isFourWayJunction()) {
			switch (arrow) {

			default:
				throw new UnsupportedOperationException("Unknown four way junction type arrow");
			}
		}

		return successorPositions;
	}

	/**
	 * gets one position of an successor based on the current position and the
	 * direction facing to
	 * 
	 * @param arrow
	 *            the direction facing to
	 * @return x and y position of the requested direction
	 */
	private int[] getNextPositionOfAnStraightElementByArrowDirection(MapElement.Arrow arrow) {
		if (arrow.isStraight()) {
			int[] successorPosition = new int[] { currentXPosition, currentYPosition };

			switch (arrow) {
			case WEST:
				int incrementedXPosition = currentXPosition + 1;
				successorPosition[0] = incrementedXPosition > mapDimension.getWidth() - 1 ? 0 : incrementedXPosition;
				break;

			case SOUTH:
				int incrementedYPosition = currentYPosition + 1;
				successorPosition[1] = incrementedYPosition > mapDimension.getHeight() - 1 ? 0 : incrementedYPosition;
				break;


			default:
				throw new UnsupportedOperationException("Unknown straight arrow");
			}
			return successorPosition;
		} else {
			throw new UnsupportedOperationException("The arrow is not an straight element!");
		}
	}

	/**
	 * reads the map elements from the tuple space of the given positions
	 * 
	 * @param positions
	 *            the list of positions
	 * @return the list of actual map elements from the tuple space
	 */
	private Set<MapElement> getNextMapElementsByPositions(List<int[]> positions) {
		Set<MapElement> mapElements = new HashSet<MapElement>();
		for (int[] pos : positions) {
			MapElement mapElem = new MapElement();
			mapElem.setX(pos[0]);
			mapElem.setY(pos[1]);

			MapElement mapElemFromSpace = gigaSpace.read(mapElem);
			mapElements.add(mapElemFromSpace);
		}

		return mapElements;
	}


}
