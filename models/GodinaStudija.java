package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
 
@Entity
public class GodinaStudija {
	
	private Long id;
	private String nazivGodineStudija;
	
	private List<Student> studenti=new ArrayList<>();//
    private List<AkademskaGodina> akademskeGodine=new ArrayList<>();//
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazivGodineStudija() {
		return nazivGodineStudija;
	}

	public void setNazivGodineStudija(String nazivGodineStudija) {
		this.nazivGodineStudija = nazivGodineStudija;
	}

    @OneToMany(mappedBy="godinaStudija",cascade=CascadeType.ALL)	
	public List<Student> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
     
	@ManyToMany(cascade=CascadeType.ALL,mappedBy="godineStudija")
	public List<AkademskaGodina> getAkademskeGodine() {
		return akademskeGodine;
	}

	public void setAkademskeGodine(List<AkademskaGodina> akademskeGodine) {
		this.akademskeGodine = akademskeGodine;
	}
	
	public String toString() {
		return nazivGodineStudija;
	}
	

}
