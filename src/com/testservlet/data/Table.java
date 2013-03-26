package com.testservlet.data;

import net.sf.json.JSONObject;

import com.testservlet.params.Params;


public class Table {
	public String tableID;
	public int maxNumber;

	public Table(){
		
	}
	
	public Table(String tableID,int maxNumber){
		this.tableID = tableID;
		this.maxNumber = maxNumber;
	}
	
	@Override
	public String toString() {
		return tableID;
	}
	
	public JSONObject table2Json(){
		JSONObject jo = new JSONObject();
		jo.element(Params.Table.TABLE_ID,this.tableID);
		jo.element(Params.Table.TABLE_MAX_NUMBER,this.maxNumber);
		return jo;
	}
	
	public Table(JSONObject jo){
		this.tableID = jo.optString(Params.Table.TABLE_ID,null);
		this.maxNumber = jo.optInt(Params.Table.TABLE_MAX_NUMBER,0);
	}
	
}
