package com.testservlet.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.testservlet.data.Customer;
import com.testservlet.data.QueensManager;
import com.testservlet.params.Params;
import com.testservlet.utils.Utils;

public class GetListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4966061025393707614L;

	/**
	 * Constructor of the object.
	 */
	public GetListServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); 
		// Just puts "destroy" string in log
		// Put your code here
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String head = request.getParameter(Params.Code.REQUEST);
		if (head == null) return;

		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();

		if (head.equals(Params.Code.GET_QUEEN_LIST)) {
			jsonObject.element(Params.Code.RESPONSE,Params.Code.GET_QUEEN_LIST);
			jsonObject.element(Params.Code.THE_LIST,Utils.getQueensJsonArray());
		
		} else if (head.equals(Params.Code.GET_CALLED_LIST)){
			JSONArray ja = new JSONArray();
			for (Customer c : QueensManager.getInstance().getCalledList())  ja.add(c.customer2Json());
			jsonObject.element(Params.Customer.CUSTOMER,ja);
			jsonObject.element(Params.Code.RESPONSE,Params.Code.GET_CALLED_LIST);
		}
		
		out.print(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
