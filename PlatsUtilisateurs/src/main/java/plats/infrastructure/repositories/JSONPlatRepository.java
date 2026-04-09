package plats.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import plats.domain.entities.Plat;
import plats.domain.repositories.IPlatRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

@ApplicationScoped
public class JSONPlatRepository implements IPlatRepository {
    private List<Plat> plats = new ArrayList<>();
    private final String FILE_PATH = "plats.json";
    private final Jsonb jsonb = JsonbBuilder.create();

    public JSONPlatRepository() {
        loadData();
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileInputStream is = new FileInputStream(file)) {
                Plat[] loadedPlats = jsonb.fromJson(is, Plat[].class);
                plats = new ArrayList<>(Arrays.asList(loadedPlats));
            } catch (Exception e) {
                e.printStackTrace();
                initDefaultData();
            }
        } else {
            initDefaultData();
        }
    }

    private void initDefaultData() {
        plats.add(new Plat(1, "Pizza", "Pizza aux légumes", 10.0));
        plats.add(new Plat(2, "Pâtes", "Pâtes à la carbonara", 12.0));
        plats.add(new Plat(3, "Salade", "Salade composée", 8.0));
        plats.add(new Plat(4, "Steak", "Steak frites", 15.0));
        plats.add(new Plat(5, "Poisson", "Poisson grillé", 18.0));
        saveData();
    }

    private void saveData() {
        try (FileOutputStream os = new FileOutputStream(FILE_PATH)) {
            jsonb.toJson(plats, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Plat> getAll() {
        return plats;
    }

    @Override
    public Plat getById(int id) {
        return plats.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void create(Plat plat) {
        if (plat.getId() == 0) {
            int maxId = plats.stream().mapToInt(Plat::getId).max().orElse(0);
            plat.setId(maxId + 1);
        }
        plats.add(plat);
        saveData();
    }

    @Override
    public Plat update(Plat platUpdate) {
        Plat plat = getById(platUpdate.getId());
        if (plat != null) {
            plat.setNom(platUpdate.getNom());
            plat.setDescription(platUpdate.getDescription());
            plat.setPrix(platUpdate.getPrix());
            saveData();
            return plat;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        boolean removed = plats.removeIf(p -> p.getId() == id);
        if (removed) {
            saveData();
        }
        return removed;
    }
}