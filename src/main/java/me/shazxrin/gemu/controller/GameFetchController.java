package me.shazxrin.gemu.controller;

import me.shazxrin.gemu.service.GameFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/fetch")
public class GameFetchController {
    private final GameFetchService gameFetchService;

    @Autowired
    public GameFetchController(GameFetchService gameFetchService) {
        this.gameFetchService = gameFetchService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping
    public void getStartFetchGame() {
        this.gameFetchService.fetchGames();
    }
}
