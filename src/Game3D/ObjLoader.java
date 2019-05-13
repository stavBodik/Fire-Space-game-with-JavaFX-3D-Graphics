package Game3D;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * This class used to load object files using interactivemesh company ObjModelImporter API.
 * @author Stanislav Bodik
 */

public class ObjLoader {

	public ArrayList<MeshHolder> loadObject(String resourceName) {
        
	 	ArrayList<MeshHolder> objectMeshes = new ArrayList<>();
        ObjModelImporter objImp = new ObjModelImporter();                
        
        try {
            final URL modelUrl = ObjLoader.class.getResource("resources/"+resourceName);
            objImp.read(modelUrl);
        }
        catch(ImportException e) {
            e.printStackTrace();
            return null;
        }
        
        final Map<String, MeshView> namedMeshViews = objImp.getNamedMeshViews();
        final Map<String, PhongMaterial> namedPhgMats = objImp.getNamedMaterials();

        objImp.close();
             
        Iterator<Entry<String, MeshView>> it = namedMeshViews.entrySet().iterator();
        while (it.hasNext()) {
            
        	Map.Entry<String, MeshView> pair = it.next();
            String meshName = pair.getKey().toString();
            TriangleMesh triangelMesh = (TriangleMesh)namedMeshViews.get(pair.getKey()).getMesh();
            PhongMaterial phoneMaterial = null;
            
            String materialName=null;
            if(meshName.contains("$")){
            	materialName= meshName.split("\\$")[1];
            	phoneMaterial = namedPhgMats.get("$"+materialName);
            }else{
            	phoneMaterial = new PhongMaterial(Color.WHITE);
            }
            
            objectMeshes.add(new MeshHolder(triangelMesh, phoneMaterial, meshName));	            
        }

        return objectMeshes;
    }
}
