package view.contextmenu;

import com.sun.javafx.collections.ObservableListWrapper;

import model.Tag;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.Key;
import application.TagServices;

public class HTMLContext extends Stage {
	Tag tag = new Tag();
	HBox box;
	Label tagPreview;
	WebView htmlPreview;
	WebEngine webEngine;
	ListView<Node> listTags, listProperties;
	public ObservableList<Node> tagItems, propertyItems;
	
	
	
	
	public HTMLContext() {
		init();		
		setLayout();
		setListeners();
		show();
	}
	
	private void init(){
		htmlPreview = new WebView();
		webEngine = htmlPreview.getEngine();
		
		tagItems = FXCollections.observableArrayList();
		propertyItems = FXCollections.observableArrayList();
		
		TagServices.getAllTags().forEach(action -> tagItems.add(new Label(action)));
		TagServices.getAllProperties().forEach(action -> propertyItems.add(new TextFieldListCell<String>()));
//		TagServices.getAllProperties().forEach(action ->{
//			TextField prop = new TextField(); 
//			prop.textProperty().addListener((InvalidationListener) invalidation->{
//				tag.put(action, prop.getText());
//			});
//			
//			prop.setPromptText(action);
//			Tooltip tooltip = new Tooltip(action);
//			prop.setTooltip(tooltip);
//			
//			prop.focusedProperty().addListener((InvalidationListener) listener ->{
//				if(prop.isFocused()){
//					prop.getTooltip().setOpacity(0.8d);
//					prop.getTooltip().setAutoFix(true);
//					prop.getTooltip().setX(prop.getWidth()+this.getX()+listTags.getWidth());
//					prop.getTooltip().setY(propertyItems.indexOf(prop) * prop.getHeight()+this.getY());
//					prop.getTooltip().show(this);					
//				}else{
//					prop.getTooltip().hide();
//				}
//			});
//			propertyItems.add(prop);
//			
//		});
		
		
		
		TextField content = new TextField();
		content.setPromptText("content");
		content.textProperty().bindBidirectional(tag.content);
		propertyItems.add(content);
		
		
		listTags = new ListView<Node>(tagItems);
		listProperties = new ListView<Node>(propertyItems);
		
	}
	
	public int findFocus(){
		for(int i = 0; i < tagItems.size(); i++){			
			if(tagItems.get(i).isFocused()){
				return i;
			}
		}
		return - 1;
	}
	
	
	private void setLayout(){
		initStyle(StageStyle.UNDECORATED);
		
		ListView list = new ListView(new ObservableListWrapper(TagServices.getAllProperties()));
		list.setCellFactory(TextFieldListCell.forListView());
		list.setEditable(true);
		
		
		
		
		setOpacity(0.96);
		listTags.setPrefSize(200, 150);
		tagPreview = new Label("");
		tagPreview.textProperty().bind(tag.allContent);
		box = new HBox(list);
//		box = new HBox(listTags);
		listProperties.setFocusTraversable(false);
		htmlPreview.setPrefHeight(100);
		box.setFocusTraversable(false);
		VBox firstBox = new VBox(tagPreview, box, htmlPreview);
		Scene scene = new Scene(firstBox, 400, 150);
		setScene(scene);
	}

	private void setListeners(){
		
		tag.allContent.addListener((InvalidationListener) listener ->{
			webEngine.loadContent(tag.allContent.get());
		});
		
		listTags.getSelectionModel().selectedIndexProperty().addListener((InvalidationListener) listener->{
			Label item = (Label) tagItems.get(listTags.getSelectionModel().getSelectedIndex());
			if(item != null){
				tag.tagName.set(item.getText());
			}
		});
		
		
		addEventHandler(KeyEvent.KEY_RELEASED, e ->{
			if(Key.ALT_RIGHT.match(e)){
				if(!box.getChildren().contains(listProperties)){
					box.getChildren().add(1, listProperties);
					listProperties.setPrefHeight(listTags.getPrefHeight());
					listProperties.requestFocus();
				}
			}else if(Key.ALT_LEFT.match(e)){
				if(box.getChildren().contains(listProperties)){
					box.getChildren().remove(listProperties);
				}
			}
			
			switch(e.getCode()){

				
			}			
		});
	}
	
	
}
