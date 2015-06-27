package controller;

import java.awt.event.TextEvent;

import javafx.beans.InvalidationListener;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;
import view.TextPanePlus;

public class TextPanePlusController extends TextPanePlus{
	
	
	public TextPanePlusController() {
//		InvalidationListener changeCaret = (InvalidationListener)(observable-> {
//			int caretPosition = textArea.getCaretPosition();
//			int size = textArea.getText().length();
//			String nextText;
//			if(size != caretPosition){
//				String letter = textArea.getText(caretPosition, caretPosition+1);
//				if(letter.equals("{")){
//					System.out.println("{");
//					textArea.setText(textArea.getText(0, caretPosition) + "}"+textArea.getText(caretPosition, size));
//				}	
//			}
//
//			
//		});
		textArea.setOnKeyReleased(value->{
			
			int caretPosition = textArea.getCaretPosition();
			int size = textArea.getText().length();
			String nextText;
			System.out.println(value.getCode()+ " caret: "+caretPosition+ " size: "+size);
			
			switch (value.getCode()) {
			case BRACELEFT:
				if(size != caretPosition){
					textArea.setText(textArea.getText(0, caretPosition) + "}"+textArea.getText(caretPosition, size));	
				}else{
					textArea.setText(textArea.getText(0, caretPosition) + "}");
				}
				textArea.positionCaret(caretPosition);
				

				break;

			}
			
		});
		
		
		
//		textArea.caretPositionProperty().addListener(changeCaret);
	}
	
	
	
	
}
