package application;

import java.util.ArrayList;

import org.bson.Document;

public class TagServices {
	public static ArrayList<String> getAllTags(){
		return DBUtils.getTagCollection().find(new Document("_id", "tags")).first().get("values", ArrayList.class);
	}
	
	public static ArrayList<String> getAllProperties(){
		return DBUtils.getTagCollection().find(new Document("_id", "properties")).first().get("values", ArrayList.class);
	}
}
