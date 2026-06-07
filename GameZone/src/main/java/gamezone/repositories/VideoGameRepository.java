package gamezone.repositories;

import gamezone.entities.VideoGame;
import gamezone.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoGameRepository {

    private List<VideoGame> catalog;

    public VideoGameRepository() {
        this.catalog = JsonUtil.loadGames();
    }

    // CREATE
    public void add(VideoGame game) {
        catalog.add(game);
        JsonUtil.saveGames(catalog);
    }

    // READ - todos
    public List<VideoGame> getAll() {
        return new ArrayList<>(catalog);
    }

    // READ - por titulo (case-insensitive)
    public VideoGame findByTitle(String title) {
        return catalog.stream()
                .filter(g -> g.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    // READ - por plataforma (case-insensitive)
    public List<VideoGame> findByPlatform(String platform) {
        return catalog.stream()
                .filter(g -> g.getPlatform().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }

    // EXISTS
    public boolean existsByTitle(String title) {
        return catalog.stream().anyMatch(g -> g.getTitle().equalsIgnoreCase(title));
    }

    // UPDATE
    public boolean update(String title, VideoGame updated) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getTitle().equalsIgnoreCase(title)) {
                catalog.set(i, updated);
                JsonUtil.saveGames(catalog);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean delete(String title) {
        boolean removed = catalog.removeIf(g -> g.getTitle().equalsIgnoreCase(title));
        if (removed) JsonUtil.saveGames(catalog);
        return removed;
    }
}
