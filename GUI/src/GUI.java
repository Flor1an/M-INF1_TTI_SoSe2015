import haw.common.MapElement;
import haw.feeder.Feeder.FeederTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

import Tiles.CarTile;
import Tiles.JunctionTile;
import Tiles.NoRoadTile;
import Tiles.RoadTile;
import Tiles.TrafficLightTile;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class GUI extends GameGrid {
	Logger log = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = -6268178828310212085L;
	private GigaSpace gigaSpace;
	HashMap<Integer, CarTile> cars = null;

	public static void main(String[] args) throws InterruptedException {
		new GUI();
	}

	public GUI() throws InterruptedException {
		super(FeederTask.WIDTH , FeederTask.HEIGHT, 50, null, null, false);
		connect();
		cars = new HashMap<Integer, CarTile>();

		drawMap();
		show();
		doRun();
		while (true) {
			Thread.sleep(400);
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

		try {

			// gets all cars that are currently drawn (in order to move them)
			for (Actor actor : getActors(new CarTile().getClass())) {
				cars.put(((CarTile) actor).getCarId(), (CarTile) actor);
			}

			MapElement carME = new MapElement();
			carME.setRoad(true);
			carME.setEmpty(false);

			for (MapElement mapElem : gigaSpace.readMultiple(carME)) {

				// Initial setup (or if cars get added)
				if (!cars.containsKey(mapElem.getCurrentCarId())) {
					CarTile ct = new CarTile(mapElem.getCurrentCarId());
					cars.put(mapElem.getCurrentCarId(), ct);
					addActor(ct, new Location(mapElem.getX(), mapElem.getY()));
				}

				CarTile foundCar = cars.get(mapElem.getCurrentCarId());
				foundCar.setLocation(new Location(mapElem.getX(), mapElem.getY()));

				// Orientation if heading east or south
				if (mapElem.isEastAllowed() && !mapElem.isSouthAllowed()) {
					foundCar.setDirection(0);
				} else if (!mapElem.isEastAllowed() && mapElem.isSouthAllowed()) {
					foundCar.setDirection(90);
				}
			}

		} catch (Exception e) {
			log.warning("DRWAING CARS: " + e.getMessage());
		}

	}

	private void drawTrafficLights() {
		try {

			
			
			

			MapElement carME = new MapElement();
			carME.setRoad(true);
			carME.setJunction(true);

			for (MapElement mapElem : gigaSpace.readMultiple(carME)) {
				ArrayList<Actor> tl = getActorsAt(new Location(mapElem.getX(), mapElem.getY()), new TrafficLightTile().getClass());

				addActor(new TrafficLightTile(mapElem.isEastAllowed(),  mapElem.isSouthAllowed()), new Location(mapElem.getX(), mapElem.getY()));
				tl.removeAll(tl);
				
				
				
			}
		} catch (Exception e) {
			log.warning("TRAFFIC LIGHTS: " + e.getMessage());
		}

	}

}
