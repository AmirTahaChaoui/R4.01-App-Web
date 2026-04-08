package menu.menus;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.time.LocalDate; // import the LocalDate class



/**
 * Classe utilisée pour récupérer les informations nécessaires à la ressource
 * (permet de dissocier ressource et mode d'éccès aux données)
 */
public class MenuService {

    /**
     * Objet permettant d'accéder au dépôt où sont stockées les informations sur les livres
     */
    protected MenuRepositoryInterface MenuRepo ;

    /**
     * Constructeur permettant d'injecter l'accès aux données
     * @param MenuRepo objet implémentant l'interface d'accès aux données
     */
    public  MenuService( MenuRepositoryInterface MenuRepo) {
        this.MenuRepo = MenuRepo;
    }

    /**
     * Méthode retournant les informations sur les livres au format JSON
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getAllMenusJSON(){

        ArrayList<Menu> allMenus = MenuRepo.getAllMenus();

        // création du json et conversion de la liste de livres
        String result = null;
        try( Jsonb jsonb = JsonbBuilder.create()){
            result = jsonb.toJson(allMenus);
        }
        catch (Exception e){
            System.err.println( e.getMessage() );
        }

        return result;
    }

    /**
     * Méthode retournant au format JSON les informations sur un livre recherché
     * @param reference la référence du livre recherché
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getMenuJSON( int reference ){
        String result = null;
        Menu myMenu = MenuRepo.getMenu(reference);

        // si le livre a été trouvé
        if( myMenu != null ) {

            // création du json et conversion du livre
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myMenu);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Méthode permettant de mettre à jour les informations d'un livre
     * @param reference référence du livre à mettre à jour
     * @param Menu les nouvelles infromations a été utiliser
     * @return true si le livre a pu être mis à jour
     */
    public boolean updateMenu(int reference, Menu Menu) {
        return MenuRepo.updateMenu(reference, Menu.nom, Menu.platsIds, Menu.auteur, String.valueOf(LocalDate.now()));
    }
}