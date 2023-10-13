package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class AkademskaGodina { 
	private int id;
	private String godina;
	private Timestamp datumPocetka;
    private Timestamp datumKraja;
    private Timestamp datumPocetkaUpisa;
    private Timestamp datumKrajaUpisa;
	
	private Prodekan prodekan;//
	private List<AkademskiPredmet> akademskiPredmeti=new ArrayList<>();//
	private List<GodinaStudija> godineStudija=new ArrayList<>();//
     private List<Student> studenti=new ArrayList<>();//
     private List<Zahtjev> zahtjeviZaPromjenu=new ArrayList<>();//
	
	 public AkademskaGodina() {
	        // Inicijalizacija članova
	         // Dodaj sedam dana
	    }
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getGodina() {
		
		return godina;
	}
	public void setGodina(String godina) {
		this.godina = godina;
	}
	public Timestamp getDatumPocetka() {
		return datumPocetka;
	}
	public void setDatumPocetka(Timestamp datumPocetka) {
		this.datumPocetka = datumPocetka;
	}
	public Timestamp getDatumKraja() {
		return datumKraja;
	}
	public void setDatumKraja(Timestamp datumKraja) {
		this.datumKraja = datumKraja;
	}

	
	public Timestamp getDatumPocetkaUpisa() {
		return datumPocetkaUpisa;
	}
	public void setDatumPocetkaUpisa(Timestamp datumPocetkaUpisa) {
		this.datumPocetkaUpisa = datumPocetkaUpisa;
	}
	public Timestamp getDatumKrajaUpisa() {
		return datumKrajaUpisa;
	}
	public void setDatumKrajaUpisa(Timestamp datumKrajaUpisa) {
		this.datumKrajaUpisa = datumKrajaUpisa;
	}
	@ManyToOne()
	public Prodekan getProdekan() {
		return prodekan;
	}
	public void setProdekan(Prodekan prodekan) {
		this.prodekan = prodekan;
	}
	
	@OneToMany(mappedBy="akademskaGodina",cascade=CascadeType.ALL)
	public List<AkademskiPredmet> getAkademskiPredmeti() {
		return akademskiPredmeti;
	}
	public void setAkademskiPredmeti(List<AkademskiPredmet> akademskiPredmeti) {
		this.akademskiPredmeti = akademskiPredmeti;
	}
	
	
	public String toString() {
		
		return  this.godina+" "+"Upis:"+this.datumPocetkaUpisa+" "+"Kraj upisa: "+this.datumKrajaUpisa;
	}
	
	public boolean upisOtvoren() {
	    Timestamp trenutniDatum = new Timestamp(System.currentTimeMillis());
	    return ((trenutniDatum.before(datumKrajaUpisa))&&(trenutniDatum.after(datumPocetkaUpisa)));
	}

	
	@ManyToMany
	@JoinTable(name="akademskaGodina_godinaStudija",
	joinColumns=@JoinColumn(name="akademskaGodina_id"),
	inverseJoinColumns=@JoinColumn(name="godinaStudija_id"))
	public List<GodinaStudija> getGodineStudija() {
		return godineStudija;
	}

	public void setGodineStudija(List<GodinaStudija> godineStudija) {
		this.godineStudija = godineStudija;
	}

	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="akademskaGodina")
	public List<Student> getStudenti() {
		return studenti; 
	}

	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
    
	@OneToMany
	public List<Zahtjev> getZahtjeviZaPromjenu() {
		return zahtjeviZaPromjenu;
	}

	public void setZahtjeviZaPromjenu(List<Zahtjev> zahtjeviZaPromjenu) {
		this.zahtjeviZaPromjenu = zahtjeviZaPromjenu;
	}
	
	
	public List<AkademskiPredmet> zimskiSemestar(){
		List<AkademskiPredmet> zimski=new ArrayList<>();
		
		for(AkademskiPredmet p: akademskiPredmeti) {
			if(p.getPredmet().getSemestar().getLjetniZimski().equals("zimski")) 
				zimski.add(p);
			
		}
		return zimski;
		
	}
	
	
	
	public List<AkademskiPredmet> ljetniSemestar(){
	List<AkademskiPredmet> zimski=new ArrayList<>();
		
		for(AkademskiPredmet p: akademskiPredmeti) {
			if(p.getPredmet().getSemestar().getLjetniZimski().equals("ljetni")) 
				zimski.add(p);
			
		}
		return zimski;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
