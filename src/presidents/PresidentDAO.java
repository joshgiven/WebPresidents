package presidents;

import java.util.*;
import java.util.function.*;

public interface PresidentDAO {
	@SuppressWarnings("serial")
	public static class PresidentMap extends HashMap<Integer, President> {}
	
	@SuppressWarnings("serial")
	public static class PresidentList extends ArrayList<President> {}
	
	public PresidentMap getPresidentsMap();
	
	public PresidentList getFilteredPresidents(Predicate<President> filter);
	
	public President getPresident(int ordinal);
	
	public default PresidentList getAllPresidents() {
		return getFilteredPresidents( p -> true );
	}
	
	public default List<String> getParties() {
		return getParties(getAllPresidents());
	}
	
	public default List<String> getParties(PresidentList presidents) {
		return getValueList(presidents, President::getParty );
	}
	
	public default List<String> getFirstNames() {
		return getFirstNames(getAllPresidents());
	}
	
	public default List<String> getFirstNames(PresidentList presidents) {
		return getValueList(presidents, President::getFirstName );
	}

	public default List<String> getLastNames() {
		return getLastNames(getAllPresidents());
	}

	public default List<String> getLastNames(PresidentList presidents) {
		return getValueList(presidents, President::getLastName );
	}
	
	public default List<Integer> getTermLengths() {
		return getTermLengths(getAllPresidents());
	}
	
	public default List<Integer> getTermLengths(PresidentList presidents) {
		return getValueList(presidents, p -> p.getEndTerm() - p.getStartTerm() );
	}
	
	public default <T> List<T> getValueList(PresidentList presidents, Function<President,T> pred) {
		List<T> values = new ArrayList<>();
		
		return values;
	}
}
