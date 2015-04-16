package GUI;

import roxel.MapElement;

import com.gigaspaces.annotation.pojo.SpaceId;

public class MapElementUpdate {

	private Integer carId;
	private Integer oldX;
	private Integer oldY;
	private Integer newX;
	private Integer newY;
	private MapElement.Arrow arrow;

	public MapElementUpdate() {
	}

	public MapElementUpdate(Integer carId, Integer oldX, Integer oldY, Integer newX, Integer newY, MapElement.Arrow arrow) {
		this.carId = carId;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.arrow = arrow;
	}

	@SpaceId(autoGenerate = false)
	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Integer getOldX() {
		return oldX;
	}

	public void setOldX(Integer oldX) {
		this.oldX = oldX;
	}

	public Integer getOldY() {
		return oldY;
	}

	public void setOldY(Integer oldY) {
		this.oldY = oldY;
	}

	public Integer getNewX() {
		return newX;
	}

	public void setNewX(Integer newX) {
		this.newX = newX;
	}

	public Integer getNewY() {
		return newY;
	}

	public void setNewY(Integer newY) {
		this.newY = newY;
	}

	public MapElement.Arrow getArrow() {
		return arrow;
	}

	public void setArrow(MapElement.Arrow arrow) {
		this.arrow = arrow;
	}


	@Override
	public String toString() {
		return "MapElementUpdate [carId=" + carId + "    X: " + oldX + "->" + newX + "    Y: " + oldY + "->" + newY + "]";
	}

}
