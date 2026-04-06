package plats.dao;

import plats.model.Utilisateur;
import java.util.*;

public class UtilisateurDAO {
    private static List<Utilisateur> utilisateurs = new ArrayList<>();

    // Initialiser avec quelques données (optionnel, pour tester)
    static {
        utilisateurs.add(new Utilisateur(1, "Doe", "John", "john.doe@email.com", "123 Rue de la Paix, Paris"));
        utilisateurs.add(new Utilisateur(2, "Martin", "Alice", "alice.martin@email.com", "45 Avenue des Champs-Élysées, Paris"));
        utilisateurs.add(new Utilisateur(3, "Dupont", "Jean", "jean.dupont@email.com", "8 Impasse de la Gare, Lyon"));
        utilisateurs.add(new Utilisateur(4, "Lefebvre", "Sophie", "sophie.lefebvre@email.com", "22 Boulevard Victor Hugo, Lille"));
        utilisateurs.add(new Utilisateur(5, "Moreau", "Thomas", "thomas.moreau@email.com", "15 Rue de la Liberté, Bordeaux"));
    }

    public List<Utilisateur> getAll() {
        return utilisateurs;
    }

    public Utilisateur getById(int id) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                return utilisateur;
            }
        }
        return null;
    }

    public void create(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
    }

    public Utilisateur update(Utilisateur utilisateurUpdate) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == utilisateurUpdate.getId()) {
                utilisateur.setNom(utilisateurUpdate.getNom());
                utilisateur.setPrenom(utilisateurUpdate.getPrenom());
                utilisateur.setEmail(utilisateurUpdate.getEmail());
                utilisateur.setAdresse(utilisateurUpdate.getAdresse());
                return utilisateur;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        Utilisateur utilisateurToRemove = null;
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                utilisateurToRemove = utilisateur;
                break;
            }
        }
        if (utilisateurToRemove != null) {
            utilisateurs.remove(utilisateurToRemove);
            return true;
        }
        return false;
    }
}