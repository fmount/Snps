package org.osgi.snps.base.daoservice;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoDAOFactory extends DataManagerDaoFactory {

	
	static String dbName = "name"; //TODO: create db..
	static int PORT = 27017;
    final static String DB_URL = "localhost";
    
        
    public MongoManager getMongoManager(){
            try{
            	Mongo mongo = new Mongo(DB_URL, PORT);
            	DB db = mongo.getDB("dbName");
                  return new MongoManager(db);
            }catch(Exception e){
                  e.printStackTrace();
                  return null;
            }
    }
}
