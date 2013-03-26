package com.testservlet.data;

import java.util.ArrayList;

public class MyQueen<T> extends ArrayList<T>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5878766919454279280L;
	
	private char groupName;
	private int limit;

	public MyQueen(){
		
	}
	/**
	 * 
	 * @param groupName   name
	 * @param limit       groupMax
	 */
	public MyQueen(char groupName,int limit){
		System.out.println(groupName);
		this.limit = limit;
		this.groupName = groupName;
	}
	
	public char getName() {
		return groupName;
	}

	public void setName(char name) {
		this.groupName = name;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 *           按照客户ID删除
	 * @param    id 客户ID
	 * @return   the Customer that was removed from the list
	 */
	public boolean removeCustomer(String id){
		for (T t : this) {
			if (t.toString().equals(id))
				return this.remove(t);		
		}
		return false;
	}
	
}
