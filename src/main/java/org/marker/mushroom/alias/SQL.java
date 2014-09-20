package org.marker.mushroom.alias;


public interface SQL {
	
	/** 查询 */
	String QUERY_FOR_FORM = "select * from ";
	
	
	String FORM = "select * from ";
	String ALIAS_CATEGORY = " C";
	String ALIAS_MODEL = " M";
	
	
	String RIGHT_JOIN = " right join ";
	String ON_MCID_E_CID = " on M.cid = C.id ";
	
	/** 主表别名 */
	String QUERY_FOR_ALIAS = " A ";
	
	
	/** 主表别名点 */
	String QUERY_FOR_ALIAS_DOT = " M.";
	
	String QUERY_FOR_WHERE = " where ";
	
	String QUERY_FOR_AND = " and ";
	
	String QUERY_FOR_ORDERBY = " order by ";
}
