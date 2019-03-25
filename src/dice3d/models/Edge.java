package dice3d.models;

import java.awt.Graphics2D;

import dice3d.main.World;
import dice3d.math.Vector;

public class Edge {

	private Vertex a;
	private Vertex b;
	private double designatedLength;

	public Edge(Vertex a, Vertex b) {
		this.a = a;
		this.b = b;
		Vector length = new Vector(a.position);
		length.sub(b.position);
		designatedLength = length.getSize();
	}

	/**
	 * fix the length of an edge to be keep it's size
	 * just push both sides out/in the needed amount
	 * @param w
	 */
	public void update(World w) {
		Vector vTmp = new Vector();
		vTmp.set(b.position);
        vTmp.sub(a.position);
        double currentSize = vTmp.getSize();
        double dif = (currentSize - designatedLength) * 0.5;
        vTmp.normalize();
        vTmp.scale(dif);
        a.position.add(vTmp);
        b.position.sub(vTmp);
	}

	public void draw(Graphics2D g) {
		double ax = World.projectionDistance * a.position.x / a.position.z;
		double ay = World.projectionDistance * a.position.y / a.position.z;
		double bx = World.projectionDistance * b.position.x / b.position.z;
		double by = World.projectionDistance * b.position.y / b.position.z;

		g.drawLine((int) ax, (int) ay, (int) bx, (int) by);
	}

}
