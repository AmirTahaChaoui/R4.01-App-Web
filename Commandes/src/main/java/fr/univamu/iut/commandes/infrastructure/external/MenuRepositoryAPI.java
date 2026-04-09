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
            if (response.getStatus() == 200) {
                return response.readEntity(Menu.class);
            }
            return null;
        } finally {
            client.close();
        }
    }
}
