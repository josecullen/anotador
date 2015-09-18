package application;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.BooleanProperty;

import org.bson.Document;

import com.mongodb.client.model.Filters;

public class Conf {

	
	private static Conf conf = new Conf();
	private DoubleProperty 
		height = new SimpleDoubleProperty(),
		width = new SimpleDoubleProperty();
	private BooleanProperty
		traductorVisible = new SimpleBooleanProperty(false);
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
				.append("traductor", false)
				.append("lastDoc", lastDoc.get());	
			DBUtils.getCollection().insertOne(confDoc);
		}else{
			System.out.println(confDoc.toJson());
			height.set(confDoc.getDouble("height"));
			width.set(confDoc.getDouble("width"));
			traductorVisible.set(confDoc.getBoolean("traductor", false));
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
		DBUtils.getCollection().updateOne(
				Filters.eq("_id", "conf"), new Document( "$set", new Document("lastDoc", lastDoc )));
	}
	

	public final BooleanProperty traductorVisibleProperty() {
		return this.traductorVisible;
	}

	public final boolean isTraductorVisible() {
		return this.traductorVisibleProperty().get();
	}

	public final void setTraductorVisible(final boolean traductorVisible) {
		this.traductorVisibleProperty().set(traductorVisible);
		DBUtils.getCollection().updateOne(
				Filters.eq("_id", "conf"), new Document( "$set", new Document("traductor", traductorVisible )));
	}
	

	
	
	
}
