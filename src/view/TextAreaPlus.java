package view;


import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import application.Key;
import application.Main;

public class TextAreaPlus extends TextArea {
	public TextAreaPlus() {
		
		addEventHandler(KeyEvent.KEY_RELEASED, (EventHandler<KeyEvent>)(event)->{
			
		});
		setOnKeyReleased(value->{
			
			int caretPosition = getCaretPosition();
			int size = getText().length();
			String selection = getSelectedText();
			String nextText;
			System.out.println("released  "+value.getCode()+ " caret: "+caretPosition+ " size: "+size+ " selection: "+selection);
			
//			switch (value.getCode()) {
//				case BRACELEFT:
//					cerrarLlave("}", caretPosition, size);
//					break;
//				
//				}
			if(Key.CTRL_SPACE.match(value)){
				System.out.println(getContextMenu());
			}
				
		});
		
		setOnKeyPressed(value ->{
			int caretPosition = getCaretPosition();
			int size = getText().length();
			String selection = getSelectedText();

			System.out.println("pressed  "+ value.getCode()+ " caret: "+caretPosition+ " size: "+size+ " selection: "+selection);
			
			if(Key.CTRL_BRACELEFT.match(value)){
				System.out.println("CTRL_BRACELEFT"+ getSelection().getStart()+" "+getSelection().getEnd()+" "+getSelection().getLength());
				insertText(getSelection().getStart(), "{");
				insertText(getSelection().getEnd(), "}");
			}
			
		});
		
		setOnContextMenuRequested(value ->{
			System.out.println("context menu");
		});
		
	}
	
	
	
	private void cerrarLlave(String cierre, int caret, int size){
//		if(size != caret){
//			setText(getText(0, caret) + cierre+getText(caret, size));	
//		}else{
//			setText(getText(0, caret) + cierre);
//		}
		insertText(caret, cierre);
		positionCaret(caret);
	}
	
}
