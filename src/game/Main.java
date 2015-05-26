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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is the most godawful code I've ever written
 * I should be ashamed
 *
 */

public class Main extends Application{
	
	final double GRAVITY = 1000;
	final double JUMP_VEL = 420;
	private ImageView bkgrd = null ;
	private ImageView flappy = null ;
	private ImageView ground = null;
	private ImageView youSuck = null;
	private ImageView upper1 = null;
	private ImageView lower1 = null;
	private ImageView upper2 = null;
	private ImageView lower2 = null;
	private ImageView start = null;
	private Text scoreDisplay = null;
	private SequentialTransition flappyTransition = null;
	private TranslateTransition flappyFall = null;
	private TranslateTransition flappyFlap = null;
	private TranslateTransition groundMove = null;
	
	private TranslateTransition upper1Move1 = null;
	private TranslateTransition upper2Move1 = null;
	private TranslateTransition lower1Move1 = null;
	private TranslateTransition lower2Move1 = null;
	private TranslateTransition upper1Move2 = null;
	private TranslateTransition upper2Move2 = null;
	private TranslateTransition lower1Move2 = null;
	private TranslateTransition lower2Move2 = null;
	
	private int set1Height;
	private int set2Height;
	private int score = 0;
	
	private MediaPlayer flap = null;
	private Group root = null;
	private boolean started = false;
	private boolean gameOver = false;
	private boolean number2Started = false;
    
    private void addKeyEventHandler(){
    	root.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent m) {
    	        if(!m.getButton().equals(MouseButton.PRIMARY)) return;
    	        if(gameOver){
    	        	resetGame();
    	        	return;
    	        }
    	        if(!started){
    	        	root.getChildren().remove(start);
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
                	groundMove.play();
                	
                	upper1Move1.play();
                	lower1Move1.play();
                	started = true;
    	        }
    	        flap();
    	    }
    	});
    }
    
    private void addGroundHandler(){
    	groundMove.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(gameOver) return;
				ground.setTranslateX(0);
				groundMove.play();
			}
    	});
    	flappyTransition.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				if(gameOver) return;
				endGame();
			}
    	});
    }
    
    private void addObstacleHandler(){
    	upper1Move1.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(flappy.getTranslateY() - set1Height < -110 ||
						flappy.getTranslateY() - set1Height > -40){
					flappy.rotateProperty().set(90);
					endGame();
					flap();
					return;
				}
				score += 1;
				scoreDisplay.setText("Score: " + score);
				upper1Move2.play();
				lower1Move2.play();
				if(!number2Started){
					upper2Move1.play();
					lower2Move1.play();
					number2Started = true;
				}
			}
    	});
    	upper2Move1.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if(flappy.getTranslateY() - set2Height < -110 ||
						flappy.getTranslateY() - set2Height > -40){
					flappy.rotateProperty().set(90);
					endGame();
					flap();
					return;
				}
				score += 1;
				scoreDisplay.setText("Score: " + score);
				upper2Move2.play();
				lower2Move2.play();
			}
    	});
    	
    	upper1Move2.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				set1Height = -(int) (Math.random() * 170);
		    	upper1.setTranslateY(set1Height);
		    	lower1.setTranslateY(set1Height);
				upper1.setTranslateX(0);
				lower1.setTranslateX(0);
				upper1Move1.play();
				lower1Move1.play();
			}
    	});
    	upper2Move2.onFinishedProperty().set(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
		    	set2Height = -(int) (Math.random() * 170);
		    	upper2.setTranslateY(set2Height);
		    	lower2.setTranslateY(set2Height);
				upper2.setTranslateX(0);
				lower2.setTranslateX(0);
				upper2Move1.play();
				lower2Move1.play();
			}
    	});
    }
    
    private void endGame(){
    	groundMove.stop();
		started = false;
		gameOver = true;
		root.getChildren().add(youSuck);
		upper1Move1.pause();
		upper2Move1.pause();
		upper1Move2.pause();
		upper2Move2.pause();
		lower1Move1.pause();
		lower2Move1.pause();
		lower1Move2.pause();
		lower2Move2.pause();
    }
    private void resetGame(){
    	root.getChildren().remove(youSuck);
    	root.getChildren().add(start);
    	flappy.translateYProperty().set(-200);
    	flappy.setRotate(0);
    	flappyTransition.stop();
    	gameOver = false;
    	number2Started = false;
    	ground.setTranslateX(0);
    	upper1.setTranslateX(0);
		upper2.setTranslateX(0);
		lower1.setTranslateX(0);
		lower2.setTranslateX(0);
		score = 0;
    	scoreDisplay.setText("Score:" + score);
    }
    private void flap(){
    	flap.stop();
    	flappyTransition.stop();
    	flappyTransition.getChildren().clear();
    	flappyFlap.setToY(Math.max(flappy.getTranslateY() - (JUMP_VEL/GRAVITY)*(JUMP_VEL/2), -330));
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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a Group 
		root = new Group();
		
		bkgrd = new ImageView("./resources/background.png");
		flappy = new ImageView("./resources/flappy.png");
		start = new ImageView("./resources/getready.png");
		ground = new ImageView("./resources/ground.png");
		youSuck = new ImageView("./resources/gameover.png");
		upper1 = new ImageView("./resources/obstacle_top.png");
		upper2 = new ImageView("./resources/obstacle_top.png");
		lower1 = new ImageView("./resources/obstacle_bottom.png");
		lower2 = new ImageView("./resources/obstacle_bottom.png");
		
		
		flap = new MediaPlayer(new Media(ResourceLoader.class.getResource("flap.mp3").toString()));
		
		flappyTransition = new SequentialTransition();
    	flappyFlap = new TranslateTransition(Duration.seconds(JUMP_VEL/GRAVITY), flappy);
		flappyFall = new TranslateTransition();

		/*LET'S DO ALL THIS MANUALLY BECAUSE REASONS*/
		upper1Move1 = new TranslateTransition(Duration.seconds(2.25), upper1);
		upper1Move1.setToX(-250);
    	upper1Move1.setInterpolator(Interpolator.LINEAR);
    	upper2Move1 = new TranslateTransition(Duration.seconds(2.25), upper2);
		upper2Move1.setToX(-250);
    	upper2Move1.setInterpolator(Interpolator.LINEAR);
    	lower1Move1 = new TranslateTransition(Duration.seconds(2.25), lower1);
		lower1Move1.setToX(-250);
    	lower1Move1.setInterpolator(Interpolator.LINEAR);
    	lower2Move1 = new TranslateTransition(Duration.seconds(2.25), lower2);
		lower2Move1.setToX(-250);
    	lower2Move1.setInterpolator(Interpolator.LINEAR);
    	
    	upper1Move2 = new TranslateTransition(Duration.seconds(2.25), upper1);
		upper1Move2.setToX(-500);
    	upper1Move2.setInterpolator(Interpolator.LINEAR);
    	upper2Move2 = new TranslateTransition(Duration.seconds(2.25), upper2);
		upper2Move2.setToX(-500);
    	upper2Move2.setInterpolator(Interpolator.LINEAR);
    	lower1Move2 = new TranslateTransition(Duration.seconds(2.25), lower1);
		lower1Move2.setToX(-500);
    	lower1Move2.setInterpolator(Interpolator.LINEAR);
    	lower2Move2 = new TranslateTransition(Duration.seconds(2.25), lower2);
		lower2Move2.setToX(-500);
    	lower2Move2.setInterpolator(Interpolator.LINEAR);
    	
    	scoreDisplay = new Text();
    	scoreDisplay.setText("Score:" + score);
    	scoreDisplay.toFront();
    	scoreDisplay.setFont(new Font(24));
    	scoreDisplay.setX(25);
    	scoreDisplay.setY(25);

		
		
		//Add controls
		root.getChildren().addAll( bkgrd,
									upper1,
									lower1,
									upper2,
									lower2,
									ground,
									flappy,
									start,
									scoreDisplay);

		
		
		//Create scene and add to stage
		Scene scene = new Scene(root, 400, 400);
		
    	flappy.xProperty().set(scene.getWidth()/2 - 25);
    	flappy.yProperty().set(scene.getHeight() - 70);
    	flappy.translateYProperty().set(-scene.getHeight()/2);
    	
    	start.xProperty().set(100);
    	start.translateYProperty().set(50);
    	
    	youSuck.xProperty().set(100);
    	youSuck.translateYProperty().set(50);
    	
    	ground.yProperty().set(scene.getHeight() - 48);
    	ground.xProperty().set(0);
    	groundMove = new TranslateTransition(Duration.seconds(4), ground);
    	groundMove.setToX(-400);
    	groundMove.setInterpolator(Interpolator.LINEAR);
    	
    	upper1.xProperty().set(400);
    	upper2.xProperty().set(400);
    	lower1.xProperty().set(400);
    	lower2.xProperty().set(400);
    	
    	upper1.yProperty().set(-100);
    	upper2.yProperty().set(-100);
    	lower1.yProperty().set(320);
    	lower2.yProperty().set(320);
    	
    	set1Height = -(int) (Math.random() * 170);
    	set2Height = -(int) (Math.random() * 170);
    	
    	upper1.setTranslateY(set1Height);
    	lower1.setTranslateY(set1Height);
    	upper2.setTranslateY(set2Height);
    	lower2.setTranslateY(set2Height);

		addKeyEventHandler();
		addGroundHandler();
		addObstacleHandler();
    	
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setTitle("CrappyBird");
		
		//lower2Move1.play();
//		primaryStage.setResizable(false);
	}

	public static void main(String[] args) {
		Application.launch(args); 	
	}

}