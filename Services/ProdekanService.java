package Services;

import models.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class ProdekanService {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");
	private Prodekan prodekan;

	public ProdekanService(String username) {
		this.prodekan = findProdekan(username);

	}

	public Prodekan findProdekan(String username) {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Prodekan> query = em.createQuery("SELECT p FROM Prodekan p WHERE p.username = :username",
					Prodekan.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}

	public Nastavnik azurirajZvanje(Nastavnik n, String novo_zvanje) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		n.setZvanje(novo_zvanje);
		Nastavnik azurirani = em.merge(n);
		em.getTransaction().commit();
		em.close();
		return azurirani;
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



	public List<Predmet> getPredmeti() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Predmet> query = em.createQuery("SELECT p FROM Predmet p", Predmet.class);
			return query.getResultList();
		} finally {
			em.close();
		}

	}

	public List<Nastavnik> getNastavnici() {
		EntityManager em = emf.createEntityManager();

		try {
			TypedQuery<Nastavnik> query = em.createQuery("SELECT n FROM Nastavnik n", Nastavnik.class);
			return query.getResultList();

		} finally {
			em.close();
		}

	}

	public Predmet professorTosubject(Nastavnik n, Predmet p) {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			n.getPredmeti().add(p);
			p.setNastavnik(n);
			em.merge(n);

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}

		finally {
			em.close();
		}
		return p;

	}

	public boolean dodajAkademskuGodinu(AkademskaGodina a) {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			em.persist(a);
			for (Predmet p : getPredmeti()) {

				AkademskiPredmet ak_predmet = new AkademskiPredmet();
				em.persist(ak_predmet);
				ak_predmet.setAkademskaGodina(a);

				ak_predmet.setPredmet(p);
				p.getAkademskiPredmeti().add(ak_predmet);
				a.getAkademskiPredmeti().add(ak_predmet);
				em.merge(p);
				if(p.getNastavnik()==null) {
                	em.remove(a);
                	return false;
                }
			}
			prodekan.getAkademskeGodine().add(a);
			a.setProdekan(em.merge(prodekan));

			List<GodinaStudija> godStudija = getGodineStudija();
			for (GodinaStudija godStud : godStudija) {
				a.getGodineStudija().add(em.merge(godStud));
			}
			
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}

	}

	public List<GodinaStudija> getGodineStudija() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<GodinaStudija> query = em.createQuery("SELECT g FROM GodinaStudija g", GodinaStudija.class);
			return query.getResultList();
		} finally {
			em.close();
		}
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
public boolean provjeraPreduslova(AkademskiPredmet ak,Student student) {
		
		String preduslovi=ak.getPredmet().getPreduslovi();
		String[] niz=preduslovi.split(",");
		
		if(preduslovi.length()==0||preduslovi==null) {
			return true;
		}else {
			
			List<PredmetOcjena> ocjene=student.getOcjene();
			
			for(PredmetOcjena o:ocjene) {
			for(String sifra:niz) {
			  if(o.getAkademskiPredmet().getPredmet().getSifra_predmeta().equals(sifra)) {
				  if(o.getOcjena()>=6)
					  return true;
				  
			  }
				
			}
			}
			return false;
		}
		
		
	}

public boolean slusaPrviPuta(AkademskiPredmet p,Student s) {
	
	int i=0;
	for(AkademskiPredmet p2:s.getPredmeti_ak_godine()) {
		
		if(p2.getPredmet().getId()==p.getPredmet().getId())
			++i;
	}
	
	if(i>=2)
		return false;
	else
		return true;
	
	
}

public Predmet promjeniPreduslove(Predmet p,String preduslovi) {
	EntityManager em=emf.createEntityManager();
	
	p.setPreduslovi(preduslovi);
	
	em.getTransaction().begin();
	Predmet p2=em.merge(p);
	
	em.getTransaction().commit();
	
	
	em.close();
	
	return p2;
	
	
}

public void izbrisiZahtjevZaPromjenu(Zahtjev z,AkademskaGodina ak) {
	EntityManager em = emf.createEntityManager();

	em.getTransaction().begin();
	AkademskaGodina ak2=em.merge(ak);
    
	Zahtjev z2=em.merge(z);
	ak2.getZahtjeviZaPromjenu().remove(z2);
    
	em.getTransaction().commit();
	em.close();
	
}


	public Zahtjev obradiZahtjev(Zahtjev z) {
		
		EntityManager em=emf.createEntityManager();
		em.getTransaction().begin();
		
		Student s=em.merge(z.getStudent());
		AkademskiPredmet zaIzbacit=em.merge(z.getTrenutni());
		AkademskiPredmet zaUbacit=em.merge(z.getZamjenski());  
		Zahtjev z2=new Zahtjev();
		if(slusaPrviPuta(zaIzbacit,s)&&provjeraPreduslova(zaUbacit,s)) {
		
			z.setStatus("Odobren");
			z.setObrazlozenje("");
			z2=em.merge(z);
			
			zaIzbacit.getStudenti().remove(s);
			s.getPredmeti_ak_godine().add(zaUbacit);
			s.getPredmeti_ak_godine().remove(zaIzbacit);
			zaUbacit.getStudenti().add(s);
			
		}else {
			

			z.setStatus("Odbijen");
			z.setObrazlozenje("Niste ispravno izabrali predmete");
			z2=em.merge(z);
			
		}
			
			
		
		
		
		
		
		em.getTransaction().commit();
		
		em.close();
		
		return z2;

		
	}

}