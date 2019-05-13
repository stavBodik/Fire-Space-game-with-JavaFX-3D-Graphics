package Game3D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

/**
 * This class represent bullet in 3D world,
 * Stores bullet object mesh views and information about flying bullet.
 * @author Stanislav Bodik
 */

public class Bullet {

	private Group  bulletMeshGroup = new Group();
	private Rotate bulletRotateXZ = new Rotate();
	private Rotate bulletRotateY   = new Rotate();
	private final int startBulletRotateAngel=270;
	private MyPoint3D flyDirection;
	private MyPoint3D currentBulletPosition;
	private MyPoint3D startPosition;
	private float rotationXZAngelDeg,rotationYAngelDeg;
	private boolean isHitAsteroid;
	
 	public Bullet(Group bulletMeshViewGroup,MyPoint3D startPosition,float size,float rotationXZAngelDeg,float rotationYAngelDeg) {
		
		this.startPosition=startPosition;
		this.rotationXZAngelDeg=rotationXZAngelDeg;
		this.rotationYAngelDeg=rotationYAngelDeg;

		bulletMeshGroup.getChildren().add(bulletMeshViewGroup);
		setSize(size);
		setPosition(startPosition);
		initRotations();
		setRotateXZ(rotationXZAngelDeg);
		setRotateY(rotationYAngelDeg);
	}
	
 	public void setHitAsteroid(boolean isHitAsteroid) {
		this.isHitAsteroid = isHitAsteroid;
	}
 	
 	public boolean isHitAsteroid() {
		return isHitAsteroid;
	}
 	
	public int getStartBulletRotateAngel() {
		return startBulletRotateAngel;
	}
	
	public float getRotationXZAngelDeg() {
		return rotationXZAngelDeg;
	}
	
	public float getRotationYAngelDeg() {
		return rotationYAngelDeg;
	}
	
	public void setPosition(MyPoint3D position){
		bulletMeshGroup.setTranslateX(position.x);
		bulletMeshGroup.setTranslateY(position.y);
		bulletMeshGroup.setTranslateZ(position.z);
		currentBulletPosition=position;
	}
	
	public MyPoint3D getCurrentBulletPosition() {
		return currentBulletPosition;
	}
	
	public MyPoint3D getStartPosition() {
		return startPosition;
	}
	
	private void setRotateXZ(float angel){
		bulletRotateXZ.setAngle(angel);
	}
	
	private void setRotateY(float angel){
		bulletRotateY.setAngle(angel);
	}

	public void setFlyDirection(MyPoint3D flyDirection) {
		this.flyDirection = flyDirection;
	}
	
	public MyPoint3D getFlyDirection() {
		return flyDirection;
	}
	
	private void initRotations(){
		bulletMeshGroup.getTransforms().add(bulletRotateXZ);
		bulletMeshGroup.getTransforms().add(bulletRotateY);
    	
		bulletRotateXZ.setAxis(new Point3D(0, 1, 0));
    	bulletRotateY.setAxis(new Point3D(1, 0, 0));
	}
	
 	private void setSize(float size){
		bulletMeshGroup.setScaleX(size);
		bulletMeshGroup.setScaleY(size);
		bulletMeshGroup.setScaleZ(size);
	}
	
	public Group getBulletMeshGroup(){
		return bulletMeshGroup;
	}
}
