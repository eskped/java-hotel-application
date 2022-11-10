package hotelBooking.fxui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import hotelBooking.model.Hotel;
import hotelBooking.model.HotelRoom;
import hotelBooking.model.HotelRoomIterator;
import hotelBooking.model.StandardRoom;
import hotelBooking.model.Suite;

public class HotelBookingFileSupport implements IHotelBookingFileReading {

	
	@Override
	public void writeHotelToFile(String filename, Hotel hotel) {
		HotelRoomIterator iterator = new HotelRoomIterator(hotel.getHotelRooms());
		HotelRoom hotelRoom;
		try {
			PrintWriter writer = new PrintWriter(new File(filename));
			
			while(iterator.hasNext()) {
				hotelRoom = iterator.next();
				String s = "";
				s += hotelRoom.getRoomName() + ";" + hotelRoom.getRoomNumber() + ";" 
						+ hotelRoom.getBasePricePerNight() + ";" + hotelRoom.getPricePerAdultPerNight() 
						+ ";" + hotelRoom.getPricePerChildPerNight() + ";" + hotelRoom.getAdults() + ";" 
						+ hotelRoom.getChildren();
				
				if (hotelRoom.getRoomName().equals("Standard rom")) {
					s += ";" + ((StandardRoom) hotelRoom).getSmokingAllowed();
				} else if (hotelRoom.getRoomName().equals("Suite")) {
					s += ";" + ((Suite) hotelRoom).getPriceForSpa() + ";" 
							+ ((Suite) hotelRoom).getIndoorPool() + ";" 
							+ ((Suite) hotelRoom).getSpa();
				}
				s += ";" + hotelRoom.getDatesBookedToString();
				writer.println(s);
			}
			
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	
	@Override
	public Hotel readFromFile(String filename) {
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
		try {

			Scanner scanner = new Scanner(new File(filename));
			ArrayList<HotelRoom> hotelRooms = new ArrayList<HotelRoom>();

			while(scanner.hasNextLine()) {

				String line = scanner.nextLine();
				String[] lineInfo = line.split(";");

				String roomName = lineInfo[0];
				int roomNumber = Integer.parseInt(lineInfo[1]);
				double basePricePerNight = Double.parseDouble(lineInfo[2]);
				double pricePerAdultPerNight = Double.parseDouble(lineInfo[3]);
				double pricePerChildPerNight = Double.parseDouble(lineInfo[4]);
				int adults = Integer.parseInt(lineInfo[5]);
				int children = Integer.parseInt(lineInfo[6]);

				String[] datesOnString = null;
				boolean smoking = false;
				double priceForSpa = 0;
				boolean indoorPool = false;
				int spa = 0;
								
				if (roomName.equals("Standard rom")) {
					smoking = Boolean.parseBoolean(lineInfo[7]);
					if (lineInfo.length >= 9) {
						datesOnString = lineInfo[8].split(" , ");
					}

				} else if (roomName.equals("Suite" )) {
					priceForSpa = Double.parseDouble(lineInfo[7]);
					indoorPool = Boolean.parseBoolean(lineInfo[8]);
					spa = Integer.parseInt(lineInfo[9]);
					if (lineInfo.length >= 11) {
						datesOnString = lineInfo[10].split(" , ");
					}
				}

				ArrayList<LocalDate> datesBooked = new ArrayList<LocalDate>();
				if (datesOnString != null) {
					for (String dateOnString : datesOnString) {
						datesBooked.add(LocalDate.parse(dateOnString, formatter));
					}
				}
				

				if (roomName.equals("Standard rom")) {
					StandardRoom standardRoom = new StandardRoom(roomName, roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, smoking, datesBooked);
					hotelRooms.add(standardRoom);

				} else {
					Suite suite = new Suite(roomName, roomNumber, basePricePerNight, pricePerAdultPerNight, pricePerChildPerNight, adults, children, priceForSpa, spa, indoorPool, datesBooked);
					hotelRooms.add(suite);
				}

			}
			
			return new Hotel(hotelRooms);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
}
