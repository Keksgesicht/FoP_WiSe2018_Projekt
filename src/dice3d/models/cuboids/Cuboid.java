package dice3d.models.cuboids;
import java.awt.Graphics2D;
import java.util.ArrayList;

import dice3d.main.World;
import dice3d.math.Vector;
import dice3d.models.Edge;
import dice3d.models.Vertex;

public class Cuboid {

	public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();

	public Cuboid(double x, double y, double z, int length, int width, int height) {
		
		Vector corner = new Vector(x, y, z);

		Vertex a = new Vertex(corner.x, corner.y, corner.z); 							//p1 -
		Vertex b = new Vertex(corner.x + length, corner.y, corner.z);					//p2 -
		Vertex c = new Vertex(corner.x + length, corner.y + height, corner.z);			//p3
		Vertex d = new Vertex(corner.x, corner.y + height, corner.z);					//p4 -
		Vertex e = new Vertex(corner.x, corner.y, corner.z + width);					//p5 -
		Vertex f = new Vertex(corner.x + length, corner.y, corner.z + width);			//p6
		Vertex g = new Vertex(corner.x + length, corner.y + height, corner.z + width);	//p7
		Vertex h = new Vertex(corner.x, corner.y + height, corner.z + width);			//p8

		a.id = "A";
		b.id = "B";
		c.id = "C";
		d.id = "D";
		e.id = "E";
		f.id = "F";
		g.id = "G";
		h.id = "H";

		vertices.add(a);
		vertices.add(b);
		vertices.add(c);
		vertices.add(d);

		vertices.add(e);
		vertices.add(f);
		vertices.add(g);
		vertices.add(h);

		Edge edge01 = new Edge(a, b);
		Edge edge02 = new Edge(b, c);
		Edge edge03 = new Edge(c, d);
		Edge edge04 = new Edge(d, a);

		Edge edge05 = new Edge(e, f);
		Edge edge06 = new Edge(f, g);
		Edge edge07 = new Edge(g, h);
		Edge edge08 = new Edge(h, e);

		Edge edge09 = new Edge(a, e);
		Edge edge10 = new Edge(b, f);
		Edge edge11 = new Edge(c, g);
		Edge edge12 = new Edge(d, h);

		Edge edge13 = new Edge(a, g);
		Edge edge14 = new Edge(b, h);
		Edge edge15 = new Edge(c, e);
		Edge edge16 = new Edge(d, f);

		Edge edge17 = new Edge(a, c);
		Edge edge18 = new Edge(b, g);
		Edge edge19 = new Edge(f, h);
		Edge edge20 = new Edge(d, e);
		Edge edge21 = new Edge(d, g);
		Edge edge22 = new Edge(a, f);

		edges.add(edge01);
		edges.add(edge02);
		edges.add(edge03);
		edges.add(edge04);
		edges.add(edge05);
		edges.add(edge06);
		edges.add(edge07);
		edges.add(edge08);
		edges.add(edge09);
		edges.add(edge10);
		edges.add(edge11);
		edges.add(edge12);

		edges.add(edge13);
		edges.add(edge14);
		edges.add(edge15);
		edges.add(edge16);

		edges.add(edge17);
		edges.add(edge18);
		edges.add(edge19);
		edges.add(edge20);
		edges.add(edge21);
		edges.add(edge22);

	}

	/**
	 * update the position with good looking physics
	 * call me every tick
	 * @param w
	 * World to see, so I can feel when toushing someone
	 */
	public void update(World w) {
		for (Vertex v : vertices) v.update(w);
		for (int i = 0; i < 5; i++) {
			for (Edge e : edges) e.update(w);
			for ( Cuboid c2 : w.cuboids ) if ( this != c2 ) updateCollision(w, c2);
		}
	}

	/**
	 * reset all points
	 */
	public void reset() {
		for (Vertex v : vertices) v.reset();
	}

	/**
	 * draw the Cuboid
	 * (we don't actually do anything, as we don't want to see the ground!)
	 * @param g
	 */
	public void draw(Graphics2D g) {
		//g.setColor(Color.GREEN);
		//for (Edge e: edges) e.draw(g);
		//g.setColor(Color.BLACK);
		//for (Vertex point : vertices) point.draw(g);
	}

	/**
	 * decide whether the element is (still) moving based on the
	 * sum of the last absolute position change of each vertex
	 * @return
	 */
	public boolean notMoving() {
		double change = 0;
		for (Vertex v : vertices) {
			change += Math.abs(v.a.x);
			change += Math.abs(v.a.y);
			change += Math.abs(v.a.z);
		}
		if (change < 0.4) return true;
		return false;
	}
	
	/**
	 * @param x
	 * @param b1
	 * @param b2
	 * @return
	 * whether the value x is in between the border b1 and the border b2
	 */
	private boolean inBetween(double x, double b1, double b2) {
		if (b1 <= x && x <= b2) return true;
		if (b2 <= x && x <= b1) return true;
		return false;
	}
	
	/**
	 * calculate whether a given point is in between all the points of
	 * the current instance
	 * @param vertex
	 * @return
	 * true when the vertex lays inside
	 * false when not
	 */
	public boolean isInside(Vertex vertex) {
		Vector p1 = null, p2 = null, p4 = null, p5 = null;
		for (Vertex vert : vertices) {
			switch (vert.id) {
			case "A":
				p1 = vert.position; break;
			case "B":
				p2 = vert.position; break;
			case "D":
				p4 = vert.position; break;
			case "E":
				p5 = vert.position; break;
		}}
		Vector u = new Vector(p1); u.sub(p4); Vector u2 = new Vector(p1); u2.sub(p5); u.crossmult(u2);
		Vector v = new Vector(p1); v.sub(p2); Vector v2 = new Vector(p1); v2.sub(p5); v.crossmult(v2);
		Vector w = new Vector(p1); w.sub(p2); Vector w2 = new Vector(p1); w2.sub(p4); w.crossmult(w2);
		if ( !inBetween(u.dotmult(vertex.position), u.dotmult(p1), u.dotmult(p2))) return false;
		if ( !inBetween(v.dotmult(vertex.position), v.dotmult(p1), v.dotmult(p4))) return false;
		if ( !inBetween(w.dotmult(vertex.position), w.dotmult(p1), w.dotmult(p5))) return false;
		return true;
	}
	
	/**
	 * check whether the object the vertex collided with is the ground and if so
	 * set new position, movement (and the last position) to get a realistic movement
	 * @param vert collided vertex
	 * @param d Cuboid it collided with
	 * @param w world it collided in
	 */
	private void Collision(Vertex vert, Cuboid d, World w) {
		if (d == w.floor) {
			//collision with floor
			double f = .4;
			vert.a.set(vert.position);
			vert.a.sub(vert.positionOld);
			vert.a.scale(f);
	        vert.position.y = 400;
	        vert.positionOld.y = vert.position.y + (int) (vert.a.y * 3);
	        vert.positionOld.x += (vert.position.x - vert.positionOld.x) * f;
	        vert.positionOld.z += (vert.position.z - vert.positionOld.z) * f;
        }
		else {
			//collision with dice
			w.col = true;
//			Vector totalDir1 = new Vector();
//			for (Vertex v : vertices) totalDir1.add(v.a);
//			totalDir1.scale(1/10);
//			for (Vertex v : d.vertices) v.position.add(totalDir1);
//
//			Vector tmp = new Vector(vert.positionOld);
//			tmp.sub(vert.position);
//			tmp.scale(4/6);
//			vert.position.add(tmp);
//			vert.a.scale(0.8);
//			vert.positionOld.add(vert.a);
			
		}
	}
	
	public int getZSum() {
		int sum = 0;
		for (Vertex v : vertices) sum += v.position.z;
		return sum;
	}
	
	/**
	 * check all vertices for collisions with the given cuboid
	 * and triggers the Collision() function to maintain a nice movement
	 * @param w world it collided in
	 * @param d Cuboid to check for collision with
	 */
	public void updateCollision(World w, Cuboid d) {
		//vertex in other cuboid
		for (Vertex vertex : vertices) {
			if (d.isInside(vertex)) {
				Collision(vertex, d, w);
			}
		}
	}
}
