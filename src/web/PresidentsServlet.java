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
			Predicate<President> filter = p -> true;
			
			for (String param : filterParams) {
				String[] tokens = param.split(":");
				if(tokens.length != 3) {
					continue;
				}
				
				String entity = tokens[0];
				String op   = tokens[1];
				String arg  = tokens[2];
				
				Predicate<President> subFilter = null;
				switch(entity) {
				case "party":
				case "first-name":
				case "last-name":
					subFilter = generateStringFilter(entity, op, arg);
					break;

				case "term-length":
				case "term-start":
				case "term-end":
					subFilter = generateIntegerFilter(entity, op, arg);
					break;

				default:
				}
				
				if(subFilter != null) {
					filter = filter.and(subFilter);
				}
			}
			
			PresidentList filterList = presidentDAO.getFilteredPresidents(filter);
			req.getSession().setAttribute("presidents", filterList);
		}

		
		String mode = req.getParameter("view");
		if(mode != null && mode.equals("pres")) {
			
			List<Object> path = Arrays.asList( 
					req.getSession().getAttribute("presidents"),
					req.getServletContext().getAttribute("presidents") );
			
			PresidentList list = 
					(PresidentList) path.stream().filter(x -> x != null).findFirst().get();
			
			try {
				int ordinal = Integer.parseInt(req.getParameter("id"));
				
				President current = presidentDAO.getPresident(ordinal);
				PresidentTrio trio = new PresidentTrio();
				trio.setCurrent(current);
				trio.setPrevious(presidentDAO.getPrevious(list, current));
				trio.setNext(presidentDAO.getNext(list, current));
	
				req.getSession().setAttribute("trio", trio);
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
	
	private Predicate<President> generateIntegerFilter(String entity, String op, String arg) {
		Function<President,Integer> presFunc = null;

		switch(entity) {
		case "term-length":
			presFunc = p -> p.getEndTerm() - p.getStartTerm();
			break;
		case "term-start":
			presFunc = President::getStartTerm; break;
		case "term-end":
			presFunc = President::getEndTerm;   break;
		}
		
		BiFunction<Integer, Integer, Boolean> opFunc = null;
		switch(op) {
		case "eq":
			opFunc = Integer::equals; break;
		case "gt":
			opFunc = (a,b) -> a > b;  break;
		case "lt":
			opFunc = (a,b) -> a < b;  break;
		case "ge":
			opFunc = (a,b) -> a >= b; break;
		case "le":
			opFunc = (a,b) -> a <= b; break;
		}
		
		Integer argVal = null;
		try {
			argVal = new Integer(arg);
		}
		catch(NumberFormatException e) {
			e.printStackTrace(System.err);
		}
		
		if(presFunc == null || opFunc == null || argVal == null) {
			return null;
		}
		
		final Function<President,Integer>           piFunc = presFunc;
		final BiFunction<Integer, Integer, Boolean> ibFunc = opFunc;
		final Integer fArgVal                              = argVal;
		
		return (pres) -> ibFunc.apply(piFunc.apply(pres), fArgVal);
	}

	private Predicate<President> generateStringFilter(String entity, String op, String arg) {
		Function<President,String> presFunc = null;
		switch(entity) {
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
		
		if(presFunc == null || opFunc == null || arg == null) {
			return null;
		}
		
		final Function<President,String>          psFunc = presFunc;
		final BiFunction<String, String, Boolean> sbFunc = opFunc;

		return (pres) -> sbFunc.apply(psFunc.apply(pres), arg);
	}

	private static final long serialVersionUID = 1L;

}
