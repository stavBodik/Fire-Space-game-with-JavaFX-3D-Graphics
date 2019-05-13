package Game3D;

import GameApplication.Constants;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

/**
 * This class represent asteroid in 3D world,
 * Stores asteroid object mesh views and information about flying asteroid.
 * @author Stanislav Bodik
 */

public class Asteroid {

	private Group asteroidMeshGroup ;
	private Group asteroidBoundingBoxMeshGroup = new Group();

	private final float objScaleFactor=0.05f;
	private final float objOffsetFactorX=4;
	private final float objOffsetFactorY=-0.7f;
	private float speed=-0.7f;
	
	private MyPoint3D currentPosition,startPosition,targetPosition;
	private MyBoundingBox asteroidBoundingBox;
	private Rotate asteroidRotation;
	private float distanceFromTarged;
	
	
 	public Asteroid(Group asteroidMeshViewGroup,MyPoint3D startPosition,MyPoint3D targetPosition,float size,float speed) {
		
 		this.asteroidMeshGroup=asteroidMeshViewGroup;
		setSize(new MyPoint3D(size, size, size));
		this.speed=speed;
		this.startPosition=startPosition;
		this.targetPosition=targetPosition;
		asteroidBoundingBox = new MyBoundingBox(asteroidMeshGroup.getBoundsInParent().getMinX(), asteroidMeshGroup.getBoundsInParent().getMinY(), asteroidMeshGroup.getBoundsInParent().getMinZ(), asteroidMeshGroup.getBoundsInParent().getWidth(), asteroidMeshGroup.getBoundsInParent().getHeight(), asteroidMeshGroup.getBoundsInParent().getDepth());
		setPosition(startPosition);
	}
	
	public void setRotation(Rotate rotation){
		asteroidMeshGroup.getTransforms().setAll(rotation);
		asteroidRotation = rotation;
	}

	public void drawBoundingBox(){
		for(int i=0; i<asteroidBoundingBox.getBoundingBoxMeshes().length; i++){
			MeshView v  = new MeshView(asteroidBoundingBox.getBoundingBoxMeshes()[i]);
	 	 	PhongMaterial phongMaterial = new PhongMaterial();
	 	 	phongMaterial.setSpecularColor(Color.WHITE);
	 	 	phongMaterial.setDiffuseColor(Color.RED);
			v.setMaterial(phongMaterial);
			v.setRotate(asteroidMeshGroup.getRotate());
			asteroidBoundingBoxMeshGroup.getChildren().add(v);
		}
	}
	
	public Group getAsteroidBoundingBoxMeshGroup() {
		return asteroidBoundingBoxMeshGroup;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public MyPoint3D getStartPosition() {
		return startPosition;
	}
	
	public MyPoint3D getTargetPosition() {
		return targetPosition;
	}
	
	public void setDistanceFromTarged(float distanceFromTarged) {
		this.distanceFromTarged = distanceFromTarged;
	}
	
	public float getDistanceFromTarged() {
		return distanceFromTarged;
	}
	
	private void setSize(MyPoint3D size){
		asteroidMeshGroup.setScaleX(size.x*objScaleFactor);
		asteroidMeshGroup.setScaleY(size.y*objScaleFactor);
		asteroidMeshGroup.setScaleZ(size.z*objScaleFactor);
	}
	
	public MyPoint3D getCurrentPosition() {
		return currentPosition;
	}
	
	public void setPosition(MyPoint3D position){
		asteroidMeshGroup.setTranslateX(position.x+objOffsetFactorX);
		asteroidMeshGroup.setTranslateY(position.y+objOffsetFactorY);
		asteroidMeshGroup.setTranslateZ(position.z);	
		this.currentPosition = position;
	}
	
	public Group getAsteroidMeshViewGroup() {
		return asteroidMeshGroup;
	}

	@Override
	public String toString() {
		return "Asteroid [asteroidMeshGroup=" + asteroidMeshGroup + ", objScaleFactor=" + objScaleFactor
				+ ", objOffsetFactorX=" + objOffsetFactorX + ", objOffsetFactorY=" + objOffsetFactorY + ", speed="
				+ speed + ", startPosition=" + startPosition + ", targetPosition=" + targetPosition + "]";
	}
	
	// Checks intersection of flying bullet with asteroid in 3D world.
	public boolean checkIntersectionWithBullet(Bullet bullet){
		
		//bullet fly line
		MyPoint3D startLine = new MyPoint3D(bullet.getCurrentBulletPosition().x, bullet.getCurrentBulletPosition().y, bullet.getCurrentBulletPosition().z);
		// length of line, in which distance to check intersection.
		float offset=1f;
		MyPoint3D endLine = new MyPoint3D(bullet.getCurrentBulletPosition().x+offset*bullet.getFlyDirection().x, bullet.getCurrentBulletPosition().y+offset*bullet.getFlyDirection().y, bullet.getCurrentBulletPosition().z+offset*bullet.getFlyDirection().z);		
		
		// Iterate over asteroid bounding box and check if intersected with bullet fly line with offset length
		// Check only front and back meshes of bounding box.
		for(int i=0; i<2; i++){
			Mesh mesh = asteroidBoundingBox.getBoundingBoxMeshes()[i];
			// 4 points each point 3 coordinates x,y,z
			float meshPoints[] = new float[4*3];
			mesh.getPoints().toArray(meshPoints);
			MyPoint3D localPointsOfMesh[] = {new MyPoint3D(meshPoints[0],meshPoints[1],meshPoints[2]),new MyPoint3D(meshPoints[3],meshPoints[4],meshPoints[5]),new MyPoint3D(meshPoints[6],meshPoints[7],meshPoints[8]),new MyPoint3D(meshPoints[9],meshPoints[10],meshPoints[11])};
	    	MyPoint3D asteroidTranslations = new MyPoint3D(asteroidMeshGroup.getTranslateX(),asteroidMeshGroup.getTranslateY(), asteroidMeshGroup.getTranslateZ());
	    	
	    	MyPoint3D asteroidRotationAxis = new MyPoint3D(asteroidRotation.getAxis().getX(),asteroidRotation.getAxis().getY(),asteroidRotation.getAxis().getZ());
	    	double    asteroidRotationDegree = asteroidRotation.getAngle();
			
	    	MyPoint3D meshLocalCenter = mesh.getLocalCenter();

	    	//Translate this mesh to 0,0,0 world from 0,0,0 asteroid before applying to it transformations
	    	MyPoint3D translateToWorld = new MyPoint3D(-meshLocalCenter.x,-meshLocalCenter.y,-meshLocalCenter.z);
	    	MyPoint3D rotateAxis = new MyPoint3D(0, 0, 0);
	    	MyPoint3D meshAtWorldOrigin[] = Utils3D.applayRotationTranslation(localPointsOfMesh, translateToWorld, rotateAxis, 0);
	    
	    	//Apply to it translation + rotation which made to asteroid
	    	MyPoint3D meshAfterRotationTranslation[] = Utils3D.applayRotationTranslation(meshAtWorldOrigin, asteroidTranslations, asteroidRotationAxis, asteroidRotationDegree);

	    	//Translate this mesh back to asteroid 0,0,0 
	    	MyPoint3D translateToAsteroid = new MyPoint3D(meshLocalCenter.x,meshLocalCenter.y,meshLocalCenter.z);
	    	MyPoint3D meshAtAsteroidOrigin[] = Utils3D.applayRotationTranslation(meshAfterRotationTranslation, translateToAsteroid, rotateAxis, 0);
	

	    	MyPoint3D intersectionPoint = Utils3D.getIntersectionPointSurfaceLine(startLine,endLine, meshAtAsteroidOrigin[0],meshAtAsteroidOrigin[1],meshAtAsteroidOrigin[2],offset);
	    	if(intersectionPoint==null)return false;
	    	
	    	if(asteroidMeshGroup.getBoundsInParent().contains(intersectionPoint.x,intersectionPoint.y,intersectionPoint.z)){
	    		return true;
	    	}
		}
		
    	return false;
	}
	
	// Checks intersection of mouse click in 2D with asteroid in 3D world.
 	public MyPoint3D checkIntersectionWithMouse(RayFromCameraTo3DWorld ray){
		
		asteroidRotation = new Rotate();
		
		// check only front // back mesh of bounding box 0,1
		for(int i=0; i<2; i++){
			//Iterate over asteroid bounding box and check if intersected with ray
			Mesh mesh = asteroidBoundingBox.getBoundingBoxMeshes()[i];
			float meshPoints[] = new float[4*3];
			mesh.getPoints().toArray(meshPoints);
			MyPoint3D localPointsOfMesh[] = {new MyPoint3D(meshPoints[0],meshPoints[1],meshPoints[2]),new MyPoint3D(meshPoints[3],meshPoints[4],meshPoints[5]),new MyPoint3D(meshPoints[6],meshPoints[7],meshPoints[8]),new MyPoint3D(meshPoints[9],meshPoints[10],meshPoints[11])};
	    	MyPoint3D asteroidTranslations = new MyPoint3D(asteroidMeshGroup.getTranslateX(),asteroidMeshGroup.getTranslateY(), asteroidMeshGroup.getTranslateZ());
	    	
	    	MyPoint3D asteroidRotationAxis = new MyPoint3D(asteroidRotation.getAxis().getX(),asteroidRotation.getAxis().getY(),asteroidRotation.getAxis().getZ());
	    	double    asteroidRotationDegree = asteroidRotation.getAngle();
			
	    	MyPoint3D meshLocalCenter = mesh.getLocalCenter();

	    	//translate this mesh to 0,0,0 world from 0,0,0 asteroid before applying to it transformations
	    	MyPoint3D translateToWorld = new MyPoint3D(-meshLocalCenter.x,-meshLocalCenter.y,-meshLocalCenter.z);
	    	MyPoint3D rotateAxis = new MyPoint3D(0, 0, 0);
	    	MyPoint3D meshAtWorldOrigin[] = Utils3D.applayRotationTranslation(localPointsOfMesh, translateToWorld, rotateAxis, 0);
	    
	    	//applay to it translation + rotation which made to asteroid
	    	MyPoint3D meshAfterRotationTranslation[] = Utils3D.applayRotationTranslation(meshAtWorldOrigin, asteroidTranslations, asteroidRotationAxis, asteroidRotationDegree);

	    	// translate this mesh back to asteroid 0,0,0 
	    	MyPoint3D translateToAsteroid = new MyPoint3D(meshLocalCenter.x,meshLocalCenter.y,meshLocalCenter.z);
	    	MyPoint3D meshAtAsteroidOrigin[] = Utils3D.applayRotationTranslation(meshAfterRotationTranslation, translateToAsteroid, rotateAxis, 0);
	

	    	MyPoint3D intersectionPoint = Utils3D.getIntersectionPointSurfaceLine(ray.getCameraEyeNearPoint(), ray.getCameraEyeFarPoint(), meshAtAsteroidOrigin[0],meshAtAsteroidOrigin[1],meshAtAsteroidOrigin[2],Constants.WORLD_SIZE);
	    	
	    	if(asteroidMeshGroup.getBoundsInParent().contains(intersectionPoint.x,intersectionPoint.y,intersectionPoint.z)){
	    		return intersectionPoint;
	    	}
		}
		
    	return null;

	}
}
