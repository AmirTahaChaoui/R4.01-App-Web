package plats;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    // URI de base sur laquelle le serveur Grizzly va écouter
    public static final String BASE_URI = "http://localhost:8080/PlatsUtilisateurs/api/";

    public static HttpServer startServer() {
        // Scanner le package des ressources pour les enregistrer
        final ResourceConfig rc = new ResourceConfig().packages("plats.resources");
        
        // Créer et démarrer une nouvelle instance du serveur Grizzly
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer server = startServer();
        System.out.println(String.format(
            "Application lancée sur %splats et %sutilisateurs\nAppuyez sur Entrée pour arrêter le serveur...",
            BASE_URI, BASE_URI));
            
        System.in.read();
        server.shutdownNow();
    }
}
