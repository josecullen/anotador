package view;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import org.bson.Document;

import controller.ContentState;
import application.NotaServices;
import application.Key;
import application.Styles;

public class TextAreaPlusDocument extends TextAreaPlus {
	public ObjectProperty<Document> doc = new SimpleObjectProperty<Document>();

	private ContentState contentState;
	
	
	public TextAreaPlusDocument() {
		super();
		doc = new SimpleObjectProperty<Document>(NotaServices.getNew());
		doc.get().append("content", "hola mundo");
		setText(doc.get().getString("content"));
		setContentState(ContentState.SAVED);
		setListeners();
	}
	
	public ContentState getContentState(){
		return contentState;
	}
	
	public void setContentState(ContentState contentState){
		this.contentState = contentState; 
		switch (contentState) {
			case SAVED:
				setBorder(Styles.getBorder(Color.GREEN));
				setEditable(true);
				break;
			case UNSAVED:
				setBorder(Styles.getBorder(Color.BLUE));
				setEditable(true);
				break;
			case DELETED:
				setBorder(Styles.getBorder(Color.RED));
				setEditable(false);
				break;
			case RESTORED:
				setBorder(Styles.getBorder(Color.BLUEVIOLET));
				setEditable(true);
				break;				
		}
	}
	
	
	
	
	public void load(Document doc){
		saveLastChanges();
		NotaServices.setInLastOrder(doc);
		this.doc.set(doc);
		resetTextFromDoc();
	}	
	
	
	
	public void saveLastChanges(){
		NotaServices.saveLastChanges(this.doc.get());
	}
	
	public void save(){
		NotaServices.passLastChangeAndSave(doc.get(), false);
		setContentState(ContentState.SAVED);
	}
	
	public void delete(){
		NotaServices.remove(doc.get());
		setContentState(ContentState.DELETED);		
	}
	
	public void restore(){
		NotaServices.save(doc.get(), false);
		setContentState(ContentState.RESTORED);
		resetTextFromDoc();
	}
	
	public void next(){
		saveLastChanges();
		doc.set(NotaServices.getNext(doc.get()));
		resetTextFromDoc();
	}
	
	public void previous(){
		saveLastChanges();
		doc.set(NotaServices.getPrevious(doc.get()));
		resetTextFromDoc();
	}
	
	public void newDoc(){
		NotaServices.saveLastChanges(doc.get());
		doc.set(NotaServices.getNew());
		resetTextFromDoc();
	}
	
	
	private void setListeners(){
		setOnKeyReleased(e->{
			doc.get().append("lastChanges", getText());
			System.out.println(e.getCode());
			if(Key.ALT_LEFT.match(e)){
				previous();
			}else if(Key.ALT_RIGHT.match(e)){
				next();
			}else if(Key.NEW.match(e)){
				newDoc();
			}else if(Key.SAVE.match(e)){
				save();
			}else if(Key.DEL.match(e)){
				delete();
			}else if(Key.RENEW.match(e)){
				restore();
			}

		});

		textProperty().addListener((InvalidationListener) listener ->{
			if(getContentState() == ContentState.SAVED){
				if(isContentChange()){
					setContentState(ContentState.UNSAVED);
				}
			}
		});
	}
	
	private void resetTextFromDoc(){
		if(isUnsaved()){
			setText(doc.get().getString("lastChanges"));
			setContentState(ContentState.UNSAVED);
		}else{
			doc.get().append("lastChanges", doc.get().getString("content"));
			setText(doc.get().getString("content"));
		}
	}
	
	
	private boolean isContentChange(){
		return !doc.get().getString("content").equals(getText());
	}
	
	private boolean isUnsaved(){
		return !doc.get().getString("lastChanges").isEmpty() && doc.get().getString("lastChanges") != "";
	}
	
	
}
