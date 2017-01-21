package web;

import presidents.President;

public class PresidentTrio {
	private President current;
	private President previous;
	private President next;
	
	public PresidentTrio() { }
	
	public President getCurrent() {
		return current;
	}
	
	public void setCurrent(President current) {
		this.current = current;
	}
	
	public President getPrevious() {
		return previous;
	}
	
	public void setPrevious(President previous) {
		this.previous = previous;
	}
	
	public President getNext() {
		return next;
	}
	
	public void setNext(President next) {
		this.next = next;
	}
}
