package roxel;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Describes the map dimensions
 */
public class MapDimension {

	private Integer id;
	private Integer width;
	private Integer height;
	private Integer horizontalRoadFrequency;
	private Integer verticalRoadFrequency;
	private Integer imageSize;

	public MapDimension() {
	}

	/**
	 * represents the dimension of the map
	 * 
	 * @param id
	 * @param width
	 *            represents the horizontal size of the map
	 * @param height
	 *            represents the vertical size of the map
	 * @param horizontalRoadFrequency
	 *            the frequency of horizontal roads on the map
	 * @param verticalRoadFrequency
	 *            the frequency of vertical roads on the map
	 * @param imageSize
	 *            size in px of the tiles
	 */
	public MapDimension(Integer id, Integer width, Integer height, Integer horizontalRoadFrequency, Integer verticalRoadFrequency, Integer imageSize) {
		this.id = id;
		this.height = height;
		this.width = width;
		this.horizontalRoadFrequency = horizontalRoadFrequency;
		this.verticalRoadFrequency = verticalRoadFrequency;
		this.imageSize = imageSize;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void raiseId() {
		this.id++;
	}

	@Override
	public String toString() {
		return String.format("MapDimension(Dimension(%s, %s), Size(%s))", width, height, imageSize);
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer newWidth) {
		this.width = newWidth;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer newHeight) {
		this.height = newHeight;
	}

	public Integer getHorizontalRoadFrequency() {
		return horizontalRoadFrequency;
	}

	public void setHorizontalRoadFrequency(Integer horizontalRoadFrequency) {
		this.horizontalRoadFrequency = horizontalRoadFrequency;
	}

	public Integer getVerticalRoadFrequency() {
		return verticalRoadFrequency;
	}

	public void setVerticalRoadFrequency(Integer verticalRoadFrequency) {
		this.verticalRoadFrequency = verticalRoadFrequency;
	}

	public Integer getImageSize() {
		return imageSize;
	}

	public void setImageSize(Integer newImageSize) {
		this.imageSize = newImageSize;
	}

}
