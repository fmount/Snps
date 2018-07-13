package org.osgi.snps.base.daoservice;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.util.JSonUtil;
import org.osgi.snps.base.util.Util;

public class DataManager implements DataInterface {

	// Prepare statement to run command db
	PreparedStatement SQLPreparedStatement;
	Connection con;

	public DataManager(Connection con) {
		this.con = con;
	}

	@Override
	public boolean insertComponent(String key, String type, String description,
			String sensor) {
		try {
			String insertQuery = "INSERT INTO SensorNodes (SensorId,SensorType,Description,SensorImage,Timestamp) VALUES (?,?,?,?,?)";
			SQLPreparedStatement = con.prepareStatement(insertQuery);

			SQLPreparedStatement.setString(1, key);
			SQLPreparedStatement.setString(2, type);
			SQLPreparedStatement.setString(3, description);
			SQLPreparedStatement.setString(4, sensor);
			SQLPreparedStatement.setTimestamp(5, getTimestamp());

			// Esecuzione dello statement
			SQLPreparedStatement.executeUpdate();

			// Chiusura dello statement
			SQLPreparedStatement.close();
			con.close();
			System.out.println("Component Registered!");
			return true;
		} catch (Exception e) {
			System.out.println("[DAO:SQL EXCEPTION]" + e.getMessage());
		}
		return false;

	}

	@Override
	public String getSensorImage(String key) {
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT * FROM SensorNodes WHERE SensorId=\"" + key
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			rsQuery = SQLPreparedStatement.executeQuery();

			while (rsQuery.next()) {
				String s = rsQuery.getString("SensorImage");
				return s;
			}

			con.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public List<String> getAllSensorImages() {
		try {
			List<String> sensList = new ArrayList<String>();
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT * FROM SensorNodes;";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			while (rsQuery.next()) {

				String s = rsQuery.getString("SensorImage");
				sensList.add(s);
			}

			con.close();
			return sensList;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	@Override
	public String getSensorDescription(String key) {
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT Description FROM SensorNodes WHERE SensorId=\""
					+ key + "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				String doc = rsQuery.getString("Description");
				return doc;
			}
			// Chiusura della connessione al DB..
			con.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public List<String> getSensorBYType(String key) {
		List<String> senslist = new ArrayList<String>();
		try {
			ResultSet rsQuery; // Set di record
			String SQL = "SELECT * FROM SensorNodes WHERE SensorType=\"" + key
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				String s = rsQuery.getString("SensorImage");
				senslist.add(s);
			}
			// Chiusura della connessione al DB..
			con.close();
			return senslist;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return senslist;
		}
	}

	@Override
	public boolean removeComponent(String key) {
		try {
			String deleteQuery = "DELETE FROM SensorNodes WHERE SensorId=\""
					+ key + "\";";
			SQLPreparedStatement = con.prepareStatement(deleteQuery);
			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean updateComponent(String key, String type, String description,
			String sensor) {
		try {
			// if(exist(key)){
			String SQL = "UPDATE SensorNodes SET SensorType = ?, Description = ?, SensorImage = ?, Timestamp = ? WHERE SensorId = \""
					+ key + "\";";
			SQLPreparedStatement = con.prepareStatement(SQL);
			
			SQLPreparedStatement.setString(1, type);
			SQLPreparedStatement.setString(2, description);
			SQLPreparedStatement.setString(3, sensor);
			SQLPreparedStatement.setTimestamp(4, getTimestamp());

			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();

			System.out.println("Sensor " + key + " informations updated");
			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/* Questo metodo nn va!! xd */
	// TODO: SISTEMARE QUESTO METODO...
	public boolean exist(String key) {
		try {
			String description = getSensorDescription(key);
			if (description != null)
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * Metodi di gestione della mappa della rete sottostante (WSN)
	 * 
	 */

	@Override
	public boolean insertEntry(String sensId, String nodeId, String zoneId) {
		try {
			if (checkEntry(sensId, nodeId, zoneId)) {
				String insertQuery = "INSERT INTO NetworkMap (sensId,nodeId,zoneId) VALUES (?,?,?)";
				SQLPreparedStatement = con.prepareStatement(insertQuery);

				SQLPreparedStatement.setString(1, sensId);
				SQLPreparedStatement.setString(2, nodeId);
				SQLPreparedStatement.setString(3, zoneId);

				// Esecuzione dello statement
				SQLPreparedStatement.executeUpdate();

				// Chiusura dello statement
				SQLPreparedStatement.close();
				con.close();
				System.out.println("New Entry in my Net Vision");
				return true;
			} else {
				System.out.println("Skip insert");
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean checkEntry(String sid, String bsid, String zoneid) {
		String query = "SELECT * FROM NetworkMap WHERE sensId=\"" + sid
				+ "\" AND nodeId=\"" + bsid + "\" AND zoneId=\"" + zoneid
				+ "\";";

		System.out.println(query);
		try {
			SQLPreparedStatement = con.prepareStatement(query);

			ResultSet rsQuery; // Set di record
			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				return false;
			}
			// con.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public List<String> getSensorsByZone(String zoneid) {
		String query = "SELECT sensId FROM NetworkMap WHERE zoneId=\"" + zoneid
				+ "\";";
		List<String> sList = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		System.out.println(query);
		try {
			SQLPreparedStatement = con.prepareStatement(query);

			ResultSet rsQuery; // Set di record
			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				sList.add(rsQuery.getString("sensId"));
			}
			
			//PERFORM SECOND QUERY
			for(int i=0;i<sList.size();i++){
				result.add(getSensorImage(sList.get(i)));
			}
			// con.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<String> getSensorsByNode(String bsid) {
		String query = "SELECT sensId FROM NetworkMap WHERE nodeId=\"" + bsid
				+ "\";";
		List<String> sList = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		System.out.println(query);
		try {
			SQLPreparedStatement = con.prepareStatement(query);

			ResultSet rsQuery; // Set di record
			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				sList.add(rsQuery.getString("sensId"));
			}
			
			//PERFORM SECOND QUERY
			for(int i=0;i<sList.size();i++){
				result.add(getSensorImage(sList.get(i)));
			}
			// con.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean insertZone(String zoneId, String description, String name,
			String edificio, String piano) {

		try {
			String insertQuery = "INSERT INTO Zone (zoneId,description,name,edificio,piano) VALUES (?,?,?,?,?)";
			SQLPreparedStatement = con.prepareStatement(insertQuery);

			/*
			 * Impostazione dei parametri (valori da sostituire ai punti
			 * interrogativi).
			 */
			SQLPreparedStatement.setString(1, zoneId);
			SQLPreparedStatement.setString(2, description);
			SQLPreparedStatement.setString(3, name);
			SQLPreparedStatement.setString(4, edificio);
			SQLPreparedStatement.setString(5, piano);

			// Esecuzione dello statement
			SQLPreparedStatement.executeUpdate();

			// Chiusura dello statement
			SQLPreparedStatement.close();
			con.close();
			System.out.println("New Entry in my ZONE Vision");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean insertBS(String bsId, String description, String name,
			String localIp) {
		try {
			String insertQuery = "INSERT INTO BaseStation (bsId,description,name,ip) VALUES (?,?,?,?)";
			SQLPreparedStatement = con.prepareStatement(insertQuery);

			SQLPreparedStatement.setString(1, bsId);
			SQLPreparedStatement.setString(2, description);
			SQLPreparedStatement.setString(3, name);
			SQLPreparedStatement.setString(4, localIp);

			// Esecuzione dello statement
			SQLPreparedStatement.executeUpdate();

			// Chiusura dello statement
			SQLPreparedStatement.close();
			con.close();
			System.out.println("New BS in my Vision");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean removeEntry(String sensId) {
		try {
			String deleteQuery = "DELETE FROM NetworkMap WHERE sensId=\""
					+ sensId + "\";";
			SQLPreparedStatement = con.prepareStatement(deleteQuery);
			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean removeZone(String zoneId) {
		try {
			String deleteQuery = "DELETE FROM Zone WHERE zoneId=\"" + zoneId
					+ "\";";
			SQLPreparedStatement = con.prepareStatement(deleteQuery);
			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean removeBaseStation(String bsId) {
		try {
			String deleteQuery = "DELETE FROM BaseStation WHERE bsId=\"" + bsId
					+ "\";";
			SQLPreparedStatement = con.prepareStatement(deleteQuery);
			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	/*
	 * @Override public boolean updateEntry(String sensId, String nodeId, String
	 * zoneId){ try { if (existEntry(sensId)) { String SQL =
	 * "UPDATE NetworkMap SET nodeId = ?, zoneId = ? WHERE sensId = \"" + sensId
	 * + "\";"; SQLPreparedStatement = con.prepareStatement(SQL);
	 * 
	 * SQLPreparedStatement.setObject(1, nodeId);
	 * SQLPreparedStatement.setObject(2, zoneId);
	 * 
	 * SQLPreparedStatement.executeUpdate(); SQLPreparedStatement.close();
	 * 
	 * System.out.println("Network map entry: " + sensId + " updated"); return
	 * true; } else { System.out
	 * .println("Network map entry doesn't exist, performing insert"); return
	 * insertEntry(sensId, nodeId, zoneId); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return false; }
	 */

	@Override
	public Map<String, String> getSensZone(String sId) {
		Map<String, String> zoneinfo = new HashMap<String, String>();
		try {
			Map<String, String> sinfo = getSensorPosition(sId);
			if (!(sinfo.isEmpty())) {
				String zoneId = sinfo.get("zoneId");
				ResultSet rsQuery; // Set di record
				String SQL = "SELECT * FROM Zone WHERE zoneId=\"" + zoneId
						+ "\";";
				System.out.println(SQL);
				SQLPreparedStatement = con.prepareStatement(SQL);

				rsQuery = SQLPreparedStatement.executeQuery();

				// Ciclo e assegnazione delle righe del resultSet
				while (rsQuery.next()) {
					zoneinfo.put("zoneId", zoneId);
					zoneinfo.put("description",
							rsQuery.getString("description"));
					zoneinfo.put("name", rsQuery.getString("name"));
					zoneinfo.put("edificio", rsQuery.getString("edificio"));
					zoneinfo.put("piano", rsQuery.getString("piano"));
					return zoneinfo;
				}
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return zoneinfo;
		}
		return zoneinfo;
	}

	@Override
	public Map<String, String> getSensBS(String sId) {
		Map<String, String> bsinfo = new HashMap<String, String>();
		try {
			Map<String, String> sinfo = getSensorPosition(sId);
			if (!(sinfo.isEmpty())) {
				String bsId = sinfo.get("nodeId");
				ResultSet rsQuery; // Set di record
				String SQL = "SELECT * FROM BaseStation WHERE bsId=\"" + bsId
						+ "\";";
				System.out.println(SQL);
				SQLPreparedStatement = con.prepareStatement(SQL);

				/*
				 * le righe corrispondenti alla SELECT vengono memorizzate in
				 * rsQuery reQuery ?? un resultSet.
				 */

				rsQuery = SQLPreparedStatement.executeQuery();

				// Ciclo e assegnazione delle righe del resultSet
				while (rsQuery.next()) {
					bsinfo.put("bsId", bsId);
					bsinfo.put("description", rsQuery.getString("description"));
					bsinfo.put("name", rsQuery.getString("name"));
					bsinfo.put("ip", rsQuery.getString("ip"));
					return bsinfo;
				}
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return bsinfo;
		}
		return bsinfo;
	}

	/**
	 * Ritorna il sensore selezionato e le relative info about la sua posizione
	 * nella sottorete
	 */
	@Override
	public Map<String, String> getSensorPosition(String sensId) {
		Map<String, String> sensInfo = new HashMap<String, String>();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT * FROM NetworkMap WHERE sensId=\"" + sensId
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				// String nodeId = (String)rsQuery.getString("nodeId");
				sensInfo.put("nodeId", rsQuery.getString("nodeId"));
				// String zoneId = (String)rsQuery.getString("zoneId");
				sensInfo.put("zoneId", rsQuery.getString("zoneId"));
				return sensInfo;
			}
			// Chiusura della connessione al DB..
			con.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return sensInfo;
		}
		return sensInfo;
	}

	/**
	 * Ritorna una lista di sensori (id dei sensori) associati ad una certa BS
	 */
	@Override
	public Map<String, List<String>> getSensList(String nodeId) {

		List<String> sensInfo = new ArrayList<String>();
		Map<String, List<String>> sensList = new HashMap<String, List<String>>();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT sensId FROM NetworkMap WHERE nodeId=\""
					+ nodeId + "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				sensInfo.add(rsQuery.getString("sensId"));
			}
			sensList.put(nodeId, sensInfo);
			// Chiusura della connessione al DB..
			con.close();
			return sensList;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return sensList;
		}
	}

	/**
	 * Ritorna una lista di sensori (id dei sensori) associati ad una certa BS
	 */
	@Override
	public Map<String, List<String>> getSensListByZone(String zoneId) {

		List<String> sensInfo = new ArrayList<String>();
		Map<String, List<String>> sensList = new HashMap<String, List<String>>();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT sensId FROM NetworkMap WHERE zoneId=\""
					+ zoneId + "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				sensInfo.add(rsQuery.getString("sensId"));
			}
			sensList.put(zoneId, sensInfo);
			// Chiusura della connessione al DB..
			con.close();
			return sensList;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return sensList;
		}
	}

	/**
	 * Restituisce l'insieme di nodi presenti in una zona
	 */
	@Override
	public Map<String, List<String>> getNodeList(String zoneId) {
		List<String> nodeList = new ArrayList<String>();
		Map<String, List<String>> zoneInfo = new HashMap<String, List<String>>();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT nodeId FROM NetworkMap WHERE zoneId=\""
					+ zoneId + "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				nodeList.add(rsQuery.getString("nodeId"));
			}
			zoneInfo.put(zoneId, nodeList);
			// Chiusura della connessione al DB..
			con.close();
			return zoneInfo;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return zoneInfo;
		}
	}

	/**
	 * Restituisce l'insieme di zone presenti in una rete
	 */
	@Override
	public Map<String, List<String>> getZoneList(String netId) {
		List<String> zoneList = new ArrayList<String>();
		Map<String, List<String>> netInfo = new HashMap<String, List<String>>();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT zoneId FROM NetworkMap WHERE netId=\"" + netId
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				zoneList.add(rsQuery.getString("nodeId"));
			}
			netInfo.put(netId, zoneList);
			// Chiusura della connessione al DB..
			con.close();
			return netInfo;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return netInfo;
		}
	}

	public boolean existEntry(String key) {
		Map<String, String> sensInfo = new HashMap<String, String>();
		try {
			sensInfo = getSensorPosition(key);
			if (!sensInfo.isEmpty())
				return true;
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/* Gestione tabella Misure dei Sensori.. */
	
	public boolean registerMeas(SimpleData data) {
		try {
			String insertQuery = "INSERT INTO Measures (SensorId,id_meas,data,date,time) VALUES (?,?,?,?,?)";
			SQLPreparedStatement = con.prepareStatement(insertQuery);

			/*
			 * Impostazione dei parametri (valori da sostituire ai punti
			 * interrogativi).
			 */
			SQLPreparedStatement.setString(1, data.getSid());
			SQLPreparedStatement.setString(2, data.get_id_meas());
			SQLPreparedStatement.setString(3, data.getData());
			SQLPreparedStatement.setString(4, Util.whatDayIsToday());
			SQLPreparedStatement.setString(5, Util.whatTimeIsIt());
			// Esecuzione dello statement
			SQLPreparedStatement.executeUpdate();

			// Chiusura dello statement
			SQLPreparedStatement.close();
			con.close();
			System.out.println("Data Registered!");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean removeMeas(String key) {
		try {
			String deleteQuery = "DELETE FROM Measures WHERE SensorId=\"" + key
					+ "\";";
			SQLPreparedStatement = con.prepareStatement(deleteQuery);
			SQLPreparedStatement.executeUpdate();
			SQLPreparedStatement.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	// GET DETECTION BY ID_MEAS (ONE SHOT)
	@Override
	public String getSingleDetection(String id_meas) {
		ResultSet rsQuery; // Set di record
		SimpleData sd = new SimpleData();
		try {
			String SQL = "SELECT * FROM Measures WHERE id_meas=\"" + id_meas
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				sd.set_id_meas(rsQuery.getString("id_meas"));
				sd.setSid(rsQuery.getString("SensorId"));
				sd.setData(rsQuery.getString("data"));
				sd.setDate(rsQuery.getString("date"));
				sd.setTime(rsQuery.getString("time"));
			}
			// Chiusura della connessione al DB..
			con.close();
			return JSonUtil.SimpleDataToJSON(sd);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	@Override
	public List<String> getDetectionByDate(String sid, String initDate,
			String endDate) {
		try {
			String tmp1[] = initDate.split("-");
			String date_1= tmp1[2]+"-"+tmp1[1]+"-"+tmp1[0];
			String tmp2[] = endDate.split("-");
			String date_2= tmp2[2]+"-"+tmp2[1]+"-"+tmp2[0];
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = (Date) formatter.parse(date_1);
			Date date2 = (Date) formatter.parse(date_2);
			List<String> dList = new ArrayList<String>();
			System.out.println("InitDate: " + formatter.format(date1) + " EndDate: " + formatter.format(date2));
			if (sid.equalsIgnoreCase("")) {
				// Query su tutti i sensori presenti..
				// SELEZIONO TUTTI I DATI BETWEEN UNA DATA E UN'ALTRA..
				String SQL = "SELECT * FROM Measures WHERE STR_TO_DATE(date,'%d-%m-%Y') BETWEEN \""
						+ formatter.format(date1) + "\"AND \""
						+ formatter.format(date2) + "\";";
				dList = getDetect(SQL);
				return dList;
			} else {
				// SELEZIONO TUTTI I DATI BETWEEN UNA DATA E UN'ALTRA..
				String SQL = "SELECT * FROM Measures WHERE SensorId=\"" + sid
						+ "\" AND STR_TO_DATE(date,'%d-%m-%Y') BETWEEN \"" + formatter.format(date1)
						+ "\"AND \"" + formatter.format(date2) + "\";";
				dList = getDetect(SQL);
				return dList;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> getDetectionByTime(String sid, String date,
			String time1, String time2) {
		try {
			List<String> dList = new ArrayList<String>();
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = (Date) formatter.parse(date);

			if (sid.equalsIgnoreCase("")) {
				String SQL = "SELECT * FROM Measures WHERE date=\""
						+ formatter.format(date1) + "\" AND time BETWEEN \""
						+ time1 + "\"AND \"" + time2 + "\";";
				dList = getDetect(SQL);
				return dList;
			} else {
				String SQL = "SELECT * FROM Measures WHERE SensorId=\"" + sid
						+ "\" AND date=\"" + formatter.format(date1)
						+ "\" AND time BETWEEN \"" + time1 + "\"AND \"" + time2
						+ "\";";
				dList = getDetect(SQL);
				return dList;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public List<String> getDetectionByDateAndTime(String sid, String initDate,
			String endDate, String initTime, String endTime) {
		try {
			
			List<String> dList = new ArrayList<String>();
			String SQL = "";
			
			if (initDate.equals(endDate)) {
			return getDetectionByTime(sid, initDate, initTime, endTime);
				
			} else {
				String tmp1[] = initDate.split("-");
				String date_1 = tmp1[2] + "-" + tmp1[1] + "-" + tmp1[0];
				String tmp2[] = endDate.split("-");
				String date_2 = tmp2[2] + "-" + tmp2[1] + "-" + tmp2[0];
				DateFormat formatter;
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = (Date) formatter.parse(date_1);
				Date date2 = (Date) formatter.parse(date_2);
				Date dateAfter = new Date(date1.getTime() + (1000 * 60 * 60 * 24));
				Date dateBefore = new Date(date2.getTime() - (1000 * 60 * 60 * 24));
								
				if (sid.equalsIgnoreCase("")) {
					SQL = "SELECT * FROM Measures WHERE  ( ( STR_TO_DATE(date,'%d-%m-%Y')=\""
							+ formatter.format(date1)
							+ "\" AND time >= \""
							+ initTime
							+ "\") OR  (STR_TO_DATE(date,'%d-%m-%Y') BETWEEN \""
							+ formatter.format(dateAfter)
							+ "\" AND \""
							+ formatter.format(dateBefore)
							+ "\") OR (STR_TO_DATE(date,'%d-%m-%Y')=\""
							+ formatter.format(date2)
							+ "\" AND time <= \""
							+ endTime + "\"));";
				} else {
					// String SQL = "( SELECT * From Measures WHERE SensorId=\""
					// + sid
					// + "\" AND STR_TO_DATE(date,'%d-%m-%Y')=\""
					// + formatter.format(date1)
					// + "\" AND time >= \""
					// + initTime
					// + "\") UNION(SELECT * FROM Measures WHERE SensorId=\""
					// + sid
					// + "\" AND STR_TO_DATE(date,'%d-%m-%Y') BETWEEN \""
					// + formatter.format(dateAfter)
					// + "\" AND \""
					// + formatter.format(dateBefore)
					// + "\") UNION  ( SELECT * From Measures WHERE SensorId=\""
					// + sid + "\" AND STR_TO_DATE(date,'%d-%m-%Y')=\""
					// + formatter.format(date2) + "\" AND time <= \""
					// + endTime + "\")";
					SQL = "SELECT * FROM Measures WHERE SensorId=\""
							+ sid
							+ "\" AND ( ( STR_TO_DATE(date,'%d-%m-%Y')=\""
							+ formatter.format(date1)
							+ "\" AND time >= \""
							+ initTime
							+ "\") OR  (STR_TO_DATE(date,'%d-%m-%Y') BETWEEN \""
							+ formatter.format(dateAfter) + "\" AND \""
							+ formatter.format(dateBefore)
							+ "\") OR (STR_TO_DATE(date,'%d-%m-%Y')=\""
							+ formatter.format(date2) + "\" AND time <= \""
							+ endTime + "\"));";
					
				}
			}
			
			dList = getDetect(SQL);
			return dList;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public List<String> getDetect(String SQL) {
		List<String> dList = new ArrayList<String>();
		try {
			ResultSet rsQuery; // Set di record

			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				SimpleData sd = new SimpleData();
				sd.set_id_meas(rsQuery.getString("id_meas"));
				sd.setSid(rsQuery.getString("SensorId"));
				sd.setData(rsQuery.getString("data"));
				sd.setDate(rsQuery.getString("date"));
				sd.setTime(rsQuery.getString("time"));

				dList.add(JSonUtil.SimpleDataToJSON(sd));
			}
			// Chiusura della connessione al DB..
			con.close();
			return dList;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return dList;
		}
	}

	@Override
	public Map<String, List<String>> Allhistory() {
		Map<String, List<String>> allmeas = new HashMap<String, List<String>>();
		SimpleData sd = new SimpleData();
		try {
			ResultSet rsQuery; // Set di record

			String SQL = "SELECT * FROM Measures;";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {
				sd.set_id_meas(rsQuery.getString("id_meas"));
				sd.setSid(rsQuery.getString("SensorId"));
				sd.setData(rsQuery.getString("data"));
				sd.setDate(rsQuery.getString("date"));
				sd.setTime(rsQuery.getString("time"));

				if (allmeas.get(sd.getSid()) == null) {
					List<String> al = new ArrayList<String>();
					al.add(JSonUtil.SimpleDataToJSON(sd));
					allmeas.put(sd.getSid(), al);
				} else {
					allmeas.get(sd.getSid()).add(JSonUtil.SimpleDataToJSON(sd));
				}
			}
			// Chiusura della connessione al DB..
			con.close();
			return allmeas;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return allmeas;
		}
	}

	@Override
	public List<String> history(String key) {
		List<String> allmeas = new ArrayList<String>();
		try {
			ResultSet rsQuery; // Set di record
			String SQL = "SELECT * FROM Measures WHERE SensorId=\"" + key
					+ "\";";
			System.out.println(SQL);
			SQLPreparedStatement = con.prepareStatement(SQL);

			/*
			 * le righe corrispondenti alla SELECT vengono memorizzate in
			 * rsQuery reQuery ?? un resultSet.
			 */

			rsQuery = SQLPreparedStatement.executeQuery();

			// Ciclo e assegnazione delle righe del resultSet
			while (rsQuery.next()) {

				SimpleData sd = new SimpleData();
				sd.set_id_meas(rsQuery.getString("id_meas"));
				sd.setSid(rsQuery.getString("SensorId"));
				sd.setData(rsQuery.getString("data"));
				sd.setDate(rsQuery.getString("date"));
				sd.setTime(rsQuery.getString("time"));

				allmeas.add(JSonUtil.SimpleDataToJSON(sd));
			}
			// Chiusura della connessione al DB..
			con.close();
			return allmeas;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return allmeas;
		}
	}

	public static Timestamp getTimestamp() {
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	/**
	 * **
	 * 
	 * @param args
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */

	/* TEST MAIN */
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		// MySqlDAOFactory dao = (MySqlDAOFactory)
		// DataManagerDaoFactory.getDAOFactory(3);
	}
}
