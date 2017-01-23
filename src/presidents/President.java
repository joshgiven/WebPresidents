package presidents;

import java.io.Serializable;

public class President implements Serializable{
	private int ordinal;
	private String firstName;
	private String lastName;
	private String fullName;
	private int startTerm;
	private int endTerm;
	private String party;
	private String factoid;
	private String imagePath;
	private String thumbnailPath;
	
	public President() { }
	
	public int getOrdinal() {
		return ordinal;
	}
	
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public int getStartTerm() {
		return startTerm;
	}
	
	public void setStartTerm(int startTerm) {
		this.startTerm = startTerm;
	}
	
	public int getEndTerm() {
		return endTerm;
	}
	
	public void setEndTerm(int endTerm) {
		this.endTerm = endTerm;
	}
	
	public String getParty() {
		return party;
	}
	
	public void setParty(String party) {
		this.party = party;
	}
	
	public String getFactoid() {
		return factoid;
	}
	
	public void setFactoid(String factoid) {
		this.factoid = factoid;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getThumbnailPath() { 
		return thumbnailPath;
	}
	
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	private static final long serialVersionUID = 1L;
}
