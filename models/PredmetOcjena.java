package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity  
public class PredmetOcjena {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id; 
	private int ocjena;
	private AkademskiPredmet akademskiPredmet;//
	
	
	 
	@ManyToOne 
	public AkademskiPredmet getAkademskiPredmet() {
		return akademskiPredmet;
	}

	public void setAkademskiPredmet(AkademskiPredmet akademskiPredmet) {
		this.akademskiPredmet = akademskiPredmet;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOcjena() {
		return ocjena;
	}

	public void setOcjena(int ocjena) {
		this.ocjena = ocjena;
	}
	
	public String toString() {
		return akademskiPredmet + " Ocjena: " + ocjena;
	}

}
