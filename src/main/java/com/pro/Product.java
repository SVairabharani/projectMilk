package com.pro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Servlet implementation class Product
 */
@WebServlet("/product")
public class Product extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Product() {
        super();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _id;
		String holder_id;
		// reading query params
		try {
			_id = getQueryParamValue(request.getQueryString(),"_id");
			holder_id = getQueryParamValue(request.getQueryString(),"holder_id");
			if(_id.isBlank() || holder_id.isBlank()) { 
				throw new Exception("error");
			}
		}catch(Exception e) {
			response.setContentType("text/html");
			response.getWriter().write("Your request not contain valid data");
			return; 
		}
		
		//connecting with mongo db 
		String uri = "mongodb+srv://<username>:<password>@cluster0.<projectId>.mongodb.net/?retryWrites=true&w=majority";
		/*
		 * the above uri string contain username password and project id
		 * replace with your database details
		 */
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			
			//getting product data 
            MongoDatabase database = mongoClient.getDatabase("projectMilk");
            
            // Set up the MongoDB collection
            MongoCollection<Document> collection_products = database.getCollection("productData");
            
            // getting document as per client request
            Document product_data = collection_products.find(Filters.eq("_id", _id)).first();
            
            //getting holder data
            MongoCollection<Document> collection_holders = database.getCollection("holders");
            
            Document holder_data = collection_holders.find(Filters.eq("holder_id", holder_id)).first();
            
            if(product_data==null || holder_data==null) {
            	response.setContentType("text/html");
    			response.getWriter().write("Can't fetch data due to Cloud server error or Wrong Id from client");
    			return; 
            }
            
            request.setAttribute("product", product_data);
            request.setAttribute("holder", holder_data);
            
            request.getRequestDispatcher("product/index.jsp").forward(request, response);
            
        }
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static String getQueryParamValue(String queryString, String paramName) {
	    String[] queryParamPairs = queryString.split("&");
	    for (String queryParamPair : queryParamPairs) {
	        String[] keyValue = queryParamPair.split("=");
	        if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
	            return keyValue[1];
	        }
	    }
	    return " ";
	}
}
