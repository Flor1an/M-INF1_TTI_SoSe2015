package haw.feeder;

import haw.common.MapElement;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.SpaceInterruptedException;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * A feeder bean starts a scheduled task that writes a new Data objects to the
 * space (in an unprocessed state).
 *
 * <p>
 * The space is injected into this bean using OpenSpaces support for @GigaSpaceContext
 * annotation.
 *
 * <p>
 * The scheduling uses the java.util.concurrent Scheduled Executor Service. It
 * is started and stopped based on Spring life cycle events.
 *
 * @author kimchy
 */
public class Feeder implements InitializingBean, DisposableBean {

	Logger log = Logger.getLogger(this.getClass().getName());

	private ScheduledExecutorService executorService;

	private ScheduledFuture<?> sf;

	private long defaultDelay = 10;

	private FeederTask feederTask;

	@GigaSpaceContext
	private GigaSpace gigaSpace;
	
	private int x=0;
	private int y=0;

	/**
	 * Sets the number of types that will be used to set
	 * {@link org.openspaces.example.data.common.Data#setType(Long)}.
	 *
	 * <p>
	 * The type is used as the routing index for partitioned space. This will
	 * affect the distribution of Data objects over a partitioned space.
	 */

	public void setDefaultDelay(long defaultDelay) {
		this.defaultDelay = defaultDelay;
	}

	public void afterPropertiesSet() throws Exception {
		log.info("--- STARTING FEEDER WITH CYCLE [" + defaultDelay + "]");
		executorService = Executors.newScheduledThreadPool(1);
		feederTask = new FeederTask();
		sf = executorService.scheduleAtFixedRate(feederTask, defaultDelay, defaultDelay, TimeUnit.MILLISECONDS);
	}

	public void destroy() throws Exception {
		sf.cancel(false);
		sf = null;
		executorService.shutdown();
	}

	public long getFeedCount() {
		return feederTask.getCounter();
	}

	public class FeederTask implements Runnable {

		private int counter = 0;

		public void run() {
			try {

				MapElement me = new MapElement(counter++, x++, y, true, false, true, false);
				if(counter==1){
					me.setCurrentCarId(0);
				}
				gigaSpace.write(me);
				log.info(">> INITIALIZE " + me + "\n");
				
				if (counter > 10){
					destroy();
					log.info("\n\n\n\n");
				}
				
			} catch (SpaceInterruptedException e) {
				// ignore, we are being shutdown
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public long getCounter() {
			return counter;
		}
	}

}
