package com.librarymanagement;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/adminHome")
public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("userId") == null ) {
			response.sendRedirect("index.html");
		}
		System.out.println(request.getSession().getAttribute("userId"));
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate" );
		response.getWriter().print("<h1>Hi Admin.!</h1><br>"
				+ "					<a href=\"addNewBook\">Add a new book</a><br>"
				+ "					<a href=\"displayAllUser\">Display all staff and student</a><br>"
				+ "					<a href=\"displayAllBooks\">Display All Books</a><br>");
		response.getWriter().print("<form action =\"logout\" method = \"POST\">\r\n"
				+ "         <input type = \"submit\" value = \"Logout\" />\r\n"
				+ "      </form> ");
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
