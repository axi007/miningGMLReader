package gml.reader.view;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class Popout {

	public static void popoutMessage(String title, String msg) {

		// TWORZENIE I USTAWIENIA G£ÓWNEGO OKNA
		Stage secondStage = new Stage();
		secondStage.initModality(Modality.APPLICATION_MODAL);
		secondStage.setTitle(title);
		secondStage.setResizable(false);

		// ETYKIETA WYSWIETLAJACA TEKST
		Label label = new Label();
		label.setWrapText(true);
		label.setText(msg);
		label.setTextAlignment(TextAlignment.CENTER);
		Button ok = new Button("Powrót");
		ok.setOnAction(e-> secondStage.close());

		// TWORZENIE I USTAWIENIA G£ÓWNEJ SCENY
		VBox layout = new VBox(5);
		layout.getChildren().addAll(label, ok);
		layout.setAlignment(Pos.CENTER);
		Scene secondScene = new Scene(layout);
		secondStage.setScene(secondScene);
		secondStage.showAndWait();
	}
}
