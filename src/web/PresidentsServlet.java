package web;

import java.io.IOException;
import java.util.function.Predicate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import presidents.*;
import presidents.PresidentDAO.PresidentList;

@WebServlet("/lookup.do")
public class PresidentsServlet extends HttpServlet {
	PresidentDAO presidentDAO;
	
	@Override
	public void init() throws ServletException {
		String fileName = getServletContext().getRealPath("WEB-INF/presidents.csv");
		presidentDAO = new PresidentDAOFileImpl(fileName);
	}
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String mode = req.getParameter("view");
		if(mode.equals("pres")) {
			try {
			int ordinal = Integer.parseInt(req.getParameter("id"));
			
			PresidentTrio trio = new PresidentTrio();
			trio.setCurrent(presidentDAO.getPresident(ordinal));
			trio.setPrevious(presidentDAO.getPresident(ordinal-1));
			trio.setNext(presidentDAO.getPresident(ordinal+1));
			
			req.getRequestDispatcher("/presidents.jsp").forward(req, resp);
			}
			catch(NumberFormatException e) {
				// error.jsp?
				return;
			}
		}
		else {
			PresidentList list = presidentDAO.getFilteredPresidents( p -> true );
			req.getSession().setAttribute("presidents", list);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
		
	}
}
