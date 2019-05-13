package Game3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.TriangleMesh;

/**
 * This class used to hold TriangleMesh and PhongMaterial information of created mesh.
 * @author Stanislav Bodik
 */

public class MeshHolder {

	private TriangleMesh triangelMesh;
	private PhongMaterial phoneMaterial;
	private String name;
	
	public MeshHolder(TriangleMesh triangelMesh, PhongMaterial phoneMaterial, String name) {
		super();
		this.triangelMesh = triangelMesh;
		this.phoneMaterial = phoneMaterial;
		this.name = name;
	}
	
	public TriangleMesh getTriangelMesh() {
		return triangelMesh;
	}
	
	public void setTriangelMesh(TriangleMesh triangelMesh) {
		this.triangelMesh = triangelMesh;
	}
	public PhongMaterial getPhoneMaterial() {
		return phoneMaterial;
	}
	
	public void setPhoneMaterial(PhongMaterial phoneMaterial) {
		this.phoneMaterial = phoneMaterial;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
}
