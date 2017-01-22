package presidents;

import java.io.*;
import java.util.Collections;
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
		
		//list.sort((a,b) -> Integer.compare(a.getOrdinal(), b.getOrdinal()));
		return list;
	}

	@Override
	public President getPresident(int ordinal) {
		while(ordinal < 1) 
			ordinal += presidents.size();
		
		while(ordinal > presidents.size())
			ordinal -= presidents.size();
		
		return presidents.get(ordinal);
	}
	
	private void importPresidentsFromFile(String fileName) {
		presidents = new PresidentMap();
		
		try(BufferedReader input = new BufferedReader(new FileReader(fileName))) {
			String line = null;
			while((line = input.readLine()) != null) {
				String[] tokens = line.split("\\|");
				
				President p = new President();
				try {
					p.setOrdinal(Integer.parseInt(tokens[0]));
					p.setFirstName(tokens[1]);
					p.setLastName(tokens[3]);
					p.setFullName(String.join(" ", tokens[1], tokens[2], tokens[3]));
					p.setStartTerm(Integer.parseInt(tokens[4].split("-")[0]));
					p.setEndTerm(Integer.parseInt(tokens[4].split("-")[1]));
					p.setParty(tokens[5]);
					p.setFactoid(tokens[6]);
					p.setImagePath(tokens[7]);
					//p.setThumbnailPath(tokens[7].replace("large_image", "thumbnail"));
					p.setThumbnailPath(tokens[8]);
				}
				catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
					e.printStackTrace(System.err);
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
