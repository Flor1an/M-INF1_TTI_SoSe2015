package updates;

import com.gigaspaces.annotation.pojo.SpaceId;

public class NotifyGuiAboutTrafficLightChange {
private Integer id;	
	private Integer x;
	private Integer y;
	private Boolean toWest;
	private Boolean toSouth;


	public NotifyGuiAboutTrafficLightChange() {
	}

	public NotifyGuiAboutTrafficLightChange(Integer x, Integer y, Boolean toWest, Boolean toSouth) {

		this.id=Integer.parseInt(x+""+y);
		this.x = x;
		this.y = y;
		this.toSouth=toSouth;
		this.toWest=toWest;

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

	public Boolean getToWest() {
		return toWest;
	}

	public void setToWest(Boolean toWest) {
		this.toWest = toWest;
	}

	public Boolean getToSouth() {
		return toSouth;
	}

	public void setToSouth(Boolean toSouth) {
		this.toSouth = toSouth;
	}

	@Override
	public String toString() {
		return "UpdateForTrafficLight [id=" + id + ", x=" + x + ", y=" + y + ", toWest=" + toWest + ", toSouth=" + toSouth + "]";
	}

	

}
