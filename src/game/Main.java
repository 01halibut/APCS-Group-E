package game;

import resources.ResourceLoader;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application{
	
	final double GRAVITY = 1000;
	final double JUMP_VEL = 420;
	private ImageView bkgrd = null ;
	private ImageView flappy = null ;
	private SequentialTransition flappyTransition = null;
	private TranslateTransition flappyFall = null;
	private TranslateTransition flappyFlap = null;
	private MediaPlayer flap = null;
	private Group root = null;
	private boolean started = false;
    
    private void addKeyEventHandler(){
    	root.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent m) {
    	        if(!m.getButton().equals(MouseButton.PRIMARY)) return;
    	        if(!started){
                	flappyFall = new TranslateTransition(
                			Duration.seconds(Math.sqrt(2*(-flappy.getTranslateY())/GRAVITY))
                			, flappy);
                	flappyFall.setToY(0);
                	flappyFall.setInterpolator(new Interpolator() {
                		protected double curve(double t) {
        					return t * t;
        				}});
                	flappyTransition.getChildren().add(flappyFall);
                	flappyTransition.play();
    	        }
    	        flap.stop();
            	flappyTransition.stop();
            	flappyTransition.getChildren().clear();
            	flappyFlap.setToY(flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2));
            	flappyFlap.setInterpolator(new Interpolator() {
            		protected double curve(double t) {
        				return Math.sqrt(t);
        			}});
            	
            	flappyFall = new TranslateTransition(
            			Duration.seconds(Math.sqrt(-2*(flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2))/GRAVITY))
            			, flappy);
            	flappyFall.setToY(0);
            	flappyFall.setInterpolator(new Interpolator() {
            		protected double curve(double t) {
    					return t * t;
    				}});
            	
            	flappyTransition.getChildren().addAll(flappyFlap, flappyFall);
            	flap.play();
            	flappyTransition.play();
    	    }
    	});
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a Group 
		root = new Group();
		
		bkgrd = new ImageView("./resources/background.png");
		flappy = new ImageView("./resources/flappy.png");
		
		flap = new MediaPlayer(new Media(ResourceLoader.class.getResource("flap.mp3").toString()));
		
		flappyTransition = new SequentialTransition();
		flappyFlap = new TranslateTransition();
    	flappyFlap = new TranslateTransition(Duration.seconds(JUMP_VEL/GRAVITY), flappy);
		flappyFall = new TranslateTransition();

		
		
		//Add controls
		root.getChildren().add( bkgrd );
		root.getChildren().add( flappy );

		
		addKeyEventHandler();
		
		//Create scene and add to stage
		Scene scene = new Scene(root, 400, 400);
		
    	flappy.xProperty().set(scene.getWidth()/2 - 25);
    	flappy.yProperty().set(scene.getHeight() - 100);
    	flappy.translateYProperty().set(-scene.getHeight()/2);
    	
    	
		primaryStage.setScene(scene);
		primaryStage.show();
//		primaryStage.setResizable(false);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}