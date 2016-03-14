
package br.com.deepmapper.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.IndexOptions;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;

public class MongoUtil {
	private static final Logger logger = LogManager.getLogger(MongoUtil.class);
	@SuppressWarnings("resource")
	public MongoCollection<Document> getMongoColl(String mongoColl) {
		logger.trace(getClass());
		logger.trace(
				"Connecting to MongoDB IP: " + DBConstants.mongoIp + " Port: " + DBConstants.mongoPort + ".");
		MongoClient mongoClient = new MongoClient(DBConstants.mongoIp, DBConstants.mongoPort);

		logger.trace("Connecting to db named: " + DBConstants.mongoDB + ".");
		MongoDatabase db = mongoClient.getDatabase(DBConstants.mongoDB);

		if (mongoFindColl(db, mongoColl)) {
			logger.trace("Collection not found, creatting collection: " + mongoColl + ".");
			db.createCollection(mongoColl);
		}

		logger.trace("Getting collection: " + mongoColl + ".");
		MongoCollection<Document> setedColl = db.getCollection(mongoColl);

		creatIndex(setedColl, mongoColl);

		return setedColl;
	}

	public boolean mongoFindColl(MongoDatabase db, String mongoColl) {
		logger.trace("mongoFindColl()");

		boolean findColl = true;
		MongoIterable<String> tables = db.listCollectionNames();

		logger.trace("Looking for the collection before create. Collection name: " + mongoColl + ".");

		for (String coll : tables) {
			if (coll.equals(mongoColl)) {
				findColl = false;
				break;
			}
		}
		return findColl;
	}

	public void insertNoClass(List<NoClassifiedLinksDto> noClassList, String mongoColl) {
		logger.trace("insertNoClass()");

		MongoCollection<Document> setedColl = getMongoColl(mongoColl);
		List<Document> docMany = new ArrayList<Document>();

		noClassList.forEach(regLine -> {
			Document docSing = new Document();
			docSing.append("externalLink", regLine.getSourceLink());
			docSing.append("onionLink", regLine.getOnionLink());
			
			logger.trace("Inserting into the mongodb.");
			logger.trace("externalLink: "+regLine.getSourceLink());
			logger.trace("onionLink: "+regLine.getOnionLink());
			
			docMany.add(docSing);
		});

		try {
			logger.trace("Inserting document list into mongodb.");
			setedColl.insertMany(docMany);
		} catch (Exception e) {
			logger.error("Document list has at last one duplicated onionLink.");
		}

	}

	public void creatIndex(MongoCollection<Document> setedColl, String mongoColl) {
		logger.trace("creatIndex()");

		if (mongoColl.equals(DBConstants.noClassColl)) {
			logger.trace("Creating index for " + DBConstants.noClassColl + ".");
			setedColl.createIndex(new BasicDBObject("onionLink", 1), new IndexOptions().unique(true));
		}
	}
}
