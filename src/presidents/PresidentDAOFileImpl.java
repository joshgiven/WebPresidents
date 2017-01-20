package presidents;

import java.io.*;
import java.util.function.*;

public class PresidentDAOFileImpl implements PresidentDAO {
	private PresidentMap presidents;
	
	public PresidentDAOFileImpl(String fileName) {
		importPresidentsFromFile(fileName);
	}

	@Override
	public PresidentMap getAllPresidents() {
		return presidents;
	}

	@Override
	public PresidentList getFilteredPresidents(Predicate<President> filter) {
		PresidentList list = new PresidentList();
		
		for(Integer ordinal : presidents.keySet()) {
			President p = presidents.get(ordinal);
			if(filter.test(p)) {
				list.add(p);
			}
		}
		
		return list;
	}

	@Override
	public President getPresident(int ordinal) {
		return presidents.get(ordinal);
	}
	
	private void importPresidentsFromFile(String fileName) {
		presidents = new PresidentMap();
		
		try(BufferedReader input = new BufferedReader(new FileReader(fileName))) {
			String line = null;
			while((line = input.readLine()) != null) {
				String[] tokens = line.split(",");
				
				President p = new President();
				try {
					p.setOrdinal(Integer.parseInt(tokens[0]));
					p.setFirstName(tokens[1]);
					p.setLastName(tokens[2]);
					p.setFullName(String.format("%s %s", tokens[1], tokens[2]));
					p.setStartTerm(Integer.parseInt(tokens[3]));
					p.setEndTerm(Integer.parseInt(tokens[4]));
					p.setParty(tokens[5]);
					p.setFactoid(tokens[6]);
					p.setImagePath(tokens[7]);
				}
				catch(NumberFormatException e) {
					continue;
				}
				
				presidents.put(p.getOrdinal(), p);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
