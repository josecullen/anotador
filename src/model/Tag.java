package model;


import javafx.beans.InvalidationListener;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;

public class Tag {
	public MapProperty<String, String> properties = new SimpleMapProperty<String, String>(FXCollections.observableHashMap());
	public StringProperty tagName = new SimpleStringProperty("");
	public StringProperty allContent = new SimpleStringProperty("");
	public StringProperty content = new SimpleStringProperty("");
	
	
	public Tag() {
		allContent.set("< ></>");
		tagName.addListener((ChangeListener<String>) (observable, oldValue, newValue)->{
			if(oldValue != null && !oldValue.equals("") && allContent.get().contains(oldValue)){
				allContent.set(allContent.get().replaceAll(oldValue, newValue));
			}else{
				allContent.set("<"+newValue+" >"+"</"+newValue+">");
			}
		});
		
		properties.addListener((MapChangeListener<String, String>) (change) ->{
			if(change.wasAdded() && change.wasRemoved()){
				allContent.set(allContent.get().replaceFirst(getOldProperty(change), getNewProperty(change)));
			}else if(change.wasAdded() && !change.wasRemoved()){
				allContent.set(allContent.get().replaceFirst(" ", " "+getNewProperty(change)+" "));
			}else{
				allContent.set(allContent.get().replaceFirst(getOldProperty(change), ""));
			}
			allContent.set(allContent.get().replaceAll("  ", " "));
		});		
		
		content.addListener((ChangeListener<String>) (observable, oldValue, newValue)->{
			if(oldValue != null){
				allContent.set(
					allContent.get().replace(oldValue+"</"+tagName.get()+">", newValue+"</"+tagName.get()+">")
				);
			}else{
				allContent.set(
					allContent.get().replace("</"+tagName.get()+">", newValue+"</"+tagName.get()+">")
				);
			}
		});
					
	}

	public void put(String key, String value){
		if(value.isEmpty()){
			if(properties.keySet().contains(key)){
				properties.get().remove(key);
			}
		}else{
			if(properties.keySet().contains(key)){
				properties.get().replace(key, value);
			}else{				
				properties.get().put(key, value);
			}
		}
	}
	
	public String getOldProperty(MapChangeListener.Change change){
		return change.getKey()+"=\""+change.getValueRemoved()+"\"";
	}
	
	public String getNewProperty(MapChangeListener.Change change){
		return change.getKey()+"=\""+change.getValueAdded()+"\"";
	}
	
	
}
