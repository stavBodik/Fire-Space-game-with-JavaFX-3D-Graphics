package Game3D;
import java.util.ArrayList;
import java.util.HashMap;
import GameApplication.Constants;
import GameApplication.Constants.OBJECT_NAMES;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * This class used to hold loaded objects mesh view groups
 * Which is used later when drawing 3D world . 
 * @author Stanislav Bodik
 */

public class ObjectsContainer{
	
	private HashMap<OBJECT_NAMES, Group> objects = new HashMap<OBJECT_NAMES, Group>();
	private ObjLoader objLoader = new ObjLoader();
	public ObjectsContainer(){
		initObjectsMap();
		loadObjects();
	}
	
	private void initObjectsMap(){
		for(int i=0; i<Constants.objects_files.length; i++){
			
			switch (Constants.objects_files[i]) {
			case "gun TBS 001C.obj":
				objects.put(OBJECT_NAMES.CANNON_BASE, new Group());
				objects.put(OBJECT_NAMES.CANNON_BASE_CONNECTOR, new Group());
				objects.put(OBJECT_NAMES.CANNON_GUN, new Group());
				break;
			case "projektil OBJ.obj":
				objects.put(OBJECT_NAMES.BULLET, new Group());
				break;
			case "asteroid OBJ.obj":
				objects.put(OBJECT_NAMES.ASTEROID, new Group());
				break;
			}
		}
	}
	
	private void loadObjects(){
		for(int i=0; i<Constants.objects_files.length; i++){
			
			// objHolder contains for each object file the groups of vertexes (meshes)
			ObjectHolder objHolder = new ObjectHolder(objLoader.loadObject(Constants.objects_files[i]));
			
			for(int j=0; j<objHolder.getObjectMeshes().size(); j++){
				MeshView meshView;
				MeshHolder meshHolder = objHolder.getObjectMeshes().get(j);
				PhongMaterial meshMaterial = meshHolder.getPhoneMaterial();
				TriangleMesh  triangleMesh  = meshHolder.getTriangelMesh();
				
			    switch (Constants.objects_files[i]) {
				case "gun TBS 001C.obj":
						
					meshView = new MeshView(triangleMesh);
				    meshView.setMaterial(meshMaterial);
			    
				 	if(meshHolder.getName().contains("base")){
					 objects.get(OBJECT_NAMES.CANNON_BASE).getChildren().add(meshView);
		            }else if(meshHolder.getName().contains("top")){
		             objects.get(OBJECT_NAMES.CANNON_GUN).getChildren().add(meshView);
		            }else{
			         objects.get(OBJECT_NAMES.CANNON_BASE_CONNECTOR).getChildren().add(meshView);
		            }
				 	
					break;
				case "projektil OBJ.obj":
					
					if(j==0){
						meshMaterial.setDiffuseMap(new Image(Bullet.class.getResource("resources/"+Constants.BULLET_TEXTURE).toExternalForm(),320,200,false,true));
						meshMaterial.setSpecularMap(new Image(Bullet.class.getResource("resources/"+Constants.BULLET_TEXTURE).toExternalForm(),320,200,false,true));
					}
					
					meshView = new MeshView(triangleMesh);
				    meshView.setMaterial(meshMaterial);
				    
				    objects.get(OBJECT_NAMES.BULLET).getChildren().add(meshView);
				
				break;
				
				case "asteroid OBJ.obj":
					
					meshMaterial.setDiffuseMap(new Image(ObjLoader.class.getResource("resources/"+Constants.ASTROID_TEXTURE).toExternalForm(),4000,2000,true,true));
					meshMaterial.setBumpMap(new Image(ObjLoader.class.getResource("resources/"+Constants.ASTROID_TEXTURE).toExternalForm(),4000,2000,true,true));
					meshMaterial.setSpecularMap(new Image(ObjLoader.class.getResource("resources/"+Constants.ASTROID_TEXTURE).toExternalForm(),4000,2000,true,true));
				 	    
					meshMaterial.setDiffuseColor(Color.GRAY);
					
					meshView = new MeshView(triangleMesh);
				    meshView.setMaterial(meshMaterial);
				    
				    objects.get(OBJECT_NAMES.ASTEROID).getChildren().add(meshView);
					break;
				}
			}
		}
	}
	
	private class ObjectHolder {

		private ArrayList<MeshHolder> objectMeshes = new ArrayList<>();
		
		public ObjectHolder(ArrayList<MeshHolder> objectMeshes) {
			this.objectMeshes = objectMeshes;
		}
		
		public ArrayList<MeshHolder> getObjectMeshes() {
			return objectMeshes;
		}
	}
	
	public Group getObjectMeshViewGroup(OBJECT_NAMES objectName){
		//create individual instance of this object group
		Group requestedGroup = objects.get(objectName);
		Group copyGroup = new Group();
		
		for(int i=0; i<requestedGroup.getChildren().size(); i++){
			MeshView mv = new MeshView(((MeshView) requestedGroup.getChildren().get(i)).getMesh());
			mv.setMaterial(((MeshView) requestedGroup.getChildren().get(i)).getMaterial());
			copyGroup.getChildren().add(mv);
		}
		
		return copyGroup;
	}
}





