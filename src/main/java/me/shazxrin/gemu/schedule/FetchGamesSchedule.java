package me.shazxrin.gemu.schedule;

import me.shazxrin.gemu.service.GameFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FetchGamesSchedule {
    private static final Logger log = LoggerFactory.getLogger(FetchGamesSchedule.class);
    private final GameFetchService gameFetchService;

    @Autowired
    public FetchGamesSchedule(GameFetchService gameFetchService) {
        this.gameFetchService = gameFetchService;
    }

//    @Scheduled(cron = "")
    public void fetchGames() {
        log.info("Fetching games from IGDB...");
        this.gameFetchService.fetchGames();
        log.info("Completed fetching games from IGDB...");
    }
}
