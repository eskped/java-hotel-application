package hotelBooking.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HotelBookingApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("HotelBooking.fxml"));
		stage.setTitle("Hotelbooking");
		stage.setScene(new Scene(parent));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(HotelBookingApp.class, args);
	}

}
