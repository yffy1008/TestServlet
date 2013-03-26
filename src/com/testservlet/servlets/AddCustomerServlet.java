package com.testservlet.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.testservlet.data.Customer;
import com.testservlet.data.QueensManager;
import com.testservlet.params.Params;

public class AddCustomerServlet extends HttpServlet {
	private QueensManager manager = QueensManager.getInstance();
	/**
	 * 
	 */
	private static final long serialVersionUID = -59877369171756975L;

	/**
	 * Constructor of the object.
	 */
	public AddCustomerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String head = request.getParameter(Params.Code.REQUEST);
		
		if (head == null) return;
		
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		/**
		 * 根据客户人数自动排队并且给号
		 */
		if (head.equals(Params.Code.ADD_TO_QUEEN)){
			try {
				String number = request.getParameter(Params.Customer.NUMBER);
				Customer c = manager.addCustomerToQueen(Integer.parseInt(number));
				jsonObject.element(Params.Code.RESPONSE,Params.Code.ADD_SUCCEED);
				jsonObject.element(Params.Customer.CUSTOMER,c.customer2Json());
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.element(Params.Code.RESPONSE, Params.Code.FAILED);
			}
		}
		
		out.println(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		if (!Params.Settings.isInitialed) {
			final int[] is = {5,10,15,20};
			manager.setQueensList(is);	
			Params.Settings.isInitialed = true;
			Params.Settings.isDingDingYang = false;
		}	
	}

}
