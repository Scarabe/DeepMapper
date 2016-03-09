package br.com.deepmapper.constans;

public class DBConstants {
	public static final String mongoIp;
	public static final int mongoPort;
	public static final String mongoDB;
	public static final String noClassColl;

	static{
		mongoIp = "localhost";
		mongoPort = 27017;
		mongoDB = "DeepMapper";
		noClassColl = "NoClassifiedLinks";
	}
}
