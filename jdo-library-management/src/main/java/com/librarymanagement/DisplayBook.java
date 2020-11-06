package com.librarymanagement;



import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@WebServlet("/displayBook")
public class DisplayBook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("userId") == null ) {
			response.sendRedirect("index.html");
		}
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate" );
		User user = (User) getServletContext().getAttribute("user");
		response.getWriter().print("<style>"
				+ "table, th, td {\r\n"
				+ "  border: 1px solid black;\r\n"
				+ "}"
				+ "</style>");
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
	    Filter propertyFilter = new FilterPredicate("barrowerId", FilterOperator.EQUAL, user.getId());
	    Query query = new Query("Book").setFilter(propertyFilter);
		PreparedQuery pq = dataStoreService.prepare(query);
		List<Book> books = new ArrayList<>();
		for(Entity ent:pq.asIterable()) {
			Book book = new Book();
			book.id = (long) ent.getProperty("id");
			book.name = (String) ent.getProperty("name");
			book.barrowerId = (Long) ent.getProperty("barrowerId");
			book.borrowedOn = (String) ent.getProperty("borrowedOn");
			book.dueDate = (String) ent.getProperty("dueDate");
			books.add(book);
		}
	    
		int barrowedBookCount = books.size();
		if(books != null && barrowedBookCount>0) {
			String bookAsString = books.size() >1 ? "books":"book";
			response.getWriter().print("<p>You have borrowed "+books.size()+" "+ bookAsString+"</p>");
			response.getWriter().print("<table>\r\n"
					+ "			  <tr>\r\n"
					+ "			    <th>Name</th>\r\n"
					+ "			    <th>Valid till</th>\r\n"
					+ "				<th>no of days</th>\r\n"
					+ "			  </tr>");
			books.forEach(book -> {
				try {
					if(book.barrowerId == user.getId()) {
						response.getWriter().print("<tr>");
						response.getWriter().print("<td>"+book.name+"</td>");
						response.getWriter().print("<td>"+ book.getBorrowedOn().toLocalDate()+"</td>" );
						response.getWriter().print("<td>"+ ChronoUnit.DAYS.between(LocalDateTime.now(), book.getDueDate())+"</td>");
						response.getWriter().print("</tr>");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			response.getWriter().print("</table>");
		}else {
			response.getWriter().println("<h1>No books to display..!</h1>");
		}
		response.getWriter().println("<a href=\"home\">back</a>");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
