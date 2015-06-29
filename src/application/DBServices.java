package application;

import static com.mongodb.client.model.Filters.text;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class DBServices {	
	
	public static MongoCursor<Document> textMatch(String words){
		return DBUtils.getCollection()
				.find(text(words))
				.projection(Projections.metaTextScore("score"))
				.sort(Sorts.metaTextScore("score"))						
				.iterator();
	}
	
	public static void save(Document doc, boolean updateOrder){				
		doc.append("lastChanges", "");
		if(doc != null && doc.get("_id") != null){
			if(updateOrder){
				doc.append("order", getLastOrder()+1);
			}
			DBUtils.getCollection().updateOne(Filters.eq("_id", doc.get("_id")), new Document( "$set", doc));
		}else{			
			doc.append("order", getLastOrder()+1);
			DBUtils.getCollection().insertOne(doc);
		}			
	}
	
	public static void saveLastChanges(Document doc){
		if(!exist(doc) && !doc.getString("lastChanges").isEmpty()){
			passLastChangeAndSave(doc, false);
		}else{
			DBUtils.getCollection()
				.updateOne(
					Filters.eq("_id", doc.get("_id")), new Document( "$set", new Document("lastChanges", doc.getString("lastChanges"))));
		}			
	}
	
	public static Document getNew(){
		return new Document("order", getLastOrder()+1).append("lastChanges", "").append("content", "");		
	}
	
	public static void passLastChangeAndSave(Document doc, boolean updateOrder){
		doc.append("content", doc.getString("lastChanges"));
		save(doc, updateOrder);
	}

	public static int getLastOrder(){
		Integer order = DBUtils.getCollection().find().sort(Sorts.descending("order")).first().getInteger("order");
		if(order == null){
			return 0;
		}
		return order;
	}
	
	public static void setInLastOrder(Document doc){
		if(!isLastOrder(doc.getInteger("order"))){
			DBUtils.getCollection()
				.updateOne(
					Filters.eq("_id", doc.get("_id")), new Document( "$set", new Document("order", getLastOrder()+1)));
		}
	}
	
	public static boolean isLastOrder(int order){
		return getLastOrder() == order;
	}
	
	public static Document getPrevious(Document docActual){
		int actual = docActual.getInteger("order");
		if(actual > 1){
//			return DBUtils.getCollection().find(new Document("order", actual-1)).first();
			Document doc =  DBUtils.getCollection().find(Filters.lt("order", docActual.getInteger("order"))).sort(Sorts.descending("order")).limit(1).first();
			if(doc != null){
				return doc;
			}		
		}
		return docActual;
	}
	
	public static Document getNext(Document docActual){
		int actual = docActual.getInteger("order");
		if(actual < getLastOrder()){
//			return DBUtils.getCollection().find(new Document("order", actual+1)).first();
			Document doc =  DBUtils.getCollection().find(Filters.gt("order", docActual.getInteger("order"))).sort(Sorts.ascending("order")).limit(1).first();
			if(doc != null){
				return doc;
			}else{
				return docActual;
			}
				
		}else if(actual > getLastOrder()){
			return docActual;	
		}else{
			return getNew();
		}		
	}
	
	public static boolean exist(Document doc){
		return DBUtils.getCollection().find(Filters.eq("_id", doc.get("_id"))).first() != null;
	}
	
	public static void remove(Document doc){
		DBUtils.getCollection().findOneAndDelete(new Document("_id", doc.get("_id")));
	}
	
	
}
