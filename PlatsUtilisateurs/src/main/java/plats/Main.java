package plats;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/PlatsUtilisateurs/api/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("plats.resources");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws Exception {
        final HttpServer server = startServer();
        System.out.println(String.format(
            "Application disponible sur %sapi/plats et %sapi/utilisateurs\nAppuyez sur Entrée pour arrêter...",
            BASE_URI, BASE_URI));
        System.in.read();
        server.shutdown();
    }
}
