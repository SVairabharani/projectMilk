<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="
    org.bson.Document,
    java.sql.Date,
	java.text.DateFormat,
	java.text.SimpleDateFormat"%>
    
    <%
	   	Document product = (Document)request.getAttribute("product"); 
	    Document holder = (Document)request.getAttribute("holder");
	    Document color = (Document)product.get("color");
	    String _id = product.getString("_id");
	    int ph = Integer.valueOf(product.getString("ph"));
	    int red = color.getInteger("r");
	    Long update = Long.valueOf(product.getString("last_update"));
	    Long post = Long.valueOf(product.getString("post"));
	    String type="";
	    if(ph >= 6 && ph < 8){
	    	if(red > 70 && red < 90){
	    		type = "Transitional Milk";
	    	}
	    	else if(red > 30 && red < 50){
	    		type = "Colostrum Milk";
	    	}
	    	else if(red > 50 && red < 70){
	    		type = "Mature milk";
	    	}
	    }
	    String holder_id = holder.getString("holder_id");
	    String bank_name = holder.getString("bank_name");
	    String incharge = holder.getString("incharge");
	    int temp = holder.getInteger("temprature");
	    int humidity = holder.getInteger("humidity");
	    String city = holder.getString("city");
	    String mobile = holder.getString("mobile");
    %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="product/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Raleway:wght@300&display=swap" rel="stylesheet">
    <title><%=_id%> | <%=bank_name%></title>
</head>
<body>
    <div class="details_card" id="product_card">
        <p>Product details</p>
        <table>
            <tr>
                <th><img src="https://cdn-icons-png.flaticon.com/512/365/365738.png" alt="">ID</th>
                <td><%=_id%></td>
            </tr>
            <tr>
                <th><img src="https://e7.pngegg.com/pngimages/816/796/png-clipart-national-secondary-school-d%C4%97stymas-education-ph-school-blue-text.png" alt="ph">ph</th>
                <td><%=ph%></td> 
            </tr>
            <tr>
                <th><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/BYR_color_wheel.svg/1024px-BYR_color_wheel.svg.png" alt="color">color</th>
                <td><%="R"+color.getInteger("r")+" G"+color.getInteger("g")+" B"+color.getInteger("b")%></td> 
            </tr>
            <tr>
                <th><img src=" https://icon-library.com/images/update-icon-png/update-icon-png-22.jpg" alt="update">last update</th>
                <td><%
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date(update);
                String ut = dateFormat.format(date);
                %>
                <%=ut%></td> 
            </tr>
            <tr>
                <th><img src="https://www.freepnglogos.com/uploads/note-png/post-note-png-download-clip-art-clip-25.png" alt="posted">posted</th>
                <td><%date = new Date(post);
                	ut = dateFormat.format(date);
                %>
                <%=ut%> </td> 
            </tr>
            <%if(!(type.isEmpty())){ %>
	           <tr>
	               <th><img src="https://ml4l5wfimak6.i.optimole.com/7Bumah0-lRukPLKd/w:auto/h:auto/q:auto/http://sapinsdairy.com/wp-content/uploads/2021/12/milk-bottle.png" alt="type">Type</th>
	               <td><%=type%></td> 
	           </tr>
	        <% } %>
        </table>
        
    </div>
    <div class="details_card" id="holder_card">
        <p>Holder details</p>
        <table>
            <tr>
                <th> <img src="https://cdn-icons-png.flaticon.com/512/365/365738.png" alt="id"> ID</th>
                <td><%=holder_id %></td>
            </tr>
            <tr>
                <th> <img src="https://cdn-icons-png.flaticon.com/512/195/195488.png" alt="bank"> Bank name</th>
                <td><%=bank_name %></td>
            </tr>
            <tr>
                <th> <img src="https://cdn-icons-png.flaticon.com/512/4206/4206265.png" alt="id"> Incharge</th>
                <td><%=incharge %></td>
            </tr>
            <tr>
                <th> <img src="https://cdn-icons-png.flaticon.com/512/1035/1035618.png" alt="id"> Temprature</th>
                <td><%=temp %>°C</td>
            </tr>
            <tr>
                <th> <img src="https://cdn-icons-png.flaticon.com/512/728/728093.png" alt="id"> humidity</th>
                <td><%=humidity %></td>
            </tr>
            <tr>
                <th> <img src="https://png.pngtree.com/png-clipart/20230123/original/pngtree-flat-red-location-sign-png-image_8927579.png " alt="id"> City</th>
                <td><%=city %></td>
            </tr>
            <tr>
                <th> <img src="https://www.pngall.com/wp-content/uploads/10/Call-PNG-File.png" alt="id"> Mobile</th>
                <td><%=mobile %></td>
            </tr>
        </table>
    </div>
</body>
</html>