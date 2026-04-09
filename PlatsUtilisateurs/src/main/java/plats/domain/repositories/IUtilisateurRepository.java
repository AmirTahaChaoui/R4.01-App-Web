package plats.domain.repositories;

import plats.domain.entities.Utilisateur;
import java.util.List;

public interface IUtilisateurRepository {
    List<Utilisateur> getAll();

    Utilisateur getById(int id);

    void create(Utilisateur utilisateur);

    Utilisateur update(Utilisateur utilisateur);

    boolean delete(int id);
}
