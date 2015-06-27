package application;
	
import view.TextAreaPlus;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.AnotadorController;
import controller.TextPanePlusController;


public class Main extends Application {
	public static Stage primaryStage;
	
	Conf conf;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			conf = Conf.getConf();
//			DBUtils.dropDatabase();
			primaryStage.initStyle(StageStyle.UNDECORATED);
//			primaryStage.initStyle(StageStyle.UTILITY);
			primaryStage.setWidth(conf.getWidth());
			primaryStage.setHeight(conf.getHeight());
			primaryStage.setResizable(true);
			conf.heightProperty().bind(primaryStage.heightProperty());
			conf.widthProperty().bind(primaryStage.widthProperty());
			
			
//			DBUtils.remove();
//			Document doc = new Document();
//			doc.append("content", "hola loco mía");
//			DBUtils.getCollection().insertOne(doc);
//			doc = new Document("content", "hola, cómo andás papá ¿Todo bien? hola gil");
//			DBUtils.getCollection().insertOne(doc);
//			doc = new Document("content", "hola, yo no sé cómo hola este hola. La verdad es que el hola de las holas es muy grande. Hola");
//			DBUtils.getCollection().insertOne(doc);

//			MongoCursor<Document> cursor = DBUtils.getCollection().find(Filters.text("hola")).projection(Projections.metaTextScore("hola")).iterator();
//			while(cursor.hasNext()){
//				doc = cursor.next();
//				System.out.println(doc.toJson());
//				System.out.println(doc.get("_id"));
//			}
			
			
			
			AnotadorController anotador = new AnotadorController();
			anotador.setStyle("-fx-background-color: green;");
			
			
			
			Scene scene = new Scene(anotador, conf.getWidth(), conf.getHeight());
//			Scene scene = new Scene(new TextPanePlusController(), conf.getWidth(), conf.getHeight());
//			Scene scene = new Scene(new TextAreaPlus(), conf.getWidth(), conf.getHeight());
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
//					if(primaryStage.getStyle().equals(StageStyle.UNDECORATED)){
//						primaryStage.hide();
//						primaryStage.initStyle(StageStyle.DECORATED);
//						primaryStage.show();
//					
//						
//					}else{
//						primaryStage.hide();
//						primaryStage.initStyle(StageStyle.UNDECORATED);
//						primaryStage.show();
//					}					
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
			
//			maximize(primaryStage);
			
			
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
