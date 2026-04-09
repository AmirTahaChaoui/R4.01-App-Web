package menu.menus.infrastructure.repositories;

import menu.menus.domain.entities.Plat;
import menu.menus.domain.repositories.PlatsRepositoryInterface;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.ArrayList;

/**
 * Implémentation du repository des plats via API REST
 * Couche Infrastructure - contient les détails d'implémentation concrète
 */
public class PlatsRepositoryAPI implements PlatsRepositoryInterface {

    private final String url;

    /**
     * Constructeur initialisant l'URL de l'API
     * @param url URL de base de l'API Plats-Utilisateurs
     */
    public PlatsRepositoryAPI(String url) {
        this.url = url;
    }

    @Override
    public void close() {
        // Pas de ressources à fermer pour une API REST
    }

    @Override
    public Plat getPlat(int id) {
        Plat myPlat = null;

        try {
            Client client = ClientBuilder.newClient();
            WebTarget platResource = client.target(url);
            WebTarget platEndpoint = platResource.path("plats/" + id);
            Response response = platEndpoint.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200) {
                myPlat = response.readEntity(Plat.class);
            }

            response.close();
            client.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du plat: " + e.getMessage());
        }

        return myPlat;
    }

    @Override
    public ArrayList<Plat> getAllPlats() {
        ArrayList<Plat> plats = new ArrayList<>();

        try {
            Client client = ClientBuilder.newClient();
            WebTarget platResource = client.target(url);
            WebTarget platsEndpoint = platResource.path("plats");
            Response response = platsEndpoint.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200) {
                String jsonString = response.readEntity(String.class);

                try (Jsonb jsonb = JsonbBuilder.create()) {
                    Plat[] platsArray = jsonb.fromJson(jsonString, Plat[].class);
                    if (platsArray != null) {
                        for (Plat plat : platsArray) {
                            plats.add(plat);
                        }
                    }
                }
            }

            response.close();
            client.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des plats: " + e.getMessage());
        }

        return plats;
    }
}
