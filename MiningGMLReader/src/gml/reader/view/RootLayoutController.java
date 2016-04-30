package gml.reader.view;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gml.reader.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class RootLayoutController implements Initializable {

	@FXML
	private MenuItem openDataMenuItem;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	private Button parseButton;

	@FXML
	private Button poligonyButton;

	@FXML
	private Button drawButton;

	@FXML
	private ToggleButton showParametersButton;

	@FXML
	private Label activeFileLabel;

	@FXML
	private Label fileNameLabel;

	@FXML
	private AnchorPane anchor;

	private double scale;
	private double offsetX;
	private double offsetY;
	private List<Polygon> polygons = new ArrayList<Polygon>();
	private Group group = new Group();

	// RYSOWANIE PARCELI
	@FXML
	void draw(ActionEvent event) {
		setScale(0);
		polygons.clear();
		if (group.getChildren().size() > 0) {
			group.getChildren().remove(0, group.getChildren().size());
		}
		if (anchor.getChildren().size() > 0) {
			anchor.getChildren().remove(0);
			anchor.setScaleY(1);
		}

		if (Logika.parcele.size() > 0 && Logika.getGmlFile() != null) {

			// TWORZENIE POLIGONÓW
			for (int i = 0; i < Logika.parcele.size(); i++) {
				double[] wsp = new double[Logika.parcele.get(i).punkty.size() * 2];
				for (int j = 0; j < Logika.parcele.get(i).punkty.size(); j++) {
					wsp[j * 2] = Logika.parcele.get(i).punkty.get(j).Y;
					wsp[j * 2 + 1] = Logika.parcele.get(i).punkty.get(j).X;
				}

				Polygon poligon = new Polygon(wsp);
				polygons.add(poligon);
			}

			// USTAWIENIA WYGLADU POLIGONOW
			for (Polygon poligon : polygons) {
				poligon.setFill(Color.GRAY);
				poligon.setStroke(Color.BLACK);
				poligon.setStrokeWidth(2);
				group.getChildren().add(poligon);
			}

			// USTAWIENIA SKALI
			anchor.setScaleY(-1);
			double scaleX = group.getBoundsInLocal().getHeight() / 400;
			double scaleY = group.getBoundsInLocal().getWidth() / 400;
			if (scaleX > scaleY) {
				setScale(scaleX);
			} else {
				setScale(scaleY);
			}

			// DOPASOWANIE DO OBSZARU WYSWIETLANIA
			double height = group.layoutBoundsProperty().getValue().getHeight();
			double width = group.layoutBoundsProperty().getValue().getWidth();
			double offsetX = Math.abs((height - 500) * 0.5);
			double offsetY = Math.abs((width - 500) * 0.5);
			if (height < 500) {
				offsetX = -offsetX;
			}
			if (width < 500) {
				offsetY = -offsetY;
			}
			setOffsetX(offsetX);
			setOffsetY(offsetY);

			group.setTranslateX(-(group.layoutBoundsProperty().getValue().getMinX()) - getOffsetY());
			group.setTranslateY(-(group.layoutBoundsProperty().getValue().getMinY()) - getOffsetX());
			group.setScaleX(1 / getScale());
			group.setScaleY(1 / getScale());
			anchor.getChildren().add(group);

		} else {
			Popout.popoutMessage("B£¥D", "Brak poprawnie wczytanych danych!");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// USTALANIE WLASCIWOSCI POCZATKOWYCH
		activeFileLabel.setTextFill(Color.RED);
		anchor.getStyleClass().add("anchor");

		// ZAMKNIJ Z MENU
		closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});

		// WCZYTYWANIE PLIKU GML
		openDataMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				File gml = Logika.loadGML();
				Logika.setGmlFile(gml);
				if (Logika.getGmlFile() != null) {
					activeFileLabel.setTextFill(Color.GREEN);
					fileNameLabel.setText(Logika.getGmlFile().getName());
				}
			}
		});

		// PARSOWANIE
		parseButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (Logika.getGmlFile() != null) {
					Logika.parsowanie();
				} else {
					Popout.popoutMessage("B£¥D", "Wczytaj plik do sparsowania.");
				}
			}
		});

		ToggleGroup group = new ToggleGroup();
		showParametersButton.setToggleGroup(group);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue != null) {
					for (Polygon poligon : polygons) {
						Tooltip tip = new Tooltip();

						poligon.setOnMouseEntered(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {
								poligon.setCursor(Cursor.HAND);
								poligon.setOnMouseClicked(new EventHandler<MouseEvent>() {

									@Override
									public void handle(MouseEvent me) {
										Point2D p = anchor.localToScreen(anchor.getLayoutBounds().getMinX(),
												anchor.getLayoutBounds().getMaxY());
										tip.setText(" ID: " + Logika.parcele.get(polygons.indexOf(poligon)).ID
												+ "\n Nazwa: " + Logika.parcele.get(polygons.indexOf(poligon)).nazwa
												+ "\n Rok Eksploatacji: "
												+ Logika.parcele.get(polygons.indexOf(poligon)).rokEksploatacji
												+ "\n Mia¿szoœæ "
												+ Logika.parcele.get(polygons.indexOf(poligon)).miazszosc
												+ "\n Sposób kierowania stropem: "
												+ Logika.parcele.get(polygons.indexOf(poligon)).sposobKierStropem
												+ "\n Powierzchnia: "
												+ Logika.parcele.get(polygons.indexOf(poligon)).powierzchnia);
										tip.show(anchor, p.getX(), p.getY());
									};
								});
							}
						});
						poligon.setOnMouseExited(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {
								tip.hide();
							}
						});
					}
				} else {
					for (Polygon poligon : polygons) {
						poligon.setOnMouseClicked(null);
						poligon.setOnMouseEntered(null);
						poligon.setCursor(Cursor.DEFAULT);

					}
				}
			}
		});

		aboutMenuItem.setOnAction(event -> Popout.popoutMessage("\nO Programie",
				"Program MiningGMLReader wersja 1.0" + "\nUtworzony w ramach pracy dyplomowej in¿ynierskiej"
						+ "\nAutor: Albert Gierlicki" + "\ne-mail: albert.gierlicki@gmail.com"
						+ "\nAkademia Górniczo-Hutnicza im. Stanis³awa Staszica " + "\nKraków, 2015r."));

	} // Koniec initialize

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}
} // Koniec klasy RootLayoutController
