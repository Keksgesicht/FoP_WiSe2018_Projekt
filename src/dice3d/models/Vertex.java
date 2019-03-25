package dice3d.models;

import java.awt.Graphics2D;

import dice3d.main.World;
import dice3d.math.Vector;

public class Vertex {
	
	public Vector position = new Vector();
	public Vector positionOld = new Vector();
	private Vector positionReset = new Vector();

	public Vector a;

	public String id;

	public Vertex(double x, double y, double z) {
		position.set(x, y, z);
		positionOld.set(position);
		positionReset.set(position);
		a = new Vector();
	}

	/**
	 * reset everything to the original position and
	 * make the movement vector unequal to 0 so it's technically moving
	 * and not falsely determined as not moving
	 */
	public void reset() {
		position.set(positionReset);
		positionOld.set(positionReset);
		a = new Vector(1, 1, 1);
	}

	/**
	 * keep the movement as before but add gravity
	 * @param w
	 */
	public void update(World w) {
		a.set(position);
        a.sub(positionOld);
        positionOld.set(position);
        position.add(a);
        position.add(w.gravity);
	}

	public void draw(Graphics2D g) {
		double px = World.projectionDistance * position.x / position.z;
		double py = World.projectionDistance * position.y / position.z;
		if (id == null) {
			int size = 6;
			g.fillOval((int) (px - (size / 2)), (int) (py - (size / 2)), size, size);
		} else {
			g.drawString(id, (int)px, (int)py);
		}
	}
}
