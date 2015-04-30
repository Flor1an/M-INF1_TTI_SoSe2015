package listener;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

import roxel.MapDimension;
import roxel.MapElement;
import updates.NotifyGuiAboutCarMovement;

@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class ListenerForCarMovement {
	Integer i = 0;
	private int currentXPosition;
	private int currentYPosition;
	private GigaSpace gigaspace;
	int carId;
	MapDimension md;

	/**
	 * Listener for car movement
	 */
	public ListenerForCarMovement(GigaSpace gigaspace, int carId) {
		this.gigaspace = gigaspace;
		this.carId = carId;

		md = gigaspace.read(new MapDimension());

	}

	@EventTemplate
	MapElement unprocessedData() {

		MapElement template = new MapElement();
		template.setCurrentCarId(carId);
		return template;
	}

	@SuppressWarnings("finally")
	@SpaceDataEvent
	public MapElement eventListener(MapElement event) {
		try {
			// Speed of car
			Thread.sleep(400);

			currentXPosition = event.getX();
			currentYPosition = event.getY();

			int successorXPosition = currentXPosition;
			int successorYPosition = currentYPosition;

			// calculate successor position
			if (event.isEastAllowed()) {
				int incrementedXPosition = currentXPosition + 1;
				successorXPosition = incrementedXPosition > md.getWidth() - 1 ? 0 : incrementedXPosition;
			} else if (event.isSouthAllowed()) {
				int incrementedYPosition = currentYPosition + 1;
				successorYPosition = incrementedYPosition > md.getHeight() - 1 ? 0 : incrementedYPosition;
			}

			// ///////////////// //
			// get the successor //
			// ///////////////// //
			MapElement template = new MapElement();
			template.setX(successorXPosition);
			template.setY(successorYPosition);

			// read (temporary) the successor in order to check if successor is
			// a valid successor
			MapElement successor = gigaspace.read(template, Integer.MAX_VALUE);

			// verify if successor is a valid successor
			if (event.isSouthAllowed() == successor.isSouthAllowed() || event.isEastAllowed() == successor.isEastAllowed()) {
				if (!successor.hasCar()) {

					// Debug
					MapElement tmp = successor;
					successor = gigaspace.take(successor, Integer.MAX_VALUE);
					if (successor == null) {
						// Debug
						System.err.println("CANNOT TAKE: \t" + tmp);
						return event;
					}

					// Set car to successor
					successor.setCurrentCarId(carId);
					gigaspace.write(successor);

					// remove car from current position
					event.removeCar();

					// notify about the car movement
					gigaspace.write(new NotifyGuiAboutCarMovement(carId, event.getX(), event.getY(), successor.getX(), successor.getY(), successor.isEastAllowed(), successor.isSouthAllowed()));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			return event;
		}
	}
}