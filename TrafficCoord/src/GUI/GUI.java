package GUI;

import java.awt.Color;
import java.util.ArrayList;

import logger.Logger;

import org.openspaces.core.GigaSpace;

import roxel.MapElement;
import roxel.MapDimension;
import traffic.Traffic;
import ch.aplu.jgamegrid.Actor;

import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GUI extends GameGrid {


	private static final long serialVersionUID = -6268178828310212085L;
	private final GigaSpace gigaSpace;
	private final Traffic traffic;
	Logger log = new Logger();

	public GUI(GigaSpace gigaSpace, Traffic traffic) {
		super(gigaSpace.read(new MapDimension()).getWidth(), gigaSpace.read(new MapDimension()).getHeight(), gigaSpace.read(new MapDimension()).getImageSize(), Color.red);
		log.read(gigaSpace.read(new MapDimension()).toString());
		log.logLine();

		this.gigaSpace = gigaSpace;
		this.traffic = traffic;

		drawMap();
		show();

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
			if (mapElem.getArrow().isRoad()) {
				tile = new RoadTile(mapElem.getArrow());

			} else {
				tile = new NoRoadTile();
			}
			addActor(tile, new Location(mapElem.getX(), mapElem.getY()));
		}
	}

	@Override
	/**
	 * get called from the gui buttons (single move with the "step" function, continuous moves with the "run" button) 
	 */
	public void act() {
		traffic.moveCars();
	}

	/**
	 * responsible for reading changes from the tuple space
	 */
	private void checkForUpdates() {
		MapElementUpdate[] taken = gigaSpace.takeMultiple(new MapElementUpdate());
		if (taken != null && taken.length > 0) {
			//log.log("perform " + taken.length + " GUI update");
			for (MapElementUpdate mapElementUpdate : taken) {
				performUpdate(mapElementUpdate);
			}
		}
	}

	/**
	 * does the actual update (move and turn of the actual car)
	 * 
	 * @param update
	 */
	public void performUpdate(MapElementUpdate update) {
		Integer oldX = update.getOldX();
		Integer oldY = update.getOldY();

		Integer newX = update.getNewX();
		Integer newY = update.getNewY();
		
		Integer carId = update.getCarId();
		MapElement.Arrow arrow = update.getArrow();
		
		//Allows to change the orientation of the car (in order to look like driving "forward")
		int degreesToTurn = -1;
		if (arrow.isStraight()) {
			switch (arrow) {
			case WEST:
				degreesToTurn = 0;
				break;
			case SOUTH:
				degreesToTurn = 90;
				break;

			default:
				break;
			}
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
