package hotelBooking.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Suite extends HotelRoom {
	
	private double priceForSpa;
	private int spa;
	private boolean indoorPool;
	ArrayList<LocalDate> datesBooked = new ArrayList<>();

	
	public Suite(String roomName, int roomNumber, double basePricePerNight, double pricePerAdultPerNight, double pricePerChildPerNight, int adults, int children, double priceForSpa, int spa, boolean indoorPool, ArrayList<LocalDate> datesBooked) {
		super(roomName, roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, datesBooked);
		this.priceForSpa = priceForSpa;
		this.spa = spa;
		this.indoorPool = indoorPool;
	}
	
	
	@Override
	public String toString() {
		return "Romtype: \t\t\t" + this.getRoomName() + super.toString() + "\nPris for spa: \t\t" + 
		this.priceForSpa + "\nBadebasseng: \t\t" + (this.indoorPool ? "Ja" : "Nei");
	}
	
	public Double getPriceForRoom() {
		return super.getPriceForRoom() + this.spa * this.priceForSpa + 3000 * (this.indoorPool ? 1 : 0);
	}
	
	public double getPriceForSpa() {
		return priceForSpa;
	}
	
	public void setSpa() {
		if (this.spa == 0) {
			this.spa = 1;
		}
	}
	
	public int getSpa() {
		return spa;
	}
	
	public void removeSpa() {
		this.spa = 0;
	}
	
	public boolean getIndoorPool() {
		return this.indoorPool;
	}
}
