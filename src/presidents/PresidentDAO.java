package presidents;

import java.util.*;
import java.util.function.*;

public interface PresidentDAO {
	@SuppressWarnings("serial")
	public static class PresidentMap extends HashMap<Integer, President> {}
	
	@SuppressWarnings("serial")
	public static class PresidentList extends ArrayList<President> {}
	
	public PresidentMap getAllPresidents();
	
	public PresidentList getFilteredPresidents(Predicate<President> filter);
	
	public President getPresident(int ordinal);
}
