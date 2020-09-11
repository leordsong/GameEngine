package core;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Transform extends Component {

	private Vector2 position;
	private Vector2 size;
	private float depth;
	
	public Transform() {
		position = new Vector2(0,0);
		size = new Vector2(20,20);
		depth = 0;
	}

	@Nonnull
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = Optional.ofNullable(position).orElseThrow(IllegalArgumentException::new);
	}

	public void updatePosition(float x, float y) {
		position = new Vector2(position.getX() + x, position.getY() + y);
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	@Nonnull
	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = Optional.ofNullable(size).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public String toString() {
		return position + "@" + depth;
	}

}
