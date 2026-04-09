package fr.univamu.iut.commandes.infrastructure.external;

import fr.univamu.iut.commandes.domain.Menu;
import fr.univamu.iut.commandes.domain.MenuRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class MenuRepositoryAPI implements MenuRepository {

    private final String url;

    public MenuRepositoryAPI(String url) {
        this.url = url;
    }

    @Override
    public void close() {}

    @Override
    public Menu getMenu(Long id) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(url);
            Response response = target.path("menus/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.readEntity(Menu.class);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erreur de connexion au service Menus (" + url + ") : " + e.getMessage());
            // En cas d'erreur (ex: service indisponible), on retourne null 
            // pour permettre à la commande d'être créée avec un prix de 0.
            return null;
        } finally {
            client.close();
        }
    }
}
