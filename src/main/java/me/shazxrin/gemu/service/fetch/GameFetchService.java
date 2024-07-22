package me.shazxrin.gemu.service.fetch;

import org.springframework.scheduling.annotation.Async;

public interface GameFetchService {
    @Async
    void fetchGames();
}
