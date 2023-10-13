package Services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import models.Administrator;
import models.Nastavnik;
import models.Prodekan;
import models.Student;

public class CheckLogin{
private EntityManagerFactory emf=Persistence.createEntityManagerFactory("sistem_za_upis_akademske_godine");

public boolean authenticate(String username, String password, String uloga) {
    EntityManager em = emf.createEntityManager();

    try {
        switch (uloga) {
            case "Student":
                Student student = em.createQuery("SELECT s FROM Student s WHERE s.username = :username AND s.password = :password", Student.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .getSingleResult();
                return student != null;
            case "Prodekan":
                Prodekan prodekan = em.createQuery("SELECT p FROM Prodekan p WHERE p.username = :username AND p.password = :password", Prodekan.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .getSingleResult();
                return prodekan != null;
            case "Administrator":
                Administrator administrator = em.createQuery("SELECT a FROM Administrator a WHERE a.username = :username AND a.password = :password", Administrator.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .getSingleResult();
                return administrator != null;
            case "Nastavnik":
                Nastavnik nastavnik = em.createQuery("SELECT n FROM Nastavnik n WHERE n.username = :username AND n.password = :password", Nastavnik.class)
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .getSingleResult();
                return nastavnik != null;
            default:
                return false;
        }
    } catch (NoResultException e) {
        // Ako ne postoji korisnik s navedenim podacima, JPA upit će baciti izuzetak
        return false;
    } finally {
        em.close(); // Obrada je gotova, zatvaramo EntityManager
    }
}
   


}