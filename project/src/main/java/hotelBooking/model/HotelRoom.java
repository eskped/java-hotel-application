package hotelBooking.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class HotelRoom  {

	private final int roomNumber;
	private List<LocalDate> datesBooked;
	private String s;
	
	private double pricePerChildPerNight;
	private double pricePerAdultPerNight;
	private double basePricePerNight;
	private int children;
	private int adults;
	private String roomName;


	
	public HotelRoom(String roomName, int roomNumber, double basePricePerNight, double pricePerAdultPerNight, double pricePerChildPerNight, int adults, int children, ArrayList<LocalDate> datesBooked) {
		this.roomName = roomName;
		this.roomNumber = roomNumber;
		this.basePricePerNight = basePricePerNight;
		this.pricePerAdultPerNight = pricePerAdultPerNight;
		this.pricePerChildPerNight = pricePerChildPerNight;
		this.adults = adults;
		this.children = children;
		this.datesBooked = datesBooked;
	}
	

	public String toString() {
		if (this.getDatesBooked().size()==0) {
			return "\nRomnummer: \t\t" + this.roomNumber + "\nPris per natt: \t\t" + String.valueOf(this.basePricePerNight) 
			+ "\nBooket dager:\t\tIngen";
		} else {
			return "\nRomnummer: \t\t" + this.roomNumber + "\nPris per natt: \t\t" + String.valueOf(this.basePricePerNight) 
			+ "\nBooket dager: \t\t" +  this.getDatesBookedToString() + "\nTotal pris: \t\t\t" +  this.getPriceForRoom();
		}
	}
	
	public String getDatesBookedToString() {
		s = "";
		for (LocalDate date : datesBooked) {
			s += date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
			if (!datesBooked.get(datesBooked.size()-1).equals(date)) {
				s += " , ";
			}
		}
		return s;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public Double getBasePricePerNight() {
		return basePricePerNight;
	}

	public List<LocalDate> getDatesBooked() {
		return datesBooked;
	}
	
	public int getAdults() {
		return adults;
	}
	
	public int getChildren() {
		return children;
	}
	
	public double getPricePerAdultPerNight() {
		return pricePerAdultPerNight;
	}
	
	public double getPricePerChildPerNight() {
		return pricePerChildPerNight;
	}
	
	public void setDatesBooked(LocalDate dateFrom, LocalDate dateTo, int adults, int children, boolean spaChecked) throws IllegalArgumentException {
		
		for (LocalDate date = dateFrom; date.isBefore(dateTo.plusDays(1)); date = date.plusDays(1)) {
			if (this.datesBooked.contains(date)) {
				throw new IllegalArgumentException("Datoen er allerede booket");
			}
		}
		
		for (LocalDate date = dateFrom; date.isBefore(dateTo.plusDays(1)); date = date.plusDays(1)) {
		    this.adults += adults;
		    this.children += children;
		    this.datesBooked.add(date);
		    
		}
		if (spaChecked && (this instanceof Suite)) {
			((Suite) this).setSpa();
		}
	}
	
	public void cancelRoom() {
		this.datesBooked.clear();
		this.adults = 0;
		this.children = 0;
		
		if (this instanceof Suite) {
			((Suite) this).removeSpa();
		}
	}
	
	protected Double getPriceForRoom() {
		return this.getBasePricePerNight() * this.getDatesBooked().size() + pricePerAdultPerNight * this.adults + this.pricePerChildPerNight * this.children;
	}
	
	
	public String getRoomName() {
		return this.roomName;
	}
	
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
}
