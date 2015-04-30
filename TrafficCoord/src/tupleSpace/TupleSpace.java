package tupleSpace;

import logger.Logger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

import roxel.MapDimension;
import roxel.MapElement;
import updates.NotifyGuiAboutCarMovement;
import updates.NotifyGuiAboutTrafficLightChange;

public class TupleSpace {
	private final String GIGASPACE_VERSION = "gigaspaces-10.1.0-XAPPremium-ga";
	private final Logger log = new Logger();
	private final String spaceName;
	private GigaSpace gigaSpace;

	public TupleSpace(String spaceName) {
		this.spaceName = spaceName;
	}

	/**
	 * starts the accusal connecting process (~10 seconds)
	 * 
	 * @return the connected gigaspace object
	 */
	public GigaSpace connect() {
		log.log("Connecting to data grid...");

		SpaceProxyConfigurer configurer = new SpaceProxyConfigurer(spaceName);
		configurer.lookupGroups(GIGASPACE_VERSION);

		gigaSpace = new GigaSpaceConfigurer(configurer).create();
		log.log("Connected!");
		log.logLine();

		return gigaSpace;
	}

	/**
	 * removes all existing "MapElements()", "MapDimension()" and
	 * "MapElementUpdate()" from the space
	 */
	public void cleanTupleSpace() {
		log.log("Cleaning Space...");
		gigaSpace.takeMultiple(new MapElement());
		gigaSpace.takeMultiple(new MapDimension());
		gigaSpace.takeMultiple(new NotifyGuiAboutCarMovement());
		gigaSpace.takeMultiple(new NotifyGuiAboutTrafficLightChange());
		log.log("Space Cleaned!");
		log.logLine();
	}
}
