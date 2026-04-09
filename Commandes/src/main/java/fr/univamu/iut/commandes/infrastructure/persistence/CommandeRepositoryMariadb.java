package fr.univamu.iut.commandes.infrastructure.persistence;

import fr.univamu.iut.commandes.domain.Commande;
import fr.univamu.iut.commandes.domain.CommandeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CommandeRepositoryMariadb implements CommandeRepository {

    private EntityManagerFactory emf;
    private EntityManager em;

    public CommandeRepositoryMariadb() {
        this.emf = Persistence.createEntityManagerFactory("CommandesPU");
        this.em = emf.createEntityManager();
    }

    @Override
    public boolean addCommande(Commande commande) {
        try {
            em.getTransaction().begin();
            em.persist(commande);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erreur lors de l'enregistrement en BDD : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Commande getCommande(Long id) {
        return em.find(Commande.class, id);
    }

    @Override
    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }
}
