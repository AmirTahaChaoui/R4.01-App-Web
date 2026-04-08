package menu.menus;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe permettant d'accéder aux menus stockés dans une base de données Mariadb
 */
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable {

    /**
     * Accès à la base de données (session)
     */
    protected Connection dbConnection;

    /**
     * Constructeur de la classe
     */
    public MenuRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {

        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Menu getMenu(int id) {

        Menu selectedMenu = null;

        String query = "SELECT * FROM Menu WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                String nom = result.getString("nom");
                String plats = result.getString("platsIds");
                String auteur = result.getString("auteur");
                String dateCreation = result.getString("dateCreation");

                // conversion String -> List<Integer>
                List<Integer> platsIds = Arrays.stream(plats.split(","))
                        .map(Integer::parseInt)
                        .toList();

                selectedMenu = new Menu(id, nom, platsIds, auteur, dateCreation, null);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return selectedMenu;
    }

    @Override
    public ArrayList<Menu> getAllMenus() {

        ArrayList<Menu> listMenus;

        String query = "SELECT * FROM Menu";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {

            ResultSet result = ps.executeQuery();

            listMenus = new ArrayList<>();

            while (result.next()) {

                int id = result.getInt("id");
                String nom = result.getString("nom");
                String plats = result.getString("platsIds");
                String auteur = result.getString("auteur");
                String dateCreation = result.getString("dateCreation");
                String dateModification = result.getString("dateModification");

                List<Integer> platsIds = Arrays.stream(plats.split(","))
                        .map(Integer::parseInt)
                        .toList();

                Menu currentMenu = new Menu(id, nom, platsIds, auteur, dateCreation, dateModification  );

                listMenus.add(currentMenu);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listMenus;
    }

    @Override
    public boolean addMenu(Menu menu) {
        return false;
    }


    @Override
    public boolean deleteMenu(int id) {
        return false;
    }

    @Override
    public boolean updateMenu(int id, String nom, List<Integer> platsIds, String auteur, String dateModification) {

        String query = "UPDATE Menu SET nom=?, platsIds=?, auteur=?, dateModification=? WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {

            // conversion List -> String
            String plats = String.join(",",
                    platsIds.stream().map(String::valueOf).toList());

            ps.setString(1, nom);
            ps.setString(2, plats);
            ps.setString(3, auteur);
            ps.setString(4, dateModification);
            ps.setInt(5, id);

            nbRowModified = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }
}