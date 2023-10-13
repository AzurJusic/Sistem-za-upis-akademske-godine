package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

import models.*;

public class TestService {

	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");

	
	
	public void test() {
		
		EntityManager em=emf.createEntityManager();
		Administrator a=new Administrator();
		a.setIme("admin");   
		
		a.setPassword("admin");
		a.setPrezime("admin");
		a.setUsername("admin");
		a.setId(1L);
		
		StudijskiProgram sp=new StudijskiProgram();
		sp.setNaziv("ER");
		sp.setAkademska_godina("2016/2017");
		
		
		Usmjerenje u=new Usmjerenje();
		u.setNaziv("RI");
		
		Usmjerenje u1=new Usmjerenje();
		u1.setNaziv("AR");
		
		Usmjerenje u2=new Usmjerenje();
		u2.setNaziv("ESKE");
		
		Usmjerenje u3=new Usmjerenje();
		u3.setNaziv("EEMS");
		
		Usmjerenje u4=new Usmjerenje();
		u4.setNaziv("TK");
		
		
		Semestar s1=new Semestar();
		s1.setBroj("1");
		s1.setLjetniZimski("zimski");
		
		
		Semestar s2=new Semestar();
		s2.setBroj("2");
		s2.setLjetniZimski("ljetni");
		
		
		Semestar s3=new Semestar();
		s3.setBroj("3");
		s3.setLjetniZimski("zimski");
		
		Semestar s4=new Semestar();
		s4.setBroj("4");
		s4.setLjetniZimski("ljetni");
		
		
		Semestar s5=new Semestar();
		s5.setBroj("5");
		s5.setLjetniZimski("zimski");
		
		Semestar s6=new Semestar();
		s6.setBroj("6");
		s6.setLjetniZimski("ljetni");
		
		Semestar s7=new Semestar();
		s7.setBroj("7");
		s7.setLjetniZimski("zimski");
		
		Semestar s8=new Semestar();
		s8.setBroj("8");
		s8.setLjetniZimski("ljetni");
		
		
		
		Predmet p1=new Predmet();
		p1.setEcts(6);
		p1.setSifra_predmeta("RI001");
		p1.setNaziv("Osnovi racunarstva");
		
		
		Predmet p2=new Predmet();
		p2.setEcts(6);
		p2.setSifra_predmeta("RI101");
		p2.setNaziv("Osnovi programiranja");
		
		Predmet p3=new Predmet();
		p3.setEcts(6);
		p3.setSifra_predmeta("RI201");
		p3.setNaziv("Arhitektura racunara");
		
		
		Predmet p4=new Predmet();
		p4.setEcts(6);
		p4.setSifra_predmeta("RI202");
		p4.setNaziv("Objektno orijentisano programiranje");
		
		Predmet p5=new Predmet();
		p5.setEcts(6);
		p5.setSifra_predmeta("RI203");
		p5.setNaziv("Uvod u racunarske algoritme");
		
		Predmet p6=new Predmet();
		p6.setEcts(6);
		p6.setSifra_predmeta("MAT1");
		p6.setNaziv("Matematika 1");
		
		Predmet p7=new Predmet();
		p7.setEcts(6);
		p7.setSifra_predmeta("MAT2");
		p7.setNaziv("Matematika 2");
		
		Predmet p8=new Predmet();
		p8.setEcts(6);
		p8.setSifra_predmeta("FIZ1");
		p8.setNaziv("Fizika 1");
		
		Predmet p9=new Predmet();
		p9.setEcts(6);
		p9.setSifra_predmeta("Fiz2");
		p9.setNaziv("Fizika 2");
		
		Predmet p10=new Predmet();
		p10.setEcts(6);
		p10.setSifra_predmeta("ESKE001");
		p10.setNaziv("Osnovi elektrotehnike 1");
		
		Predmet p11=new Predmet();
		p11.setEcts(6);
		p11.setSifra_predmeta("ESKE002");
		p11.setNaziv("Osnovi elektrotehnike 2");
		
		Predmet p12=new Predmet();
		p12.setEcts(6);
		p12.setSifra_predmeta("TK001");
		p12.setNaziv("Tehnologije za podrsku tehnickom pisanju");
		
		Predmet p13=new Predmet();
		p13.setEcts(6);
		p13.setSifra_predmeta("EEMS001");
		p13.setNaziv("Uvod u energetske sisteme");
		
		Predmet p14=new Predmet();
		p14.setEcts(6);
		p14.setSifra_predmeta("AR001");
		p14.setNaziv("Mjerenja u automatici i robotici");
		
		
		Predmet p15=new Predmet();
		p15.setEcts(6);
		p15.setSifra_predmeta("AR203");
		p15.setNaziv("AKtuatori");
		
		
		Predmet p16=new Predmet();
		p16.setEcts(6);
		p16.setSifra_predmeta("AR206");
		p16.setNaziv("Inteligentni sistemi");
		
		Predmet p17=new Predmet();
		p17.setEcts(6);
		p17.setSifra_predmeta("AR104");
		p17.setNaziv("Instrumentacija");
		
		
		Predmet p18=new Predmet();
		p18.setEcts(6);
		p18.setSifra_predmeta("EEMS202");
		p18.setNaziv("Elektrane");
		
		
		
		Predmet p19=new Predmet();
		p19.setEcts(6);
		p19.setSifra_predmeta("ESKE105");
		p19.setNaziv("Elektricne masina 1");
		
		
		Predmet p20=new Predmet();
		p20.setEcts(6);
		p20.setSifra_predmeta("EEMS201");
		p20.setNaziv("Simulacija sistema");
		
		Predmet p21=new Predmet();
		p21.setEcts(6);
		p21.setSifra_predmeta("ESKE104");
		p21.setNaziv("OSnovi mehatronike");
		
		Predmet p22=new Predmet();
		p22.setEcts(6);
		p22.setSifra_predmeta("TK002");
		p22.setNaziv("Sekvencijalni sklopovi");
		
		
		Predmet p23=new Predmet();
		p23.setEcts(6);
		p23.setSifra_predmeta("TK103");
		p23.setNaziv("Komutacijski sistemi");
		
		Predmet p24=new Predmet();
		p24.setEcts(6);
		p24.setSifra_predmeta("RI204");
		p24.setNaziv("Windows programiranje");
		
		
		Predmet p25=new Predmet();
		p25.setEcts(6);
		p25.setSifra_predmeta("RI207");
		p25.setNaziv("Baze podataka");
		
		
		Predmet p26=new Predmet();
		p26.setEcts(6);
		p26.setSifra_predmeta("RI401");
		p26.setNaziv("Operativni sistemi");
		
		
		Predmet p27=new Predmet();
		p27.setEcts(6);
		p27.setSifra_predmeta("RI402");
		p27.setNaziv("Dizajn kompajlera");
		
		
		Predmet p28=new Predmet();
		p28.setEcts(6);
		p28.setSifra_predmeta("RI205");
		p28.setNaziv("Racunarska grafika");
		
		
		
		Predmet p29=new Predmet();
		p29.setEcts(6);
		p29.setSifra_predmeta("RI501");
		p29.setNaziv("Racuanrske mreze");
		
		
		Predmet p30=new Predmet();
		p30.setEcts(6);
		p30.setSifra_predmeta("RI301");
		p30.setNaziv("Strukture podataka");
		
		
		Predmet p31=new Predmet();
		p31.setEcts(6);
		p31.setSifra_predmeta("RI302");
		p31.setNaziv("Razvoj softvera");
		
		
		Predmet p32=new Predmet();
		p32.setEcts(6);
		p32.setSifra_predmeta("RI601");
		p32.setNaziv("Razvoj Web aplikacija");
		
		
		Predmet p33=new Predmet();
		p33.setEcts(6);
		p33.setSifra_predmeta("TK303");
		p33.setNaziv("Mjerenja u tk");
		
		Predmet p34=new Predmet();
		p34.setEcts(6);
		p34.setSifra_predmeta("TK304");
		p34.setNaziv("Multimedijski sistemi i komunikacije");
		
		
		Predmet p35=new Predmet();
		p35.setEcts(6);
		p35.setSifra_predmeta("TK302");
		p35.setNaziv("Digitalne telekomunikacije");
		
		
		Predmet p36=new Predmet();
		p36.setEcts(6);
		p36.setSifra_predmeta("Tk302");
		p36.setNaziv("Opticke telekomunikacije");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Student stud1=new Student();
		stud1.setBroj_indeksa("19020");
		stud1.setIme("Azur");
		stud1.setPrezime("Jusic");
		stud1.setPassword("azur.jusic");
		stud1.setUsername("azur.jusic");
		
		
		Student stud2=new Student();
		stud2.setBroj_indeksa("19155");
		stud2.setIme("Admir");
		stud2.setPrezime("Mustafic");
		stud2.setPassword("admir.mustafic");
		stud2.setUsername("admir.mustafic");
		
		GodinaStudija g1=new GodinaStudija();
		g1.setNazivGodineStudija("prva");
		
		GodinaStudija g2=new GodinaStudija();
		g2.setNazivGodineStudija("druga");
		
		GodinaStudija g3=new GodinaStudija();
		g3.setNazivGodineStudija("treca");
		
		
		GodinaStudija g4=new GodinaStudija();
		g4.setNazivGodineStudija("cetvrta");
		
		
		em.getTransaction().begin();
		em.persist(a);
		em.persist(sp);
		em.persist(u);
		em.persist(u1);
		em.persist(u2);
		em.persist(u3);
		em.persist(u4);

		
		em.persist(s1);
		em.persist(s2);
		em.persist(s3);
		em.persist(s4);
		em.persist(s5);
		em.persist(s6);
		em.persist(s7);
		em.persist(s8);
		
		em.persist(g1);
		em.persist(g2);
		em.persist(g3);
		em.persist(g4);

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(p5);
        em.persist(p6);
        em.persist(p7);
        em.persist(p8);
        em.persist(p9);
        em.persist(p10);
        em.persist(p11);
        em.persist(p12);
        em.persist(p13);
        em.persist(p14);
        em.persist(p15);
        em.persist(p16);
        em.persist(p17);
        em.persist(p18);
        em.persist(p19);
        em.persist(p20);
        em.persist(p21);
        em.persist(p22);
        em.persist(p23);
        em.persist(p24);  
        em.persist(p25);
        em.persist(p26);
        em.persist(p27);
        em.persist(p28);
        em.persist(p29);
        em.persist(p30);
        em.persist(p31);
        em.persist(p32);
        em.persist(p33);
        em.persist(p34);
        em.persist(p35);
        em.persist(p36);
        
        
     
        
        
        
        em.persist(stud1);
        em.persist(stud2);
        
        stud1.setStudijskiProgram(sp);
        stud1.setUsmjerenje(u);
        sp.getStudenti().add(stud2);
        sp.getStudenti().add(stud1);
        
        p1.setSemestar(s1);
        p1.setUsmjerenje(u);
        p1.setSp(sp);
        
        p2.setSemestar(s2);
        p2.setUsmjerenje(u);
        p2.setSp(sp);
        
        sp.getPredmeti().add(p1);
        sp.getPredmeti().add(p2);
        sp.getPredmeti().add(p3);
        sp.getPredmeti().add(p4);
        sp.getPredmeti().add(p5);
        sp.getPredmeti().add(p6);
        sp.getPredmeti().add(p7);
        sp.getPredmeti().add(p8);
        sp.getPredmeti().add(p9);
        sp.getPredmeti().add(p10);
        sp.getPredmeti().add(p11);
        sp.getPredmeti().add(p12);
        sp.getPredmeti().add(p13);
        
        sp.getPredmeti().add(p20);
        sp.getPredmeti().add(p21);
        sp.getPredmeti().add(p22);
        sp.getPredmeti().add(p23);
        sp.getPredmeti().add(p24);
        sp.getPredmeti().add(p25);
        sp.getPredmeti().add(p26);
        sp.getPredmeti().add(p27);
        sp.getPredmeti().add(p28);
        sp.getPredmeti().add(p29);
        sp.getPredmeti().add(p30);
        sp.getPredmeti().add(p31);
        sp.getPredmeti().add(p32);
        sp.getPredmeti().add(p33);
        sp.getPredmeti().add(p34);
        sp.getPredmeti().add(p35);
        sp.getPredmeti().add(p36);
        
        
        sp.getPredmeti().add(p14);
        sp.getPredmeti().add(p15);
        sp.getPredmeti().add(p16);
        sp.getPredmeti().add(p17);
        sp.getPredmeti().add(p18);
        sp.getPredmeti().add(p19);
      
        









        p3.setSemestar(s4);
        p3.setUsmjerenje(u);
        p3.setSp(sp);
        
        
        p4.setSemestar(s3);
        p4.setUsmjerenje(u);
        p4.setSp(sp);
        
        
        p5.setSemestar(s4);
        p5.setUsmjerenje(u);
        p5.setSp(sp);
        
        p6.setSemestar(s1);
        p6.setUsmjerenje(u);
        p6.setSp(sp);
        
        p7.setSemestar(s2);
        p7.setUsmjerenje(u);
        p7.setSp(sp);
        
        p8.setSemestar(s1);
        p8.setUsmjerenje(u);
        p8.setSp(sp);
        
        p9.setSemestar(s2);
        p9.setUsmjerenje(u);
        p9.setSp(sp);
        
        
        p10.setSemestar(s1);
        p10.setUsmjerenje(u);
        p10.setSp(sp);
        
        p11.setSemestar(s2);
        p11.setUsmjerenje(u);
        p11.setSp(sp);
        
        
        p12.setSemestar(s2);
        p12.setUsmjerenje(u);
        p12.setSp(sp);
        
        p13.setSemestar(s1);
        p13.setUsmjerenje(u);
        p13.setSp(sp);
        
        p14.setSemestar(s4);
        p14.setUsmjerenje(u);
        p14.setSp(sp);
        
        
        p15.setSemestar(s3);
        p15.setUsmjerenje(u);
        p15.setSp(sp);
        
        
        p16.setSemestar(s4);
        p16.setUsmjerenje(u);
        p16.setSp(sp);
        
        p17.setSemestar(s6);
        p17.setUsmjerenje(u);
        p17.setSp(sp);
        
        p18.setSemestar(s3);
        p18.setUsmjerenje(u);
        p18.setSp(sp);
        
        p19.setSemestar(s4);
        p19.setUsmjerenje(u);
        p19.setSp(sp);
        
        p20.setSemestar(s5);
        p20.setUsmjerenje(u);
        p20.setSp(sp);
        
        
        p20.setSemestar(s8);
        p20.setUsmjerenje(u);
        p20.setSp(sp);
        
        p21.setSemestar(s7);
        p21.setUsmjerenje(u);
        p21.setSp(sp);
        
        
        p22.setSemestar(s6);
        p22.setUsmjerenje(u);
        p22.setSp(sp);
        
        p23.setSemestar(s5);
        p23.setUsmjerenje(u);
        p23.setSp(sp);
        
        p24.setSemestar(s6);
        p24.setUsmjerenje(u);
        p24.setSp(sp);
        
        p25.setSemestar(s5);
        p25.setUsmjerenje(u);
        p25.setSp(sp);
        
        p26.setSemestar(s6);
        p26.setUsmjerenje(u);
        p26.setSp(sp);
        
        p27.setSemestar(s5);
        p27.setUsmjerenje(u);
        p27.setSp(sp);
        
        p28.setSemestar(s6);
        p28.setUsmjerenje(u);
        p28.setSp(sp);
        
        p29.setSemestar(s5);
        p29.setUsmjerenje(u);
        p29.setSp(sp);
        
        
        p30.setSemestar(s6);
        p30.setUsmjerenje(u);
        p30.setSp(sp);
        
        p31.setSemestar(s5);
        p31.setUsmjerenje(u);
        p31.setSp(sp);
        
        p32.setSemestar(s6);
        p32.setUsmjerenje(u);
        p32.setSp(sp);
        
        p33.setSemestar(s6);
        p33.setUsmjerenje(u);
        p33.setSp(sp);
        
        
        p34.setSemestar(s6);
        p34.setUsmjerenje(u);
        p34.setSp(sp);
        
        
        p35.setSemestar(s6);
        p35.setUsmjerenje(u);
        p35.setSp(sp);
        
        p36.setSemestar(s6);
        p36.setUsmjerenje(u);
        p36.setSp(sp);
        
        u.getPredmeti().add(p36);
        u.getPredmeti().add(p35);
        u.getPredmeti().add(p34);
        u.getPredmeti().add(p33);
        u.getPredmeti().add(p32);
        u.getPredmeti().add(p31);
        u.getPredmeti().add(p30);
        u.getPredmeti().add(p29);
        u.getPredmeti().add(p28);
        u.getPredmeti().add(p27);
     
        u.getPredmeti().add(p26);
        u.getPredmeti().add(p25);
        u.getPredmeti().add(p24);
        u.getPredmeti().add(p23);
        u.getPredmeti().add(p22);
        u.getPredmeti().add(p21);
        u.getPredmeti().add(p20);
        u.getPredmeti().add(p19);
        u.getPredmeti().add(p18);
        u.getPredmeti().add(p17);
        u.getPredmeti().add(p16);
        u.getPredmeti().add(p15);
        u.getPredmeti().add(p14);
        
        u.getPredmeti().add(p13);
        u.getPredmeti().add(p12);
        u.getPredmeti().add(p11);
        u.getPredmeti().add(p10);
        u.getPredmeti().add(p9);
        u.getPredmeti().add(p8);
        u.getPredmeti().add(p7);
        u.getPredmeti().add(p6);
        u.getPredmeti().add(p5);
        u.getPredmeti().add(p4);
        u.getPredmeti().add(p3);
        u.getPredmeti().add(p2);
        u.getPredmeti().add(p1);
        

        

		
		sp.setAdministrator(a);
		a.getSp().add(sp);
		
		u.setStudijskiProgram(sp);
		sp.getUsmjerenja().add(u);
		
		
		u1.setStudijskiProgram(sp);
		u2.setStudijskiProgram(sp);
		u3.setStudijskiProgram(sp);
		u4.setStudijskiProgram(sp);
		sp.getUsmjerenja().add(u1);
		sp.getUsmjerenja().add(u4);
		sp.getUsmjerenja().add(u3);
		sp.getUsmjerenja().add(u2);
		
		
		sp.getSemestri().add(s8);
		sp.getSemestri().add(s7);
		sp.getSemestri().add(s6);
		sp.getSemestri().add(s5);
		sp.getSemestri().add(s4);
		sp.getSemestri().add(s3);
		sp.getSemestri().add(s2);
		sp.getSemestri().add(s1);

		
		
		
		em.getTransaction().commit();
		
		
		
		
	}
	
	
//	public static void main(String[] args) {
//		test();
//	}
	
}
/*if (student.getStatus() == null && student.getGodinaStudija() == null) {
			List<AkademskiPredmet> akPred = new ArrayList<>();
			if (ak != null) {
				akPred = studentService.getPrvaGodPred(ak);
				studentService.addStudentToSubject(akPred);

//				student = studentService.getStudent();

				JOptionPane.showMessageDialog(studentFrame, "Vasi predmeti su vec uneseni", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(studentFrame, "Nije unesena akademska godina!", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {

			if (student.getPredmeti_ak_godine().size() == 10) {
				List<AkademskiPredmet> predmeti = student.getPredmeti_ak_godine();
				int brojac = 0;
				for (AkademskiPredmet p : predmeti) {
					if (p.getAkademskaGodina().getId() == studentService.getTrenutna().getId())

						++brojac;

				}
				if (brojac == 0) {
					JOptionPane.showMessageDialog(studentFrame,
							"Studenti koji prvi puta upisuju" + "nemaju pristup registraciji.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
			*/
