package listener;


import java.util.Random;



import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

import GUI.MapElementUpdate;
import roxel.MapDimension;
import roxel.MapElement;

@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class ListenerForCarMovement {
	Integer i = 0;
	private int currentXPosition;
	private int currentYPosition;
	private GigaSpace gigaspace;
	int carId;

	public ListenerForCarMovement(GigaSpace gigaspace, int carId) {
		this.gigaspace = gigaspace;
		this.carId = carId;
	}

	@EventTemplate
	MapElement unprocessedData() { 
		MapElement template = new MapElement();
		template.setCurrentCarId(carId); 
		return template;
	}

	@SpaceDataEvent
	public MapElement eventListener(MapElement event) throws InterruptedException {

		Thread.sleep(500);

//		if (carId == 0)
//			System.out.println("Auto bewegen " + event.toString());

		MapElement updated = moveForward(event);

//		if (carId == 0)
//			System.out.println("\tupdate auf (ohne auto drauf): " + updated + "\n");

		return updated;

	}

	/**
	 * moves the car one element forward (or turn on junctions) bases on the
	 * game logic
	 * 
	 * @throws InterruptedException
	 */
	public MapElement moveForward(MapElement currentMapElementFromSpace) throws InterruptedException {
		currentXPosition = currentMapElementFromSpace.getX();
		currentYPosition = currentMapElementFromSpace.getY();

		int successorXPosition = currentXPosition;
		int successorYPosition = currentYPosition;

		MapDimension md = gigaspace.read(new MapDimension());

		MapElement.Arrow arrow = currentMapElementFromSpace.getArrow();
		if (arrow.isStraight() == false) {
			if (new Random().nextInt(2) == 0) {
				arrow = MapElement.Arrow.WEST;
			} else {
				arrow = MapElement.Arrow.SOUTH;
			}
		}

		switch (arrow) {
		case WEST:
			int incrementedXPosition = currentXPosition + 1;
			successorXPosition = incrementedXPosition > md.getWidth() - 1 ? 0 : incrementedXPosition;
			break;

		case SOUTH:
			int incrementedYPosition = currentYPosition + 1;
			successorYPosition = incrementedYPosition > md.getHeight() - 1 ? 0 : incrementedYPosition;
			break;

		default:
			throw new UnsupportedOperationException("Unknown arrow direction");
		}

		//get the successor
		MapElement template = new MapElement();
		template.setX(successorXPosition);
		template.setY(successorYPosition);
		MapElement fromSpace = gigaspace.read(template, Integer.MAX_VALUE);

		//check if successor is blocked with other car
		if (fromSpace.hasCar() == false) {
			//if not blocked do the movement
			fromSpace = gigaspace.take(fromSpace, Integer.MAX_VALUE);
			fromSpace.setCurrentCarId(carId);
			gigaspace.write(fromSpace);

			currentMapElementFromSpace.removeCar();
			//TODO: Set currentMapElementFromSpace so, that the trafficlight coordinator can do stuff

			MapElementUpdate meu = new MapElementUpdate(carId, currentXPosition, currentYPosition, successorXPosition, successorYPosition, fromSpace.getArrow());
			gigaspace.write(meu);

			return currentMapElementFromSpace;
		} else {
			// blocked with other car -> try again (see if other car is gone)
			if (carId == 0)
				System.out.println("congestion! Try again in 1 sec to move...");
			Thread.sleep(1000);
			moveForward(currentMapElementFromSpace);
		}

		return null; // not reachable due to loop

	}

}