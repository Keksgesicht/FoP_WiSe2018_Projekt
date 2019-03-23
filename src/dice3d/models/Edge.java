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

	public void update(World w) {
		Vector vTmp = new Vector();
		vTmp.set(b.position);
		vTmp.sub(a.position);
		double currentSize = vTmp.getSize();
		double dif = (currentSize - designatedLength) * 0.5;
		vTmp.normalize();
		vTmp.scale(dif);
		Vector vTmp2 = new Vector(vTmp);
		vTmp2.scale(2);
		if (!(a.isPinned ^ b.isPinned)) {
			a.position.add(vTmp);
			b.position.sub(vTmp);
			return;
		}
		if (a.isPinned) {
			b.positionOld.set(b.position);
			b.position.sub(vTmp2);
		}
		if (b.isPinned) {
			a.positionOld.set(a.position);
			a.position.add(vTmp2);
		}
	}

	public void draw(Graphics2D g) {
		double ax = World.projectionDistance * a.position.x / a.position.z;
		double ay = World.projectionDistance * a.position.y / a.position.z;
		double bx = World.projectionDistance * b.position.x / b.position.z;
		double by = World.projectionDistance * b.position.y / b.position.z;

		g.drawLine((int) ax, (int) ay, (int) bx, (int) by);
	}

}
