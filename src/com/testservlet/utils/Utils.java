package com.testservlet.utils;


import java.util.ArrayList;

import net.sf.json.JSONArray;


import com.testservlet.data.Customer;
import com.testservlet.data.QueensManager;
import com.testservlet.data.MyQueen;
import com.testservlet.data.Table;

public final class Utils {

	public static boolean isBetween(int left, int number, int right) {
		boolean isBetwwen = false;
		if (left < number && number <= right) {
			isBetwwen = true;
		}
		return isBetwwen;
	}

	/**
	 * 正常的饭店的叫号方法，桌子固定大小，先来后到，允许一个桌子不坐满
	 * @param maxNumber   	桌子允许的最大人数
	 * @return				匹配的客户
	 */
	public static Customer getMatchedCustomer(Table table) {
		ArrayList<MyQueen<Customer>> mList = QueensManager.getInstance().getQueensList();
		int index = getGroupIndex(table.maxNumber);
		for (int i = index; i > 0; i--) {
			MyQueen<Customer> m = mList.get(i);
			for(Customer c : m){
				if (m.size() > 0 && table.maxNumber >= c.number) 
					return c;
			}
		}
		return null;
	}
	
	
	/**
	 * 自动补齐叫号的方式   丁丁洋
	 */
	public static ArrayList<Customer> getMatchedCustomers(Table table){
		ArrayList<Customer> mList = new ArrayList<Customer>();
		Customer c = getMatchedCustomer(table);
		while (c != null && table.maxNumber >= c.number) {
			mList.add(c);
			table.maxNumber = table.maxNumber - c.number;
			QueensManager.getInstance().deleteCustomerByID(c.name);
			c = getMatchedCustomer(table);
		}
		if (mList.size() > 0) return mList;

		return null;
	}
	
	
	
	/**
	 * 
	 * @param maxNumber  桌子能坐的最大人数
	 * @return			 小组的下标
	 */
	public static int getGroupIndex(int maxNumber){
		ArrayList<MyQueen<Customer>> mList = QueensManager.getInstance().getQueensList();
		for (int i = 0; i < mList.size() - 2; i++) {
			if (isBetween(mList.get(i).getLimit(),maxNumber,mList.get(i+1).getLimit()))
				return (i + 1);
		}
		return mList.size() - 1;
	}

	public static JSONArray getQueensJsonArray(){
		ArrayList<MyQueen<Customer>> m = QueensManager.getInstance().getQueensList();
		JSONArray jas = new JSONArray();	
		for (int i = 1;i < m.size();i++) {
			JSONArray ja = new JSONArray();
			for (Customer c : m.get(i)) ja.add(c.customer2Json());
			jas.add(ja);
		}
		return jas;
	}
	
	public static boolean removeMatchedCustomer(ArrayList<Customer> list,Customer c){
		for (Customer c_ : list) {
			if (c.name.equals(c_.name) && c.tableID.equals(c_.tableID)) 
				return list.remove(c_);
		}
		return false;
	}
	
}
