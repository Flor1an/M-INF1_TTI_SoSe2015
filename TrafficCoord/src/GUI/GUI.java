package GUI;

import java.util.ArrayList;

import logger.Logger;

import org.openspaces.core.GigaSpace;

import roxel.MapElement;
import roxel.MapDimension;
import updates.NotifyGuiAboutCarMovement;
import updates.NotifyGuiAboutTrafficLightChange;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GUI extends GameGrid {

	private static final long serialVersionUID = -6268178828310212085L;
	private final GigaSpace gigaSpace;
	Logger log = new Logger();

	public GUI(GigaSpace gigaSpace) {
		super(gigaSpace.read(new MapDimension()).getWidth(), gigaSpace.read(new MapDimension()).getHeight(), gigaSpace.read(new MapDimension()).getImageSize() ,null, null, false);
		log.read(gigaSpace.read(new MapDimension()).toString());
		log.logLine();

		this.gigaSpace = gigaSpace;

		drawMap();
		show();
		doRun();
		while (true) {
			checkForUpdates();
		}
	}

	/**
	 * draws the actual map on the gui, based on the information that gets read
	 * from the tuple space
	 */
	private void drawMap() {
		for (MapElement mapElem : gigaSpace.readMultiple(new MapElement())) {
			Actor tile;
			if (mapElem.isRoad() && !mapElem.isJunction()) {
				tile = new RoadTile(mapElem);
				addActor(tile, new Location(mapElem.getX(), mapElem.getY()));
			} else if (mapElem.isRoad() && mapElem.isJunction()) {
				tile = new JunctionTile(mapElem);
				addActor(tile, new Location(mapElem.getX(), mapElem.getY()));
				addActor(new TrafficLightTile(mapElem.isEastAllowed(), mapElem.isSouthAllowed()), new Location(mapElem.getX(), mapElem.getY()));
			} else {
				tile = new NoRoadTile();
				addActor(tile, new Location(mapElem.getX(), mapElem.getY()));
			}

		}
	}


	/**
	 * responsible for reading changes from the tuple space
	 */
	private void checkForUpdates() {

		NotifyGuiAboutTrafficLightChange[] taken1 = gigaSpace.takeMultiple(new NotifyGuiAboutTrafficLightChange());
		if (taken1 != null && taken1.length > 0) {
			for (NotifyGuiAboutTrafficLightChange trafficLightUpdate : taken1) {
				performTrafficLightUpdate(trafficLightUpdate);
			}
		}

		NotifyGuiAboutCarMovement[] taken = gigaSpace.takeMultiple(new NotifyGuiAboutCarMovement());
		if (taken != null && taken.length > 0) {
			for (NotifyGuiAboutCarMovement mapElementUpdate : taken) {
				performCarMovementUpdate(mapElementUpdate);
			}
		}
	}

	public void performTrafficLightUpdate(NotifyGuiAboutTrafficLightChange update) {
		ArrayList<Actor> actorsAt = getActorsAt(new Location(update.getX(), update.getY()));
		for (Actor trafficLight : actorsAt) {
			if (trafficLight instanceof TrafficLightTile) {
				addActor(new TrafficLightTile(update.getToWest(), update.getToSouth()), new Location(trafficLight.getX(), trafficLight.getY()));
				trafficLight.removeSelf();
			}
		}
	}

	/**
	 * does the actual update (move and turn of the actual car)
	 * 
	 * @param update
	 */
	public void performCarMovementUpdate(NotifyGuiAboutCarMovement update) {
		Integer oldX = update.getOldX();
		Integer oldY = update.getOldY();

		Integer newX = update.getNewX();
		Integer newY = update.getNewY();

		Integer carId = update.getCarId();

		// Allows to change the orientation of the car (in order to look like driving "forward")
		int degreesToTurn = -1;

		if (update.getEast()) {
			degreesToTurn = 0;
		} else if (update.getSouth()) {
			degreesToTurn = 90;
		}

		if (oldX == -1 || oldY == -1) {
			CarTile car = new CarTile(carId);
			addActor(car, new Location(newX, newY), degreesToTurn);

		} else {
			ArrayList<Actor> actorsAt = getActorsAt(new Location(oldX, oldY));
			for (Actor car : actorsAt) {
				if (car instanceof CarTile) {
					car.setLocation(new Location(newX, newY));
					if (degreesToTurn != -1)
						car.setDirection(degreesToTurn);
				}
			}
		}
	}

}
