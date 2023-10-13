package Services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import models.AkademskaGodina;
import models.AkademskiPredmet;
import models.GodinaStudija;
import models.PredmetOcjena;
import models.Prodekan;
import models.Student;
import models.Zahtjev;
import models.ZahtjevBodovi;
import models.ZahtjevPreduslov;

public class StudentService {

	private Student student = null;
	private EntityManagerFactory emf = null;
	private Prodekan prodekan = null;

	public StudentService(String email) {
		emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");
		this.student = findStudent(email);
		this.prodekan = findProdekan(1L);
	}

	public Student findStudent(String username) {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Student> query = em.createQuery("SELECT p FROM Student p WHERE p.username = :username",
					Student.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}

	public List<AkademskiPredmet> getAllSubjects() {
		return student.getPredmeti_ak_godine();

	}

	private Prodekan findProdekan(Long id) {
		EntityManager em = emf.createEntityManager();

		try {
			return em.find(Prodekan.class, id);
		} finally {
			em.close();
		}
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

	public List<AkademskaGodina> findAcademicYears() {

		EntityManager em = emf.createEntityManager();

		try {

			TypedQuery<AkademskaGodina> query = em.createQuery("SELECT a FROM AkademskaGodina a",
					AkademskaGodina.class);

			return query.getResultList();

		} finally {

			em.close();

		}

	}

	public void addStudentToSubject(List<AkademskiPredmet> ak) {
//		ak.forEach(System.out::println);
//		System.out.println("metod");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			Student stud = em.merge(student);

			AkademskaGodina akGod = ak.get(0).getAkademskaGodina();
			for (AkademskiPredmet akk : ak) {
				AkademskiPredmet akPred = em.merge(akk);
				stud.getPredmeti_ak_godine().add(akPred);
				akPred.getStudenti().add(stud);
			}

			if (student.getStatus() == null && student.getGodinaStudija() == null) {
				stud.setStatus("Redovan");
				akGod.getGodineStudija().get(0).getStudenti().add(stud);
				stud.setGodinaStudija(em.merge(akGod.getGodineStudija().get(0)));
			}

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Student getStudent() {
		return this.student;
	}

//	public List<AkademskiPredmet> getPrvaGodPred(AkademskaGodina akGod) {
//
//		EntityManager em = emf.createEntityManager();
//
//		int id = akGod.getId();
//		try {
//			em.getTransaction().begin();
//
//			TypedQuery<AkademskiPredmet> query = em.createQuery(
//					"SELECT a FROM AkademskiPredmet a JOIN Predmet p WHERE a.akademskaGodina.id = :akGodId AND (p.semestar.id = :semestarId1 OR p.semestar.id = :semestarId2) ",
//					AkademskiPredmet.class);
//			query.setParameter("akGodId", id); // Use the parsed ID parameter
//	        query.setParameter("semestarId1", 1); // Replace 'desiredSemstarId' with the desired semester ID
//	        query.setParameter("semestarId2", 2); // Replace 'desiredSemstarId' with the desired semester ID
//			em.getTransaction().commit();
//			
//			System.out.println(query.getResultList());
//			return query.getResultList();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	public List<AkademskiPredmet> getPrvaGodPred(AkademskaGodina akGod) {
		EntityManager em = emf.createEntityManager();
		List<AkademskiPredmet> predmeti = null;

		try {
			em.getTransaction().begin();

			TypedQuery<AkademskiPredmet> query = em.createQuery(
					"SELECT ap FROM AkademskiPredmet ap WHERE ap.akademskaGodina.id = :akGodId "
							+ "AND (ap.predmet.semestar.broj = '1' OR ap.predmet.semestar.broj = '2')", // Dodata
																										// dodatna
																										// provjera za
																										// semestar
					AkademskiPredmet.class);
			query.setParameter("akGodId", akGod.getId());
			System.out.println(query.getResultList());
			predmeti = query.getResultList();

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

		return predmeti;
	}

	public void dodajStudentaAkademskojGodini(List<AkademskiPredmet> predmeti, AkademskaGodina ak) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		Student s = em.merge(findStudent(student.getUsername()));
		AkademskaGodina akGod = em.merge(ak);
		akGod.getStudenti().add(s);
		s.setAkademskaGodina(akGod);

		for (AkademskiPredmet p : predmeti) {
			AkademskiPredmet p2 = em.merge(p);
			p2.getStudenti().add(s);
			if(!s.getPredmeti_ak_godine().contains(p2)) {
				s.getPredmeti_ak_godine().add(p2);
			    
			}
		}
		
		if(!(s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals("cetvrta"))) {
		

		if (s.getStatus().equals( "prvi puta") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals("prva")) {

			if (s.getEcts() >= 46) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("druga")));
			} else {
				s.setStatus("obnova");
			}
		} else if (s.getStatus().equals( "obnova") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase() .equals("prva")) {
			if (s.getEcts() >= 46) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("druga")));
				s.setStatus("prvi puta");
			} else {
				s.setStatus("obnova");
			}

		} else if (s.getStatus().equals( "prvi puta")
				&& s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals( "druga")) {
			if (s.getEcts() >= 108) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("treca")));
			} else {
				s.setStatus("obnova");
			}

		} else if (s.getStatus().equals( "obnova") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals("druga")) {
			if (s.getEcts() >= 108) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("treca")));
				s.setStatus("prvi puta");
			} else {
				s.setStatus("obnova");
			}

		} else if (s.getStatus().equals( "prvi puta")
				&& s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals("treca")) {
			if (s.getEcts() >= 168) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("cetvrta")));

			} else {
				s.setStatus("obnova");
			}

		} else if (s.getStatus().equals( "obnova")&& s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals( "treca")) {
			if (s.getEcts() >= 168) {
				s.setGodinaStudija(em.merge(getGodinaStudijaByName("cetvrta")));
				s.setStatus("prvi puta");
			} else {
				s.setStatus("obnova");
			}

		} else {

		}
		
		}else {
			
			if (s.getStatus().equals( "prvi puta") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals("cetvrta")) {

				if (s.getEcts() == 240) {
					s.setStatus("Diplomirao");
				} else {
					s.setStatus("apsolvent");
				}
			}else if (s.getStatus().equals( "apsolvent") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase() .equals( "cetvrta") ){

				if (s.getEcts() == 240) {
					s.setStatus("Diplomirao");
				} else {
					s.setStatus("imatrikulant");
				}
			}else if (s.getStatus().equals( "imatrikulant") && s.getGodinaStudija().getNazivGodineStudija().toLowerCase().equals( "cetvrta")) {

				if (s.getEcts() == 240) {
					s.setStatus("Diplomirao");
				} 
			}else {
				
			}
			
			
			
			
			
		}
		student=s;

		em.getTransaction().commit();
		em.close();

	}
	
	public List<AkademskiPredmet> searchPredmeti(String searchText, long akademskaGodinaId) {
	    EntityManager em = emf.createEntityManager();

	    try {
	        TypedQuery<AkademskiPredmet> query = em.createQuery(
	            "SELECT ap FROM AkademskiPredmet ap WHERE LOWER(ap.predmet.naziv) LIKE :searchText " +
	            "AND ap.akademskaGodina.id = :akademskaGodinaId",
	            AkademskiPredmet.class
	        );
	        query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
	        query.setParameter("akademskaGodinaId", akademskaGodinaId);

	        return query.getResultList();
	    } finally {
	        em.close();
	    }
	}



	public void addZahtjevBodovi(ZahtjevBodovi zaht, AkademskiPredmet ak) {
//		ak.forEach(System.out::println);
//		System.out.println("metod");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			Student stud = em.merge(student);
			em.persist(zaht);

			stud.getPrenosZahtjevi().add(zaht);
			AkademskiPredmet akPred = em.merge(ak);
			zaht.setStudent(stud);
			akPred.getZahtBod().add(zaht);

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}
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

	public void setStatus(int cnt, AkademskaGodina akG) {
		EntityManager em = emf.createEntityManager();
		GodinaStudija gs = student.getGodinaStudija();
		String status;

		try {
			em.getTransaction().begin();
			Student stud = em.merge(student);
//			em.persist(gs);
			GodinaStudija gsm = em.merge(gs);
			AkademskaGodina akg = em.merge(akG);
			System.out.println(stud.getGodinaStudija().getNazivGodineStudija());
			if (cnt < 8) {
				stud.setStatus("obnova");
			} else {

				if (cnt <= 10 && cnt >= 8) {
					if (stud.getGodinaStudija().getNazivGodineStudija().equals("Druga")) {
						stud.setStatus("obnova");
					} else {
						akg.getGodineStudija().get(1).getStudenti().add(stud);
						stud.setGodinaStudija(akg.getGodineStudija().get(1));
						stud.setStatus("redovan");
					}
				} else if (cnt <= 20 && cnt >= 18) {
					if (stud.getGodinaStudija().getNazivGodineStudija().equals("Treca")) {
						stud.setStatus("obnova");
					} else {
						akg.getGodineStudija().get(2).getStudenti().add(stud);
						stud.setGodinaStudija(akg.getGodineStudija().get(2));
						stud.setStatus("redovan");
					}
				} else if (cnt <= 30 && cnt >= 28) {
					if (stud.getGodinaStudija().getNazivGodineStudija().equals("Cetvrta")) {
						if (stud.getStatus().equals("obnova")) {
							stud.setStatus("apsolvent");
						} else {
							stud.setStatus("obnova");
						}
						if (stud.getStatus().equals("apsolvent")) {
							stud.setStatus("imatrikulant");
						}
					} else {
						akg.getGodineStudija().get(3).getStudenti().add(stud);
						stud.setGodinaStudija(akg.getGodineStudija().get(3));
						stud.setStatus("redovan");
					}
				}
			}

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}

	}

	public boolean provjeraPreduslova(AkademskiPredmet ak) {

	/*	String preduslovi = ak.getPredmet().getPreduslovi();
		String[] niz = preduslovi.split(",");

		if (preduslovi.length() == 0 || preduslovi == null) {
			return true;
		} else {

			List<PredmetOcjena> ocjene = student.getOcjene();

			for (PredmetOcjena o : ocjene) {
				for (String sifra : niz) {
					if (o.getAkademskiPredmet().getPredmet().getSifra_predmeta().equals(sifra)) {
						if (o.getOcjena() < 6)
							return false;

					}

				}
			}
			return true;
		}*/
		return true;

	}

	public boolean zahtjevOdobren(AkademskiPredmet ak) {

	/*	for (ZahtjevPreduslov z : ak.getZahtPred()) {

			if (z.getStudent().getId() == student.getId()) {
				if (z.getStatus() == "odobren")
					return true;

			}

		}
		return false;
		*/
		return true;

	}

	public void addZahtjevPreduslov(ZahtjevPreduslov zaht, AkademskiPredmet ak) {
//		ak.forEach(System.out::println);
//		System.out.println("metod");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			Student stud = em.merge(student);
			em.persist(zaht);

			stud.getPreduslovZahtjevi().add(zaht);
			AkademskiPredmet akPred = em.merge(ak);
			zaht.setStudent(stud);
			akPred.getZahtPred().add(zaht);

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	}
	

	public List<ZahtjevBodovi> zahtjevBodovi() {
	    EntityManager em = emf.createEntityManager();

	    try {
	        TypedQuery<ZahtjevBodovi> query = em.createQuery(
	            "SELECT z FROM ZahtjevBodovi z WHERE z.student.id = :studentId",
	            ZahtjevBodovi.class
	        );
	        query.setParameter("studentId", student.getId());

	        return query.getResultList();
	    } finally {
	        em.close();
	    }
	}
	
	public List<ZahtjevPreduslov> zahtjevPreduslovi() {
	    EntityManager em = emf.createEntityManager();

	    try {
	        TypedQuery<ZahtjevPreduslov> query = em.createQuery(
	            "SELECT z FROM ZahtjevPreduslov z WHERE z.student.id = :studentId",
	            ZahtjevPreduslov.class
	        );
	        query.setParameter("studentId", student.getId());

	        return query.getResultList();
	    } finally {
	        em.close();
	    }
	}
	
	
	public List<Zahtjev> zahtjeviZaPromjenuPredmeta() {
	    EntityManager em = emf.createEntityManager();

	    try {
	        TypedQuery<Zahtjev> query = em.createQuery(
	            "SELECT z FROM Zahtjev z WHERE z.student.id = :studentId",
	            Zahtjev.class
	        );
	        query.setParameter("studentId", student.getId());

	        return query.getResultList();
	    } finally {
	        em.close();
	    }
	}


	public void zahtjevPromjena(AkademskiPredmet zaIzbacit, AkademskiPredmet zaUbacit, String obrazlozenje,
			AkademskaGodina g) {
		EntityManager em = emf.createEntityManager();
		Zahtjev z = new Zahtjev();
		em.getTransaction().begin();
		em.persist(z);
		Student s=em.merge(student);
		s.getPromjenaZahtjevi().add(z);
		z.setStudent(s);
		z.setTrenutni(zaIzbacit);
		z.setZamjenski(zaUbacit);
		z.setObrazlozenje(obrazlozenje);

		AkademskaGodina ak = em.merge(g);
		ak.getZahtjeviZaPromjenu().add(z);
		student=s;

		em.getTransaction().commit();

		em.close();

	}

	public void dodajPredmetStudentu(AkademskiPredmet p) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Student stud = em.merge(student);
		stud.getPredmeti_ak_godine().add(em.merge(p));
		em.getTransaction().commit();
		em.close();
	}

	public void izbrisiPredmetStudentu(AkademskiPredmet p) {

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Student stud = em.merge(student);
        AkademskiPredmet p2=em.merge(p);
		stud.getPredmeti_ak_godine().remove(p2);

		em.getTransaction().commit();
		em.close();

	}
	
	public void izbrisiZahtjevZaPreduslov(ZahtjevPreduslov z) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		   Student s=em.merge(student);
		ZahtjevPreduslov z2=em.merge(z);
		s.getPreduslovZahtjevi().remove(z2);
		
        
		em.getTransaction().commit();
		em.close();
	}
	
	
	public void izbrisiZahtjevZaPromjenu(Zahtjev z) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
         Student s=em.merge(student);
		Zahtjev z2=em.merge(z);
		s.getPromjenaZahtjevi().remove(z2);
        
		em.getTransaction().commit();
		em.close();
		
	}
	
	public GodinaStudija getGodinaStudijaByName(String imeGodine) {
	    EntityManager em = emf.createEntityManager();

	    try {
	        TypedQuery<GodinaStudija> query = em.createQuery(
	            "SELECT g FROM GodinaStudija g WHERE g.nazivGodineStudija = :ime",
	            GodinaStudija.class
	        );
	        query.setParameter("ime", imeGodine);

	        return query.getSingleResult();
	    } catch (NoResultException e) {
	        return null; // Ako nema rezultata, vraćamo null
	    } finally {
	        em.close();
	    }
	}


	public AkademskaGodina obradiAkademskuGodinu(AkademskaGodina akGod) {
		EntityManager em = emf.createEntityManager();
		
			
			if (student.getStatus() == null && student.getGodinaStudija() == null) {
				em.getTransaction().begin();
				Student s=em.merge(student);
			List<AkademskiPredmet> predmetiPrveGodine = getPrvaGodPred(akGod);

			for (AkademskiPredmet p : predmetiPrveGodine) {
				p.getStudenti().add(s);
				s.getPredmeti_ak_godine().add(em.merge(p));
			}
			s.setStatus("prvi puta");
			GodinaStudija g = getGodinaStudijaByName("prva");
			System.out.println(akGod.getGodineStudija().get(0));
			s.setGodinaStudija(em.merge(g));

			AkademskaGodina ak = em.merge(akGod);
			ak.getStudenti().add(s);
			s.setAkademskaGodina(akGod);
			student=s;
			em.getTransaction().commit();
			 em.close();
			 return ak;

		} else {
			
			em.getTransaction().begin();
			Student s2=em.merge(student);
			List<AkademskiPredmet> nepolozeni=new ArrayList<>();
			
           
            
			
			if(s2.getAkademskaGodina()!=null) {
			for(PredmetOcjena o: s2.getOcjene()) {
				if(o.getOcjena()<6&&o.getAkademskiPredmet().getAkademskaGodina().getId()==s2.getAkademskaGodina().getId())
					nepolozeni.add(o.getAkademskiPredmet());
				
			}
			
			
			for(AkademskiPredmet p2: akGod.getAkademskiPredmeti()) {
			
				for(AkademskiPredmet p:nepolozeni) {
					if(p2.getPredmet().getId()==p.getPredmet().getId()) {
						 AkademskiPredmet p3=em.merge(p2);
						 if(!s2.getPredmeti_ak_godine().contains(p3))
						    s2.getPredmeti_ak_godine().add(p3);
					}
			   
			
			}
			}
			}
            
			
			
			
			em.getTransaction().commit();
			em.close();
			
			return akGod;
			
		}

}
}
