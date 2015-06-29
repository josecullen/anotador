package view;


import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import application.Key;

public class TextAreaPlus extends TextArea {
	public TextAreaPlus() {
		
		setOnKeyReleased(value->{			
			int caretPosition = getCaretPosition();
			int size = getText().length();
			String selection = getSelectedText();
			System.out.println("released  "+value.getCode()+ " caret: "+caretPosition+ " size: "+size+ " selection: "+selection);
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
