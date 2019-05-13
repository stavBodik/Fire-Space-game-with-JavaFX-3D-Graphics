package Game3D;
import javafx.scene.shape.TriangleMesh;

/**
 * This class represent simple mesh with 4 points in 3d world.
 * points:
 * 1      3
 *  -------   texture:
 *  |\    |  1,1    1,0
 *  | \   |    -------
 *  |  \  |    |     |
 *  |   \ |    |     |
 *  |    \|    -------
 *  -------  0,1    0,0
 * 0      2
 *
 * texture[3] 0,0 maps to vertex 2
 * texture[2] 0,1 maps to vertex 0
 * texture[0] 1,1 maps to vertex 1
 * texture[1] 1,0 maps to vertex 3
 *
 * Two triangles define rectangular faces:
 * p0, t0, p1, t1, p2, t2 // First triangle of a textured rectangle
 * p0, t0, p2, t2, p3, t3 // Second triangle of a textured rectangle
 * @author Wrriten by ajeh 
 * http://stackoverflow.com/questions/19960368/how-to-make-sense-of-javafx-triangle-mesh
 * Edited by Stanislav Bodik
 */

public class Mesh extends TriangleMesh {

	public Mesh(float[] points){
        install(points);
	}
	
	public Mesh (MyPoint3D[] pointsm){
		
		float points[] = new float[pointsm.length*3];

		int j=0;
		for(int i=0; i<pointsm.length; i++){
			points[j]=pointsm[i].x;
			points[j+1]=pointsm[i].y;
			points[j+2]=pointsm[i].z;
			j+=3;
		}
		install(points);
	}
	
	public MyPoint3D getLocalCenter(){
		float points[] = new float[getPoints().size()];
		getPoints().toArray(points);
		return new MyPoint3D((points[0]+points[6])/2,(points[1]+points[4])/2,(points[2]+points[8])/2);
	}
	
	public Mesh(float Width, float Height) {
        float[] points = {
               -Width/2,  Height/2, 0, // idx p0
               -Width/2, -Height/2, 0, // idx p1
                Width/2,  Height/2, 0, // idx p2
                Width/2, -Height/2, 0  // idx p3
        };
        
        install(points);
    }
	
	private void install(float[] points){
		
		float[] texCoords = {
                1, 1, // idx t0
                1, 0, // idx t1
                0, 1, // idx t2
                0, 0  // idx t3
        };
        
        int[] faces = {
                0, 2,    2, 3,   1, 0,    2, 3,    3, 1,     1, 0
        };

        this.getPoints().setAll(points);
        this.getTexCoords().setAll(texCoords);
        this.getFaces().setAll(faces);
	}
 
}