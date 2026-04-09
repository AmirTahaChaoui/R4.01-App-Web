package menu.menus.infrastructure.repositories;

import menu.menus.domain.entities.Plat;
import menu.menus.domain.repositories.PlatsRepositoryInterface;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * Implémentation du repository des plats via fichier JSON
 * Charge les données depuis le fichier plats-utilisateurs.json
 * Couche Infrastructure - contient les détails d'implémentation concrète
 */
public class PlatsRepositoryJSON implements PlatsRepositoryInterface {

    private ArrayList<Plat> plats;
    private final String filePath;

    /**
     * Constructeur initialisant le chemin du fichier JSON
     * Utilise plusieurs stratégies de recherche :
     * 1. Variable d'environnement PLATS_JSON_PATH
     * 2. Chemin relatif ../plats-utilisateurs.json
     * @param filePath chemin par défaut vers le fichier plats-utilisateurs.json
     */
    public PlatsRepositoryJSON(String filePath) {
        // Essayer d'abord avec la variable d'environnement
        String envPath = System.getenv("PLATS_JSON_PATH");
        if (envPath != null && !envPath.isEmpty()) {
            this.filePath = envPath;
        } else {
            this.filePath = filePath;
        }
        
        this.plats = new ArrayList<>();
        chargerPlats();
    }

    /**
     * Charge les plats depuis le fichier JSON
     */
    @SuppressWarnings("unchecked")
    private void chargerPlats() {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            
            // Lire le fichier JSON
            FileReader fileReader = new FileReader(filePath);
            Map<String, Object> data = jsonb.fromJson(fileReader, Map.class);
            
            // Extraire la liste des plats
            if (data.containsKey("plats")) {
                ArrayList<Map<String, Object>> platsData = (ArrayList<Map<String, Object>>) data.get("plats");
                
                for (Map<String, Object> platData : platsData) {
                    Plat plat = new Plat();
                    plat.setId(((Number) platData.get("id")).intValue());
                    plat.setNom((String) platData.get("nom"));
                    plat.setDescription((String) platData.get("description"));
                    plat.setPrix(((Number) platData.get("prix")).doubleValue());
                    plats.add(plat);
                }
            }
            
            fileReader.close();
            System.out.println("✓ " + plats.size() + " plats chargés depuis " + filePath);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des plats depuis " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Plat getPlat(int id) {
        for (Plat plat : plats) {
            if (plat.getId() == id) {
                return plat;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Plat> getAllPlats() {
        return new ArrayList<>(plats);
    }

    @Override
    public void close() {
        // Pas de ressources à fermer pour JSON
    }
}
