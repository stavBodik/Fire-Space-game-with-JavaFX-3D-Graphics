package Game3D;

/**
 * This class represents 3D point, includes math functions utilities with points . 
 * @author Sean Phillips, Jason Pollastrini and Jose Pereda 
 * Edit by Stanislav Bodik
 */

import java.util.stream.DoubleStream;

/**
 *
 * @author Sean
 * @Description Just a useful data structure for X,Y,Z triplets.

 */
public class MyPoint3D {
    
    public float x = 0;
    public float y = 0;
    public float z = 0;

    public float f = 0; // for function evaluation
    /* 
    * @param X,Y,Z are all floats to align with TriangleMesh needs 
    */
    public MyPoint3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public MyPoint3D(double x, double y, double z) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }
    
    public MyPoint3D(float x, float y, float z, float f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;
    }
    
    public DoubleStream getCoordinates() { return DoubleStream.of(x,y,z); }
    
    public DoubleStream getCoordinates(float factor) { return DoubleStream.of(factor*x,factor*y,factor*z); }
    
    public MyPoint3D add(MyPoint3D point) {
        return add(point.x, point.y, point.z);
    }
    
    public MyPoint3D add(float x, float y, float z) {
        return new MyPoint3D(this.x + x, this.y + y, this.z + z);
    }
    
    public MyPoint3D substract(MyPoint3D point) {
        return substract(point.x, point.y, point.z);
    }
    
    public MyPoint3D substract(float x, float y, float z) {
        return new MyPoint3D(this.x - x, this.y - y, this.z - z);
    }
    
    public MyPoint3D multiply(float factor) {
        return new MyPoint3D(this.x * factor, this.y * factor, this.z * factor);
    }
    
    public MyPoint3D normalize() {
        final float mag = magnitude();

        if (mag == 0.0) {
            return new MyPoint3D(0f, 0f, 0f);
        }

        return new MyPoint3D(x / mag, y / mag, z / mag);
    }
    
    public float magnitude() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float distance(MyPoint3D otherPoint){
    	return (float) Math.sqrt(Math.pow(otherPoint.x-x, 2) + Math.pow(otherPoint.y-y, 2) + Math.pow(otherPoint.z-z, 2));
    }
    
    public float dotProduct(MyPoint3D point) {
        return dotProduct(point.x, point.y, point.z);
    }
    
    public float dotProduct(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }
    
    public MyPoint3D crossProduct(MyPoint3D point) {
        return crossProduct(point.x, point.y, point.z);
    }
    
    public MyPoint3D crossProduct(float x, float y, float z) {
        return new MyPoint3D(-this.z * y + this.y * z, 
                            this.z * x - this.x * z, 
                           -this.y * x + this.x * y);
    }
    
    @Override
    public String toString() {
        return "Point3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Float.floatToIntBits(this.x);
        hash = 29 * hash + Float.floatToIntBits(this.y);
        hash = 29 * hash + Float.floatToIntBits(this.z);
        hash = 29 * hash + Float.floatToIntBits(this.f);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyPoint3D other = (MyPoint3D) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.f) != Float.floatToIntBits(other.f)) {
            return false;
        }
        return true;
    }

    public String toCSV() {
        return "" + x + ";" + y + ";" + z + ";"+f;
    }
    
    
}
