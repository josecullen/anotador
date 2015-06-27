package controller;

import static com.mongodb.client.model.Filters.text;

import java.io.IOException;

import javax.jws.soap.SOAPBinding.Style;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.bson.Document;
import org.bson.conversions.Bson;

import application.Conf;
import application.DBUtils;
import application.Key;
import application.Main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
public class AnotadorController extends VBox {
	
	//	@FXML Button btnCancelar;
//	, btnGuardar;
	@FXML TextArea taContent;
	@FXML TextField txtFind;	
	ListView<LabelContent> txtList = new ListView<LabelContent>();
	WebView web = new WebView();
	WebEngine engine = web.getEngine();
	
	public ObjectProperty<Document> doc = new SimpleObjectProperty<Document>();
	

	
	public AnotadorController() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(	Class.class.getResource("/view/AnotadorView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);	
		
		fxmlLoader.load();
//		taContent.setStyle( 
//				"-fx-font: Monospaced;"+
//				"-fx-font-weight: bold;"+
//			    "-fx-font-size: 12;");
		
		taContent.setFont(Font.font("Monospaced",FontWeight.BOLD,12));
		

		setListeners();
		taContent.autosize();
		VBox.setVgrow(txtList, Priority.ALWAYS);
		txtList.autosize();
		
		
//		taContent.setStyle("-fx-font-family: " + GUIConstants.SysResponseFont.getFamily());
//		taContent.setStyle("-fx-font-size: " + GUIConstants.SysResponseFont.getSize());
//		taContent.setStyle("-fx-font-weight: " + GUIConstants.SysResponseFont.getStyle());
	}
	
	
	private void setListeners(){
		if(Conf.getConf().getLastDoc() != null){
			doc.set(Conf.getConf().getLastDoc());
			taContent.setText(doc.get().getString("content"));
			taContent.positionCaret(taContent.getText().length());
		}
		
		Conf.getConf().lastDocProperty().bindBidirectional(doc);
		txtList.setFocusTraversable(false);
		txtFind.setFocusTraversable(false);
		
		EventHandler<KeyEvent> newSaveFindEdit = (EventHandler<KeyEvent>) (event)->{
//			Key.SAVE.
			if (Key.SAVE.match(event)) {
				saveText();
			}else if(Key.NEW.match(event)){
				newText();
			}else if(Key.FIND.match(event)){
				txtFind.requestFocus();
			}else if (Key.EDIT.match(event)) {
				txtFind.setText("");
				setEditor(true);
				taContent.requestFocus();
			}else if(Key.DEL.match(event)){
				deleteText(doc.get());
			}else if(Key.WEB.match(event)){
				setWeb();
			}
		};
		
		 
		taContent.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);		
		txtFind.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		web.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		
		
		txtFind.setOnKeyReleased(value ->{
			MongoCollection<Document> collection = DBUtils.getCollection();
			String word = txtFind.getText();
			testMongoCommand(word);
			
			if(!word.equals("")){
				txtList.getItems().remove(0, txtList.getItems().size());
				Bson proj = Projections.metaTextScore(word);
				
				MongoCursor<Document> cursor2 = DBUtils.getCollection()
						.find(text(word))
						.projection(proj)						
						.iterator();
								
				while(cursor2.hasNext()){
					Document docc = cursor2.next();
					if(!docc.get("_id").equals("conf")){
						System.out.println(docc.toJson());
						if(docc.get("content").toString().toLowerCase().contains(word.toLowerCase())){
							txtList.getItems().add(new LabelContent(docc));
						}else{
							taContent.setText("");
						}
						setEditor(!(txtList.getItems().size() > 0 && txtFind.getText().length() > 1));	
					}
										
				}	
			}
			
		});
	}
	
	void testMongoCommand(String word){
		switch(word){
			case "::find()":
				System.out.println("find all");
				break;
			case "::remove()":
				System.out.println("remove all");
				break;
		}
	}
	
	private void setWeb(){
		if(getChildren().contains(web)){
			getChildren().remove(1);
			getChildren().add(taContent);
		}else{
			getChildren().remove(1);
			getChildren().add(web);
			engine.loadContent(taContent.getText());	
		}
		
	}
	
	private void setEditor(boolean showEditor){
		if(!showEditor){
			if(getChildren().contains(taContent)){
				getChildren().remove(taContent);
				getChildren().add(txtList);	
			}			
		}else{
			if(getChildren().contains(txtList)){
				getChildren().remove(txtList);
				getChildren().add(taContent);
			}
		}
	}
	
	private void saveText(){
		if(doc.get() != null){
//			DBUtils.getCollection().updateOne(doc, new Document("content", taContent.getText()));
			DBUtils.getCollection().findOneAndDelete(new Document("_id", doc.get().get("_id")));
			doc.set(new Document("content", taContent.getText()));
			DBUtils.getCollection().insertOne(doc.get());
		}else{
			doc.set(new Document("content", taContent.getText()));
			DBUtils.getCollection().insertOne(doc.get());
		}
	}
	
	private void newText(){
		taContent.setText("");
		txtFind.setText("");
		doc.set(null);
		setEditor(true);
		taContent.requestFocus();
	}
	
	public void deleteText(Document doc){
		taContent.setText("");
		if(doc != null){
			DBUtils.getCollection().findOneAndDelete(new Document("_id", doc.get("_id")));	
		}	
		doc = null;
	}
	
	class LabelContent extends TextArea{
		Document docLoad;
		Insets insets = new Insets(3);
		CornerRadii corner = new CornerRadii(3);
		BackgroundFill bgfillBlue = new BackgroundFill(javafx.scene.paint.Color.LIGHTBLUE, corner, insets);
		Background backgroundBlue = new Background(bgfillBlue);
		
		BackgroundFill bgfillRed = new BackgroundFill(javafx.scene.paint.Color.ORANGE, corner, insets);
		Background backgroundOrange = new Background(bgfillRed);
		public LabelContent(Document doc) {			
			this.docLoad = doc;
			init();
			layouts();
			listeners();
		}
		
		void init(){
			setText(docLoad.getString("content"));
		}
		
		void layouts(){
			
			setPrefRowCount(2);
			autosize();
			setPrefWidth(Main.primaryStage.getWidth()-80);
			Main.primaryStage.widthProperty().addListener((InvalidationListener)(listener)->{
				setPrefWidth(Main.primaryStage.getWidth()-80);
			});
//			setPrefWidth(Main.primaryStage.getWidth());
		}
		
		void setBorderColor(Color color){
			BorderStrokeStyle bss = new BorderStrokeStyle(null, null, null, 10, 0, null);
			BorderStroke bs = new BorderStroke(color, color, color, color, bss,bss,bss,bss,corner, BorderWidths.DEFAULT, insets);
			
			Border border = new Border(bs);
			setBorder(border);
		}
		
		void listeners(){
			
			EventHandler<KeyEvent> events =(EventHandler<KeyEvent>)(event)-> {				
				if (Key.EDIT.match(event)) {
					editText();						
				}else if(Key.ALT_TAB.match(event)){
					setFocused(false);
				}else if(Key.FIND.match(event)){
					txtFind.requestFocus();
				}else if(Key.NEW.match(event)){
					newText();
					setEditor(true);
				}else if(Key.SAVE.match(event)){
					DBUtils.getCollection().findOneAndDelete(new Document("_id", docLoad.get("_id")));
					docLoad = new Document("content", getText());
					DBUtils.getCollection().insertOne(docLoad);
					setBorderColor(Color.GREEN);
				}else if(Key.DEL.match(event)){
					deleteText(docLoad);
					setBorderColor(Color.RED);
					setEditable(false);
				}else if(Key.RENEW.match(event)){
					docLoad = new Document("content", getText());
					DBUtils.getCollection().insertOne(docLoad);
					setBorderColor(Color.AZURE);
					setEditable(true);					
				}else if(Key.LIST_H_UP.match(event)){
					setPrefRowCount(getPrefRowCount()+1);				
				}else if(Key.LIST_H_DOWN.match(event)){
					setPrefRowCount(getPrefRowCount()-1);
				}
			};
			
			setOnKeyReleased(value->{
//				setBorderColor(Color.BLUE);
			});
			
			focusedProperty().addListener((InvalidationListener) (listener)->{
				if(isFocused()){
					positionCaret(getText().length());
				}
			});
			
			addEventHandler(KeyEvent.KEY_RELEASED, events);		
		}
		private void deleteText(Document doc){
			taContent.setText("");
			if(doc != null){
				DBUtils.getCollection().findOneAndDelete(new Document("_id", doc.get("_id")));	
			}	
			doc = null;
		}
		
		private void editText(){
			docLoad.append("content", getText());
			doc.set(docLoad);
			txtFind.setText("");
			taContent.setText(docLoad.getString("content"));						
			setEditor(true);
			taContent.requestFocus();
			taContent.positionCaret(taContent.getText().length());
		}
		
	}
	
}
