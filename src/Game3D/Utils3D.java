package Game3D;

/**
 * This class used as 3D utilities helper with math functions.
 * @author Stanislav Bodik  
 */

import java.util.Random;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;

public class Utils3D {

	public static float generateRandomFloat(float min, float max) {
		Random random = new Random();
		return random.nextFloat() * (max - min) + min;
	}

	// written by Sean Phillips, Jason Pollastrini and Jose Pereda
	public static Vec3d unProjectDirection(PerspectiveCamera camera,double sceneX, double sceneY, double sWidth, double sHeight) {
        double tanHFov = Math.tan(Math.toRadians(camera.getFieldOfView()) * 0.5f);
        Vec3d vMouse = new Vec3d(2*sceneX/sWidth-1, 2*sceneY/sWidth-sHeight/sWidth, 1);
        vMouse.x *= tanHFov;
        vMouse.y *= tanHFov;

        Vec3d result = localToSceneDirection(camera,vMouse, new Vec3d());
        result.normalize();
        return result;
    }
    
	// written by Sean Phillips, Jason Pollastrini and Jose Pereda
    public static Vec3d localToScene(PerspectiveCamera camera,Vec3d pt, Vec3d result) {
        Point3D res = camera.localToParentTransformProperty().get().transform(pt.x, pt.y, pt.z);
        if (camera.getParent() != null) {
            res = camera.getParent().localToSceneTransformProperty().get().transform(res);
        }
        result.set(res.getX(), res.getY(), res.getZ());
        return result;
    }
    
	// written by Sean Phillips, Jason Pollastrini and Jose Pereda
    public static Vec3d localToSceneDirection(PerspectiveCamera camera,Vec3d dir, Vec3d result) {
        localToScene(camera,dir, result);
        result.sub(localToScene(camera,new Vec3d(0, 0, 0), new Vec3d()));
        return result;
    }
    
    //checks if point is inside the surface of the rectangle. if threshold > 0 than it is calculated inside of enlarged rectangle
    public static boolean isPointInVerticalRectangle(MyPoint3D point,MyPoint3D rectangle[], double threshold) {
        

    	// when mesh is vertical standing
    	if(rectangle[0].x<=rectangle[1].x && rectangle[0].z>=rectangle[1].z){

            if (    point.x>=rectangle[0].x      - threshold &&
                    point.x<=rectangle[1].x  + threshold &&
                    point.y<=rectangle[0].y  - threshold &&
                    point.y>=rectangle[2].y  + threshold &&
                    point.z<=rectangle[0].z  - threshold &&
                    point.z>=rectangle[1].z  + threshold ){
                return true;
            }
        }
        else if(rectangle[0].x>=rectangle[1].x && rectangle[0].z<=rectangle[1].z){
            if (    point.x<=rectangle[0].x + threshold &&
                    point.x>=rectangle[1].x  - threshold &&
                    point.y<=rectangle[0].y  - threshold &&
                    point.y>=rectangle[2].y  + threshold &&
                    point.z>=rectangle[0].z  + threshold &&
                    point.z<=rectangle[1].z  - threshold){
                return true;
            }
        }
        else if(rectangle[0].x>=rectangle[1].x && rectangle[0].z>=rectangle[1].z){
            if (    point.x<=rectangle[0].x + threshold &&
                    point.x>=rectangle[1].x - threshold &&
                    point.y<=rectangle[0].y - threshold &&
                    point.y>=rectangle[2].y + threshold &&
                    point.z<=rectangle[0].z - threshold &&
                    point.z>=rectangle[1].z + threshold){
                return true;
            }
        }
        else if(rectangle[0].x<=rectangle[1].x && rectangle[0].z<=rectangle[1].z){
            if (    point.x>=rectangle[0].x - threshold&&
                    point.x<=rectangle[1].x + threshold&&
                    point.y<=rectangle[0].y - threshold&&
                    point.y>=rectangle[2].y + threshold&&
                    point.z>=rectangle[0].z + threshold&&
                    point.z<=rectangle[1].z - threshold){
                return true;
            }
        }
        return false;
    }
    
     // given 3 points which (representing surface) and line, returns intersection point of line with surface 3d.
    // p1 = left-bottom, p2 = right-bottom, p3 = left top
    public static MyPoint3D getIntersectionPointSurfaceLine(MyPoint3D lineStart,MyPoint3D lineEnd,MyPoint3D p1,MyPoint3D p2,MyPoint3D p3,float limitDistance){
    	
    	// given 3 points of mesh returns coefficients a,b,c,d of equation  ax+by+cz=d   representing the surface of this mesh
    	float coefficients[] = Utils3D.getMeshEquation(p1,p2,p3);
    	
    	// find intersection of ray vector with mesh surface,  watch this https://www.youtube.com/watch?v=qVvvy5hsQwk
        float t = (coefficients[3]-coefficients[0]*lineStart.x-coefficients[1]*lineStart.y-coefficients[2]*lineStart.z)/(coefficients[0]*(lineEnd.x-lineStart.x)+coefficients[1]*(lineEnd.y-lineStart.y)+coefficients[2]*(lineEnd.z-lineStart.z));
        if(Math.abs(t)>limitDistance)return null;
        
        MyPoint3D intersectionPoint =  new MyPoint3D((lineEnd.x-lineStart.x)*t+lineStart.x,(lineEnd.y-lineStart.y)*t+lineStart.y,(lineEnd.z-lineStart.z)*t+lineStart.z);
        
        // round numbers
        intersectionPoint.x=(float) Math.round(intersectionPoint.x*10000)/10000f;
        intersectionPoint.y=(float) Math.round(intersectionPoint.y*10000)/10000f;
        intersectionPoint.z=(float) Math.round(intersectionPoint.z*10000)/10000f;

        
    	return intersectionPoint;
    }
    
     // given 3 points of mesh returns coefficients a,b,c,d of equation  ax+by+cz=d   representing the surface of this mesh
     // p1 = left-bottom, p2 = right-bottom, p3 = left top
     public static float[] getMeshEquation(MyPoint3D p1,MyPoint3D p2,MyPoint3D p3){
    	
    	//1. converting the 3 points to 2 vectors (subtract v3-v1,v2-v1)
        MyPoint3D v1 = new MyPoint3D(p3.x-p1.x, p3.y-p1.y, p3.z-p1.z);
        MyPoint3D v2 = new MyPoint3D(p2.x-p1.x, p2.y-p1.y, p2.z-p1.z);
        
      //2. find cross product of the vectors which is the normal/perpendicular vector to v1,v2 to the surface .
        float normal[] = {v1.y*v2.z-v1.z*v2.y,v1.z*v2.x-v1.x*v2.z,v1.x*v2.y-v1.y*v2.x};
        
        float d=normal[0]*p1.x+normal[1]*p1.y+normal[2]*p1.z;
        
        return new float[]{normal[0],normal[1],normal[2],d};
    }

     //https://en.wikipedia.org/wiki/Rotation_matrix  Rotation matrix from axis and angle
     public static MyPoint3D[]  applayRotationTranslation(MyPoint3D[] meshLocals,MyPoint3D translationMade,MyPoint3D rotationAxis,double rotatedAngelDegree){
    	 
    	 double rotatedAngelRad = rotatedAngelDegree*Math.PI/180;
    	 
    	 //calculate rotation matrix
    	 MyPoint3D Rx = new MyPoint3D(Math.cos(rotatedAngelRad)+Math.pow(rotationAxis.x, 2)*(1-Math.cos(rotatedAngelRad)),rotationAxis.x*rotationAxis.y*(1-Math.cos(rotatedAngelRad))-rotationAxis.z*Math.sin(rotatedAngelRad),rotationAxis.x*rotationAxis.z*(1-Math.cos(rotatedAngelRad))+rotationAxis.y*Math.sin(rotatedAngelRad));
    	 MyPoint3D Ry = new MyPoint3D(rotationAxis.x*rotationAxis.y*(1-Math.cos(rotatedAngelRad))+rotationAxis.z*Math.sin(rotatedAngelRad),Math.cos(rotatedAngelRad)+Math.pow(rotationAxis.y, 2)*(1-Math.cos(rotatedAngelRad)),rotationAxis.y*rotationAxis.z*(1-Math.cos(rotatedAngelRad))-rotationAxis.x*Math.sin(rotatedAngelRad));
    	 MyPoint3D Rz = new MyPoint3D(rotationAxis.z*rotationAxis.x*(1-Math.cos(rotatedAngelRad))-rotationAxis.y*Math.sin(rotatedAngelRad),rotationAxis.z*rotationAxis.y*(1-Math.cos(rotatedAngelRad))+rotationAxis.x*Math.sin(rotatedAngelRad),Math.cos(rotatedAngelRad)+Math.pow(rotationAxis.z, 2)*(1-Math.cos(rotatedAngelRad)));


    	// multiply locals with the rotations matrix and add to it transformation
    	 MyPoint3D result[] = new MyPoint3D[meshLocals.length];
    	 for(int i=0; i<meshLocals.length; i++){
	    	//rotate
    		 MyPoint3D rotatedPoint = new MyPoint3D(Rx.x*meshLocals[i].x+Rx.y*meshLocals[i].y+Rx.z*meshLocals[i].z,Ry.x*meshLocals[i].x+Ry.y*meshLocals[i].y+Ry.z*meshLocals[i].z,Rz.x*meshLocals[i].x+Rz.y*meshLocals[i].y+Rz.z*meshLocals[i].z);
	    	 //translate
	    	 rotatedPoint.x+=translationMade.x;
	    	 rotatedPoint.y+=translationMade.y;
	    	 rotatedPoint.z+=translationMade.z;
	    	 
	    	 result[i] = rotatedPoint;
    	 }
    	 
    	 return result;
     }
}
