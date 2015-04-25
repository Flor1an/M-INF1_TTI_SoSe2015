import org.openspaces.core.GigaSpace;


import GUI.GUI;
import roxel.MapGenerator;
import traffic.Traffic;
import tupleSpace.TupleSpace;

public class Starter {
	// gs-agent.bat
	// gs.bat deploy-space -cluster total_members=1,1 myTrafficGrid

	public static void main(String[] args) {
		try {
			
			TupleSpace ts = new TupleSpace("myTrafficGrid");
			GigaSpace gigaSpace = ts.connect();
			ts.cleanTupleSpace();

			MapGenerator mapGen = new MapGenerator(gigaSpace, 20, 10);
			mapGen.generateMap();

			Traffic traffic = new Traffic(gigaSpace, 3);
			new GUI(gigaSpace, traffic);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
