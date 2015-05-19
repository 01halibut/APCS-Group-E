package game;

import resources.ResourceLoader;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
	private TranslateTransition flappyFall = null;
	private TranslateTransition flappyFlap = null;
	private Button button = null;
	private MediaPlayer flap = null;
	private Group root = null;
	
    private void addButtonPressHandler(){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	root.getChildren().remove(button);
            	flappyFall = new TranslateTransition(
            			Duration.seconds(Math.sqrt(2*(300 - (flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2)))/GRAVITY))
            			, flappy);
            	flappyFall.setToY(300);
            	flappyFall.setInterpolator(new Interpolator() {
            		protected double curve(double t) {
    					return t * t;
    				}});
            	flappyFall.play();
            }
        });
    }
    
    private void addMouseEventHandler(){
    	root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	flap.stop();
            	if(root.getChildren().contains(button)) return;
            	flappyFlap.setToY(flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2));
            	flappyFlap.setInterpolator(new Interpolator() {
            		protected double curve(double t) {
        				return Math.sqrt(t);
        			}});
            	addFlapFinishedHandler();
            	flappyFlap.play();
            	flap.play();
            }
        });
    }
    
    private void addFlapFinishedHandler(){
    	flappyFlap.setOnFinished(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent event) {
            	flappyFall = new TranslateTransition(
            			Duration.seconds(Math.sqrt(2*(300 - (flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2)))/GRAVITY))
            			, flappy);
            	flappyFall.setToY(300);
            	flappyFall.setInterpolator(new Interpolator() {
            		protected double curve(double t) {
    					return t * t;
    				}});
            	flappyFall.play();
            }
    	});
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a Group 
		root = new Group();
		
		bkgrd = new ImageView("./resources/background.png");
		flappy = new ImageView("./resources/flappy.png");
		button = new Button("Start");
		
		flap = new MediaPlayer(new Media(ResourceLoader.class.getResource("flap.mp3").toString()));
		
		flappyFlap = new TranslateTransition();
    	flappyFlap = new TranslateTransition(Duration.seconds(JUMP_VEL/GRAVITY), flappy);
		flappyFall = new TranslateTransition();

		
		
		//Add controls
		root.getChildren().add( bkgrd );
		root.getChildren().add( flappy );
		root.getChildren().add( button );
		
		addButtonPressHandler();
		addMouseEventHandler();
		addFlapFinishedHandler();
		
		
		//Create scene and add to stage
		Scene scene = new Scene(root, 400, 400);
		
    	flappy.translateXProperty().set(scene.getWidth()/2 - 25);
    	flappy.translateYProperty().set(scene.getHeight()/2 - 25);
    	
    	button.translateXProperty().set(scene.getWidth()/2 - 25);
    	button.translateYProperty().set(scene.getHeight() - 50);
    	
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
