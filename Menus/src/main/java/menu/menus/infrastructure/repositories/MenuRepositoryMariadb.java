package menu.menus.infrastructure.repositories;

import menu.menus.domain.entities.Menu;
import menu.menus.application.services.MenuRepositoryInterface;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implémentation du repository des menus avec base de données MariaDB
 * Couche Infrastructure - contient les détails d'implémentation concrète
 */
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable {

    private Connection dbConnection;

    /**
     * Constructeur établissant la connexion à la base de données
     * @param infoConnection chaîne de connexion
     * @param user utilisateur
     * @param pwd mot de passe
     * @throws SQLException si erreur de connexion
     * @throws ClassNotFoundException si driver non trouvé
     */
    public MenuRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        this.dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }

    @Override
    public Menu getMenu(int id) {
        String query = "SELECT * FROM Menu WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                return buildMenuFromResultSet(result);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du menu: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<Menu> getAllMenus() {
        ArrayList<Menu> listMenus = new ArrayList<>();
        String query = "SELECT * FROM Menu";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                listMenus.add(buildMenuFromResultSet(result));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des menus: " + e.getMessage());
        }

        return listMenus;
    }

    @Override
    public int addMenu(Menu menu) {
        String query = "INSERT INTO Menu (nom, platsIds, auteur, dateCreation, dateModification) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            String plats = String.join(",",
                    menu.getPlatsIds().stream().map(String::valueOf).toList());

            ps.setString(1, menu.getNom());
            ps.setString(2, plats);
            ps.setString(3, menu.getAuteur());
            ps.setString(4, menu.getDateCreation());
            ps.setString(5, menu.getDateModification());

            int nbRowInserted = ps.executeUpdate();
            if (nbRowInserted > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("✅ Menu créé avec l'ID: " + generatedId);
                    return generatedId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du menu: " + e.getMessage());
        }

        return -1;
    }

    @Override
    public boolean deleteMenu(int id) {
        String query = "DELETE FROM Menu WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            int nbRowDeleted = ps.executeUpdate();
            return nbRowDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du menu: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean updateMenu(int id, String nom, List<Integer> platsIds, String auteur, String dateModification) {
        String query = "UPDATE Menu SET nom=?, platsIds=?, auteur=?, dateModification=? WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            String plats = String.join(",",
                    platsIds.stream().map(String::valueOf).toList());

            ps.setString(1, nom);
            ps.setString(2, plats);
            ps.setString(3, auteur);
            ps.setString(4, dateModification);
            ps.setInt(5, id);

            int nbRowModified = ps.executeUpdate();
            return nbRowModified > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du menu: " + e.getMessage());
        }

        return false;
    }

    /**
     * Méthode utilitaire pour construire un Menu à partir d'une ligne ResultSet
     * @param result ligne du résultat SQL
     * @return objet Menu construit
     * @throws SQLException si erreur d'accès aux colonnes
     */
    private Menu buildMenuFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        String nom = result.getString("nom");
        String plats = result.getString("platsIds");
        String auteur = result.getString("auteur");
        String dateCreation = result.getString("dateCreation");
        String dateModification = result.getString("dateModification");

        List<Integer> platsIds = Arrays.stream(plats.split(","))
                .map(Integer::parseInt)
                .toList();

        return new Menu(id, nom, platsIds, auteur, dateCreation, dateModification);
    }
}
