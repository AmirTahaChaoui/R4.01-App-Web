package plats;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import java.net.URI;

public class Main {
    // URI de base sur laquelle le serveur Grizzly va écouter
    public static final String BASE_URI = "http://localhost:8080/PlatsUtilisateurs/api/";

    public static HttpServer startServer() {
        // Scanner le package des ressources
        final ResourceConfig rc = new ResourceConfig().packages("plats.presentation.resources");
        
        // Créer et démarrer une nouvelle instance du serveur Grizzly
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws Exception {
        // Initialiser le conteneur CDI (Weld)
        // On laisse SeContainerInitializer trouver le beans.xml automatiquement
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            final HttpServer server = startServer();
            System.out.println(String.format(
                "Application Clean Architecture lancée sur %splats et %sutilisateurs",
                BASE_URI, BASE_URI));
            System.out.println("Appuyez sur Entrée pour arrêter le serveur...");
                
            System.in.read();
            server.shutdownNow();
        }
    }
}
