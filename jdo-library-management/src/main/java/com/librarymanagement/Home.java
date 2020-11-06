package com.librarymanagement;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * Servlet implementation class Home
 */
@WebServlet("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Home() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("userId") == null ) {
			response.sendRedirect("index.html");
		}
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate" );
			User user = (User) getServletContext().getAttribute("user");
			byte[] bytes = user.getProfilePic().getBytes();
            byte[] encodeBase64 = Base64.encodeBase64(bytes);
            String base64Encoded = new String(encodeBase64, "UTF-8");
			try {
			response.getWriter().print("<h1>Hi "+user.getName()+".!</h1><br>"
					+ "					<img alt=\"img\" style=\"border-radius: 50%; height:150px;position: absolute; top: 40px; right: 80px;\" src=\"data:image/jpeg;base64,"+base64Encoded+"\"/>"
					+ "					<a href=\"displayBook\">Display borrowed book</a><br>"
					+ "					<a href=\"barrowBook\">Get Book from Library</a><br>"
					+ "					<a href=\"returnBook\">Return a book to Library</a><br>");
			response.getWriter().print("<form action =\"logout\" method = \"POST\">\r\n"
					+ "         <input type = \"submit\" value = \"Logout\" />\r\n"
					+ "      </form> ");
			}catch(NullPointerException nullPointerExp) {
				response.sendRedirect("index.html");
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
