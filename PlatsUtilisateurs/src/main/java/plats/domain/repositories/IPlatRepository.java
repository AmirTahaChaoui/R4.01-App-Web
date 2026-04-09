package plats.domain.repositories;

import plats.domain.entities.Plat;
import java.util.List;

public interface IPlatRepository {
    List<Plat> getAll();

    Plat getById(int id);

    void create(Plat plat);

    Plat update(Plat plat);

    boolean delete(int id);
}
