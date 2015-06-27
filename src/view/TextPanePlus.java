package view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class TextPanePlus extends VBox {
	public TextArea textArea;
	WebView webView;
	WebEngine webEngine;
	
	public TextPanePlus() {
		init();
		setView();
	}
	
	private void init(){
		textArea = new TextArea();
	}
	
	private void setView(){
		getChildren().add(textArea);
	}
	
	
}
