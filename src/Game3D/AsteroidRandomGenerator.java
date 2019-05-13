package Game3D;

import GameApplication.Constants.OBJECT_NAMES;
import GameApplication.ModelManager;

/**
 * This class used to generate random asteroids 
 * random values generated are speed,size. (when creating animation outside this class there is also random rotation)
 * @author Stanislav Bodik
 */

public class AsteroidRandomGenerator {

	private Range astroidsSize,astroidsSpeed;
	private float astroidStartRadius;
	private ModelManager modelManagerInstance;
	
  	public AsteroidRandomGenerator(ModelManager modelManagerInstance,int astroidStartRadius,Range asteroidsSize,Range asteroidsSpeed) {
		this.astroidsSize=asteroidsSize;
		this.astroidsSpeed = asteroidsSpeed;
		this.astroidStartRadius=astroidStartRadius;
		this.modelManagerInstance=modelManagerInstance;
	}
	
	public Asteroid getNextRandomAstroid(){
		float asteroidSpeed  = Utils3D.generateRandomFloat(astroidsSpeed.getMin(),astroidsSpeed.getMax());
		float asteroidSize  = Utils3D.generateRandomFloat(astroidsSize.getMin(),astroidsSize.getMax());
		float randomAngel = Utils3D.generateRandomFloat(0,(float)(Math.PI));
		float randomHeightStart = Utils3D.generateRandomFloat(-1,-5);
		float randomHeightEnd = Utils3D.generateRandomFloat(-1,-5);

		MyPoint3D astroidStartPosition  = new MyPoint3D((float)(astroidStartRadius*Math.cos(randomAngel)),randomHeightStart,(float)(astroidStartRadius*Math.sin(randomAngel)));
		MyPoint3D astroidEndPosition  = new MyPoint3D(-astroidStartPosition.x,randomHeightEnd,-astroidStartPosition.z);
				
		return new Asteroid(modelManagerInstance.getObjectMeshViewGroup(OBJECT_NAMES.ASTEROID),astroidStartPosition, astroidEndPosition, asteroidSize, asteroidSpeed);
	}
	
}
