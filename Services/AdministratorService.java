package Services;

import models.Administrator;
import models.AkademskaGodina;
import models.AkademskiPredmet;
import models.Nastavnik;
import models.Predmet;
import models.Prodekan;
import models.Semestar;
import models.Student;
import models.StudijskiProgram;
import models.Usmjerenje;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class AdministratorService {
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");
	private Administrator admin;

	// konstruktori

	// Uzima adminstratora iz baze koji je zaduzen za EIR studijski program
	public AdministratorService() {
		this.admin = findAdmin(1L);
	}

	public AdministratorService(Long id) {
		this.emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");

	}

	public boolean addProfessor(Nastavnik n, StudijskiProgram sp, Usmjerenje usmjerenje) {
		if (!checkNastavnik(n)) {
			EntityManager em = emf.createEntityManager();
			try {
				em.getTransaction().begin();
				em.persist(n);
				if (usmjerenje == null) {
					sp.getNastavnici().add(n);
					n.setStudijskiProgram(em.merge(sp));
				} else {
					sp.getNastavnici().add(n);
					n.setStudijskiProgram(em.merge(sp));
					usmjerenje.getNastavnici().add(n);
					n.setUsmjerenje(em.merge(usmjerenje));
				}
				em.getTransaction().commit();
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
				return false;
			} finally {
				em.close();
			}
			if (n.isProdekan())
				addProdekan(n);
		} else {
			return false;
		}
		return true;
	}

	public boolean addStudent(Student s, StudijskiProgram sp, Usmjerenje usmjerenje) {

		if (!checkStudent(s)) {
			EntityManager em = emf.createEntityManager();

			try {

				em.getTransaction().begin();

				em.persist(s);

				sp.getStudenti().add(s);
				s.setStudijskiProgram(em.merge(sp));
				usmjerenje.getStudenti().add(s);
				s.setUsmjerenje(em.merge(usmjerenje));

				em.getTransaction().commit();
				
				
				return true;
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
				return false;
			} finally {
				em.close();
			}
		} else {
			return false;
		}

	}

	public boolean addPredmet(StudijskiProgram sp, Predmet p, Usmjerenje u, Semestar s) {

		if (!checkPredmet(p)) {

			EntityManager em = emf.createEntityManager();
			try {
				em.getTransaction().begin();
				em.persist(p);

				sp.getPredmeti().add(p);
				p.setSp(em.merge(sp));

				u.getPredmeti().add(p);
				p.setUsmjerenje(em.merge(u));

				s.getPredmeti().add(p);
				p.setSemestar(em.merge(s));

				em.getTransaction().commit();
				return true;
			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
				return false;

			} finally {
				em.close();

			}
		} else {
//    		System.out.println("Predmet vec postoji");
			return false;
		}

	}

	public void addProdekan(Nastavnik n) {

		Prodekan p = new Prodekan();
		p.setIme(n.getIme());
		p.setPrezime(n.getPrezime());
		p.setUsername(n.getUsername());
		p.setPassword(n.getPassword());
		if (!checkProdekan(p)) {
			EntityManager em = emf.createEntityManager();

			try {
				em.getTransaction().begin();
				em.persist(p);
				StudijskiProgram sp = n.getStudijskiProgram();
				sp.setProdekan(p);
				p.getStudProgrami().add(em.merge(sp));

				em.getTransaction().commit();

			} catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();

			} finally {
				em.close();
			}

		}

	}

	public void dodajAdmina(Administrator a) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(a);

		em.getTransaction().commit();

	}

	private Administrator findAdmin(Long id) {
		EntityManager em = emf.createEntityManager();
		Administrator a = em.find(Administrator.class, id);
		em.close();
		return a;

	}

	public String vratiImeStudijskogPrograma() {
		return admin.getSp().get(0).getNaziv();

	}

	private boolean checkNastavnik(Nastavnik n) {
		EntityManager em = emf.createEntityManager();

		// Koristimo JPA upit za provjeru postoji li nastavnik s istim korisničkim
		// imenom

		String username = n.getUsername();
		Query query = em.createQuery("SELECT COUNT(n) FROM Nastavnik n WHERE n.username = :username");
		query.setParameter("username", username);

		Long count = (Long) query.getSingleResult();

		// Ako rezultat upita nije jednak nuli, to znači da nastavnik već postoji u bazi
		return count > 0;
	}

	private boolean checkStudent(Student s) {
		EntityManager em = emf.createEntityManager();

		String username = s.getUsername();
		String brojIndeksa = s.getBroj_indeksa();

		// Koristimo JPA upit za provjeru postoji li student s istim korisničkim imenom
		// ili brojem indeksa
		Query query = em.createQuery(
				"SELECT COUNT(s) FROM Student s WHERE s.username = :username OR s.broj_indeksa = :brojIndeksa");
		query.setParameter("username", username);
		query.setParameter("brojIndeksa", brojIndeksa);

		// Izvršavamo upit
		Long count = (Long) query.getSingleResult();

		// Ako rezultat upita nije jednak nuli, to znači da student već postoji u bazi
		return count > 0;
	}

	private boolean checkProdekan(Prodekan p) {

		EntityManager em = emf.createEntityManager();

		// Koristimo JPA upit za provjeru postoji li nastavnik s istim korisničkim
		// imenom

		String username = p.getUsername();
		Query query = em.createQuery("SELECT COUNT(n) FROM Prodekan n WHERE n.username = :username");
		query.setParameter("username", username);

		Long count = (Long) query.getSingleResult();

		// Ako rezultat upita nije jednak nuli, to znači da nastavnik već postoji u bazi
		return count > 0;

	}

	private boolean checkPredmet(Predmet p) {
		EntityManager em = emf.createEntityManager();

		String sifraPredmeta = p.getSifra_predmeta();
		String naziv = p.getNaziv();

		// Koristimo JPA upit za provjeru postoji li predmet s istom šifrom ili nazivom
		Query query = em.createQuery(
				"SELECT COUNT(p) FROM Predmet p WHERE p.sifra_predmeta = :sifraPredmeta OR p.naziv = :naziv");
		query.setParameter("sifraPredmeta", sifraPredmeta);
		query.setParameter("naziv", naziv);

		// Izvršavamo upit
		Long count = (Long) query.getSingleResult();

		// Ako rezultat upita nije jednak nuli, to znači da predmet već postoji u bazi
		return count > 0;
	}

	public List<StudijskiProgram> getStudijskiProgrami() {
		return admin.getSp();
	}

	public List<Usmjerenje> getUsmjerenja() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Usmjerenje> query = em.createQuery("SELECT u FROM Usmjerenje u", Usmjerenje.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Semestar> getSemestri() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Semestar> query = em.createQuery("SELECT s FROM Semestar s", Semestar.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public boolean checkProdekanS() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT COUNT(p) FROM Prodekan p");
		Long count = (Long) query.getSingleResult();

		return count > 0;
	}
	
	
	public List<Predmet>  getPredmeti(){
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Predmet> query = em.createQuery("SELECT a FROM Predmet a",
					Predmet.class);
			return query.getResultList();
		} finally {
			em.close();
		}
		
		
		
	}
	
	public List<Student>  getStudenti(){
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Student> query = em.createQuery("SELECT a FROM Student a",
					Student.class);
			return query.getResultList();
		} finally {
			em.close();
		}
		
		
		
	}
	
	
	public List<Nastavnik>  getNastavnici(){
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Nastavnik> query = em.createQuery("SELECT a FROM Nastavnik a",
					Nastavnik.class);
			return query.getResultList();
		} finally {
			em.close();
		}		
		
		
	}
	
	public Predmet mergePredmet(Predmet p) {
		EntityManager em=emf.createEntityManager();
		
		em.getTransaction().begin();
		Predmet p2=em.merge(p);
		em.getTransaction().commit();
		em.close();
		return p2;
		
	}
	
	public Student mergeStudent(Student s) {
		EntityManager em=emf.createEntityManager();
		
		em.getTransaction().begin();
		Student s2=em.merge(s);
		em.getTransaction().commit();
		em.close();
		return s2;
		
	}
	
	public Nastavnik mergeNastavnik(Nastavnik n) {
		EntityManager em=emf.createEntityManager();
		
		em.getTransaction().begin();
		Nastavnik n2=em.merge(n);
		em.getTransaction().commit();
		em.close();
		return n2;
		
	}
	
	
	

}
