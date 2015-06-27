package application;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import org.bson.Document;

import com.mongodb.client.model.Filters;

public class Conf {
//	public static final KeyCombination keyCombSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombNew = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombFind = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombEdit = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombTab = new KeyCodeCombination(KeyCode.TAB, KeyCombination.ALT_DOWN);
//	public static final KeyCombination keyCombDel = new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombRenew = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombWeb = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombListItemHeight_UP = new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
//	public static final KeyCombination keyCombListItemHeight_DOWN = new KeyCodeCombination(KeyCode.BRACERIGHT, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
//	public static final KeyCombination keyCombPlusW = new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombBraceW = new KeyCodeCombination(KeyCode.BRACERIGHT, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombPlusH = new KeyCodeCombination(KeyCode.PLUS, KeyCombination.ALT_DOWN);
//	public static final KeyCombination keyCombBraceH = new KeyCodeCombination(KeyCode.BRACERIGHT, KeyCombination.ALT_DOWN);
//	public static final KeyCombination keyCombDecorate = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
//	
//	public static final KeyCombination keyCombMoveUp = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombMoveDown = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombMoveLeft = new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN);
//	public static final KeyCombination keyCombMoveRight = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
	
	
	
	private static Conf conf = new Conf();
	private DoubleProperty 
		height = new SimpleDoubleProperty(),
		width = new SimpleDoubleProperty();
	boolean 
		centered;
	ObjectProperty<Document> 
		lastDoc = new SimpleObjectProperty<Document>();
	Document
		confDoc;
	
	public static Conf getConf(){
		return conf;
	}
	
	private Conf(){
		
		confDoc = DBUtils.getConfDoc();
		if(confDoc == null){			
			confDoc = new Document();
			confDoc
				.append("_id", "conf")
				.append("width", 400d)
				.append("height", 400d)
				.append("lastDoc", lastDoc);	
			DBUtils.getCollection().insertOne(confDoc);
		}else{
			System.out.println(confDoc.toJson());
			height.set(confDoc.getDouble("height"));
			width.set(confDoc.getDouble("width"));
			Document _ld = (Document) confDoc.get("lastDoc");
			if(_ld != null){
				lastDoc.set(_ld);	
			}
			
		}
		setListeners();
	}
	
	private void setListeners(){
		widthProperty().addListener((InvalidationListener) (listener)->{
			confDoc.append("width", width.get());
//			save();
			DBUtils.getCollection().updateOne(
					Filters.eq("_id", "conf"), new Document( "$set", new Document("width", width.get() )));
		});
		
		heightProperty().addListener((InvalidationListener) (listener)->{
			confDoc.append("height", height.get());
			DBUtils.getCollection().updateOne(
					Filters.eq("_id", "conf"), new Document( "$set", new Document("height", height.get() )));

		});
		
		lastDocProperty().addListener((InvalidationListener) (listener)->{
			confDoc.append("lastDoc", lastDoc.get());
			DBUtils.getCollection().updateOne(
					Filters.eq("_id", "conf"), new Document( "$set", new Document("lastDoc", lastDoc.get() )));

		});
		
		
	}

	public final DoubleProperty heightProperty() {
		return this.height;
	}

	public final double getHeight() {
		return this.heightProperty().get();
	}

	public final void setHeight(final double height) {
		this.heightProperty().set(height);
	}

	public final DoubleProperty widthProperty() {
		return this.width;
	}

	public final double getWidth() {
		return this.widthProperty().get();
	}

	public final void setWidth(final double width) {
		this.widthProperty().set(width);
		save();
	}

	public final ObjectProperty<Document> lastDocProperty() {
		return this.lastDoc;
	}

	public final org.bson.Document getLastDoc() {
		return this.lastDocProperty().get();
	}

	public final void setLastDoc(final org.bson.Document lastDoc) {
		this.lastDocProperty().set(lastDoc);
		confDoc.append("lastDoc", lastDoc);
	}
	
	
	private void save(){
//		DBUtils.getCollection().findOneAndDelete(new Document("_id", "conf"));
//		DBUtils.getCollection().insertOne(confDoc);
//		DBUtils.getCollection().updateOne(Filters.eq("_id", "conf"), confDoc);
//		System.out.println(confDoc.toJson());
	}
	
	
	
	
}
