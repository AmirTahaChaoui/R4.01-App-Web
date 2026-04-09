package plats.application.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import plats.domain.entities.Plat;
import plats.domain.repositories.IPlatRepository;
import java.util.List;

@ApplicationScoped
public class PlatService {

    @Inject
    private IPlatRepository platRepository;

    public List<Plat> getAllPlats() {
        return platRepository.getAll();
    }

    public Plat getPlatById(int id) {
        return platRepository.getById(id);
    }

    public void addPlat(Plat plat) {
        platRepository.create(plat);
    }

    public Plat updatePlat(Plat plat) {
        return platRepository.update(plat);
    }

    public boolean deletePlat(int id) {
        return platRepository.delete(id);
    }
}
