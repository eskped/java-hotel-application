package hotelBooking.model;

import java.util.Iterator;
import java.util.List;

public class HotelRoomIterator implements Iterator<HotelRoom> {
	
	private int hotelRoomNumber;
	private List<HotelRoom> hotelRooms;
	
	public HotelRoomIterator(List<HotelRoom> hotelRooms) {
		this.hotelRooms = hotelRooms;
		this.hotelRoomNumber = -1;
	}
	
	@Override
	public boolean hasNext() {
		if (this.hotelRooms.size() > hotelRoomNumber+1)
			return true;
		
		return false;
	}

	@Override
	public HotelRoom next() {
		this.hotelRoomNumber += 1;
		return this.hotelRooms.get(hotelRoomNumber);
	}
	
	public void remove() {
		throw new UnsupportedOperationException("Kan ikke fjerne et hotellrom");
	}

}
