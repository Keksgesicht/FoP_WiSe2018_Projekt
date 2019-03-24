package dice3d.math;

public class line {
	private Vector point = new Vector();
	private Vector direction = new Vector();
	
	public line(Vector v1, Vector v2) {
		point.set(v1);
		direction.set(v2);
		direction.sub(v1);
	}

	public line(Vector v1, Vector v2, boolean b) {
		point.set(v1);
		direction.set(v2);
	}
	
	/**
	 * Determines the point of intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
	 */
	public Vector intersects(plane p) {
		Vector planePoint = new Vector(p.getStuetzVektor());
		Vector planeNormal = new Vector(p.getNormalenvektor());
		if (planeNormal.dotmult(direction) == 0) return null;
	    double t = (planeNormal.dotmult(planePoint) - planeNormal.dotmult(point)) / planeNormal.dotmult(direction);
	    if (t > 1) return null;
	    Vector dirCpy = new Vector(direction);
	    dirCpy.scale(t);
	    Vector pnyCpy = new Vector(point);
	    pnyCpy.add(dirCpy);
	    return pnyCpy;
	}
}
