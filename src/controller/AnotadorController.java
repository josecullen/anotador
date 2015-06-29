package controller;

import static com.mongodb.client.model.Filters.text;

import java.io.IOException;

import javax.jws.soap.SOAPBinding.Style;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
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

import view.TextAreaPlusDocument;
import application.Conf;
import application.DBServices;
import application.DBUtils;
import application.Key;
import application.Main;
import application.Styles;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
public class AnotadorController extends VBox {
	
	//	@FXML Button btnCancelar;
//	, btnGuardar;
	@FXML TextAreaPlusDocument taContent;
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
		taContent.setFont(Font.font("Monospaced",FontWeight.BOLD,12));
		
		setListeners();
		taContent.autosize();
		VBox.setVgrow(txtList, Priority.ALWAYS);
		txtList.autosize();
		
		
	}
	
	
	private void setListeners(){
		Document lastDoc = Conf.getConf().getLastDoc();
		if(lastDoc != null){
			doc.set(lastDoc);
			taContent.setText(getTextFromDoc(doc.get()));
			taContent.positionCaret(taContent.getText().length());
		}else{
			doc.set(DBServices.getNew());
		}
		taContent.doc.bindBidirectional(doc);
		
		Conf.getConf().lastDocProperty().bindBidirectional(doc);
		txtList.setFocusTraversable(false);
		txtFind.setFocusTraversable(false);
		
		EventHandler<KeyEvent> newSaveFindEdit = (EventHandler<KeyEvent>) (event)->{
//			if (Key.SAVE.match(event)) {
//				saveText();
//			}else if(Key.NEW.match(event)){
//				newText();
//			}
			
			if(Key.FIND.match(event)){
				txtFind.requestFocus();
			}else if (Key.EDIT.match(event)) {
				txtFind.setText("");
				setEditor(true);
				taContent.requestFocus();
			}
//			else if(Key.DEL.match(event)){
//				taContent.delete();
//				deleteText(doc.get());
//			}
//			Propios de TextArea
//			else if(Key.WEB.match(event)){
//				setWeb();
//			}else if(Key.ALT_LEFT.match(event)){
//				DBServices.saveLastChanges(doc.get());
//				doc.set(DBServices.getPrevious(doc.get()));
//				resetTaContent();
//			}else if(Key.ALT_RIGHT.match(event)){
//				DBServices.saveLastChanges(doc.get());
//				doc.set(DBServices.getNext(doc.get()));
//				resetTaContent();
//			}
		}; 
		taContent.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);		
		txtFind.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		web.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		
		
		txtFind.setOnKeyReleased(value ->{
			
			String words = txtFind.getText();
			testMongoCommand(words);
			if(!words.equals("")){
				txtList.getItems().remove(0, txtList.getItems().size());
					
				MongoCursor<Document> cursor = DBServices.textMatch(words);
				cursor.forEachRemaining(action->{
					if(!action.get("_id").equals("conf")){
						System.out.println(action.toJson());
						if(action.get("content").toString().toLowerCase().contains(words.toLowerCase())){
							txtList.getItems().add(new LabelContent(action));
						}else{
							taContent.setText("");
						}
						setEditor(!(txtList.getItems().size() > 0 && txtFind.getText().length() > 1));	
					}
				});
//				while(cursor.hasNext()){
//					Document docc = cursor.next();
//					if(!docc.get("_id").equals("conf")){
//						System.out.println(docc.toJson());
//						if(docc.get("content").toString().toLowerCase().contains(words.toLowerCase())){
//							txtList.getItems().add(new LabelContent(docc));
//						}else{
//							taContent.setText("");
//						}
//						setEditor(!(txtList.getItems().size() > 0 && txtFind.getText().length() > 1));	
//					}
//										
//				}	
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
		doc.get().append("lastChanges", taContent.getText());
		if(doc.get() != null){
			DBServices.passLastChangeAndSave(doc.get(), false);
		}else{
			doc.set(new Document("content", taContent.getText()));
			DBServices.passLastChangeAndSave(doc.get(), true);
		}
	}
	
	private void newText(){		
		doc.set(DBServices.getNew());
		resetTaContent();		
	}
	
	private void resetTaContent(){		
		taContent.setText(getTextFromDoc(doc.get()));
		txtFind.setText("");
		setEditor(true);
		taContent.requestFocus();
	}
	
	private String getTextFromDoc(Document doc){
		if(doc.getString("lastChanges") != null && !doc.getString("lastChanges").isEmpty()){
			return doc.getString("lastChanges");
		}else{
			return doc.getString("content");
		}
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
					setBorder(Styles.getBorder(Color.GREEN));

				}else if(Key.DEL.match(event)){
					deleteText(docLoad);
					setBorder(Styles.getBorder(Color.RED));
					setEditable(false);
				}else if(Key.RENEW.match(event)){
					docLoad = new Document("content", getText());
					DBUtils.getCollection().insertOne(docLoad);
					setBorder(Styles.getBorder(Color.AZURE));
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
			DBServices.save(doc.get(), true);
			doc.get().append("lastChanges", docLoad.getString("content"));
			taContent.setText(doc.get().getString("lastChanges"));						
			setEditor(true);
			taContent.requestFocus();
			taContent.positionCaret(taContent.getText().length());
		}
		
	}
	
}
