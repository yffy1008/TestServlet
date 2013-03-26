package com.testservlet.data;


import java.util.ArrayList;


import com.testservlet.params.Params;
import com.testservlet.utils.Utils;

public class QueensManager {
	private static final QueensManager single = new QueensManager();
	private ArrayList<MyQueen<Customer>> queensList;
	private ArrayList<Customer> calledList;
	private int missedCount;
	private int totalNumber;

	private QueensManager() {
		queensList = new ArrayList<MyQueen<Customer>>();
		calledList = new ArrayList<Customer>();
	}

	public synchronized static QueensManager getInstance() {
		return single;
	}

	/**
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		return totalNumber;
	}

	/**
	 * @return the missedCount
	 */
	public int getMissedCount() {
		return missedCount;
	}

	/**
	 * @return the waitingQueens
	 */
	public ArrayList<MyQueen<Customer>> getQueensList() {
		return queensList;
	}
	
	/**
	 * @return the calledList
	 */
	public ArrayList<Customer> getCalledList() {
		return calledList;
	}

	/**
	 * @param calledList the calledList to set
	 */
	public void setCalledList(ArrayList<Customer> calledList) {
		this.calledList = calledList;
	}

	public void setQueensList(int...is){
		queensList.add(new MyQueen<Customer>((char) 64,0));
		for (int i = 0; i < is.length; i++) {
			queensList.add(new MyQueen<Customer>((char)(65+i),is[i]));
		}
	}

	
	private synchronized String getANewCustomerName(int index){
		MyQueen<Customer> m = this.queensList.get(index);
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(m.getName()));
		sb.append(m.size() + 1 );
		return sb.toString();
	}

	/**
	 *  排队拿票
	 * @param number  该客户总人数
	 * @return		 Customer 客户
	 * @throws JSONException 
	 */
	public synchronized Customer addCustomerToQueen(int number) throws org.json.JSONException{
		int index = Utils.getGroupIndex(number);
		int time = queensList.get(index).size() * Params.Settings.time;
		String name = getANewCustomerName(index);
		
		Customer c = new Customer(number,time,name);
		if(queensList.get(index).add(c)){
			totalNumber++;
			return c;
		}
		return null;
	}

	/**
	 *        按照客户ID删除
	 * @param customerID
	 * @return
	 */
	public synchronized boolean deleteCustomerByID(String customerID){
		if (customerID.length() > 0) {
			char groupid = customerID.charAt(0);
			for (int i = 1; i < queensList.size(); i++) {
				if (i + 64 == groupid){
					return queensList.get(i).removeCustomer(customerID);
				}
			}
		}
		return false;
	}
	
	public synchronized void missOneCustomer(){
		missedCount++;
	}
	
}
