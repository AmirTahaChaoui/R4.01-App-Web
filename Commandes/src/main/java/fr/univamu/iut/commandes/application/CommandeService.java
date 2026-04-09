package fr.univamu.iut.commandes.application;

import fr.univamu.iut.commandes.domain.Commande;
import fr.univamu.iut.commandes.domain.CommandeRepository;
import fr.univamu.iut.commandes.domain.Menu;
import fr.univamu.iut.commandes.domain.MenuRepository;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class CommandeService {

    protected CommandeRepository commandeRepo;
    protected MenuRepository menuRepo;

    public CommandeService(CommandeRepository commandeRepo, MenuRepository menuRepo) {
        this.commandeRepo = commandeRepo;
        this.menuRepo = menuRepo;
    }

    public String creerCommandeJSON(Commande commande) {
        double total = 0.0;

        if (commande.getMenusIds() != null) {
            for (Long menuId : commande.getMenusIds()) {
                Menu m = menuRepo.getMenu(menuId);
                if (m != null && m.getPrix() != null) {
                    total += m.getPrix();
                }
            }
        }
        commande.setPrixTotal(total);

        boolean success = commandeRepo.addCommande(commande);

        String result = null;
        if (success) {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(commande);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
}
