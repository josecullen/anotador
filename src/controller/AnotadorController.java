package controller;

import java.io.IOException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.bson.Document;

import view.TextAreaPlusDocument;
import application.Conf;
import application.DBUtils;
import application.Key;
import application.Main;
import application.NotaServices;
import application.Styles;

import com.mongodb.client.MongoCursor;
public class AnotadorController extends VBox {
	
	@FXML TextAreaPlusDocument taContent;
	@FXML TextField txtFind;	
	ListView<LabelContent> txtList = new ListView<LabelContent>();
	WebPane webPane = new WebPane();
	
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
		
		Main.primaryStage.setOnCloseRequest(value ->{
			taContent.save();
			Conf.getConf().setLastDoc(doc.get());
		});
		
//		Stage testWebStage = new Stage();
//		testWebStage.setScene(new Scene(webPane, 1000, 500));
//		testWebStage.show();
		if(Conf.getConf().isTraductorVisible()){
			showTranslator();
		}
	
	}
	
	
	private void setListeners(){

		Document lastDoc = Conf.getConf().getLastDoc();		
		if(lastDoc != null){
			doc.set(lastDoc);
			taContent.setText(getTextFromDoc(doc.get()));
			taContent.positionCaret(taContent.getText().length());
		}else{
			doc.set(NotaServices.getNew());
		}
		taContent.doc.bindBidirectional(doc);
		taContent.load(doc.get());
		
		Conf.getConf().lastDocProperty().bindBidirectional(doc);
		txtList.setFocusTraversable(false);
		txtFind.setFocusTraversable(false);
		
		EventHandler<KeyEvent> newSaveFindEdit = (EventHandler<KeyEvent>) (event)->{
			if(Key.FIND.match(event)){
				txtFind.requestFocus();
			}else if (Key.EDIT.match(event)) {
				txtFind.setText("");
				setEditor(true);
				taContent.requestFocus();
			}else if(Key.WEB.match(event)){
				setWeb();
			}else if(Key.TRANSLATOR.match(event)){
				setTransLator();
			}else if(Key.TRANSLATOR_EXPRESS.match(event)){
				showTranslator();
			}
			

		}; 
		taContent.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);		
		taContent.addEventHandler(KeyEvent.KEY_PRESSED, e->{
			if(Key.CTRL_SPACE.match(e)){
				webPane.translate(taContent.getSelectedText());
			}
		});	
		txtFind.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		
//		web.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		webPane.addEventHandler(KeyEvent.KEY_RELEASED, newSaveFindEdit);
		
		txtList.addEventHandler(KeyEvent.KEY_RELEASED, event ->{
			if(Key.ALL_LIST_UP.match(event)){
				txtList.getItems().forEach(action ->{
					action.setPrefRowCount(action.getPrefRowCount()+1);	
				});
								
			}else if(Key.ALL_LIST_DOWN.match(event)){
				txtList.getItems().forEach(action ->{
					action.setPrefRowCount(action.getPrefRowCount()-1);	
				});
			}
		});
		
		
		txtFind.setOnKeyReleased(value ->{
			
			String words = txtFind.getText();
			testMongoCommand(words);
			if(!words.equals("")){
				txtList.getItems().remove(0, txtList.getItems().size());
					
				MongoCursor<Document> cursor = NotaServices.textMatch(words);
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
		switchWeb();
		webPane.loadContent(taContent.getText());
	}
	
	private void setTransLator(){
		switchWeb();
		String content = taContent.getSelectedText().isEmpty() ? taContent.getText() : taContent.getSelectedText(); 
		webPane.translate(content);
	}
	
	private void switchWeb(){
		if(getChildren().contains(webPane)){
			getChildren().remove(webPane);
			getChildren().add(1, taContent);
		}else{
			getChildren().remove(taContent);
			getChildren().add(1, webPane);			
		}
	}
	
	private void showTranslator(){
		if(!getChildren().contains(webPane.getTransLatorLabel())){
			getChildren().add(webPane.getTransLatorLabel());
			Conf.getConf().setTraductorVisible(true);
		}else{
			getChildren().remove(webPane.getTransLatorLabel());
			Conf.getConf().setTraductorVisible(false);
		}
	}
	
	
	private void setEditor(boolean showEditor){
		if(!showEditor){
			if(getChildren().contains(taContent)){
				getChildren().remove(taContent);
				getChildren().add(1, txtList);	
			}			
		}else{
			if(getChildren().contains(txtList)){
				getChildren().remove(txtList);
				getChildren().add(1, taContent);
			}
		}
	}
	
//	private void saveText(){
//		doc.get().append("lastChanges", taContent.getText());
//		if(doc.get() != null){
//			NotaServices.passLastChangeAndSave(doc.get(), false);
//		}else{
//			doc.set(new Document("content", taContent.getText()));
//			NotaServices.passLastChangeAndSave(doc.get(), true);
//		}
//	}
	
	private void newText(){		
		doc.set(NotaServices.getNew());
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
			NotaServices.save(doc.get(), true);
			doc.get().append("lastChanges", docLoad.getString("content"));
			taContent.setText(doc.get().getString("lastChanges"));						
			setEditor(true);
			taContent.requestFocus();
			taContent.positionCaret(taContent.getText().length());
		}
		
	}
	
}
