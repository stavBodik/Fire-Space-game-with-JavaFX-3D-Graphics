package Game3D;

import javafx.geometry.BoundingBox;
/**
 * This class used to hold 6 meshes representing bounding box of object in 3D world space.
 * @author Stanislav Bodik
 */

public class MyBoundingBox extends BoundingBox{

	private Mesh boundingBoxMeshes[] = new Mesh[6];
	/**
     * points:
     * 1      3
     *  -------   
     *  |\    |  
     *  | \   |    
     *  |  \  |    
     *  |   \ |    
     *  |    \|    
     *  -------  
     * 0      2
     */
	
	public void voidUpdate(double minX, double minY, double minZ,double width, double height){
		BoundingBox temp = new BoundingBox(minX, minY, getWidth(), getHeight());
		setMeshes((float)temp.getMinX(),(float)temp.getMinY(),(float)temp.getMinZ(),(float)temp.getMaxX(),(float)temp.getMaxY(),(float)temp.getMaxZ());
	}
	
	public MyBoundingBox(double minX, double minY, double minZ, double width, double height, double depth) {
		super(minX, minY, minZ, width, height, depth);
		setMeshes((float)getMinX(),(float)getMinY(),(float)getMinZ(),(float)getMaxX(),(float)getMaxY(),(float)getMaxZ());
	}
	
	public void setMeshes(float minX,float minY, float minZ,float maxX,float maxY,float maxZ){
		//back
		Mesh back = new Mesh(
				new float[]{
						minX,maxY,minZ,
						minX,minY,minZ,
						maxX,maxY,minZ,
						maxX,minY,minZ
				});
		boundingBoxMeshes[0]=back;

		//front
		Mesh front = new Mesh(
				new float[]{
						minX,maxY,maxZ,
						minX,minY,maxZ,
						maxX,maxY,maxZ,
						maxX,minY,maxZ
				});
		boundingBoxMeshes[1]=front;


		//bottom
		Mesh bottom = new Mesh(
				new float[]{
						maxX,maxY,minZ,
						maxX,maxY,maxZ,
						minX,maxY,minZ,
						minX,maxY,maxZ
				});
		boundingBoxMeshes[2]=bottom;


		//top
		Mesh top = new Mesh(
				new float[]{
						minX,minY,minZ,
						minX,minY,maxZ,
						maxX,minY,minZ,
						maxX,minY,maxZ
				});
		boundingBoxMeshes[3]=top;


		//left
		Mesh left = new Mesh(
				new float[]{
						minX,maxY,maxZ,
						minX,minY,maxZ,
						minX,maxY,minZ,
						minX,minY,minZ
				});
		boundingBoxMeshes[4]=left;

		//right
		Mesh right = new Mesh(
				new float[]{
						maxX,maxY,minZ,
						maxX,minY,minZ,
						maxX,maxY,maxZ,
						maxX,minY,maxZ
				});
		boundingBoxMeshes[5]=right;
	}
	
	public Mesh[] getBoundingBoxMeshes() {
		return boundingBoxMeshes;
	}

	
}
