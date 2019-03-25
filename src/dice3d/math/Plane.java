package dice3d.math;

public class Plane {
	
	private Vector stuetzvektor = new Vector(); 
	private Vector Richtung1 = new Vector();
	private Vector Richtung2 = new Vector();
	private Vector Normalenvektor = new Vector();
	
	/**
	 * construct a new Plane with 3 Vectors in the plane
	 * save with one point vector, 2 direction vectors and a normalized normal vector
	 * @param v1
	 * @param v2
	 * @param v3
	 */
	public Plane(Vector v1, Vector v2, Vector v3) {		
		stuetzvektor.set(v1);
		Richtung1.set(v2);
		Richtung1.sub(v1);
		Richtung2.set(v3);
		Richtung2.sub(v1);
		Normalenvektor.set(Richtung1);
		Normalenvektor.crossmult(Richtung2);
		Normalenvektor.normalize();
	}

	/**
	 * @return
	 * the point vector
	 */
	public Vector getStuetzVektor() {
		return stuetzvektor;
	}
	
	/**
	 * @return
	 * the one direction vector
	 */
	public Vector getRichtungsVektor1() {
		return Richtung1;
	}
	
	/**
	 * @return
	 * the other direction vector
	 */
	public Vector getRichtungsVektor2() {
		return Richtung2;
	}
	
	/**
	 * @return
	 * the normalized normal vector
	 */
	public Vector getNormalenvektor() {
		return Normalenvektor;
	}
	
}
