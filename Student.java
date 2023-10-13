package models;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType; 
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jgoodies.common.base.Objects;



@Entity 
public class Student  {
	private Long id;
    private String broj_indeksa;
    private String ime;
    private String prezime; 
    private String username;
	private String password;
	private String status = null;
	private int ects=0;
	
	//Tipovi za kreiranje veza na getterima
	
	private List<AkademskiPredmet> predmeti_ak_godine=new ArrayList<>();
	private Usmjerenje usmjerenje;//
	private StudijskiProgram studijskiProgram;//
	private List<Zahtjev> promjenaZahtjevi;//
	private List<ZahtjevBodovi> prenosZahtjevi = new ArrayList<>();//
	private List<ZahtjevPreduslov> preduslovZahtjevi = new ArrayList<>();//
    private GodinaStudija godinaStudija = null;	//
    private List<PredmetOcjena> ocjene=new ArrayList<>();//
    private AkademskaGodina akademskaGodina=null; //trenutna
   
	
	
	//Konstruktori
	public Student() {}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Long getId() {
		return id;
	}
   

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getBroj_indeksa() {
		return broj_indeksa;
	}

	public void setBroj_indeksa(String broj_indeksa) {
		this.broj_indeksa = broj_indeksa;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}





	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	
   

    @ManyToOne
	public Usmjerenje getUsmjerenje() { 
		return usmjerenje;
	}

	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}
    
	@ManyToOne
	public StudijskiProgram getStudijskiProgram() {
		return studijskiProgram;
	}

	public void setStudijskiProgram(StudijskiProgram studijskiProgram) {
		this.studijskiProgram = studijskiProgram;
	} 
    @ManyToMany(mappedBy="studenti",cascade=CascadeType.ALL)
	public List<AkademskiPredmet> getPredmeti_ak_godine() {
		return predmeti_ak_godine;
	}

	public void setPredmeti_ak_godine(List<AkademskiPredmet> predmeti_ak_godine) {
		this.predmeti_ak_godine = predmeti_ak_godine;
	}
    


	

   
	@ManyToOne
	public GodinaStudija getGodinaStudija() {
		return godinaStudija; 
	}

	public void setGodinaStudija(GodinaStudija godinaStudija) {
		this.godinaStudija = godinaStudija;
	}

	@OneToMany(mappedBy="student",cascade=CascadeType.ALL)
	public List<Zahtjev> getPromjenaZahtjevi() {
		return promjenaZahtjevi;
	} 

	public void setPromjenaZahtjevi(List<Zahtjev> promjenaZahtjevi) {
		this.promjenaZahtjevi = promjenaZahtjevi;
	}

	@OneToMany(mappedBy="student",cascade=CascadeType.ALL)
	public List<ZahtjevBodovi> getPrenosZahtjevi() {
		return prenosZahtjevi;
	}

	public void setPrenosZahtjevi(List<ZahtjevBodovi> prenosZahtjevi) {
		this.prenosZahtjevi = prenosZahtjevi;
	}
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="student")
	public List<ZahtjevPreduslov> getPreduslovZahtjevi() {
		return preduslovZahtjevi;
	}

	public void setPreduslovZahtjevi(List<ZahtjevPreduslov> preduslovZahtjevi) {
		this.preduslovZahtjevi = preduslovZahtjevi;
	}

	@OneToMany
	public List<PredmetOcjena> getOcjene() {
		return ocjene;
	}

	public void setOcjene(List<PredmetOcjena> ocjene) {
		this.ocjene = ocjene;
	}
	
	public String toString() {
		if(this.status==null&& this.godinaStudija==null) {
			return this.ime+" "+this.prezime+" "+this.broj_indeksa;
		}else {
		return this.ime+" "+this.prezime+" Indeks: "+this.broj_indeksa+" Godina: "
		+this.godinaStudija.getNazivGodineStudija()+", "+this.status;
	}
	}
	
	public boolean equals(Object obj) {
		
		if(this==obj) {
			return true;
		}
		if(obj==null || getClass()!=obj.getClass()) {
			return false;
		}
		
			
			Student s=(Student)obj;

		return Objects.equals(this.broj_indeksa,s.getBroj_indeksa());
		
		
		
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}

	@ManyToOne()
	public AkademskaGodina getAkademskaGodina() {
		return akademskaGodina;
	}

	public void setAkademskaGodina(AkademskaGodina akademskaGodina) {
		this.akademskaGodina = akademskaGodina;
	}
    
	
	
	
	
	

	
	
	
	
    
}