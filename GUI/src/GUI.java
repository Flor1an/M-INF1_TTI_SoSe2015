import java.sql.Timestamp;
import java.util.logging.Logger;

import Tiles.CarTile;
import Tiles.JunctionTile;
import Tiles.NoRoadTile;
import Tiles.RoadTile;
import Tiles.TrafficLightTile;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

import haw.common.CarPositions;
import haw.common.MapElement;

public class GUI extends GameGrid {
	Logger log = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = -6268178828310212085L;
	private GigaSpace gigaSpace;
	CarTile car = null;

	public static void main(String[] args) throws InterruptedException {
		GUI g = new GUI();
	}

	public GUI() throws InterruptedException {
		super(15, 10, 50, null, null, false);
		connect();

		drawMap();
		show();
		doRun();
		while (true) {
			Thread.sleep(20);
			drawCars();
			drawTrafficLights();
		}

	}

	public void connect() {
		log.config("Connecting...");
		SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");
		configurer.lookupGroups("gigaspaces-10.1.0-XAPPremium-ga");

		this.gigaSpace = new GigaSpaceConfigurer(configurer).create();
		log.info("connected!");
	}

	/**
	 * draws the actual map on the gui, based on the information that gets read
	 * from the tuple space
	 */
	private void drawMap() {
		log.info("Drawing map...");
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
		log.info("Drawing Map completed!");
	}

	private void drawCars() {

		log.info("Drawing cars...");

		removeActors(new CarTile().getClass());

		MapElement carME = new MapElement();
		carME.setRoad(true);
		carME.setEmpty(false);

		for (MapElement mapElem : gigaSpace.readMultiple(carME)) {
			CarTile ct = new CarTile(mapElem.getCurrentCarId());
			addActor(ct, new Location(mapElem.getX(), mapElem.getY()));
			System.out.println("CAR: " + mapElem.toString());

		}
		log.info("Drawing Cars completed!");

	}
	
	private void drawTrafficLights() {

		log.info("Drawing trafficLight...");

		removeActors(new TrafficLightTile().getClass());

		MapElement carME = new MapElement();
		carME.setRoad(true);
		carME.setJunction(true);

		for (MapElement mapElem : gigaSpace.readMultiple(carME)) {
			TrafficLightTile tlt = new TrafficLightTile(mapElem.isEastAllowed(),mapElem.isSouthAllowed());
			addActor(tlt, new Location(mapElem.getX(), mapElem.getY()));
			System.out.println("TrafficLight: " + mapElem.toString());

		}
		log.info("Drawing TrafficLight completed!");

	}

	/**
	 * responsible for reading changes from the tuple space
	 */
	private void checkForUpdates() {
		System.out.println(new Timestamp(new java.util.Date().getTime()) + " fired");
		try {
			Thread.sleep(1000);
			MapElement template = new MapElement();
			template.hasCar();
			template.setCurrentCarId(0); // TODO: check warum hasCar nicht geht

			MapElement newInfo = gigaSpace.read(template);

			if (newInfo != null) {
				removeActor(car);
				addActor(car, new Location(newInfo.getX(), newInfo.getY()), 0);
				System.out.println(new Location(newInfo.getX(), newInfo.getY()));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			removeAllActors();
			drawMap();
		}
	}
}
