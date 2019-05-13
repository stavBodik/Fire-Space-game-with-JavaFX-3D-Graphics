package Game3D;
import java.util.ArrayList;
import GameApplication.Constants;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * This class used to create 3D Sky Box from 6 image views.
 * @author Stanislav Bodik
 */

public class Skybox extends Group{
        
	private double size;
	    
    private final ImageView top   = new ImageView(),bottom= new ImageView(),left  = new ImageView(),right = new ImageView(),back  = new ImageView(),front = new ImageView();
    
    public ImageView getFront() {
		return front;
	}
    
    //back,front,top,bottom,left,right
    private ImageView[] views = new ImageView[]{back,front,top,bottom,left,right};    
    private Image topImg, bottomImg, leftImg, rightImg, frontImg, backImg;
    private ArrayList<MyPoint3D[]> imageViewsCoordinates = new ArrayList<>();

    public Skybox(Image topImg, Image bottomImg, Image leftImg, Image rightImg, Image frontImg, Image backImg, double size) {
        super();            
                
        this.topImg = topImg;
        this.bottomImg = bottomImg;
        this.leftImg = leftImg;
        this.rightImg = rightImg;
        this.frontImg = frontImg;
        this.backImg = backImg;       
        this.size=size;
                
        top.setId("top");
        bottom.setId("bottom");
        left.setId("left");
        right.setId("right");
        back.setId("back");
        front.setId("front");
        
        loadImageViews();
                                
        getChildren().add(front);   
        getChildren().add(top);     
        getChildren().add(left);     
        getChildren().add(right);     
        getChildren().add(back);     
        getChildren().add(bottom);     

        startAnimation();

    }

    private void startAnimation(){
    	Rotate skyBoxRotate = new Rotate(0, 0,1,0, Rotate.Y_AXIS);
         
         final KeyValue kv0 = new KeyValue(skyBoxRotate.angleProperty(), 0, Interpolator.LINEAR);
         final KeyValue kv1 = new KeyValue(skyBoxRotate.angleProperty(), 360, Interpolator.LINEAR);
         final KeyFrame kf0 = new KeyFrame(Duration.millis(0), kv0);
         final KeyFrame kf1 = new KeyFrame(Duration.millis(300000), kv1); // min speed, max duration

         Timeline tuxCubeRotTimeline = new Timeline();
         tuxCubeRotTimeline.setCycleCount(Timeline.INDEFINITE);        
         tuxCubeRotTimeline.getKeyFrames().setAll(kf0, kf1);
         
         this.getTransforms().setAll(skyBoxRotate);
         tuxCubeRotTimeline.playFromStart();
    	
    }

    private void loadImageViews(){
                        
        for(ImageView iv : views){      
            iv.setSmooth(true);
            iv.setPreserveRatio(true);            
        }
        
        setImageViews();
    }

    private void setViewsSizeAndLocation() {
        
        for(ImageView v : views){
            v.setFitWidth(size);
            v.setFitHeight(size);
        }
        
        back.setX(-size/2);
        back.setY(-size/2);
        back.setTranslateZ(-0.5 * size);  
        
     // p1 = left-bottom, p2 = right-bottom, p3 = left top , p3 = right top
    	MyPoint3D p1 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
    	MyPoint3D p2 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
    	MyPoint3D p3 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
    	MyPoint3D p4 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);

    	MyPoint3D backImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(backImageViewSurface);
                
    	front.setX(-size/2);
    	front.setY(-size/2);
    	
        front.setTranslateZ(0.5 * size);
        front.setRotationAxis(Rotate.Z_AXIS);
        front.setRotate(-180);
        front.getTransforms().add(new Rotate(180,front.getFitHeight() / 2, 0,0, Rotate.X_AXIS));
                
        
		p1 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p2 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p3 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p4 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);

    	MyPoint3D frontImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(frontImageViewSurface);
    	
    	top.setX(-size/2);
    	top.setY(-size/2);
    	
        top.setTranslateY(-0.5 * size);
        top.setRotationAxis(Rotate.X_AXIS);
        top.setRotate(-90);
        
        
       
       

        p1 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p2 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p3 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
		p4 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);

    	MyPoint3D topImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(topImageViewSurface);
                
    	
    	bottom.setX(-size/2);
    	bottom.setY(-size/2);
    	bottom.setRotationAxis(Rotate.X_AXIS);
        bottom.setRotate(90);
        bottom.setTranslateY(0.5 * size);
        
        
        p1 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p2 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p3 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
		p4 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);

    	MyPoint3D bottomImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(bottomImageViewSurface);
                
    	left.setX(-size/2);
    	left.setY(-size/2);
        left.setTranslateX(-0.5 * size);
        left.setRotationAxis(Rotate.Y_AXIS);
        left.setRotate(90);     
        
        p1 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p2 = new MyPoint3D(-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
		p3 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p4 = new MyPoint3D(-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);

    	MyPoint3D leftImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(leftImageViewSurface);
    	
    	
        
    	right.setX(-size/2);
    	right.setY(-size/2);
    	right.setTranslateX(size/2);
        right.setRotationAxis(Rotate.Y_AXIS);
        right.setRotate(-90);    
        
        
        p1 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p2 = new MyPoint3D(Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);
		p3 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,Constants.WORLD_SIZE/2);
		p4 = new MyPoint3D(Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2,-Constants.WORLD_SIZE/2);

    	MyPoint3D rightImageViewSurface[] = {p1,p2,p3,p4};
    	imageViewsCoordinates.add(rightImageViewSurface);
       
    }
   
    public ArrayList<MyPoint3D[]> getViewsSurfaceCoordinates() {
		return imageViewsCoordinates;
	}
    
    private void setImageViews() {        
        setViewsSizeAndLocation();
        
        back.setImage(frontImg);
        front.setImage(backImg);
        top.setImage(topImg);
        bottom.setImage(bottomImg);
        left.setImage(leftImg);
        right.setImage(rightImg);
    }

    // iterates overt each image view of sky box and check if ray intersect it 
    public MyPoint3D checkIntersectionWithSkyBox(RayFromCameraTo3DWorld ray){
    	
    	for(int i=0; i<views.length; i++){
    		
	    	if(views[i].getId().equals("back"))continue;
	    	
	    	//calculate local points of this view
	    	ImageView v =views[i];
	    	//localMin = upper left, localMax = bottom right
	    	MyPoint3D localMin = new MyPoint3D(v.getBoundsInLocal().getMinX(), v.getBoundsInLocal().getMinY(),0);
	    	MyPoint3D localMax = new MyPoint3D(v.getBoundsInLocal().getMaxX(), v.getBoundsInLocal().getMaxY(),0);
	    		
	    	MyPoint3D localMinRight = new MyPoint3D(localMax.x,localMin.y,0);
	    	MyPoint3D localMaxLeft = new MyPoint3D(localMin.x,localMax.y,0);
	    	
	    	MyPoint3D localcPointsOfImageView[] = {localMin,localMinRight,localMaxLeft,localMax};
	    	MyPoint3D imageViewTranslations = new MyPoint3D(v.getTranslateX(),v.getTranslateY(), v.getTranslateZ());
	    	MyPoint3D imageViewRotationAxis = new MyPoint3D(v.getRotationAxis().getX(),v.getRotationAxis().getY(),v.getRotationAxis().getZ());
	    	double    imageViewRotationDegree = v.getRotate();    
	
	    	MyPoint3D pointsAfterTranslationRotation[] = Utils3D.applayRotationTranslation(localcPointsOfImageView, imageViewTranslations, imageViewRotationAxis, imageViewRotationDegree);
	    	    	
	    	MyPoint3D intersectionPoint = Utils3D.getIntersectionPointSurfaceLine(ray.getCameraEyeNearPoint(),ray.getCameraEyeFarPoint(),pointsAfterTranslationRotation[0],pointsAfterTranslationRotation[1],pointsAfterTranslationRotation[2],Constants.WORLD_SIZE*2);
	
	    	try{
	    		if(views[i].getBoundsInParent().contains(intersectionPoint.x,intersectionPoint.y,intersectionPoint.z)){
	    			return intersectionPoint;
	    		}
	    	}catch(Exception e){
	    		//TODO : check why fails here some times
	    	}

    	}

    	return null;
    }
    
    
    
    

}
