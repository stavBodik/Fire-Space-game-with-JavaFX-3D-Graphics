package Game3D;

import com.sun.javafx.geom.Vec3d;

import GameApplication.Constants;
import javafx.scene.PerspectiveCamera;

/**
 * This class used to create ray line from camera position to 3d world .
 * @author Stanislav Bodik
 */

public class RayFromCameraTo3DWorld {

	private MyPoint3D cameraEyeNearPoint;
	private MyPoint3D cameraEyeFarPoint;
	private Vec3d rayDirection;
	double SceneWith,SceneHeight;
	private PerspectiveCamera camera;
	
	public RayFromCameraTo3DWorld(PerspectiveCamera camera,double SceneWith,double SceneHeight) {
		this.SceneWith=SceneWith;
		this.SceneHeight=SceneHeight;
		this.camera=camera;
	}
	
	public void updateRay(double mouse2DX,double mouse2DY){
		
		rayDirection = Utils3D.unProjectDirection(camera, mouse2DX, mouse2DY,SceneWith,SceneHeight);
		cameraEyeNearPoint = new MyPoint3D(Constants.cameraEyePositionPlay.x,Constants.cameraEyePositionPlay.y,(float) (Constants.cameraEyePositionPlay.z));
    	cameraEyeFarPoint = new MyPoint3D((float)(Constants.cameraEyePositionPlay.x+camera.getFarClip()*rayDirection.x),(float)(Constants.cameraEyePositionPlay.y+camera.getFarClip()*rayDirection.y),(float)(Constants.cameraEyePositionPlay.z+camera.getFarClip()*rayDirection.z));
	}
	
	public Vec3d getRayDirection() {
		return rayDirection;
	}
	
	public MyPoint3D getCameraEyeFarPoint() {
		return cameraEyeFarPoint;
	}
	
	public MyPoint3D getCameraEyeNearPoint() {
		return cameraEyeNearPoint;
	}
	
}
