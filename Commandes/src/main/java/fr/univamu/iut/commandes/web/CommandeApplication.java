package fr.univamu.iut.commandes.web;

import fr.univamu.iut.commandes.domain.CommandeRepository;
import fr.univamu.iut.commandes.infrastructure.persistence.CommandeRepositoryMariadb;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
public class CommandeApplication extends Application {

    @Produces
    public CommandeRepository getCommandeRepository() {
        return new CommandeRepositoryMariadb();
    }
}
