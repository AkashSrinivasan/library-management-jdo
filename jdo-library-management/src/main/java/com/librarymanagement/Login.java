package com.librarymanagement;

//import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.IllformedLocaleException;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.mail.SendFailedException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static int id;
   
    public Login() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			id = Integer.parseInt(request.getParameter("id"));
//			System.out.println(id);
		}catch(NumberFormatException numberFormatExc) {
			response.getWriter().print("<p align=\"center\" style=\"color:red;\">Please, enter your id correctly.!</p>"); 
			request.getRequestDispatcher("index.html").forward(request, response);
		}
		String password = request.getParameter("password");
		String userType = request.getParameter("user");
	   
	    DatastoreService dataStoreService = DatastoreServiceFactory.getDatastoreService();
		Cookie cookie = new Cookie("userType", request.getParameter("user"));	
		Filter propertyFilter = new FilterPredicate("id", FilterOperator.EQUAL, id);

		if(userType.equals("Student")) {
			Student student = new Student();
			try {
				Query query = new Query("Student").setFilter(propertyFilter);
				PreparedQuery pq = dataStoreService.prepare(query);
				
				for(Entity ent:pq.asIterable()) {
					student.id = (long) ent.getProperty("id");
					System.out.println(student.id);
					 student.name = (String) ent.getProperty("name");
					 student.password = (String) ent.getProperty("password");
					 student.profilePic = (Blob) ent.getProperty("profilePic");
				}
				if(student.name == null) {
					response.getWriter().print("<p align=\"center\" style=\"color:red;\">It's not valid id</p>"); 
					request.getRequestDispatcher("index.html").forward(request, response);
				}
			}catch(NullPointerException nullPointer){
				response.getWriter().print("<p align=\"center\" style=\"color:red;\">It's not valid id</p>"); 
				request.getRequestDispatcher("index.html").forward(request, response);
			}
			
			if(student!=null && student.id != 0) {
				getServletContext().setAttribute("user", student);
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("userId", request.getParameter("id"));
				response.addCookie(cookie);
				response.sendRedirect("home");
			}else {
				response.getWriter().print("<p align=\"center\" style=\"color:red;\">The id or password that you've entered doesn't match any account please, <a href=\"signup\">signup</a></p>"); 
				request.getRequestDispatcher("index.html").forward(request, response);
			}
		}else if(userType.equals("Staff")) {
				Staff staff =null;
				try {
					Query query = new Query("Staff").setFilter(propertyFilter);
					PreparedQuery preparedQuery = dataStoreService.prepare(query);
					staff = new Staff();
					for(Entity ent:preparedQuery.asIterable()) {
						staff.id = (long) ent.getProperty("id");
						System.out.println(staff.id);
						staff.name = (String) ent.getProperty("name");
						staff.password = (String) ent.getProperty("password");
						staff.profilePic = (Blob) ent.getProperty("profilePic");
					}
				}catch(IllegalArgumentException illligalArg){
					response.getWriter().print("<p align=\"center\" style=\"color:red;\">It's not valid id</p>"); 
					request.getRequestDispatcher("index.html").forward(request, response);
				}
				
			if(staff!=null && staff.getPassword().equals(password)) {
				getServletContext().setAttribute("user", staff);
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("userId", request.getParameter("id"));
				response.addCookie(cookie);
				response.sendRedirect("home");
			}else {
				response.getWriter().print("<p align=\"center\" style=\"color:red;\">The id or password that you've entered doesn't match any account please, <a href=\"signup\">signup</a></p>"); 
				request.getRequestDispatcher("index.html").forward(request, response);
			}
		}else {
			if(Admin.id == id && Admin.password.equals("root")) {
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("userId", request.getParameter("id"));
				response.addCookie(cookie);
				response.sendRedirect("adminHome");
		}else {
			response.getWriter().print("<p align=\"center\" style=\"color:red;\">The id or password that you've entered doesn't match any account please, <a href=\"signup\">signup</a></p>"); 
			request.getRequestDispatcher("index.html").forward(request, response);
			}	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
