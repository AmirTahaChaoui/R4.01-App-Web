package plats.application.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import plats.domain.entities.Utilisateur;
import plats.domain.repositories.IUtilisateurRepository;
import java.util.List;

@ApplicationScoped
public class UtilisateurService {

    @Inject
    private IUtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.getAll();
    }

    public Utilisateur getUtilisateurById(int id) {
        return utilisateurRepository.getById(id);
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.create(utilisateur);
    }

    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.update(utilisateur);
    }

    public boolean deleteUtilisateur(int id) {
        return utilisateurRepository.delete(id);
    }
}
