package dice3d.math;

public class Vector {

    public double x;
    public double y;
    public double z;

    public Vector() {
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public void sub(Vector v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public void scale(double s) {
        scale(s, s, s);
    }
    
    public void scale(double sx, double sy, double sz) {
        this.x *= sx;
        this.y *= sy;
        this.z *= sz;
    }
    
    public double getSize() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public void crossmult(Vector v) {
    	double xTemp = x;
    	double yTemp = y;
    	double zTemp = z;
    	x = yTemp * v.z - zTemp * v.y;
    	y = zTemp * v.x - xTemp * v.z;
    	z = xTemp * v.y - yTemp * v.x;
    }
    
    public double dotmult(Vector v) {
    	double result = x * v.x + y * v.y + z * v.z;
    	return result;
    }

    public void normalize() {
        scale(1 / getSize());
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
    
    public boolean equals(Vector v) {
    	return v.x == x && v.y == y && v.z == z;
    }
    
}
