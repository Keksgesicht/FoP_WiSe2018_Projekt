package dice3d.math;

public class Vector {

    public double x;
    public double y;
    public double z;

    public Vector() {
    	this.x = 0;
    	this.y = 0;
    	this.z = 0;
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
    
    /**
     * crossmultiplicate the instance with the given vector and save the result in this instance
     * @param v
     */
    public void crossmult(Vector v) {
    	double xTemp = this.x;
    	double yTemp = this.y;
    	double zTemp = this.z;
    	this.x = yTemp * v.z - zTemp * v.y;
    	this.y = zTemp * v.x - xTemp * v.z;
    	this.z = xTemp * v.y - yTemp * v.x;
    }
    
    /**
     * calculate the scalar with the given vector v and return it
     * @param v
     * @return
     */
    public double dotmult(Vector v) {
    	double result = x * v.x + y * v.y + z * v.z;
    	return result;
    }

    public void normalize() {
        scale(1 / getSize());
    }

    @Override
    public String toString() {
        return "[" + ((double)Math.round(x * 100))/100 + ", " + ((double)Math.round(y * 100))/100 + ", " + ((double)Math.round(z * 100))/100 + "]";
    }
    
    public boolean equals(Vector v) {
    	return v.x == x && v.y == y && v.z == z;
    }
    
}
