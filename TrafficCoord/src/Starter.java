import org.openspaces.core.GigaSpace;

import GUI.GUI;
import roxel.MapGenerator;
import traffic.Traffic;
import tupleSpace.TupleSpace;

public class Starter {
	// gs-agent.bat
	// gs.bat deploy-space -cluster total_members=1,0 myTrafficGrid

	static int amoutOfCars = 20;

	public static void main(String[] args) {
		try {

			TupleSpace ts = new TupleSpace("myTrafficGrid");
			GigaSpace gigaSpace = ts.connect();
			ts.cleanTupleSpace();

			MapGenerator mapGen = new MapGenerator(gigaSpace, 20, 10);

			mapGen.generateMap();
			Thread.sleep(1000);

			new Traffic(gigaSpace, amoutOfCars);

			for (int i = 0; i < amoutOfCars; i++) {
				new _startWorker(gigaSpace, i).start();
			}
			new GUI(gigaSpace);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
