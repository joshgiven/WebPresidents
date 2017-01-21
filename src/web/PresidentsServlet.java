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
		
		PresidentList list = presidentDAO.getFilteredPresidents( p -> true );

		String mode = req.getParameter("view");
		if(mode != null && mode.equals("pres")) {
			try {
				int ordinal = Integer.parseInt(req.getParameter("id"));
				
				PresidentTrio trio = new PresidentTrio();
				trio.setCurrent(presidentDAO.getPresident(ordinal));
				trio.setPrevious(presidentDAO.getPresident(ordinal-1));
				trio.setNext(presidentDAO.getPresident(ordinal+1));
	
				req.getSession().setAttribute("trio", trio);
				req.getSession().setAttribute("presidents", list);
				req.getRequestDispatcher("/presidents.jsp").forward(req, resp);
				
				return;
			}
			catch(NumberFormatException e) {
				// error.jsp?
				// return;
				e.printStackTrace(System.err);
			}
		}
		
		
		// Predicate<President> filter = p -> p.getParty().equals("Republican") ;
		// Predicate<President> filter = p -> p.getParty().equals("Democrat") ;
		Predicate<President> filter = p -> true ;
		PresidentList filterList = presidentDAO.getFilteredPresidents(filter);
		
		req.getSession().setAttribute("filter", filterList);
		req.getSession().setAttribute("presidents", list);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	private static final long serialVersionUID = 1L;

}
