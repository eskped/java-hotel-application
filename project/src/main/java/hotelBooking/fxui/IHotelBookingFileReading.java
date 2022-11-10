package hotelBooking.fxui;

import hotelBooking.model.Hotel;

public interface IHotelBookingFileReading  {
	
	public void writeHotelToFile(String filename, Hotel hotel);
	
	public Hotel readFromFile(String filename);
	
}
