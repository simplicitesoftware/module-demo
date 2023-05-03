package com.simplicite.tests.Demo;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.bpm.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import com.simplicite.util.exceptions.DBException;

/**
 * Unit tests DemoDataGeneration
 */
public class DemoDataGeneration {
	
	@Test
	public void test() {
		Grant g = Grant.getSystemAdmin();
		ObjectDB order = g.getObject("data_DemoOrder", "DemoOrder");
		synchronized(order.getLock()){
			for(int i=0; i<100; i++){
				try {
					order.resetValues();
					order.setFieldValue("demoOrdDate", getRandomDateInLastNDays(90));
					order.setFieldValue("demoOrdStatus", getRandomStatus(order.getField("demoOrdStatus")));
					order.setFieldValue("demoOrdCliId", getRandomRowId("demo_client"));
					order.setFieldValue("demoOrdPrdId", getRandomRowId("demo_product"));
					order.setFieldValue("demoOrdQuantity", Tool.randomInt(0,10));
					order.setFieldValue("demoOrdComments", Tool.getCurrentDateTime());
					order.getTool().validateAndSave();
				} catch (Exception e) {
					AppLog.error(e.getMessage(), e, g);
				}
			}
		}
	}
	
	private static String getRandomStatus(ObjectField f){
		String[] codes = f.getList().getCodes(true);
		return codes[Tool.randomInt(0,codes.length-1)];
	}
	
	private static String getRandomDateInLastNDays(int n){
		return Tool.shiftDays(Tool.getCurrentDate(), Tool.randomInt(-n,0));
	}
	
	private static String getRandomRowId(String tableName) throws DBException{
		return Grant.getSystemAdmin().simpleQuery("SELECT row_id FROM "+tableName+" ORDER BY "+getSqlRand()+" LIMIT 1");
	}
	
	private static String getSqlRand() throws DBException{
		int db = Grant.getSystemAdmin().getDBVendor();
		String rand;
		if(db==Globals.DB_HSQLDB || db==Globals.DB_MYSQL)
			return "RAND()";
		else if(db==Globals.DB_POSTGRESQL)
			return "RANDOM()";
		else
			throw new DBException("This function doesn't support this database (only hsql, mysql, postgres)");
	}
}
