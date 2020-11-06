package com.librarymanagement;



import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


@WebServlet("/returnBook")
public class ReturnBook extends HttpServlet {
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
		User user = (User) getServletContext().getAttribute("user");
		
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("LibraryManagement");
	    PersistenceManager pm = pmf.getPersistenceManager();
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
	    Filter propertyFilter =null;
	    try {
	    	 propertyFilter = new FilterPredicate("barrowerId", FilterOperator.EQUAL, user.getId());
		}catch(NullPointerException nullPointerExc){
			response.sendRedirect("index.html");
		}
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
		
		if(books != null && barrowedBookCount>0 ) {
			response.getWriter().print("<p> Your book list</p>");
			response.getWriter().print("<table>\r\n"
					+ "			  <tr>\r\n"
					+ "			    <th>Id</th>\r\n"
					+ "			    <th>Valid till</th>\r\n"
					+ "			  </tr>");
			books.forEach(i -> {
				try {
					if(i.barrowerId == user.getId()) {
						response.getWriter().print("<tr><td>"+i.id+"</td>");
						response.getWriter().println("<td>"+ ChronoUnit.DAYS.between(LocalDateTime.now(), i.getDueDate())+"</td></tr>");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			response.getWriter().print("</table>");
			response.getWriter().print("<form action=\"returnBook\" method=\"POST\">"
					+ "Enter the book id :<input type=\"text\" placeholder=\"Enter book Id\" name=\"bookid\" required>"
					+ "<button type=\"submit\">remove</button> "
					+ "</form>");
		}else {
			response.getWriter().print("<h1>no books in you stack</h1>");
			response.getWriter().println("<a href=\"home\">back</a>");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long bookId = (long) Integer.parseInt(request.getParameter("bookid"));		
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("LibraryManagement");
	    PersistenceManager pm = pmf.getPersistenceManager();
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
	    Filter propertyFilter =null;
	    try {
	    	 propertyFilter = new FilterPredicate("id", FilterOperator.EQUAL, bookId);
		}catch(NullPointerException nullPointerExc){
			response.sendRedirect("index.html");
		}
	    
	    Query query = new Query("Book").setFilter(propertyFilter);
		PreparedQuery pq = dataStoreService.prepare(query);
		Book book = new Book();
		Key key = null;
		for(Entity ent:pq.asIterable()) {
			book.id = (long) ent.getProperty("id");
			
			book.name = (String) ent.getProperty("name");
			book.barrowerId =  (long) 0;
			book.borrowedOn = null;
			book.dueDate = null;
			key = ent.getKey();
		}
		if(book != null) {
			dataStoreService.delete(key);
			pm.makePersistent(book);
			System.out.println("repleced sucessfully");
		}else {
			System.out.println("please enter the vaild book id");
		}
		response.sendRedirect("home");
	}

}




