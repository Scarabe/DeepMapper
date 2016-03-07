package br.com.deepmapper.constans;

public class DBConstants {

	private String mongoIp = "localhost";
	private int mongoPort = 27017;
	private String mongoDB = "DeepMapper";
	private String noClassColl = "NoClassifiedLinks";

	public String getNoClassColl() {
		return noClassColl;
	}

	public void setNoClassColl(String noClassColl) {
		this.noClassColl = noClassColl;
	}

	public String getMongoIp() {
		return mongoIp;
	}

	public void setMongoIp(String mongoIp) {
		this.mongoIp = mongoIp;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDB() {
		return mongoDB;
	}

	public void setMongoDB(String mongoDB) {
		this.mongoDB = mongoDB;
	}

}
