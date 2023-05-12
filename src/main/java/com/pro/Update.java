package com.pro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


/**
 * Servlet implementation class Search
 */
@WebServlet("/update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */ 
    public Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = "mongodb+srv://<username>:<password>@cluster0.<projectId>.mongodb.net/?retryWrites=true&w=majority";
		/*
		 * the above uri string contain username password and project id
		 * replace with your database details
		 */
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonString = reader.readLine();
        if(jsonString==null)
        {
        	response.getWriter().append("Served at: ").append(request.getContextPath()).append(" No valid datas in your request");
        	return;
        }
        //adding \"
//        jsonString=jsonString.replace(":\"", ":\\\"");
//        jsonString=jsonString.replace("\",", "\\\",");
        
//        removing  "
        jsonString=jsonString.replace("{\"","{");
        jsonString=jsonString.replace("\":",":");
        jsonString=jsonString.replace(",\"",",");
        
        System.out.println(jsonString);
//        JsonReader jsonReader = new JsonReader(jsonString);
        BsonDocument bsonDocument =BsonDocument.parse(jsonString);   // Convert the JSON data to a BsonDocument
        
        String _id=(bsonDocument.getString("_id").getValue());
        String ph=(bsonDocument.getString("ph").getValue());
        String city=(bsonDocument.getString("city").getValue());
        String holder_id=(bsonDocument.getString("holder_id").getValue());
        String holder_name=(bsonDocument.getString("holder_name").getValue());
        int temp=(bsonDocument.getInt32("temp").getValue());                          
        int hum=(bsonDocument.getInt32("hum").getValue());
        BsonDocument color=bsonDocument.getDocument("color");

        int r=color.getInt32("r").getValue();
        int b=color.getInt32("b").getValue();
        int g=color.getInt32("g").getValue();
        
        //here create Thread for update temprature and humidity to holder data
        
        //Create new Document for update/Create product
        Document product = new Document();
        product.append("_id", _id)
        .append("ph", ph)
        .append("color", new Document()
        		.append("r", r).append("b", b).append("g", g))
        .append("city", city)
        .append("holder_id", holder_id)
        .append("holder_name",holder_name)
        .append("last_update", (""+System.currentTimeMillis()));
        //      BsonDocument bsonDocument =BsonDocument.parse(jsonString);
//		System.out.println(bsonDocument.getString("id").getValue());`
		
		// Set up the document to insert or update
//        Document document = new Document("name", "John Doe")
//                .append("age", 30)
//                .append("address", new Document("street", "123 Main St")
//                        .append("city", "Anytown")
//                        .append("state", "CA")
//                        .append("zip", "12345"));

		
	 try (MongoClient mongoClient = MongoClients.create(uri)) {
        MongoDatabase database = mongoClient.getDatabase("projectMilk");

        // Set up the MongoDB collection
        MongoCollection<Document> collection = database.getCollection("productData");

        
        // Check if the document exists
        Document existingDocument = collection.find(Filters.eq("_id", _id)).first();
        print("from cloud :"+existingDocument+"\n");
        if (existingDocument != null) {
            // Update the existing document
        	String post = existingDocument.getString("post");
        	product.append("post", post);
        	
        	print("Product exist :\n"+product+"\n");
        	
            collection.replaceOne(Filters.eq("_id", _id), product);
            System.out.println("Document updated \n");
            response.setStatus(response.SC_OK);
			response.getWriter().println("document updated");
        } else {
            // Insert the new document
        	product.append("post", (""+System.currentTimeMillis()));
        	
        	print("not exist :\n"+product+"\n");
        	
            collection.insertOne(product);
            System.out.println("Document was created");
            response.setStatus(response.SC_CREATED);
			response.getWriter().println("Document was created");
        }

	 }
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("started product servlet");
			doGet(request, response);
	}
	
	public static void print(String text) {
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss : ");
        String formattedDateTime = now.format(formatter);
        System.out.println(formattedDateTime+text);
	}
	
}
