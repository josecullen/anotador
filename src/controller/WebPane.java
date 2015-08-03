package controller;

import javax.swing.SwingUtilities;

import org.w3c.dom.NodeList;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebPane extends BorderPane{
	private static String URL_TRANSLATOR = "https://translate.google.es/";
	private static TranslatorLabel translatorLabel= new TranslatorLabel();
//	StringProperty selection;
	
	WebView web;
	WebEngine engine;
	
	public WebPane() {
		init();
		setLayout();
		setListeners();
	}
	
	
	
//	public void bindSelection(ReadOnlyStringProperty selectionProperty){
//		selection.bind(selectionProperty);		
//		selection.addListener(listener ->{		
//			System.out.println(selection.getValue());
//			if(selection.getValue().length() > 2){
//				translate(selection.getValue());
//			}				
//		});
//	}
	
	public void loadContent(String content){
		engine.loadContent(content);
	}	
	
	
	public void translate(String content){
		engine.load("https://translate.google.es/#"+translatorLabel.getFrom()+"/"+translatorLabel.getTo()+"/"+content);		
		
		
		Thread task = new Thread(()->{
			
			for(int i = 0; i < 6; i++){
				try {
					Thread.currentThread().sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Platform.runLater(()->{
					printFromGoogleTranslate();
				});
			}
			


		});
		
		task.start();
		
	}	
	
	public TranslatorLabel getTransLatorLabel(){
		return translatorLabel;
	}
	
	private void init(){
		web = new WebView();
		engine = web.getEngine();
//		selection = new SimpleStringProperty("");
	}
	
	private void setLayout(){
		setCenter(web);
	}
	
	private void setListeners(){
		engine.getLoadWorker().stateProperty().addListener(updateTranslator);
		engine.getLoadWorker().progressProperty().addListener(listener ->{
//			translatorLabel.progress.setProgress(engine.getLoadWorker().getProgress());
			System.out.println(engine.getLoadWorker().getProgress());
		});
//		engine.documentProperty().addListener(listener ->{
//			System.out.println("documentProperty "+ listener);
//		});

	}
	
	ChangeListener<Worker.State> updateTranslator = new ChangeListener<Worker.State>() {

		@Override
		public void changed(ObservableValue<? extends State> observable, State oldState, State newState) {
			System.out.println(newState+ " "+engine.getLocation().contains(URL_TRANSLATOR) + " "+engine.getLocation());
			
			if(newState == State.SUCCEEDED){		// && engine.getLocation().contains(URL_TRANSLATOR)		
				if(engine.getDocument().getElementById("result_box") != null){
					printFromGoogleTranslate();
				}							
			}else{
				if(engine.getDocument() != null){
					if(engine.getDocument().getElementById("result_box") != null){						
						printFromGoogleTranslate();
					}
				}
			}		
			
		}
		
	};
	
	void sleep(int delayTime){
		long delay = System.currentTimeMillis()+delayTime;
		while(System.currentTimeMillis() < delay);
	}
	
	public void printFromGoogleTranslate(){
		StringBuilder result = new StringBuilder();
		if(engine.getDocument() != null){
			if(engine.getDocument().getElementById("result_box") != null){
				NodeList nodeList = engine.getDocument().getElementById("result_box").getChildNodes();		
				for(int i = 0; i < nodeList.getLength(); i++){
					result.append(nodeList.item(i).getTextContent());
				}
				translatorLabel.setText(result.toString());
				System.out.println(result.toString());
			}
		}
		

	}

	
	
}

class TranslatorLabel extends HBox {
	TextField txtTranslate;
	ChoiceBox<String> cbFrom, cbTo;
	ObservableList<String> items = FXCollections.observableArrayList();
	public ProgressBar progress;
	
	
	public TranslatorLabel() {
		init();		
		setLayout();	
	}
	
	private void init(){
		progress = new ProgressBar();
		txtTranslate = new TextField();		
		items.addAll("es","en");
		cbFrom = new ChoiceBox<String>(items);
		cbTo = new ChoiceBox<String>(items);
		cbFrom.getSelectionModel().select(1);
		cbTo.getSelectionModel().select(0);
	}
	
	private void setLayout(){		
		HBox.setHgrow(txtTranslate, Priority.ALWAYS);
		VBox.setVgrow(txtTranslate, Priority.ALWAYS);
	    setAlignment(Pos.BASELINE_LEFT);
	    txtTranslate.setEditable(false);
	    txtTranslate.setFocusTraversable(false);
		getChildren().addAll(txtTranslate, progress, cbFrom, cbTo);	
		VBox.setVgrow(progress, Priority.ALWAYS);
		progress.setPrefWidth(25);
		progress.setVisible(false);
		
		progress.progressProperty().addListener(listener ->{
			System.out.println(progress.getProgress());

			if(progress.getProgress() != 1 && progress.getProgress() >= 0){
				progress.setVisible(true);
			}else{
				progress.setVisible(false);
			}
			
		});
	}
	
	public void setText(String value){
		txtTranslate.setText(value);
	}
	
	public String getFrom(){
		return cbFrom.getSelectionModel().getSelectedItem();
	}
	
	public String getTo(){
		return cbTo.getSelectionModel().getSelectedItem();
	}
		
	
}
