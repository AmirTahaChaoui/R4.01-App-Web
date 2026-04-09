package plats.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import plats.domain.entities.Utilisateur;
import plats.domain.repositories.IUtilisateurRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

@ApplicationScoped
public class JSONUtilisateurRepository implements IUtilisateurRepository {
    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private final String FILE_PATH = "utilisateurs.json";
    private final Jsonb jsonb = JsonbBuilder.create();

    public JSONUtilisateurRepository() {
        loadData();
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileInputStream is = new FileInputStream(file)) {
                Utilisateur[] loadedUsers = jsonb.fromJson(is, Utilisateur[].class);
                utilisateurs = new ArrayList<>(Arrays.asList(loadedUsers));
            } catch (Exception e) {
                e.printStackTrace();
                initDefaultData();
            }
        } else {
            initDefaultData();
        }
    }

    private void initDefaultData() {
        utilisateurs.add(new Utilisateur(1, "Doe", "John", "john.doe@email.com", "123 Rue de la Paix, Paris"));
        utilisateurs.add(
                new Utilisateur(2, "Martin", "Alice", "alice.martin@email.com", "45 Avenue des Champs-Élysées, Paris"));
        utilisateurs.add(new Utilisateur(3, "Dupont", "Jean", "jean.dupont@email.com", "8 Impasse de la Gare, Lyon"));
        utilisateurs.add(new Utilisateur(4, "Lefebvre", "Sophie", "sophie.lefebvre@email.com",
                "22 Boulevard Victor Hugo, Lille"));
        utilisateurs.add(
                new Utilisateur(5, "Moreau", "Thomas", "thomas.moreau@email.com", "15 Rue de la Liberté, Bordeaux"));
        saveData();
    }

    private void saveData() {
        try (FileOutputStream os = new FileOutputStream(FILE_PATH)) {
            jsonb.toJson(utilisateurs, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        return utilisateurs;
    }

    @Override
    public Utilisateur getById(int id) {
        return utilisateurs.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void create(Utilisateur utilisateur) {
        if (utilisateur.getId() == 0) {
            int maxId = utilisateurs.stream().mapToInt(Utilisateur::getId).max().orElse(0);
            utilisateur.setId(maxId + 1);
        }
        utilisateurs.add(utilisateur);
        saveData();
    }

    @Override
    public Utilisateur update(Utilisateur utilisateurUpdate) {
        Utilisateur user = getById(utilisateurUpdate.getId());
        if (user != null) {
            user.setNom(utilisateurUpdate.getNom());
            user.setPrenom(utilisateurUpdate.getPrenom());
            user.setEmail(utilisateurUpdate.getEmail());
            user.setAdresse(utilisateurUpdate.getAdresse());
            saveData();
            return user;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        boolean removed = utilisateurs.removeIf(u -> u.getId() == id);
        if (removed) {
            saveData();
        }
        return removed;
    }
}