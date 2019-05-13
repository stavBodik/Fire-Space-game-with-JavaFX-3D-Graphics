package Game3D;
import java.util.ArrayList;

import javafx.scene.Group;

/**
 * This class represent Cannon in 3D world,
 * Stores Cannon object mesh views and information Cannon.
 * The cannon is separated to 2 moving parts and 1 standing:
 * 1. cannon gun (rotations along x,y,z)
 * 2. cannon base connector ,which connects base and gun (rotations along x,z)
 * 3. base
 * @author Stanislav Bodik
 */

public class Cannon {

	private Group cannonBase = new Group();
	private Group cannonBaseConnector= new Group();
	private Group cannontGun= new Group();
	
	//270 degrees offset of created cannon object along XZ.
	private final int startCannonRotateXZAngel=270;
	private final double cannonConnectorHeight=-0.35;
	private final double cannonHeigh=-0.8;
	private MyPoint3D gunPreparationStartPosition = new MyPoint3D(0f, -0.65f, -1f);
	private MyPoint3D gunPreparationPosition = new MyPoint3D(0f, -0.65f, -1f);
	private float gunAngelXZ,gunAngelY;
	
  	public Cannon(Group cannonBase,Group cannonBaseConnector,Group cannontGun) {
  		this.cannonBase=cannonBase;
  		this.cannonBaseConnector=cannonBaseConnector;
  		this.cannontGun=cannontGun;
		//cannonBase.getChildren().add(ControlManager.modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_BASE)) ;
		//cannonBaseConnector.getChildren().add(ControlManager.modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_BASE_CONNECTOR));
		//cannontGun.getChildren().add(ControlManager.modelManager.getObjectMeshViewGroup(OBJECT_NAMES.CANNON_GUN)); 
	}
  	
  	public void setGunAngelXZ(float gunAngelXZ) {
		this.gunAngelXZ = gunAngelXZ;
	}
  	
  	public void setGunAngelY(float gunAngelY) {
		this.gunAngelY = gunAngelY;
	}
  	
  	public float getGunAngelY() {
		return gunAngelY;
	}
  	
  	public float getGunAngelXZ() {
		return gunAngelXZ;
	}
	
 	public double getCannonConnectorHeight() {
		return cannonConnectorHeight;
	}
 	
	public int getStartRotateAngel() {
		return startCannonRotateXZAngel;
	}
	
	public Group getCannonBase() {
		return cannonBase;
	}
	
	public Group getCannontGun() {
		return cannontGun;
	}
	
	public Group getCannonBaseConnector() {
		return cannonBaseConnector;
	}
	
	public double getCannonHeigh() {
		return cannonHeigh;
	}

	public ArrayList<Group> getGunMeshGroups(){
		ArrayList<Group> gunGroups = new ArrayList<>();
		gunGroups.add(cannonBase);
		gunGroups.add(cannontGun);
		gunGroups.add(cannonBaseConnector);

		return gunGroups;
	}

	public void setGunPreparationPosition(MyPoint3D gunPreparationPosition) {
		this.gunPreparationPosition = gunPreparationPosition;
	}
	
	public MyPoint3D getGunPreparationPosition() {
		return gunPreparationPosition;
	}
	
	public MyPoint3D getGunPreparationStartPosition() {
		return gunPreparationStartPosition;
	}

}
