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
	
	public default President getPrevious(PresidentList list, President current) {
		return getRelative(list, current, -1);
	}
	
	public default President getNext(PresidentList list, President current) {
		return getRelative(list, current, 1);
	}
	
	public default President getRelative(PresidentList list, President current, int shift) {
		if(list == null || list.isEmpty())
			return null;
		
		int currentIndex = list.indexOf(current);
		if(currentIndex < 0)
			return null;
		
		int index = currentIndex + shift;
		if(index < 0)
			index = list.size() - Math.abs(shift);
		else if(index >= list.size())
			index = Math.abs(shift) - 1;
		
		return list.get(index);
	}
	
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
	
	public default <T extends Comparable<T>> List<T> getValueList(PresidentList presidents, Function<President,T> func) {
		Set<T> values = new HashSet<>();
		for(President p : presidents) {
			values.add(func.apply(p));
		}
		
		List<T> valuesList = new ArrayList<>(values);
		Collections.sort(valuesList, (a,b) -> a.compareTo(b));
		return valuesList;
	}
}
