package haw.processor;

import haw.common.MapElement;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.adapter.SpaceDataEvent;

import java.util.logging.Logger;

/**
 * The processor simulates work done no un-processed Data object. The
 * processData accepts a Data object, simulate work by sleeping, and then sets
 * the processed flag to true and returns the processed Data.
 */
public class Processor {

	Logger log = Logger.getLogger(this.getClass().getName());

	private long workDuration = 1;

	@GigaSpaceContext
	private GigaSpace gigaSpace;

	/**
	 * Sets the simulated work duration (in milliseconds). Default to 100.
	 */
	public void setWorkDuration(long workDuration) {
		this.workDuration = workDuration;
	}

	/**
	 * Process the given Data object and returning the processed Data.
	 *
	 * Can be invoked using OpenSpaces Events when a matching event occurs.
	 */

	@SpaceDataEvent
	public MapElement processData(MapElement current) {
		// sleep to simulate some work
		try {
			Thread.sleep(workDuration);
		} catch (InterruptedException e) {
			// do nothing
		}

		//3 east than 1 south
		if (current.getNumberOfCarsWentEast()>=1){
			current.setSouthAllowed(true);
			current.setNumberOfCarsWentEast(0);
			log.info("Switched Traffic Light on: ["+current.getX()+";"+current.getY() +"] dirction SOUTH (vertical road gets green)");
		}else{
			current.setEastAllowed(true);
			log.info("Switched Traffic Light on: ["+current.getX()+";"+current.getY() +"] direction EAST (horizontal road gets green) ");

		}

		return current;
	}

}
