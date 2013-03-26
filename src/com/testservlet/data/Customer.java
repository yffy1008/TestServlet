package com.testservlet.data;


import org.json.JSONException;

import net.sf.json.JSONObject;

import com.testservlet.params.Params;






public class Customer{
	/**
	 * 
	 */
	public int time;
	public int number;
	public String name;
	public String tableID;
	public String table_number;
	
	public Customer() {

	}

	public Customer(int number,int time, String name) {
		this.name = name;
		this.time = time;
		this.number = number;
	}
	
	public Customer(String jsonString) {
		try {
			org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
			this.time = jsonObject.optInt(Params.Customer.WAIT_TIME, 0);
			this.number = jsonObject.optInt(Params.Customer.NUMBER, 0);
			this.name = jsonObject.optString(Params.Customer.CUSTOMER_ID, "");
			this.tableID = jsonObject.optString(Params.Table.TABLE_ID,"");
			this.table_number = jsonObject.optString(Params.Table.TABLE_MAX_NUMBER,"");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject customer2Json(){
		JSONObject jo = new JSONObject();
		jo.element(Params.Customer.WAIT_TIME,this.time);
		jo.element(Params.Customer.CUSTOMER_ID, this.name);
		jo.element(Params.Customer.NUMBER,this.number + "");
		jo.element(Params.Table.TABLE_ID,this.tableID);
		jo.element(Params.Table.TABLE_MAX_NUMBER,this.table_number);
		return jo;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
