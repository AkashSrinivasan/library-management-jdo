package com.librarymanagement;


import java.io.IOException;
import java.io.InputStream;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@WebServlet("/signup")
@MultipartConfig
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.getWriter().print("<style> \r\n"
				+ "Body {\r\n"
				+ "  font-family: sans-serif;\r\n"
				+ "}\r\n"
				+ "button { \r\n"
				+ "       \r\n"
				+ "       width: 100%;\r\n"
				+ "        padding: 15px; \r\n"
				+ "        margin: 10px 0px; \r\n"
				+ "        border: none; \r\n"
				+ "        cursor: pointer; \r\n"
				+ "         } \r\n"
				+ " form { \r\n"
				+ "        border: 3px solid black; \r\n"
				+ "        margin-left: 25%;\r\n"
				+ "        margin-right: 25%;\r\n"
				+ "    } \r\n"
				+ " input[type=text], input[type=password],input[type=number] { \r\n"
				+ "        width: 100%; \r\n"
				+ "        margin: 8px 0;\r\n"
				+ "        padding: 12px 20px; \r\n"
				+ "        display: inline-block; \r\n"
				+ "       \r\n"
				+ "        box-sizing: border-box; \r\n"
				+ "    }\r\n"
				+ " button:hover { \r\n"
				+ "        opacity: 0.7; \r\n"
				+ "    } \r\n"
				+ "  .cancelbtn { \r\n"
				+ "        width: auto; \r\n"
				+ "        padding: 10px 18px;\r\n"
				+ "        margin: 10px 5px;\r\n"
				+ "    } \r\n"
				+ "      \r\n"
				+ "   \r\n"
				+ " .container { \r\n"
				+ "        padding: 25px; \r\n"
				+ "    } \r\n"
				+ "</style> \r\n");
		response.getWriter().print("<center><h1>Signup Form</h1></center>\r\n"
				+ "<form action=\"signup\" method=\"POST\" enctype=\"multipart/form-data\">\r\n"
				+ "        <div class=\"container\"> \r\n"
				+ "            <label>Id : </label> \r\n"
				+ "            <input type=\"number\" placeholder=\"Enter Id\" name=\"id\" required><br>\r\n"
				+ "            <label>Username : </label> \r\n"
				+ "            <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required><br>\r\n"
				+ "            <label>Password : </label> \r\n"
				+ "            <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required><br><br>\r\n"
				+ "            <label>Upload Profile Photo : </label>\r\n"
				+ "            <input type=\"file\" name=\"myFile\" required>\n<br><br>"
				+ "            <label>Select : </label>\r\n"
				+ "            <select name=\"userType\" id=\"userType\">\r\n"
				+ "			    <option value=\"Student\">Student</option>\r\n"
				+ "			    <option value=\"Staff\">Staff</option>\r\n"
				+ "			 	</select><br>\r\n"
				+ "            <button type=\"submit\">Signup</button><br> \r\n"
				+"				Already have account ? <a href=\"index.html\">login</a>"
				+ "        </div> \r\n"
				+ "    </form><br>"
				+ "		");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");
		System.out.println(id+" "+name+" "+password+" "+userType);
		DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("LibraryManagement");
	    PersistenceManager pm = pmf.getPersistenceManager();
		boolean isAlreadyExist = false;
		if(userType.equals("Student")) {	
			Filter propertyFilter = new FilterPredicate("id", FilterOperator.EQUAL, id);
			Query query = new Query("Student").setFilter(propertyFilter);
			PreparedQuery pq = dataStoreService.prepare(query);
			isAlreadyExist = pq.countEntities(FetchOptions.Builder.withDefaults())>0?true:false;
		}else {
			Filter propertyFilter = new FilterPredicate("id", FilterOperator.EQUAL, id);
			Query query = new Query("Staff").setFilter(propertyFilter);
			PreparedQuery pq = dataStoreService.prepare(query);
			isAlreadyExist = pq.countEntities(FetchOptions.Builder.withDefaults())>0?true:false;
		}

		if(!isAlreadyExist && userType.equals("Student")) {
	        InputStream imgStream = request.getPart("myFile").getInputStream();
	        Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
			Student student = new Student(id, name, password,imageBlob);
			pm.makePersistent(student);
			response.getWriter().print("<div align=\"center\">"
					+ "					<h3>Signed in sucessfully, please <a href=\"index.html\">login</a>"
					+ "					</div>");
		}
			else if(!isAlreadyExist && userType.equals("Staff")) {
				InputStream imgStream = request.getPart("myFile").getInputStream();
		        Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
				Staff staff = new Staff(id, name, password,imageBlob);
				pm.makePersistent(staff);
				pm.close();
				response.getWriter().print("<div align=\"center\">"
						+ "					<h3>Signed in sucessfully, please <a href=\"index.html\">login</a>"
						+ "					</div>");
		}
			else {
			response.getWriter().print("<div align=\"center\">"
					+ "					<h3>User already exsist, please <a href=\"index.html\">login</a>"
					+ "					</div>");
		}
	}

}
