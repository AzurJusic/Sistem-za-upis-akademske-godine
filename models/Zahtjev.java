package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity  
public class Zahtjev {
	
	private Long id;
	private String obrazlozenje; 
	private AkademskiPredmet trenutni;
	private AkademskiPredmet zamjenski;
	private Student student;
	private String status="Na cekanju.";

	 
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getObrazlozenje() {
		return obrazlozenje;
	}
	public void setObrazlozenje(String obrazlozenje) {
		this.obrazlozenje = obrazlozenje;
	}
	@ManyToOne
	public AkademskiPredmet getTrenutni() {
		return trenutni;
	}
	public void setTrenutni(AkademskiPredmet trenutni) {
		this.trenutni = trenutni;
	}
	@ManyToOne
	public AkademskiPredmet getZamjenski() {
		return zamjenski;
	}
	public void setZamjenski(AkademskiPredmet zamjenski) {
		this.zamjenski = zamjenski;
	}
	@ManyToOne
	public Student getStudent() {
		return student;
	} 
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
		return "Promjena predmeta -> Status: "+ this.status+" Student: "+student.getIme()+" "+student.getPrezime();
	}
	
	

}
