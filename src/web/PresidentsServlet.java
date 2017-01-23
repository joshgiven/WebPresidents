package web;

import java.io.IOException;
import java.util.*;
import java.util.function.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		// setup globals
		initContextAttributes(req);
		
		// process filters
//		String[] filterParams = req.getParameterValues("filter");
		String[] filterParams = {req.getParameter("field"), req.getParameter("operator"), req.getParameter("operand")};
		System.out.println(req.getParameterMap());
		if(filterParams[0] != null /*&& filterParams.length > 0*/)
		{
			Predicate<President> filter = p -> true;
			List<String> filterStrings = new ArrayList<>();
			
			filter = generateFilter(filter, filterParams, filterStrings);
			
			PresidentList filteredPresidents = null;
			if(filter != null) {
				filteredPresidents = presidentDAO.getFilteredPresidents(filter);
				req.getSession().setAttribute("presidents", filteredPresidents);
			}
			req.getSession().setAttribute("filters", filterStrings);
		}

		// present presidents view if selected...
		String mode = req.getParameter("view");
		if(mode != null && mode.equals("pres")) {
			// select the president list to use
			List<Object> path = Arrays.asList( 
					req.getSession().getAttribute("presidents"),
					req.getServletContext().getAttribute("presidents") );

	
//		System.out.println("sess " + req.getSession().getAttributeNames());
//		Enumeration<String> attrs =  req.getSession().getAttributeNames();
//		while(attrs.hasMoreElements()) {
//			String attr = attrs.nextElement();
//		    System.out.println(attr + " : " + req.getSession().getAttribute(attr));
//		}
//		
//		System.out.println("ctxt " + req.getServletContext().getAttributeNames());
//		attrs = req.getServletContext().getAttributeNames();
//		while(attrs.hasMoreElements()) {
//			String attr = attrs.nextElement();
//		    System.out.println(attr + " : " + req.getServletContext().getAttribute(attr));
//		}
		
			PresidentList list = 
					(PresidentList) path.stream().filter(x -> x != null).findFirst().get();
			
			System.out.println(list);
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
		
		// ... otherwise, serve index view
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	private void initContextAttributes(HttpServletRequest req) {
		System.out.println("init attrs");
		// setup global presidents list (if necessary)
		if(req.getServletContext().getAttribute("presidents") == null) {
			System.out.println("init pres attr");
			PresidentList fullList = presidentDAO.getAllPresidents();
			req.getServletContext().setAttribute("presidents", fullList);
		}
		
		// setup filter attributes
		if(req.getServletContext().getAttribute("filter-int-ops") == null) {
			req.getServletContext().setAttribute(
					"filterIntOps", Arrays.asList("eq", "gt", "ge", "lt", "le"));
			
			req.getServletContext().setAttribute(
					"filterIntProps", Arrays.asList("party", "first-name", "last-name"));
			
			req.getServletContext().setAttribute(
					"filterStringOps", Arrays.asList("equals", "contains", "starts-with", "ends-with"));
			
			req.getServletContext().setAttribute(
					"filterStringProps", Arrays.asList("term-length", "term-start", "term-end"));

			Map<String, String> prePartyMap = new TreeMap<>();
			for(String party : presidentDAO.getParties()) {
				prePartyMap.put(party, "party:equals:" + party);
			}
			prePartyMap.put("&lt;all parties&gt;", "");
			req.getServletContext().setAttribute(
					"filterPreParties", prePartyMap );
			
			Map<String, String> preTermLengthMap = new TreeMap<>();
			preTermLengthMap.put("Short (less than 4 years)",      "term-length:lt:4");
			preTermLengthMap.put("Single-Term (4 years)",          "term-length:eq:4");
			preTermLengthMap.put("Multi-Term (more than 4 years)", "term-length:gt:4");
			req.getServletContext().setAttribute(
					"filterPreTermLengths", preTermLengthMap );
			
			Map<String, String> preByCenturyMap = new TreeMap<>();
			preByCenturyMap.put("1700s", "term-start:lt:1800");
			//preByCenturyMap.put("1800s", "term-start:ge:1800&filter=term-start:lt:1900");
			//preByCenturyMap.put("1900s", "term-start:ge:1900&filter=term-start:lt:2000");
			preByCenturyMap.put("2000s", "term-start:ge:1999");
			req.getServletContext().setAttribute(
					"filterPreTermCenturies", preByCenturyMap );
			
			Map<String, String> preByFirstLetter = new TreeMap<>();
			for(String lname : presidentDAO.getLastNames()) {
				String init = "" + lname.charAt(0);
				preByFirstLetter.put(init, "last-name:starts-with:" + init);
			}
			req.getServletContext().setAttribute(
					"filterPreInitials", preByFirstLetter );
		}
	}

	private Predicate<President> generateFilter(Predicate<President> filter, String[] filterParams, List<String> filterStrings) {
		for (String param : filterParams) {
			String origParam = param;

			boolean doOr = false;
			if(param.startsWith("|")) {
				doOr = true;
				param = param.replaceAll("^\\|", "");
			}
			
			boolean doNegate = false;
			if(param.startsWith("-")) {
				doNegate = true;
				param = param.replaceAll("^-", "");
			}
			
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

			case "none":
				subFilter = null;
				filter = null;
				break;
				
			case "":
			default:
			}
			
			if(filter == null)    break;
			if(subFilter == null) continue;
			
			if(doNegate)
				subFilter = subFilter.negate();
			
			if(doOr)
				filter = filter.or(subFilter);
			else
				filter = filter.and(subFilter);

			filterStrings.add(origParam);
		}
		
		return filter;
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
