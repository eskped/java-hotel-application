package hotelBooking.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Hotel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String standardRoomName = "Standard rom";
	private String suiteRoomName = "Suite";
			
	private ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();	
	
	public Hotel(int numberOfFloors, int numberOfRoomsInFloor, double basePricePerNight, double pricePerAdultPerNight,
			double pricePerChildPerNight, int adults, int children, int numberOfStandardRooms, int numberOfSmokingRooms, int numberOfIndoorPools, 
			double priceStandardSpa, double pricePoolSpa, int spa, double suiteExtra, double suitePoolExtra) {
		for (int i = 1; i <= numberOfFloors; i++) {
			for (int j = 1; j <= numberOfRoomsInFloor; j++) {
				int roomNumber = 0;
				if (j > 9) {
					roomNumber = Integer.parseInt(Integer.toString(i) + Integer.toString(j));
				} else {
					roomNumber = Integer.parseInt(Integer.toString(i) + "0" + Integer.toString(j));
				}
				if (numberOfStandardRooms > 0 && numberOfSmokingRooms > 0) {
					this.hotelRooms.add(new StandardRoom(standardRoomName,roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, true, new ArrayList<LocalDate>()));
					numberOfStandardRooms--;
					numberOfSmokingRooms--;
				} else if (numberOfStandardRooms > 0) {
					this.hotelRooms.add(new StandardRoom(standardRoomName, roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, false, new ArrayList<LocalDate>()));
					numberOfStandardRooms--;
				} else if (numberOfIndoorPools > 0) {
					this.hotelRooms.add(new Suite(suiteRoomName, roomNumber, basePricePerNight + suitePoolExtra, pricePerAdultPerNight, pricePerChildPerNight, adults, children, pricePoolSpa, spa, true, new ArrayList<LocalDate>()));
					numberOfIndoorPools--;
				} else {
					this.hotelRooms.add(new Suite(suiteRoomName, roomNumber, basePricePerNight + suiteExtra, pricePerAdultPerNight, pricePerChildPerNight, adults, children, priceStandardSpa, spa, false, new ArrayList<LocalDate>()));
				}				
			}
		}
	}
	
	public Hotel(ArrayList<HotelRoom> hotelRooms) {
		this.hotelRooms = hotelRooms;
	}
	
	public HotelRoom getHotelRoom(int hotelRoomsIndex) {
		return hotelRooms.get(hotelRoomsIndex);
	} 
	
	
	public ArrayList<HotelRoom> getEmptyHotelRooms(List<HotelRoom> hotelRooms, LocalDate dateFrom, LocalDate dateTo) {
		return hotelRooms.stream()
								.filter(h -> h.getDatesBooked().stream()
								.noneMatch(d -> ((d.isAfter(dateFrom) && d.isBefore(dateTo)) || d.equals(dateFrom) || d.equals(dateTo))))
								.collect(Collectors.toCollection(ArrayList::new)); 
	}
	
	public List<HotelRoom> getSuitableHotelRooms(LocalDate dateFrom, LocalDate dateTo, boolean smokingInput, boolean spaInput) {
		ArrayList<HotelRoom> suitableHotelRooms = new ArrayList<HotelRoom>();
		
		
		HotelRoomIterator iterator = new HotelRoomIterator(this.hotelRooms);
		while (iterator.hasNext()) {
			HotelRoom HR = iterator.next();
//			System.out.println("\n\n" + suitableHotelRooms);
			
			if (!smokingInput && !spaInput) {
				suitableHotelRooms.add(HR);
				continue;
			}
			if (smokingInput && spaInput) {
				continue;
			}	
			if (spaInput && HR.getRoomName().equals("Suite")) {
				suitableHotelRooms.add(HR);
				continue;
			} 
			if (smokingInput && HR.getRoomName().equals("Standard rom")) {
				if (((StandardRoom) HR).getSmokingAllowed()) {
					suitableHotelRooms.add(HR);
				}
			}			
		}

		
//		for (HotelRoom hotelRoom : this.hotelRooms) {
//			if (!smokingInput && !spaInput) {
//				suitableHotelRooms.add(hotelRoom);
//				continue;
//			}
//			if (smokingInput && spaInput) {
//				continue;
//			}	
//			if (spaInput && hotelRoom.getRoomName().equals("Suite")) {
//				suitableHotelRooms.add(hotelRoom);
//				continue;
//			} 
//			if (smokingInput && hotelRoom.getRoomName().equals("Standard rom")) {
//				if (((StandardRoom) hotelRoom).smoking()) {
//					suitableHotelRooms.add(hotelRoom);
//				}
//			} else {
//				continue;
//			}
//		}
		ArrayList<HotelRoom> suitableHotelRooms2 = new ArrayList<HotelRoom>();
		suitableHotelRooms2.addAll(getEmptyHotelRooms(suitableHotelRooms, dateFrom, dateTo));
//		System.out.println(suitableHotelRooms2);
		return suitableHotelRooms2;
	}
	
	
	public ArrayList<HotelRoom> getBookedHotelRooms() {
		ArrayList<HotelRoom> bookedRooms = new ArrayList<>();
		
		HotelRoomIterator iterator = new HotelRoomIterator(hotelRooms);
		while(iterator.hasNext()) {
			HotelRoom HR = iterator.next();
			if (HR.getDatesBooked().size() != 0) {
				bookedRooms.add(HR);
			}
		}
//		for (HotelRoom hotelRoom : hotelRooms) {
//			if (hotelRoom.getDatesBooked().size() != 0) {
//				bookedRooms.add(hotelRoom);
//			}
//		}
		return bookedRooms;
	}
	
	public List<HotelRoom> getHotelRooms() {
		return this.hotelRooms;
	}
}
