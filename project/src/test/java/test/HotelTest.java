package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import hotelBooking.model.Hotel;
import hotelBooking.model.HotelRoom;
import hotelBooking.model.StandardRoom;
import hotelBooking.model.Suite;

class HotelTest {
	
	HotelRoom hr1 = new StandardRoom(101, 2000, 70, 50, true);
	HotelRoom hr9 = new Suite(303, 3500, 70, 50, 800, false);


	@Test
	void testHotel() {
		Hotel h1 = new Hotel(3, 3, 2000, 70,
				50, 5, 1, 2, 800, 1000,
				1500, 1000);
		assertEquals(9, h1.getHotelRooms().size(), "Antall rom var ikke korrekt");
		assertEquals(hr1.toString() , h1.getHotelRoom(0).toString(), "Det første hotellrommet ble ikke opprettet korrekt");
		assertEquals(hr9.toString(), h1.getHotelRoom(h1.getHotelRooms().size()-1).toString(), "Det siste hotellrommet ble ikke opprettet korrekt");
		
		assertEquals(5 , h1.getHotelRooms().stream().filter(h -> (h instanceof StandardRoom)).collect(Collectors.toList()).size(), "Antall standard rom er ikke korrekt" );
		assertEquals(4 , h1.getHotelRooms().stream().filter(h -> (h instanceof Suite)).collect(Collectors.toList()).size(), "Antall Suiter er ikke korrekt" );
		
		assertEquals(1 , h1.getHotelRooms().stream().filter(h -> (h instanceof StandardRoom)).filter(d -> ((StandardRoom) d).getSmokingAllowed()).collect(Collectors.toList()).size(), "Antall rom der røyking er tillat er ikke korrekt" );
		assertEquals(2 , h1.getHotelRooms().stream().filter(h -> (h instanceof Suite)).filter(d -> ((Suite) d).getIndoorPool()).collect(Collectors.toList()).size(), "Antall rom med innendørs basseng er ikke korrekt" );
		
	}

	@Test
	void testGetHotelRoom() {
		Hotel h1 = new Hotel(3, 3, 2000, 70,
				50, 5, 1, 2, 800, 1000,
				1500, 1000);
		assertEquals(hr1.toString(), h1.getHotelRoom(0).toString(), "Klarte ikke hente først hotellrom");
		assertNotEquals(hr1, h1.getHotelRoom(1), "Hentet to hotellrom som burde vært ulike, men de var like");
	}

	@Test
	void testGetEmptyHotelRooms() {
		Hotel h1 = new Hotel(3, 3, 2000, 70,
				50, 5, 1, 2, 800, 1000,
				1500, 1000);
		assertEquals(9, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now().plusDays(7)).size());
		assertEquals(9, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now()).size());
		
		h1.getHotelRoom(0).setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(2), 1, 0, false);
		
		assertEquals(8, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now().plusDays(7)).size());
		assertEquals(8, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now()).size());	
		
		h1.getHotelRoom(1).setDatesBooked(LocalDate.now().minusDays(7), LocalDate.now().minusDays(2), 1, 0, false);
		assertEquals(8, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now().plusDays(7)).size());
		assertEquals(8, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now(), LocalDate.now().minusDays(1)).size());
		assertEquals(7, h1.getEmptyHotelRooms(h1.getHotelRooms(), LocalDate.now().minusDays(5), LocalDate.now().plusDays(3)).size());

	}

	@Test
	void testGetBookedHotelRooms() {
		Hotel h1 = new Hotel(3, 3, 2000, 70,
				50, 5, 1, 2, 800, 1000,
				1500, 1000);
		assertEquals(0, h1.getBookedHotelRooms().size());
		
		h1.getHotelRoom(0).setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(2), 1, 0, false);
		assertEquals(1, h1.getBookedHotelRooms().size());
		assertEquals(h1.getHotelRoom(0), h1.getBookedHotelRooms().get(0));
		
		h1.getHotelRoom(0).setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(8), 1, 0, false);
		assertEquals(1, h1.getBookedHotelRooms().size());
		assertEquals(h1.getHotelRoom(0), h1.getBookedHotelRooms().get(0));

		h1.getHotelRoom(1).setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(7), 1, 0, false);
		assertEquals(2, h1.getBookedHotelRooms().size());
		assertEquals(h1.getHotelRoom(1), h1.getBookedHotelRooms().get(1));
	}

//	@Test
//	void testGetHotelRooms() {
//		fail("Not yet implemented");
//	}

}
