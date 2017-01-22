package web;

import java.io.IOException;
import java.util.*;
import java.util.function.*;

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
		
		// setup global presidents list
		if(req.getServletContext().getAttribute("presidents") == null) {
			PresidentList fullList = presidentDAO.getAllPresidents();
			req.getServletContext().setAttribute("presidents", fullList);
		}
		
		// setup filter attribute
		String[] filterParams = req.getParameterValues("filter");
		if(filterParams != null && filterParams.length > 0)
		{
			//List<Predicate<President>> filters = new ArrayList<>();
			// Predicate<President> filter = p -> p.getParty().equals("Democrat") ;
			// Predicate<President> filter = p -> p.getParty().equals("Republican") ;
			Predicate<President> filter = p -> true;
			
			for (String param : filterParams) {
				String[] tokens = param.split(":");
				if(tokens.length != 3) {
					continue;
				}
				
				String item = tokens[0];
				String op   = tokens[1];
				String arg  = tokens[2];
				
				Predicate<President> pred = null;
				
				switch(item) {
				case "party":
				case "first-name":
				case "last-name":
					Function<President,String> presFunc = null;
					switch(item) {
					case "party":
						presFunc = President::getParty;     break;
					case "first-name":
						presFunc = President::getFirstName; break;
					case "last-name":
						presFunc = President::getLastName;  break;
					}
					
					BiFunction<String, String, Boolean> opFunc = null;
					switch(op) {
					case "equals":
						opFunc = String::equals;     break;
					case "contains":
						opFunc = String::contains;   break;
					case "starts-with":
						opFunc = String::startsWith; break;
					case "ends-with":
						opFunc = String::endsWith;   break;
					} 
					
					final Function<President,String>          psFunc = presFunc;
					final BiFunction<String, String, Boolean> sbFunc = opFunc;
					pred = (pres) -> sbFunc.apply(psFunc.apply(pres), arg);

					break;

				case "term-length":
				case "term-start":
				case "term-end":
				default:
				}
				
				if(pred != null) {
					filter = filter.and(pred);
				}
			}
			
			PresidentList filterList = presidentDAO.getFilteredPresidents(filter);
			req.getSession().setAttribute("presidents", filterList);
		}
		

		
		String mode = req.getParameter("view");
		if(mode != null && mode.equals("pres")) {
			try {
				int ordinal = Integer.parseInt(req.getParameter("id"));
				
				PresidentTrio trio = new PresidentTrio();
				trio.setCurrent(presidentDAO.getPresident(ordinal));
				trio.setPrevious(presidentDAO.getPresident(ordinal-1));
				trio.setNext(presidentDAO.getPresident(ordinal+1));
	
				req.getSession().setAttribute("trio", trio);
				//req.getSession().setAttribute("presidents", list);
				req.getRequestDispatcher("/presidents.jsp").forward(req, resp);
				
				return;
			}
			catch(NumberFormatException e) {
				// error.jsp?
				// return;
				e.printStackTrace(System.err);
			}
		}
		
		// serve index view
		//req.getSession().setAttribute("presidents", list);
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	private static final long serialVersionUID = 1L;

}
