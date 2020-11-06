package com.librarymanagement;

//import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.time.LocalDateTime;
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



@WebServlet("/barrowBook")
public class BarrowBook extends HttpServlet {
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
	    Filter propertyFilter = new FilterPredicate("barrowerId", FilterOperator.EQUAL, 0);
	    Query query = new Query("Book").setFilter(propertyFilter);
		PreparedQuery pq = dataStoreService.prepare(query);
		List<Book> allBooks = new ArrayList<>();
		for(Entity ent:pq.asIterable()) {
			Book book = new Book();
			book.id = (long) ent.getProperty("id");
			book.name = (String) ent.getProperty("name");
			book.barrowerId = (Long) ent.getProperty("barrowerId");
			book.borrowedOn = (String) ent.getProperty("borrowedOn");
			book.dueDate = (String) ent.getProperty("dueDate");
			allBooks.add(book);
		}
	    
		int availableBookCount = allBooks.size();
		
		if(allBooks==null || availableBookCount == 0) {
			response.getWriter().println("<h1>No book are available right now.!</h1>");
			response.getWriter().println("<a href=\"home\">back</a>");
		}
		else {
				response.getWriter().print("<p>These book available right now !");
				response.getWriter().print("<table>\r\n"
						+ "			  <tr>\r\n"
						+ "			    <th>Id</th>\r\n"
						+ "			    <th>Name</th>\r\n"
						+ "			  </tr>");
				
				allBooks.stream().forEach((i)-> {
					if(i.barrowerId ==0) {
						try {
							response.getWriter().print("<tr><td>"+i.id+"</td>\r\n");
							response.getWriter().print("<td>"+i.name+"</td></tr>\r\n\r\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				response.getWriter().print("</table>");
				response.getWriter().print("<form action=\"barrowBook\" method=\"POST\">"
						+ "Enter the book id :<input type=\"text\" placeholder=\"Enter book Id\" name=\"bookid\" required>"
						+ "<button type=\"submit\">add</button> "
						+ "</form>");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) getServletContext().getAttribute("user");
		Long bookId = (long) Integer.parseInt(request.getParameter("bookid"));
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("LibraryManagement");
	    PersistenceManager pm = pmf.getPersistenceManager();
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
	    Filter propertyFilter = new FilterPredicate("id", FilterOperator.EQUAL, bookId);
	    Query query = new Query("Book").setFilter(propertyFilter);
		PreparedQuery pq = dataStoreService.prepare(query);
		Book book = new Book();
		Key key = null;
		for(Entity ent:pq.asIterable()) {
			book.id = (long) ent.getProperty("id");
			book.name = (String) ent.getProperty("name");
			try {
				book.barrowerId = user.getId();
			}catch(NullPointerException nullPointerExc) {
				System.out.println(nullPointerExc);
			}
			LocalDateTime currentDate = LocalDateTime.now();
			book.borrowedOn = currentDate + "";
			LocalDateTime currentPlusFifteen = currentDate.plusDays(15);
			book.dueDate =currentPlusFifteen+"";
			key = ent.getKey();
		}
		dataStoreService.delete(key);
		pm.makePersistent(book);
		System.out.println("book barrowed sucessfully.!");
		response.sendRedirect("home");
	}
}