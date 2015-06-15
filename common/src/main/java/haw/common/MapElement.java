package haw.common;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Roxels are the grid components of the map. They have specific
 * types/functionality and may contain a car.
 */
public class MapElement {
	private Integer id;
	private Integer x;
	private Integer y;
	private Integer currentCarId;
	private Boolean empty;
	
	private Boolean road;
	private Boolean junction;
	private Boolean eastAllowed;
	private Boolean southAllowed;
	
	private Integer numberOfCarsWentEast;
	private Integer numberOfCarsWentSouth;

	public MapElement() {
	}

	public MapElement(Integer id, Integer x, Integer y, Boolean road,Boolean junction,Boolean eastAllowed,Boolean southAllowed,Integer numberOfCarsWentEast,Integer numberOfCarsWentSouth) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.road = road;
		this.junction = junction;
		this.eastAllowed = eastAllowed;
		this.southAllowed = southAllowed;
	this.numberOfCarsWentEast=numberOfCarsWentEast;
	this.numberOfCarsWentSouth=numberOfCarsWentSouth;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}



	public Integer getCurrentCarId() {
		return currentCarId;
	}

	public void setCurrentCarId(Integer currentCarId) {
		this.currentCarId = currentCarId;
		this.empty=false;
	}

	public Boolean isRoad() {
		return road;
	}

	public void setRoad(Boolean road) {
		this.road = road;
	}

	public Boolean isJunction() {
		return junction;
	}

	public void setJunction(Boolean junction) {
		this.junction = junction;
	}

	public Boolean isEastAllowed() {
		return eastAllowed;
	}

	public void setEastAllowed(Boolean eastAllowed) {
		this.eastAllowed = eastAllowed;
	}

	public Boolean isSouthAllowed() {
		return southAllowed;
	}

	public void setSouthAllowed(Boolean southAllowed) {
		this.southAllowed = southAllowed;
	}

	public Boolean hasCar() {
		return currentCarId != null;
	}

	public void removeCar() {
		this.currentCarId = null;
		this.empty=true;
	}


	public Boolean getEmpty() {
		return empty;
	}

	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}

	public Integer getNumberOfCarsWentEast() {
		return numberOfCarsWentEast;
	}

	public void setNumberOfCarsWentEast(Integer numberOfCarsWentEast) {
		this.numberOfCarsWentEast = numberOfCarsWentEast;
	}
	
	public void increaseNumberOfCarsWentEast() {
		this.numberOfCarsWentEast++;
	}

	public Integer getNumberOfCarsWentSouth() {
		return numberOfCarsWentSouth;
	}

	public void setNumberOfCarsWentSouth(Integer numberOfCarsWentSouth) {
		this.numberOfCarsWentSouth = numberOfCarsWentSouth;
	}
	
	public void increaseNumberOfCarsWentSouth() {
		this.numberOfCarsWentSouth++;
	}

	@Override
	public String toString() {
		return String.format("MapElement(%s)[%s;%s] Car=%s \t(Road?:%s ; Junction?:%s)  [west?:%s ; south?:%s]", id, x, y, currentCarId, road, junction,eastAllowed,southAllowed);
	}



}
