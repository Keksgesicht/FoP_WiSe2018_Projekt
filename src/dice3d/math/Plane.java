package dice3d.math;

public class Plane {
	
	private Vector stuetzvektor = new Vector(); 
	private Vector Richtung1 = new Vector();
	private Vector Richtung2 = new Vector();
	private Vector Normalenvektor = new Vector();
	
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

	public Vector getStuetzVektor() {
		return stuetzvektor;
	}
	
	public Vector getRichtungsVektor1() {
		return Richtung1;
	}
	
	public Vector getRichtungsVektor2() {
		return Richtung2;
	}
	
	public Vector getNormalenvektor() {
		return Normalenvektor;
	}
	
}
