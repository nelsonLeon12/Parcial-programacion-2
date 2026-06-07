package gamezone.services.impl;

import gamezone.entities.VideoGame;
import gamezone.repositories.VideoGameRepository;
import gamezone.services.VideoGameService;

import java.util.List;

public class VideoGameServiceImpl implements VideoGameService {

    private final VideoGameRepository repository;

    public VideoGameServiceImpl(VideoGameRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addVideoGame(VideoGame game) {
        // Validaciones de negocio
        if (game.getTitle() == null || game.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío.");
        }
        if (game.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        if (game.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a 0.");
        }
        if (repository.existsByTitle(game.getTitle())) {
            // Se lanza excepción especial; la UI la captura y muestra Alert
            throw new IllegalStateException("El videojuego ya existe en el catálogo.");
        }
        repository.add(game);
    }

    @Override
    public List<VideoGame> getAllVideoGames() {
        return repository.getAll();
    }

    @Override
    public VideoGame findByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<VideoGame> findByPlatform(String platform) {
        return repository.findByPlatform(platform);
    }

    @Override
    public void updateVideoGame(String title, VideoGame updated) {
        if (!repository.existsByTitle(title)) {
            throw new IllegalArgumentException("No existe un videojuego con ese título.");
        }
        repository.update(title, updated);
    }

    @Override
    public void deleteVideoGame(String title) {
        if (!repository.delete(title)) {
            throw new IllegalArgumentException("No se encontró el videojuego para eliminar.");
        }
    }
}
