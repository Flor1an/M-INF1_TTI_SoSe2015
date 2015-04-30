package updates;

import com.gigaspaces.annotation.pojo.SpaceId;

public class NotifyGuiAboutCarMovement {

	private Integer carId;
	
	private Integer oldX;
	private Integer oldY;
	
	private Integer newX;
	private Integer newY;
	
	private Boolean east;
	private Boolean south;
	

	public NotifyGuiAboutCarMovement() {
	}

	public NotifyGuiAboutCarMovement(Integer carId, Integer oldX, Integer oldY, Integer newX, Integer newY,Boolean east,Boolean south) {
		this.carId = carId;
		
		this.oldX = oldX;
		this.oldY = oldY;
		
		this.newX = newX;
		this.newY = newY;
		
		this.east=east;
		this.south=south;
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

	public Boolean getEast() {
		return east;
	}

	public void setEast(Boolean east) {
		this.east = east;
	}

	public Boolean getSouth() {
		return south;
	}

	public void setSouth(Boolean south) {
		this.south = south;
	}

	@Override
	public String toString() {
		return "MapElementUpdate [carId=" + carId + "    X: " + oldX + "->" + newX + "    Y: " + oldY + "->" + newY + "]";
	}

}
