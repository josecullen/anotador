package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.AnotadorController;


public class Main extends Application {
	public static Stage primaryStage;
	
	Conf conf;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("mongod");
			
			
			this.primaryStage = primaryStage;
			conf = Conf.getConf();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setWidth(conf.getWidth());
			primaryStage.setHeight(conf.getHeight());
			primaryStage.setResizable(true);
			conf.heightProperty().bind(primaryStage.heightProperty());
			conf.widthProperty().bind(primaryStage.widthProperty());
			
			
			System.out.println("DBServices.getLastOrder():  " +NotaServices.getLastOrder());
			//TagServices.getAllProperties().forEach(action -> System.out.println(action));
			AnotadorController anotador = new AnotadorController();
			
			
			Scene scene = new Scene(anotador, conf.getWidth(), conf.getHeight());

			scene.addEventHandler(KeyEvent.KEY_RELEASED, (EventHandler<KeyEvent>) (event)->{
				int amount = 30;
				if(Key.WIDTH_UP.match(event)){
					primaryStage.setWidth(primaryStage.getWidth()+amount);
				}else if(Key.WIDTH_DOWN.match(event)){
					primaryStage.setWidth(primaryStage.getWidth()-amount);
				}else if(Key.HEIGTH_UP.match(event)){
					primaryStage.setHeight(primaryStage.getHeight()+amount);
				}else if(Key.HEIGTH_DOWN.match(event)){
					primaryStage.setHeight(primaryStage.getHeight()-amount);
				}else if(Key.DECORATE.match(event)){				
				}else if(Key.MOVE_UP.match(event)){					
					primaryStage.setY(primaryStage.getY()-amount);
				}else if(Key.MOVE_DOWN.match(event)){
					primaryStage.setY(primaryStage.getY()+amount);
				}else if(Key.MOVE_LEFT.match(event)){
					primaryStage.setX(primaryStage.getX()-amount);
				}else if(Key.MOVE_RIGTH.match(event)){
					primaryStage.setX(primaryStage.getX()+amount);
				}
			});
			
				
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
//			primaryStage.setScene(w.getScene());
//			maximize(primaryStage);
			
//			HTMLContext w = new HTMLContext();

			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	private void maximize(Stage stage){
		Screen screen = Screen.getPrimary();

		Rectangle2D bounds = screen.getVisualBounds();

		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
	}
	
	
	
}