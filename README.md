# TTI_SoSe2015

Technik und Technologie verteilter Informationssysteme (Teil 4)

--
_1.1 to start tuple space:_

    gs-agent.bat

 _1.2 to start partitioned space:_

    gs.bat deploy-space -cluster total_members=4,0 space

***
	
_2.1. to compile code:_	

    mvn compile 	

_2.2. Start feeder (generates the world and cars):_

    mvn os:run -Dmodule=feeder

_2.3. Start Gui (e.g. from eclipse)_

[before carMove, due to some road elements may be missing while they are processes (taken from the space)]

_2.4. Start carMover_

    mvn os:run -Dmodule=carMover

_2.5. Start trafficLight_

    mvn os:run -Dmodule=trafficLight
	
_2.6. Start collectCars (optional) [not yet working]_

    mvn os:run -Dmodule=collectCars
	
	
###### TODO:
- collect all cars within space (reduces amount of serialization)
- cluster road
- improve startup procedure

