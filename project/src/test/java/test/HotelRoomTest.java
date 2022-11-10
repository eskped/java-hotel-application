package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import hotelBooking.model.HotelRoom;

class HotelRoomTest {
	
	private List<LocalDate> emptyDatesBooked = new ArrayList<LocalDate>();

	@Test
	void testConstructor() {
		HotelRoom h1 = new HotelRoom(100,1500,70,50, "Suite");
		assertEquals(100, h1.getRoomNumber(), "Nummeret til hotellrommet ble ikke satt riktig");
		assertTrue(h1.getDatesBooked().size() == 0);
		assertEquals(1500, h1.getBasePricePerNight(), "Basisprisen til hotellrommet ble ikke satt riktig");
	}
	
	@Test
	void testToString() {
		HotelRoom h1 = new HotelRoom(100,1500,70,50, "Suite");
		String s = h1.toString();
		assertTrue(s.contains("Romnummer"));
		assertTrue(s.contains("100"));
		assertTrue(s.contains("1500"));
		assertTrue(s.contains("Ingen"));
		h1.setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(1), 0, 0, false);
		assertTrue(h1.toString().contains(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM")).toString()));
		assertTrue(h1.toString().contains(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM")).toString()));
		assertEquals('R', h1.toString().charAt(1), "Det er en feil med starten av toString-metoden");
	}
	
	@Test
	void testSetDatesBooked() {
		HotelRoom h1 = new HotelRoom(100,1500,70,50, "Suite");
		h1.setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(6), 0, 0, false);
		List<LocalDate> datesBookedOneWeek = Stream.iterate(LocalDate.now(), date -> date.plusDays(1)).limit(7).collect(Collectors.toList());
		assertEquals(datesBookedOneWeek, h1.getDatesBooked(), "Datoene ble ikke lagt til riktig");
		
		HotelRoom h2 = new HotelRoom(100,1500,70,50, "Suite");
		h2.setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(13), 2, 1, false);
		List<LocalDate> datesBookedTwoWeeks = Stream.iterate(LocalDate.now(), date -> date.plusDays(1)).limit(14).collect(Collectors.toList());
		assertEquals(datesBookedTwoWeeks, h2.getDatesBooked(), "Datoene ble ikke lagt til riktig");
		assertEquals(2*14, h2.getAdults(), "Feil antall voksne man må betale for ble lagt til");
		assertEquals(1*14, h2.getChildren(), "Feil antall barn man må betale for ble lagt til");
		
	}
	
	
	@Test
	void testCancelRoom() {
		HotelRoom h1 = new HotelRoom(100,1500,70,50, "Suite");
		h1.setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(2), 0, 0, false);
		h1.cancelRoom();
		assertEquals(0, h1.getAdults(), "Avbestilling nullstilte ikke antall voksne");
		assertEquals(0, h1.getChildren(), "Avbestilling nullstilte ikke antall barn");
		assertEquals(emptyDatesBooked, h1.getDatesBooked(), "Hotellet burde ikke hatt noen datoer booket nå");
		
		HotelRoom h2 = new HotelRoom(100,1500,70,50, "Suite");
		h2.setDatesBooked(LocalDate.now(), LocalDate.now().plusDays(42), 2, 2, false);
		h2.cancelRoom();
		assertEquals(0, h2.getAdults(), "Avbestilling nullstilte ikke antall voksne");
		assertEquals(0, h2.getChildren(), "Avbestilling nullstilte ikke antall barn");
		assertEquals(emptyDatesBooked, h2.getDatesBooked(), "Avbestilling nullstilte ikke datoer");
		
	}
	
	
	@Test
	void testIsInteger() {
		assertTrue(HotelRoom.isInteger("0"));
		assertTrue(HotelRoom.isInteger("100"));
		assertTrue(HotelRoom.isInteger("-0"));
		assertTrue(HotelRoom.isInteger("-5"));
		assertTrue(HotelRoom.isInteger("-999"));
		
		assertFalse(HotelRoom.isInteger("a"));
		assertFalse(HotelRoom.isInteger("abc"));
		assertFalse(HotelRoom.isInteger("1.5"));
		assertFalse(HotelRoom.isInteger("1.0"));
		assertFalse(HotelRoom.isInteger("90a"));
	}
}
