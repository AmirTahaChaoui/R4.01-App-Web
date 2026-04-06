package plats.dao;

import plats.model.Plat;
import java.util.*;

public class PlatDAO {
    private static List<Plat> plats = new ArrayList<>();

    // Initialiser avec quelques données (optionnel, pour tester)
    static {
        plats.add(new Plat(1, "Pizza", "Pizza aux légumes", 10.0));
        plats.add(new Plat(2, "Pâtes", "Pâtes à la carbonara", 12.0));
        plats.add(new Plat(3, "Salade", "Salade composée", 8.0));
        plats.add(new Plat(4, "Steak", "Steak frites", 15.0));
        plats.add(new Plat(5, "Poisson", "Poisson grillé", 18.0));
    }

    public List<Plat> getAll() {
        return plats;
    }

    public Plat getById(int id) {
        for (Plat plat : plats) {
            if (plat.getId() == id) {
                return plat;
            }
        }
        return null;
    }

    public void create(Plat plat) {
        plats.add(plat);
    }

    public Plat update(Plat platUpdate) {
        for (Plat plat : plats) {
            if (plat.getId() == platUpdate.getId()) {
                plat.setNom(platUpdate.getNom());
                plat.setDescription(platUpdate.getDescription());
                plat.setPrix(platUpdate.getPrix());
                return plat;
            }
        }
        return null;
    }

    public boolean delete(int id) {
        Plat platToRemove = null;
        for (Plat plat : plats) {
            if (plat.getId() == id) {
                platToRemove = plat;
                break;
            }
        }
        if (platToRemove != null) {
            plats.remove(platToRemove);
            return true;
        }
        return false;
    }
}