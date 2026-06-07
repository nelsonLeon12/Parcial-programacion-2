package gamezone.services;

import gamezone.entities.VideoGame;
import java.util.List;

public interface VideoGameService {
    void addVideoGame(VideoGame game);
    List<VideoGame> getAllVideoGames();
    VideoGame findByTitle(String title);
    List<VideoGame> findByPlatform(String platform);
    void updateVideoGame(String title, VideoGame updated);
    void deleteVideoGame(String title);
}
