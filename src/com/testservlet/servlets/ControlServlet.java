package com.testservlet.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


import com.testservlet.data.Customer;
import com.testservlet.data.QueensManager;
import com.testservlet.data.Table;
import com.testservlet.params.Params;
import com.testservlet.utils.Utils;

public class ControlServlet extends HttpServlet {
	
	private QueensManager manager = QueensManager.getInstance();
	private String latestMessage;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7730639287077029487L;

	/**
	 * Constructor of the object.
	 */
	public ControlServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); 
		// Just elements "destroy" string in log
		// element your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String head = request.getParameter(Params.Code.REQUEST);
		if (head == null) return;
		
//		System.out.println(head);
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
	
		
		/**
		 * 普通叫号方式
		 */		
		if (head.equals(Params.Code.GET_EMPTY_TABLE) && !Params.Settings.isDingDingYang) {
			try {
				String tableID = request.getParameter(Params.Table.TABLE_ID);
				String maxNumb = request.getParameter(Params.Table.TABLE_MAX_NUMBER);
				
				Customer customer = Utils.getMatchedCustomer(new Table(tableID,Integer.parseInt(maxNumb)));
				customer.tableID = tableID;
				customer.table_number = maxNumb;
				
				manager.deleteCustomerByID(customer.name);
				manager.getCalledList().add(customer);
				
				latestMessage = customer.name + "客户到" + tableID + "号桌就餐";
				jsonObject.element(Params.Code.RESPONSE,Params.Code.SEARCH_SUCCEED);
				jsonObject.element(Params.Code.MESSAGE,latestMessage);
		
			} catch (NullPointerException e) {
				System.out.println("没有找到匹配顾客");
				jsonObject.element(Params.Code.MESSAGE,"无匹配客户");
				jsonObject.element(Params.Code.RESPONSE,Params.Code.SEARCH_SUCCEED);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element(Params.Code.RESPONSE,Params.Code.FAILED);
			}
			
			
			
			
			
			/**
			 * 丁丁洋式自动补齐叫号方式，一次可以叫多个号
			 */
		} else if (head.equals(Params.Code.GET_EMPTY_TABLE) && Params.Settings.isDingDingYang) {
			try {
				System.out.println("here is dingdingyang");
				String tableID = request.getParameter(Params.Table.TABLE_ID);
				int max = Integer.valueOf(request.getParameter(Params.Table.TABLE_MAX_NUMBER));
				ArrayList<Customer> arraylist = Utils.getMatchedCustomers(new Table(tableID, max));
				StringBuilder sb = new StringBuilder();

				for (Customer c : arraylist) sb.append(c.name +"\t ");
				latestMessage = sb.toString();
				jsonObject.element(Params.Code.RESPONSE,Params.Code.SEARCH_SUCCEED);
				jsonObject.element(Params.Customer.CUSTOMER_ID,"");
				jsonObject.element(Params.Code.MESSAGE,latestMessage);
		
			} catch (NullPointerException e) {
				System.out.println("没有找到匹配顾客");
				jsonObject.element(Params.Code.MESSAGE,"无匹配客户");
				jsonObject.element(Params.Code.RESPONSE,Params.Code.SEARCH_SUCCEED);
			} catch (Exception e) {
				jsonObject.element(Params.Code.RESPONSE,Params.Code.FAILED);
			
			
			
			
			}
		/**
		 *  找到了客户后，  将 customer 从队列中删除
		 */
		} else if(head.equals(Params.Code.TABLE_IS_SEATED)){
			try {
				Customer customer_ = new Customer(request.getParameter(Params.Customer.CUSTOMER));
				if (Utils.removeMatchedCustomer(manager.getCalledList(), customer_)) 
					jsonObject.element(Params.Code.RESPONSE, Params.Code.DELETE_SUCCEED);
		
			} catch (Exception e) {
				jsonObject.element(Params.Code.RESPONSE, Params.Code.FAILED);
			}
		
			
			
			/**
			 * 客户爽约    将客户删除掉  并且需要重新安排客户就位
			 */
		} else if (head.equals(Params.Code.MISSED_CUSTOMER)) {
			try {
				Customer customer_ = new Customer(request.getParameter(Params.Customer.CUSTOMER));
				if (Utils.removeMatchedCustomer(manager.getCalledList(), customer_)) {
					manager.missOneCustomer();
					Customer customer = Utils.getMatchedCustomer(new Table(customer_.tableID,Integer.parseInt(customer_.table_number)));
					customer.tableID = customer_.tableID;
					customer.table_number = customer_.table_number;
					
					manager.deleteCustomerByID(customer.name);
					manager.getCalledList().add(customer);
					
					latestMessage = customer.name + "客户到" + customer_.tableID + "号桌就餐";
					jsonObject.element(Params.Code.RESPONSE,Params.Code.SEARCH_SUCCEED);
					jsonObject.element(Params.Code.MESSAGE,latestMessage);
				}
			
			} catch (NullPointerException e) {
				System.out.println("没有找到匹配顾客");
				jsonObject.element(Params.Code.RESPONSE, Params.Code.DELETE_SUCCEED);
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element(Params.Code.RESPONSE, Params.Code.FAILED);
			}
		
			
			
			/**
			 * 客户端显示最新信息
			 */
		} else if (head.equals(Params.Code.CALL_CUSTOMER) && latestMessage != null) {
			jsonObject.element(Params.Code.RESPONSE,Params.Code.CALL_CUSTOMER);
			jsonObject.element(Params.Code.MESSAGE, latestMessage);
			latestMessage = null;
		} 

		
		
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// element your code here
//		if (!Params.Settings.isInitialed) {
//			final int[] is = {5,10,15,20};
//			manager.setQueensList(is);	
//			Params.Settings.isInitialed = true;
//			Params.Settings.isDingDingYang = false;
//		}
	}

}
