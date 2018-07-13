package org.osgi.snps.base.daoservice;

// Abstract class DAO Factory
public abstract class DataManagerDaoFactory{

          // List of DAO types supported by the factory
          public static final int CLOUDSCAPE = 1;
          public static final int MONGO = 2;
          public static final int MYSQL = 3;
                   

          public static  DataManagerDaoFactory getDAOFactory(int whichFactory) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
          
            switch (whichFactory) {
            /*case CLOUDSCAPE: 
                  return new CloudscapeDAOFactory();
              */      
              case MONGO: 
                  return new MongoDAOFactory();
              case MYSQL: 
                  return new MySqlDAOFactory();
              default: 
                  return null;
            }
          }
}