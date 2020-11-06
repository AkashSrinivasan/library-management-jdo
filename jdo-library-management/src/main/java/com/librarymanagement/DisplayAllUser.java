package com.librarymanagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;


@WebServlet("/displayAllUser")
public class DisplayAllUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("userId") == null ) {
			response.sendRedirect("index.html");
		}
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate" );
		response.setContentType("text/html");
		response.getWriter().print("<style>"
				+ "table, th, td {\r\n"
				+ "  border: 1px solid black;\r\n"
				+ "}"
				+ "</style>");
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
	    Query query = new Query("Student");
	    
		List<Student> allStudent = new ArrayList<>();
		for (Entity entity : dataStoreService.prepare(query).asIterable()) {
			Student student = new Student();
			student.id = (long) entity.getProperty("id");
			System.out.println(student.id);
			student.name = (String) entity.getProperty("name");
			student.profilePic = (Blob) entity.getProperty("profilePic");
			allStudent.add(student);
		}
		if(allStudent.size() == 0) {
			response.getWriter().println("<p>Student List is empty</p>");
		}else {
			response.getWriter().println("<p>Student List</p>");
			response.getWriter().print("<table>\r\n"
					+ "			  <tr>\r\n"
					+ "			    <th>Id</th>\r\n"
					+ "			    <th>Name</th>\r\n"
					+ "				<th>Photo</th>\r\n"			
					+ "			  </tr>");
			for(Student student:allStudent) {
				byte[] bytes = student.getProfilePic().getBytes();
	            byte[] encodeBase64 = Base64.encodeBase64(bytes);
	            String base64Encoded = new String(encodeBase64, "UTF-8");
				response.getWriter().println("<tr><td>"+student.getId()+"</td>");
				response.getWriter().println("<td>"+student.getName()+"</td>"
						+ "<td><img alt=\"img\" style=\"height:100px;\" src=\"data:image/jpeg;base64,"+base64Encoded+"\"/></td></tr>");
			}
			response.getWriter().print("</table>");
		}
		
		Query StaffQuery = new Query("Staff");
		List<Staff> allStaff = new ArrayList<>();
		for (Entity entity : dataStoreService.prepare(StaffQuery).asIterable()) {
			Staff staff = new Staff();
			staff.id = (long) entity.getProperty("id");
			staff.name = (String) entity.getProperty("name");
			staff.profilePic = (Blob) entity.getProperty("profilePic");
			allStaff.add(staff);
		}
		if(allStaff.size() == 0) {
			response.getWriter().println("<p>Staff List is empty</p>");
		}else {
			response.getWriter().println("<p>Staff List</p>");
			response.getWriter().print("<table>\r\n"
					+ "			  <tr>\r\n"
					+ "			    <th>Id</th>\r\n"
					+ "			    <th>Name</th>\r\n"
					+ "				<th>Photo</th>\r\n"
					+ "			  </tr>");
			for(Staff staff:allStaff) {
				byte[] bytes = staff.getProfilePic().getBytes();
	            byte[] encodeBase64 = Base64.encodeBase64(bytes);
	            String base64Encoded = new String(encodeBase64, "UTF-8");
				response.getWriter().println("<tr><td>"+staff.getId()+"</td>");
				response.getWriter().println("<td>"+staff.getName()+"</td>"
						+ "<td><img alt=\"img\" style=\"height:100px;\" src=\"data:image/jpeg;base64,"+base64Encoded+"\"/></td></tr>");
			}
			response.getWriter().print("</table>");
		}
		response.getWriter().println("<a href=\"adminHome\">back</a>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}