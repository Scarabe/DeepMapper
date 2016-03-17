
package br.com.deepmapper.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
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

	/**
	 * Method description: Main metod get the mongo collection
	 *
	 * @since 15 de mar de 2016 08:03:55
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param String
	 *            mongoColl
	 * @return MongoCollection<Document> setedColl
	 */
	@SuppressWarnings("resource")
	public MongoCollection<Document> getMongoColl(String mongoColl) {
		logger.trace(getClass());
		logger.trace("Connecting to MongoDB IP: " + DBConstants.mongoIp + " Port: " + DBConstants.mongoPort + ".");
		MongoClient mongoClient = new MongoClient(DBConstants.mongoIp, DBConstants.mongoPort);

		logger.trace("Connecting to db named: " + DBConstants.mongoDB + ".");
		MongoDatabase db = mongoClient.getDatabase(DBConstants.mongoDB);

		boolean newColl = mongoFindColl(db, mongoColl);
		if (newColl) {
			logger.trace("Collection not found, creatting collection: " + mongoColl + ".");
			db.createCollection(mongoColl);
		}

		logger.trace("Getting collection: " + mongoColl + ".");
		MongoCollection<Document> setedColl = db.getCollection(mongoColl);

		if (newColl) {
			creatIndex(setedColl, mongoColl);
		}

		return setedColl;
	}

	/**
	 * Method description: Method who looks for the collection before creat it;
	 *
	 * @since 15 de mar de 2016 08:03:14
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param MongoDatabase
	 *            db
	 * @param String
	 *            mongoColl
	 * @return
	 */
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

	/**
	 * Method description: Inserting registers into mongodb
	 *
	 * @since 15 de mar de 2016 08:02:45
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param List<NoClassifiedLinksDto>
	 *            noClassList
	 * @param String
	 *            mongoColl
	 */
	public void insertNoClass(List<NoClassifiedLinksDto> noClassList, String mongoColl) {
		logger.trace("insertNoClass()");

		MongoCollection<Document> setedColl = getMongoColl(mongoColl);
		List<Document> docMany = new ArrayList<Document>();

		noClassList.forEach(regLine -> {
			Document docSing = new Document();
			docSing.append(DBConstants.sourceID, regLine.getSourceLink());
			docSing.append(DBConstants.onionID, regLine.getOnionLink());

			logger.trace("Inserting into the mongodb.");
			logger.trace(DBConstants.sourceID + ": " + regLine.getSourceLink());
			logger.trace(DBConstants.onionID + ": " + regLine.getOnionLink());

			docMany.add(docSing);
		});

		try {
			logger.trace("Inserting document list into mongodb.");
			setedColl.insertMany(docMany);
		} catch (Exception e) {
			logger.error("Document list has at last one duplicated onionLink.");
		}

	}

	/**
	 * Method description: Creating mongo index
	 *
	 * @since 15 de mar de 2016 08:01:55
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param MongoCollection<Document>
	 *            setedColl
	 * @param String
	 *            mongoColl
	 */
	public void creatIndex(MongoCollection<Document> setedColl, String mongoColl) {
		logger.trace("creatIndex()");

		if (mongoColl.equals(DBConstants.noClassColl)) {
			logger.trace("Creating index for " + DBConstants.noClassColl + ".");
			setedColl.createIndex(new BasicDBObject(DBConstants.onionID, 1), new IndexOptions().unique(true));
		}
	}
}
