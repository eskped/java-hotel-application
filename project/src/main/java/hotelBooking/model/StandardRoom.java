package hotelBooking.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class StandardRoom extends HotelRoom {

	
	private boolean smoking;
	
	public StandardRoom(String roomName, int roomNumber, double basePricePerNight, double pricePerAdultPerNight, double pricePerChildPerNight, int adults, int children, boolean smoking, ArrayList<LocalDate> datesBooked ) {
		super(roomName, roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, datesBooked);
		this.smoking = smoking; 
	
	}
	
	@Override
	public String toString() {
		return "Romtype:\t\t\t" + this.getRoomName() + super.toString() +
				"\nRøyking tillat: \t\t" + (this.smoking ? "Ja" : "Nei");
	}
	

	public boolean getSmokingAllowed() {
		return this.smoking;
	}
	
}
