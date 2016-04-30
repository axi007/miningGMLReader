package gml.reader;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	// ODWO£ANIE DO G£ÓWNEGO KONTENERA GRAFIKI
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {

		// TWORZENIE INTERFEJSU GRAFICZNEGO
		primaryStage.setResizable(false);
		primaryStage.setTitle("Mining GML Reader");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("ikona.png")));
		initRootLayout();
		Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Inicializes the RootLayout
	 */
	public void initRootLayout() {
		try {
			// WCZYTYWANIE INFORMACJI O INTERFEJSIE GRAFICZNYM
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}

	} // KONIEC INITROOTLAYOUT

	public static void main(String[] args) {
		launch(args);
	}

} //KONIEC KLASY
