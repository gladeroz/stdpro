package traitement.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import model.ConfigOdrJson;
import model.ConfigOdrRefCsv;
import model.ConfigOdrTraiteCsv;
import model.ConfigStore;
import utils.DatabaseInstance;

public class DatabaseOdr {

	private static Logger logger = Logger.getLogger(DatabaseOdr.class);

	public static final String INSERT_ODR_SQL = "INSERT INTO CSV " +
			/**"(NBR_CONTRACT_REDBOX, SEQ_NUMBER, RECORD_TYPE, SUBSIDIARY_CODE, STORE_CODE, "
			+ "PURCHASE_ORDER_NUMBER, LINE_NUMBER, TRANSACTION_TYPE, STORE_NAME, PAIEMENT_TYPE,"
			+ "PRODUCT_SALES_DATE, WARRANTY_SALES_DATE, FAMILY_INSURANCE_CODE, FAMILY_INSURANCE_LABEL, NAME_OF_SERVICE,"

		    + "PRODUCT_CODE, QUANTITY_SOLD, PRIX_UNIT_PROVISION, FAMILY_PRODUCT_CODE, FAMILY_PRODUCT_LABEL,"
		    + "PRODUCT_BRAND_CODE, BRAND_NAME_PRODUCT, PRODUCT_REFERENCE, CODIC, PRODUCT_QTY,"
		    + "PRIX_UNIT_PRODUCT, PRODUCT_PRIX_TOTAL, CLIENT_ID, CUSTOMER_TITLE, CLIENT_NAME,"

		    + "CUSTOMER_FIRST_NAME, NBR_IN_THE_TRACK, TRACK_CODE_TYPE, TRACK_NAME, POSTAL_CODE,"
		    + "CODE_INSEE, LOCATION, IMEI_NUMBER, TYPE_OF_SALE, SALES_CHANNEL, EMAIL_ADRESS)"**/
		    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String INSERT_TRAITEMENT_SQL = "INSERT INTO TRAITEMENT " +
			/**"(NBR_CONTRACT_REDBOX, TRANSACTION_TYPE, OFFRE, FILLER, FACTURE, FORMULAIRE, "
			+ "BULLETIN, RIB, DATE_RECEPTION, DATE_TRAITEMENT)"**/
		    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String COUNT_ODR_SQL = "SELECT count(1) FROM CSV";

	public static final String SELECT_CODE_ELIGIBLE_ODR_SQL = "SELECT PRODUCT_ID FROM ref_exclusion_eligibilte_odr";

	private static void printSQLException(SQLException ex) {
		for (Throwable e: ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

	private static void insertElementOdr(PreparedStatement preparedStatement, ConfigOdrJson line) throws SQLException {
		ConfigOdrRefCsv odrLine = line.getOdr();

		int i = 0;
		preparedStatement.setString(++i, odrLine.getNbrContractRedbox());
		preparedStatement.setString(++i, odrLine.getSeqNumber());
		preparedStatement.setString(++i, odrLine.getRecordType());
		preparedStatement.setString(++i, odrLine.getSubsidiaryCode());
		preparedStatement.setString(++i, odrLine.getStoreCode());
		preparedStatement.setString(++i, odrLine.getPurchaseOrderNumber());
		preparedStatement.setString(++i, odrLine.getLinenumber());
		preparedStatement.setString(++i, odrLine.getTransactionType());
		preparedStatement.setString(++i, odrLine.getStoreName());

		preparedStatement.setString(++i, odrLine.getPaymentType());

		preparedStatement.setDate(++i, new java.sql.Date(odrLine.getProductSalesDate().getTime()));
		preparedStatement.setDate(++i, new java.sql.Date(odrLine.getWarrantySalesDate().getTime()));

		preparedStatement.setString(++i, odrLine.getFamilyInsuranceCode());
		preparedStatement.setString(++i, odrLine.getFamilyInsuranceLabel());
		preparedStatement.setString(++i, odrLine.getNameofService());
		preparedStatement.setString(++i, odrLine.getProductCode());
		preparedStatement.setString(++i, odrLine.getQuantitySold());
		preparedStatement.setString(++i, odrLine.getPrixUnitProvision());
		preparedStatement.setString(++i, odrLine.getFamilyProductCode());
		preparedStatement.setString(++i, odrLine.getFamilyProductLabel());

		preparedStatement.setString(++i, odrLine.getProductBrandCode());
		preparedStatement.setString(++i, odrLine.getBrandNameProduct());
		preparedStatement.setString(++i, odrLine.getProductReference());
		preparedStatement.setString(++i, odrLine.getCodic());
		preparedStatement.setString(++i, odrLine.getProductQty());
		preparedStatement.setString(++i, odrLine.getPrixUnitProduct());
		preparedStatement.setString(++i, odrLine.getProductPrixTotal());
		preparedStatement.setString(++i, odrLine.getClientID());
		preparedStatement.setString(++i, odrLine.getCustomerTitle());
		preparedStatement.setString(++i, odrLine.getClientName());
		preparedStatement.setString(++i, odrLine.getCustomerFirstName());

		preparedStatement.setString(++i, odrLine.getNbrInTheTrack());
		preparedStatement.setString(++i, odrLine.getTrackCodeType());
		preparedStatement.setString(++i, odrLine.getTrackName());
		preparedStatement.setString(++i, odrLine.getPostalCode());
		preparedStatement.setString(++i, odrLine.getCodeINSEE());
		preparedStatement.setString(++i, odrLine.getLocation());
		preparedStatement.setString(++i, odrLine.getImeiNumber());
		preparedStatement.setString(++i, odrLine.getTypeOfSale());
		preparedStatement.setString(++i, odrLine.getSalesChannel());
		preparedStatement.setString(++i, odrLine.getEmailAdress());
	}
	
	private static void insertElementTraitement(PreparedStatement preparedStatement, ConfigOdrJson line) throws SQLException {
		ConfigOdrTraiteCsv odrLine = line.getTraitement();
		
		int i = 0;
		preparedStatement.setString(++i, odrLine.getNbrContractRedbox());
		preparedStatement.setString(++i, line.getOdr().getTransactionType());
		
		preparedStatement.setString(++i, odrLine.getOffre().toString());
		preparedStatement.setString(++i, odrLine.getFiller());
		preparedStatement.setString(++i, odrLine.getFacture().toString());
		preparedStatement.setString(++i, odrLine.getFormulaire().toString());
		preparedStatement.setString(++i, odrLine.getBulletin().toString());
		preparedStatement.setString(++i, odrLine.getRib().toString());

		preparedStatement.setDate(++i, new java.sql.Date(odrLine.getDateReception().getTime()));
		preparedStatement.setDate(++i, new java.sql.Date(odrLine.getDateTraitement().getTime()));
	}

	public static ArrayList<String> getAllCodeEligible() throws JsonParseException, JsonMappingException, IOException{
		ArrayList<String> resultats = new ArrayList<String>();

		try (Connection conn = DatabaseInstance.getInstance(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(DatabaseOdr.SELECT_CODE_ELIGIBLE_ODR_SQL)) {
			while (rs.next()) {
				resultats.add(rs.getString(1));
			}
		} catch (SQLException ex) {
			printSQLException(ex);
		} finally {
			DatabaseInstance.resetConnexion();
		}

		return resultats;
	}

	public static int getOdrCount() throws JsonParseException, JsonMappingException, IOException {
		int count = 0;

		try (Connection conn = DatabaseInstance.getInstance(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(DatabaseOdr.COUNT_ODR_SQL)) {
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException ex) {
			printSQLException(ex);
		} finally {
			DatabaseInstance.resetConnexion();
		}

		return count;
	}

	public static void initOdrDatabase(ConfigStore store, Logger logger) throws JsonParseException, JsonMappingException, IOException, SQLException {
		logger.error("Initialisation de la base de donnee");

		Connection conn = DatabaseInstance.getInstance();
		try {
			conn.setAutoCommit(false);
			for (ConfigOdrJson line : store.getStore()) {
				insertRecordOdr(line, conn);
				conn.commit();
				try {
					insertRecordTraitement(line, conn);
				} catch(Exception e) {
					e.printStackTrace();
				}
				conn.commit();
			}
			conn.setAutoCommit(true);
		} finally {
			DatabaseInstance.resetConnexion();
		}
		logger.error("Fin de l initialisation de la base de donnee");
	}

	public static void insertRecordTraitement(ConfigOdrJson importCsv) throws JsonParseException, JsonMappingException, IOException {
		insertRecordTraitement(importCsv, null);
	}
	
	public static void insertRecordTraitement(ConfigOdrJson importCsv, Connection conn) throws JsonParseException, JsonMappingException, IOException {
		if(importCsv.getTraitement() == null || importCsv.getTraitement().getNbrContractRedbox() == null) return;
		
		try {
			PreparedStatement preparedStatement = null;
			if(conn == null) {
				preparedStatement = DatabaseInstance.getStatement(DatabaseOdr.INSERT_TRAITEMENT_SQL);
			} else {
				preparedStatement = conn.prepareStatement(DatabaseOdr.INSERT_TRAITEMENT_SQL);
			}
			insertElementTraitement(preparedStatement, importCsv);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error("[ " + importCsv.getContrat() + " ] [ " + e.getMessage() + " ]");
			printSQLException(e);
		}
	}

	public static void insertRecordOdr(ConfigOdrJson importCsv) throws JsonParseException, JsonMappingException, IOException {
		insertRecordOdr(importCsv, null);
	}

	public static void insertRecordOdr(ConfigOdrJson importCsv, Connection conn) throws JsonParseException, JsonMappingException, IOException {
		try {
			PreparedStatement preparedStatement = null;
			if(conn == null) {
				preparedStatement = DatabaseInstance.getStatement(DatabaseOdr.INSERT_ODR_SQL);
			} else {
				preparedStatement = conn.prepareStatement(DatabaseOdr.INSERT_ODR_SQL);
			}
			insertElementOdr(preparedStatement, importCsv);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error("[ " + importCsv.getContrat() + " ] [ " + e.getMessage() + " ]");
			printSQLException(e);
		}
	}
}
