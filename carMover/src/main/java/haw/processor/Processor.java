package haw.processor;

import haw.common.MapElement;
import haw.feeder.Feeder.FeederTask;

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

	private long workDuration = 100;

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

		int currentXPosition = current.getX();
		int currentYPosition = current.getY();

		int successorXPosition = currentXPosition;
		int successorYPosition = currentYPosition;

		// calculate successor position
		if (current.isEastAllowed()) {
			int incrementedXPosition = currentXPosition + 1;
			successorXPosition = incrementedXPosition > FeederTask.WIDTH - 1 ? 0 : incrementedXPosition;
		} else if (current.isSouthAllowed()) {
			int incrementedYPosition = currentYPosition + 1;
			successorYPosition = incrementedYPosition > FeederTask.HEIGHT - 1 ? 0 : incrementedYPosition;
		}

		// Calculate Successor
		MapElement successorTemplate = new MapElement();
		successorTemplate.setX(successorXPosition);
		successorTemplate.setY(successorYPosition);

		// Get Successor
		MapElement successor = gigaSpace.read(successorTemplate);

		if (successor == null) {
			log.warning("cannot get successor of: " + current + " \n");
			return current;
		}

		if ((current.isSouthAllowed() == successor.isSouthAllowed()) || (current.isEastAllowed() == successor.isEastAllowed())) {
			if (!successor.hasCar()) {
				// Debug
				MapElement tmp = successor;
				successor = gigaSpace.take(successor);
				if (successor == null) {
					// Debug
					log.warning("CANNOT TAKE: \t" + tmp);
					return current;
				}
				if (!(successor.isEastAllowed()||successor.isSouthAllowed()))
				{
					//error handling: junction is gone
					gigaSpace.write(successor);
					return current;
				}
				// Set car to successor
				successor.setCurrentCarId(current.getCurrentCarId());
				gigaSpace.write(successor);

				// remove car from current position
				current.removeCar();
				
				//counting how many cars passed the element
				if(current.isEastAllowed()){
					current.increaseNumberOfCarsWentEast();
				}
				if(current.isSouthAllowed()){
					current.increaseNumberOfCarsWentSouth();
				}

				if (current.isJunction()) {
					// removes the directions, so that the traffic light can act
					// (it looks for an junction without direction)
					current.setSouthAllowed(false);
					current.setEastAllowed(false);
				}
				log.info("Car " + successor.getCurrentCarId() + " sucessfull moved from: [" + current.getX()+";"+current.getY() + "] -> [" + successor.getX()+";"+ successor.getY()+ "]\n");
				return current;
			}
		}

		log.warning("Car: " + current.getCurrentCarId() + " NOT moved. Restored information and put back in space.\n\tCar sits on: "+current+"\n");
		return current;
	}

}
