package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TranslatorField extends HBox {
	Label lblTranslate;
	ChoiceBox<String> cbFrom, cbTo;
	ObservableList<String> items = FXCollections.observableArrayList();
	
	public TranslatorField() {
		init();
		
		getChildren().addAll(lblTranslate, cbFrom, cbTo);
	}
	
	private void init(){
		lblTranslate = new Label();
		cbFrom = new ChoiceBox<String>(items);
		cbTo = new ChoiceBox<String>(items);
	}
	
	
	
	
}
