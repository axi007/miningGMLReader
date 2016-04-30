package gml.reader.model;
import java.util.List;

public class Parcela {

	// POLA
	public int ID;
	public String nazwa;
	public int rokEksploatacji;
	public double miazszosc;
	public int sposobKierStropem;
	public double powierzchnia;
	public List<Punkt> punkty;

	// KONSTRUKTOR
	public Parcela (int id, String nazwa, int rokEksploatacji, double miazszosc, int sposobKierStropem, double powierzchnia,
			List<Punkt> punkty){

		this.ID = id;
		this.nazwa = nazwa;
		this.rokEksploatacji = rokEksploatacji;
		this.miazszosc = miazszosc;
		this.sposobKierStropem = sposobKierStropem;
		this.powierzchnia = powierzchnia;
		this.punkty = punkty;
	}
} // KONIEC KLASY PARCELA
