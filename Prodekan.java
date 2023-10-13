package models;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
 
@Entity    
public class Prodekan {
    private Long id;
    private String ime;
    private String prezime;
    private String username;
    private String password;
    
    private List<AkademskaGodina> akademskeGodine=new ArrayList<>();//
    
    private List<StudijskiProgram> studProgrami=new ArrayList<>();//
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public Nastavnik promjeniZvanje(Nastavnik n,String novo_zvanje) {
		n.setZvanje(novo_zvanje);
		return n;
	}
	@OneToMany(cascade=CascadeType.ALL,mappedBy="prodekan")
	public List<AkademskaGodina> getAkademskeGodine() {
		return akademskeGodine;
	}
	public void setAkademskeGodine(List<AkademskaGodina> akademskeGodine) {
		this.akademskeGodine = akademskeGodine;
	}
	@OneToMany(mappedBy="prodekan")
	public List<StudijskiProgram> getStudProgrami() {
		return studProgrami;
	}
	public void setStudProgrami(List<StudijskiProgram> studProgrami) {
		this.studProgrami = studProgrami;
	}
	
	
	
	
	
	
}
