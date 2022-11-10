package hotelBooking.fxui;

import java.time.LocalDate;
import java.util.ArrayList;

import hotelBooking.model.Hotel;
import hotelBooking.model.HotelRoom;
import hotelBooking.model.HotelRoomIterator;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HotelBookingController {
	
	final private int numberOfFloors = 3;
	final private int numberOfRoomsInFloor = 3;
	final private int numberOfStandardRooms = 5;
	final private int numberOfSmokingRooms = 2;
	final private int numberOfIndoorPools = 1;
	private double pricePerChildPerNight = 50.0;
	private double pricePerAdultPerNight = 70.0;
	private double basePricePerNight = 2000.0;
	private double priceStandardSpa = 800.0;
	private double pricePoolSpa = 1200.0;
	private double suitePoolExtra = 1500;
	private double suiteExtra = 1000;
	private int setUpAdults = 0;
	private int setUpChildren = 0; 
	private int setUpSpa = 0;
	public Hotel hotelTrondheim = new Hotel(numberOfFloors, numberOfRoomsInFloor, basePricePerNight, pricePerAdultPerNight,
			pricePerChildPerNight, setUpAdults, setUpChildren, numberOfStandardRooms, numberOfSmokingRooms, numberOfIndoorPools,
			priceStandardSpa, pricePoolSpa, setUpSpa, suiteExtra, suitePoolExtra );
	private LocalDate dateFrom, dateTo;
	private boolean spaInput, smokingInput;
	private HotelRoom roomSelected;
	private ArrayList<HotelRoom> emptyRooms = new ArrayList<>();
	ArrayList<HotelRoom> bookedRooms = new ArrayList<>();
	private final IHotelBookingFileReading fileSupport = new HotelBookingFileSupport();
	
	@FXML private DatePicker dateFromInput, dateToInput;
	@FXML private TextField numberOfAdultsInput, numberOfChildrenInput;
	@FXML private ListView<HotelRoom> emptyRoomsOutput, bookedRoomsOutput;
	@FXML private Label bigTitle, errorMessageToUser;
	@FXML private CheckBox smokingCheckBox, spaCheckBox;

	
	
	@FXML
    public void initialize() {
		hotelTrondheim = new Hotel(numberOfFloors, numberOfRoomsInFloor, basePricePerNight, pricePerAdultPerNight,
				pricePerChildPerNight, setUpAdults, setUpChildren, numberOfStandardRooms, numberOfSmokingRooms, numberOfIndoorPools,
				priceStandardSpa, pricePoolSpa, setUpSpa, suiteExtra, suitePoolExtra );
    }
	
	public void setHotel(Hotel hotel) {
		hotelTrondheim = hotel;
		updateGUI();
	}
	
	public void updateGUI() {
		removeMessage();
		emptyRoomsOutput.getItems().clear();
		if (dateFromInput.getValue() == null) {
			dateFromInput.setValue(LocalDate.now());
		}
		if (dateToInput.getValue() == null) {
			dateToInput.setValue(LocalDate.now().plusDays(1));
		}
		
		dateFrom = dateFromInput.getValue();
		dateTo = dateToInput.getValue();
		smokingInput = smokingCheckBox.isSelected();
		spaInput = spaCheckBox.isSelected();
		
		emptyRooms.clear();
		emptyRooms.addAll(hotelTrondheim.getSuitableHotelRooms(dateFrom, dateTo, smokingInput, spaInput));
		
		if (emptyRooms.size() == 0) {
			bigTitle.setText("Ingen ledige hotellrom i oppgitt dato");
		} else {
			bigTitle.setText("Ledige hotellrom");
			HotelRoomIterator iterator = new HotelRoomIterator(emptyRooms);
			while (iterator.hasNext()) {				
				emptyRoomsOutput.getItems().add(iterator.next());
			}
		}
		
		bookedRoomsOutput.getItems().clear();
		bookedRooms.clear();
		bookedRooms.addAll(hotelTrondheim.getBookedHotelRooms());
		HotelRoomIterator iterator = new HotelRoomIterator(bookedRooms);
		while (iterator.hasNext()) {
			bookedRoomsOutput.getItems().add(iterator.next());
		}
	}
	
	@FXML
	public void handleFindHotelroom() {		
		if(checkBasicInput())
			updateGUI();
	}
	
	
	@FXML
	public void handleOrderHotelroom() {
		if (checkBasicInput()) {
			if (emptyRoomsOutput.getSelectionModel().getSelectedItem() != null) {
				
				
				dateFrom = dateFromInput.getValue();
				dateTo = dateToInput.getValue();
				
				int numberOfAdults = Integer.parseInt(numberOfAdultsInput.getText());
				int numberOfChildren = Integer.parseInt(numberOfChildrenInput.getText());
				boolean spaChecked = spaCheckBox.isSelected();
				
				roomSelected = emptyRoomsOutput.getSelectionModel().getSelectedItem();
				int index = hotelTrondheim.getHotelRooms().indexOf(roomSelected);
				try {
					hotelTrondheim.getHotelRoom(index).setDatesBooked(dateFrom, dateTo, numberOfAdults, numberOfChildren, spaChecked);
					updateGUI();
				} catch (IllegalArgumentException e) {
					showMessage(e.getMessage());
				}
				
			} else {
				showMessage("Velg et rom å bestille");
			}	
		}
	}
	
	
	@FXML
	public void handleCancelHotelroom() {
		if (bookedRoomsOutput.getSelectionModel().getSelectedItem() != null) {
			int roomSelectedNumber = hotelTrondheim.getHotelRooms().indexOf(bookedRoomsOutput.getSelectionModel().getSelectedItem());
			hotelTrondheim.getHotelRoom(roomSelectedNumber).cancelRoom();
			updateGUI();
		} else {
			showMessage("Velg et rom å avbestille");
		}
	}
	
	@FXML
	public void handleSaveToFile() {
		if (checkBasicInput()) {
			try {
				fileSupport.writeHotelToFile("fillagring.txt", hotelTrondheim);
				updateGUI();
			} catch (Exception e) {
				showMessage("Klarte ikke å lagre til fil");
			}
		}
	}
	
	@FXML
	public void handleReadFromFile() {
		try {
			setHotel(fileSupport.readFromFile("fillagring.txt"));
			updateGUI();
		} catch (Exception e) {
			showMessage("Klarte ikke laste fra fil");
		}
	}
	
	@FXML
	public void handleEmptyRoomsClick() {
		bookedRoomsOutput.getSelectionModel().clearSelection();
	}
	
	@FXML
	public void handleBookedRoomsClick() {
		emptyRoomsOutput.getSelectionModel().clearSelection();
	}
	
	private boolean checkBasicInput() {
		if (dateFromInput.getValue() == null || dateToInput.getValue() == null) {
			showMessage("Skriv inn datoer");
			return false;
		}
		else if (dateFromInput.getValue().isAfter(dateToInput.getValue())) {
			showMessage("Før-dato kan ikke\nvære etter\ntil-dato");
			return false;
		} else if (numberOfChildrenInput.getText().equals("") || numberOfAdultsInput.getText().equals("") || (numberOfChildrenInput.getText().equals("0") && numberOfAdultsInput.getText().equals("0"))) {
			showMessage("Skriv inn antall\ngjester");
			return false;
		} else if (!HotelRoom.isInteger(numberOfAdultsInput.getText())) {
			showMessage("Skriv bare inn tall\nfor voksne");
			return false;
		} else if (!HotelRoom.isInteger(numberOfChildrenInput.getText())) {
			showMessage("Skriv bare inn tall\nfor barn");
			return false;
		} else if (Integer.parseInt(numberOfChildrenInput.getText()) != 0 && Integer.parseInt(numberOfAdultsInput.getText()) == 0) {
			showMessage("Barn kan ikke booke\nalene");
			return false;
		}
		return true;
	}
	

	public void showMessage(String message) {
		errorMessageToUser.setText(message);

	}
	
	private void removeMessage() {
		errorMessageToUser.setText("");
	}
	
}
