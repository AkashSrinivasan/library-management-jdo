package com.librarymanagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

@WebServlet("/displayAllBooks")
public class DisplayAllBooks extends HttpServlet {
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
	    Query query = new Query("Book");
		List<Book> allBooks = new ArrayList<>();
		for(Entity ent:dataStoreService.prepare(query).asIterable()) {
			Book book = new Book();
			book.id = (long) ent.getProperty("id");
			book.name = (String) ent.getProperty("name");
			book.barrowerId = (Long) ent.getProperty("barrowerId");
			book.borrowedOn = (String) ent.getProperty("borrowedOn");
			book.dueDate = (String) ent.getProperty("dueDate");
			allBooks.add(book);
		}
		if(allBooks.size()>= 0) {
			response.getWriter().print("<table>\r\n"
					+ "			  <tr>\r\n"
					+ "			    <th>Id</th>\r\n"
					+ "			    <th>Name</th>\r\n"
					+ "			    <th>Barrower ID</th>\r\n"
					+ "			    <th>Valid Till</th>\r\n"
					+ "			  </tr>");
			for (Book book : allBooks) {
				response.getWriter().print("<tr align=\"center\">");
				response.getWriter().println("<td>"+book.id+"</td>");
				response.getWriter().println("<td>"+book.name+"</td>");
				if(book.barrowerId != 0) {
					response.getWriter().println("<td>"+book.barrowerId+"</td>");
				}else {
					response.getWriter().println("<td>Nil</td>");
				}
				response.getWriter().println("<td>"+book.name+"</td>");
				response.getWriter().print("</tr>");
			}
			response.getWriter().print("</table>");
		}else {
			response.getWriter().print("<p>No Book in library</p>");
		}
		
		response.getWriter().println("<a href=\"adminHome\">back</a>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
