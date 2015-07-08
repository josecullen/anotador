package application;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBUtils {
	private static MongoClient mongoClient = new MongoClient();

	public static MongoClient getMongo(){		
		return mongoClient;
	}
	
	public static MongoDatabase getDB(){
		return getMongo().getDatabase("anotador");
	}
	
	public static MongoCollection<Document> getCollection(){
		return getDB().getCollection("notas");
	}
	public static MongoCollection<Document> getTagCollection(){
		return getDB().getCollection("tags");
	}
	
	public static void dropDatabase(){
		getMongo().dropDatabase("anotador");
	}

	public static void remove(){
		getCollection().deleteMany(new Document());
	}
	
	public static Document getConfDoc(){
		return getCollection().find(new Document("_id", "conf")).first();
	}
	
	
	
}
