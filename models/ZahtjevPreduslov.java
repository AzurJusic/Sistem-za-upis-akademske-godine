package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity  
public class ZahtjevPreduslov {

	private Long id;
	private String obrazlozenje;
//	private AkademskiPredmet predmet;
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
	
//	@ManyToOne
//	public AkademskiPredmet getPredmet() {
//		return predmet;
//	}
//	public void setPredmet(AkademskiPredmet trenutni) {
//		this.predmet = trenutni;
//	}
	
	@ManyToOne
	public Student getStudent() {
		return student;
	} 
	public void setStudent(Student student) {
		this.student = student;
	}

	public String toString() {
		return student + " " + status;
	}
	
	public String getStatus() {
		return "Status: "+status +" "+"Student: "+student.getIme()+" "+student.getPrezime();
	}
	
	public void setStatus(String st) {
		this.status = st;
	}

}
