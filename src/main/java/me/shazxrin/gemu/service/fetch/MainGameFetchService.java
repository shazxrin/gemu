package me.shazxrin.gemu.service.fetch;

import me.shazxrin.gemu.igdb.dto.IGDBGameCountDto;
import me.shazxrin.gemu.igdb.dto.IGDBGameDto;
import me.shazxrin.gemu.igdb.dto.IGDBPlatformDto;
import me.shazxrin.gemu.igdb.exception.IGDBException;
import me.shazxrin.gemu.igdb.service.IGDBService;
import me.shazxrin.gemu.entity.Game;
import me.shazxrin.gemu.entity.Platform;
import me.shazxrin.gemu.repository.GameRepository;
import me.shazxrin.gemu.repository.PlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MainGameFetchService implements GameFetchService {
    private static final Logger log = LoggerFactory.getLogger(MainGameFetchService.class);

    private final IGDBService igdbService;
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final List<String> platformNames;

    @Autowired
    public MainGameFetchService(
        IGDBService igdbService,
        GameRepository gameRepository,
        PlatformRepository platformRepository,
        @Value("${gemu.platforms}") List<String> platformNames
    ) {
        this.igdbService = igdbService;
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
        this.platformNames = platformNames;
    }

    @Override
    @Async
    public void fetchGames() {
        for (String platformName : platformNames) {
            log.info("Trying to fetch games for {}", platformName);

            // Try to fetch platform from DB
            Platform platform = platformRepository.findFirstByName(platformName);
            if (platform == null) {
                // Get platform from IGDB
                try {
                    IGDBPlatformDto[] platformDtos = igdbService.getPlatformsByName(platformName);
                    if (platformDtos.length == 0) {
                        log.error("Platform {} cannot be found", platformName);
                        continue;
                    }

                    IGDBPlatformDto platformDto = platformDtos[0];
                    platform = new Platform(platformDto.id(), platformName);
                    platformRepository.save(platform);
                } catch (IGDBException exception) {
                    log.error("Error occurred while fetching {}", platformName, exception);
                    continue;
                }
            }

            // Get total games count for platform
            long totalCount = 0;
            try {
                IGDBGameCountDto gameCountDto = igdbService.getGameCountByPlatformId(platform.getExternalId());
                totalCount = gameCountDto.count();
            } catch (IGDBException exception) {
                log.error("Error occurred while fetching games count for {}", platformName, exception);
                continue;
            }
            log.info("Total games for {} is {}", platformName, totalCount);

            long limit = 10;
            long totalPages = totalCount / limit;
            long totalGamesProcessed = 0;
            long totalGamesFailed = 0;
            for (int page = 0; page < totalPages; page++) {
                long offset = page * limit;

                try {
                    IGDBGameDto[] gameDtos = igdbService.getGamesByPlatformId(platform.getExternalId(), limit, offset);
                    for (IGDBGameDto gameDto : gameDtos) {
                        Game game = gameRepository.findByExternalId(gameDto.id());

                        StringBuilder descriptionBuilder = new StringBuilder();
                        gameDto.summary().ifPresent(summary -> descriptionBuilder.append(summary));
                        gameDto.storyline().ifPresent(storyline -> {
                            if (descriptionBuilder.isEmpty()) {
                                descriptionBuilder.append(" ");
                            }
                            descriptionBuilder.append(storyline);
                        });
                        String description = descriptionBuilder.toString();

                        if (game == null) {
                            game = new Game(
                                gameDto.id(),
                                gameDto.name(),
                                description,
                                Set.of(platform)
                            );
                        } else {
                            game.setDescription(description);

                            game.getPlatforms().add(platform);
                        }
                        gameRepository.save(game);
                        totalGamesProcessed++;
                    }
                } catch (IGDBException exception) {
                    log.error("Error occurred while fetching games for {} at page {}", platformName, page, exception);
                    totalGamesFailed++;
                    continue;
                }

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException exception) {
                    log.error("Error occurred during backpressure", exception);
                }
            }
            log.info(
                "Total games processed for {} is {}, success: {}, failed: {}",
                platformName,
                totalGamesProcessed,
                totalGamesProcessed,
                totalGamesFailed
            );
        }
    }
}
