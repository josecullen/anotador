package application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public enum Key{
	SAVE 		(KeyCode.S, KeyCombination.CONTROL_DOWN),
	NEW 		(KeyCode.N, KeyCombination.CONTROL_DOWN),
	FIND		(KeyCode.F, KeyCombination.CONTROL_DOWN),
	EDIT		(KeyCode.E, KeyCombination.CONTROL_DOWN),
	ALT_TAB		(KeyCode.TAB, KeyCombination.ALT_DOWN),
	DEL 		(KeyCode.DELETE, KeyCombination.CONTROL_DOWN),
	RENEW		(KeyCode.R, KeyCombination.CONTROL_DOWN),
	WEB			(KeyCode.W, KeyCombination.CONTROL_DOWN),
	LIST_H_UP	(KeyCode.PLUS, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
	LIST_H_DOWN	(KeyCode.BRACERIGHT, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
	WIDTH_UP	(KeyCode.PLUS, KeyCombination.CONTROL_DOWN),
	WIDTH_DOWN  (KeyCode.BRACERIGHT, KeyCombination.CONTROL_DOWN),
	HEIGTH_UP	(KeyCode.PLUS, KeyCombination.ALT_DOWN),
	HEIGTH_DOWN	(KeyCode.BRACERIGHT, KeyCombination.ALT_DOWN),
	DECORATE	(KeyCode.D, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
	CTRL_SPACE 	(KeyCode.SPACE, KeyCombination.CONTROL_DOWN),
	CTRL_BRACELEFT 	(KeyCode.BRACELEFT, KeyCombination.CONTROL_DOWN),

	ALL_LIST_UP 	(KeyCode.PLUS, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN),
	ALL_LIST_DOWN	(KeyCode.BRACERIGHT, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN),
	
	ALT_LEFT		(KeyCode.LEFT, KeyCombination.ALT_DOWN),
	ALT_RIGHT		(KeyCode.RIGHT, KeyCombination.ALT_DOWN),
	
	
	MOVE_UP		(KeyCode.I, KeyCombination.CONTROL_DOWN),
	MOVE_DOWN	(KeyCode.K, KeyCombination.CONTROL_DOWN),
	MOVE_LEFT	(KeyCode.J, KeyCombination.CONTROL_DOWN),
	MOVE_RIGTH	(KeyCode.L, KeyCombination.CONTROL_DOWN), 
	
	
	OLD 		(KeyCode.O, KeyCombination.CONTROL_DOWN);
	
	KeyCombination key;
	Key(KeyCode code,KeyCombination.Modifier ...comb){
		key = new KeyCodeCombination(code, comb);
	}
	
	public boolean match(KeyEvent event){
		return key.match(event);
	}
	
	
	
}
