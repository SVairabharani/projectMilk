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
import com.mongodb.client.MongoDatabase;




/**
 * Servlet implementation class Search
 */
@WebServlet("/search_api")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
    	
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reading query parameter from requested url
		String type=getQueryParamValue(request.getQueryString(),"type");  //key
		String search=getQueryParamValue(request.getQueryString(),"search"); //value or (regex)
		String option="is";			//option for finding document 
		
		//getting data from Mongo db cloud with above values
		String uri = "mongodb+srv://<username>:<password>@cluster0.<projectId>.mongodb.net/?retryWrites=true&w=majority";
		/*
		 * the above uri string contain username password and project id
		 * replace with your database details
		 */
		
		 try (MongoClient mongoClient = MongoClients.create(uri)) {
	            MongoDatabase database = mongoClient.getDatabase("projectMilk");
	            Document product= new Document();
	            product.append(type, new Document("$regex",search).append("$options", option));
	            
				FindIterable<Document> mongodb= database.getCollection("productData").find(product);
//				System.out.println( mongodb.first().toJson());
				Document finalResult = new Document();
				int i=0;
				for(Document don:mongodb) {
//					System.out.println(i+" "+don);
					finalResult.append(""+i++,don);
				}
				
//				setting cookie for feature redirect to user page
//				response.getWriter().write(mongodb.toJson()); 
				System.out.println(finalResult);
				
//				byte[] bytes = finalResult.toJson().getBytes();
//				response.setContentLength(bytes.length);
//		        response.getOutputStream().write(bytes);
//				JSONObject jsonDoc = new JSONObject(finalResult.toJson());
				response.getWriter().write(finalResult.toJson().toString());;
	        }
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
	    return null;
	}

}
