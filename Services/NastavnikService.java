package Services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import models.AkademskaGodina;
import models.AkademskiPredmet;
import models.Nastavnik;
import models.PredmetOcjena;
import models.Prodekan;
import models.Student;

public class NastavnikService {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");
	private Nastavnik nastavnik;

	public NastavnikService(String username) {
		this.nastavnik = findNastavnik(username);
	}

	private Nastavnik findNastavnik(String u) {

		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Nastavnik> query = em.createQuery("SELECT p FROM Nastavnik p WHERE p.username = :username",
					Nastavnik.class);
			query.setParameter("username", u);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}

	public Nastavnik getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(Nastavnik nastavnik) {
		this.nastavnik = nastavnik;
	}

	public List<AkademskiPredmet> getPredmeti(AkademskaGodina ak) {
		EntityManager em = emf.createEntityManager();

		List<AkademskiPredmet> predmetiPredavaca = new ArrayList<>();

		for (AkademskiPredmet predmet : ak.getAkademskiPredmeti()) {
			if (predmet.getNastavnik().getUsername().equals(nastavnik.getUsername())) {
				predmetiPredavaca.add(predmet);
			}
		}

		em.close();
		return predmetiPredavaca;
	}

	public AkademskaGodina getTrenutna() {
		List<AkademskaGodina> godine = findAcademicYears();
		Timestamp trenutniDatum = new Timestamp(System.currentTimeMillis());

		for (AkademskaGodina akg : godine) {
			if (trenutniDatum.before(akg.getDatumKraja()) && trenutniDatum.after(akg.getDatumPocetka())) {
				return akg;
			}
		}
		return null;
	}

	public AkademskaGodina getNova() {
		List<AkademskaGodina> godine = findAcademicYears();
		Timestamp trenutniDatum = new Timestamp(System.currentTimeMillis());

		for (AkademskaGodina akg : godine) {
			if (trenutniDatum.before(akg.getDatumPocetka())) {
				return akg;
			}
		}
		return null;

	}

	private List<AkademskaGodina> findAcademicYears() {

		EntityManager em = emf.createEntityManager();

		try {

			TypedQuery<AkademskaGodina> query = em.createQuery("SELECT a FROM AkademskaGodina a",
					AkademskaGodina.class);

			return query.getResultList();

		} finally {

			em.close();

		}

	}
	
	public AkademskiPredmet  mergePredmet(AkademskiPredmet p) {
	   EntityManager em=emf.createEntityManager();
	   em.getTransaction().begin();
	   
	   AkademskiPredmet p2=em.merge(p);
	   em.getTransaction().commit();
		return p2;
	}

	public Student dodajOcjenu(AkademskiPredmet a, Student s, int ocjena) {
		EntityManager em = emf.createEntityManager();
		
		PredmetOcjena ocj = new PredmetOcjena();
		ocj.setOcjena(ocjena);
		Student noviS=null;
		try {
			em.getTransaction().begin();
			
			em.persist(ocj);
			ocj.setAkademskiPredmet(em.merge(a));
			
			 noviS = em.merge(s);
			noviS.getOcjene().add(ocj);
			if(ocjena>=6) {
			noviS.setEcts(noviS.getEcts()+a.getPredmet().getEcts());
			}
		
			
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}

		finally {
			em.close();
		}
		
       return noviS;
		
	}
	
	

}
