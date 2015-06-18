package haw.processor;

import haw.common.CarPositions;
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

	private long workDuration = 2000;

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
	public CarPositions processData(CarPositions current) {
		log.info(" CURRENT: : " + current.toString() + "\n");
		// sleep to simulate some work
		try {
			Thread.sleep(workDuration);
		} catch (InterruptedException e) {
			// do nothing
		}

		MapElement template = new MapElement();
		template.hasCar();

		MapElement[] cars = gigaSpace.readMultiple(template);

		for (MapElement me : cars) {
			if (me != null) {
				if (me.getCurrentCarId() != null) {
					log.info("ADDing: " + me.toString() + "\nMore exact: \tCar: " + me.getCurrentCarId() + "\t x: " + me.getX() + "\t y:" + me.getY());
					current.addToList(me.getCurrentCarId(), me.getX(), me.getY());
				}
			}
		}

		current.setNote(current.getNote() + ".");

		log.info(" Cars are collected! Content is: : " + current.toString() + "\n\n\n\n");
		return current;
	}

}
