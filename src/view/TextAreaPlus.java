package view;



import javax.swing.event.CaretEvent;
import javax.swing.text.Caret;

import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import application.Key;
import application.Main;

public class TextAreaPlus extends TextArea {
	public TextAreaPlus() {
		setWrapText(true);
		
		
		
		
		addEventHandler(KeyEvent.KEY_RELEASED, e ->{
			int caretPosition = getCaretPosition();
			int size = getText().length();
			String selection = getSelectedText();
//			System.out.println("released  "+value.getCode()+ " caret: "+caretPosition+ " size: "+size+ " selection: "+selection);
			IndexRange ir = new IndexRange(2, 4);
			
			if(Key.CTRL_BRACELEFT.match(e)){
				int start = getSelection().getStart();
				int end = getSelection().getEnd();
				insertText(start, "{ ");
				insertText(end+2, " }");
			} else if(KeyCode.ENTER.equals(e.getCode())){
//				selectPreviousWord();
//				String word = getSelectedText();
//				selectEnd();
//				if(word.contains("{")){					
//					insertText(getCaretPosition(), "    \n}");
//					positionCaret(getCaretPosition()-2);					
//				}else{
//					deselect();
//				}
			} else if(KeyCode.LESS.equals(e.getCode())){
				
//				setContextMenu(new HTMLContextMenu());
				getContextMenu().show(Main.primaryStage);
			}
		});
		
		
		
		setOnKeyReleased(value->{			
			int caretPosition = getCaretPosition();
			int size = getText().length();
			String selection = getSelectedText();
//			System.out.println("released  "+value.getCode()+ " caret: "+caretPosition+ " size: "+size+ " selection: "+selection);
			IndexRange ir = new IndexRange(2, 4);
			
			
			if(Key.CTRL_BRACELEFT.match(value)){
				int start = getSelection().getStart();
				int end = getSelection().getEnd();
				insertText(start, "{ ");
				insertText(end+2, " }");
			} else if(KeyCode.ENTER.equals(value.getCode())){
				selectPreviousWord();
				String word = getSelectedText();
				selectEnd();
				if(word.contains("{")){					
					insertText(getCaretPosition(), "    \n}");
					positionCaret(getCaretPosition()-2);					
				}				
			}
				
		});
		
		
	}
	
	
	
}
