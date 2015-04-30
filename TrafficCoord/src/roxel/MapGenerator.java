package roxel;

import java.util.Random;

import logger.Logger;

import org.openspaces.core.GigaSpace;

public class MapGenerator {

	private final int IMAGE_SIZE = 30;
	private final GigaSpace gigaSpace;
	private final int mapSizeVertical;
	private final int mapSizeHorizontal;
	private final Logger log = new Logger();

	/**
	 * responsible for the representation of the environment within the tuple
	 * space.
	 * 
	 * @param gigaSpace
	 *            Initialised tuple space.
	 * @param mapSizeHorizontal
	 *            amount of horizontal boxes that represents the environment.
	 * @param mapSizeVertical
	 *            amount of vertical boxes that represents the environment.
	 * 
	 */
	public MapGenerator(GigaSpace gigaSpace, int mapSizeHorizontal, int mapSizeVertical) {
		this.gigaSpace = gigaSpace;
		this.mapSizeHorizontal = mapSizeHorizontal;
		this.mapSizeVertical = mapSizeVertical;
	}

	/**
	 * 1. generates an empty map structure and stores it in the tuple space.<br />
	 * 2. generates the actual map based on information from the tuple space.
	 * 
	 * @throws Exception
	 *             if an map structure is already existing in the tuple space.
	 */
	public void generateMap() throws Exception {
		generateEmptyMap();
		generateMapElements();
	}

	/**
	 * generates an empty map structure and stores it in the specified tuple
	 * space.
	 * 
	 * @throws Exception
	 * 
	 */
	private void generateEmptyMap() throws Exception {
		MapDimension plainMapDimension = new MapDimension();
		MapDimension mapDimension = gigaSpace.read(plainMapDimension);

		if (mapDimension == null) {

			mapDimension = new MapDimension(1, mapSizeHorizontal, mapSizeVertical, 5, 10, IMAGE_SIZE);
			gigaSpace.write(mapDimension);
			log.write(mapDimension.toString());
		} else {
			throw new Exception("Map allready existing in the tuple space!");
		}
	}

	/**
	 * generates the actual map (roads, junctions, ...) based on the layout from
	 * the tuple space.
	 * 
	 */
	private void generateMapElements() {
		log.log("Generating new map...");
		MapDimension plainMapDimension = new MapDimension();
		MapDimension mapDimension = gigaSpace.read(plainMapDimension);
		int rows = mapDimension.getWidth();
		int columns = mapDimension.getHeight();

		int[][] matrix = new int[rows][columns];

		int id = 0;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {

				matrix[x][y] = id;

				MapElement mapElem;
				if (y % mapDimension.getHorizontalRoadFrequency() == 1) {
					if (x % mapDimension.getVerticalRoadFrequency() == 2) {
						// junction
						if (new Random().nextInt(2) == 0) {
							// junction direction south
							mapElem = new MapElement(id, x, y, true, true, true, false);
						} else {
							// junction direction east
							mapElem = new MapElement(id, x, y, true, true, false, true);
						}

					} else {
						// straight road direction east
						mapElem = new MapElement(id, x, y, true, false, true, false);
					}
				} else {
					if (x % mapDimension.getVerticalRoadFrequency() == 2) {
						// straight road direction south
						mapElem = new MapElement(id, x, y, true, false, false, true);
					} else {
						// no road (green land)
						mapElem = new MapElement(id, x, y, false, false, false, false);
					}

				}
				gigaSpace.write(mapElem);
				log.write(mapElem.toString());
				id++;
			}
		}

		log.log("new map generated (with " + gigaSpace.count(new MapElement()) + " roxel's) [" + columns + "x" + rows + "]!");
		log.logLine();
	}
}
