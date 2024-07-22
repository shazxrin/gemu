package me.shazxrin.gemu.controller;

import jakarta.validation.Valid;
import me.shazxrin.gemu.dto.paging.PageDto;
import me.shazxrin.gemu.dto.library.*;
import me.shazxrin.gemu.exception.NotFoundException;
import me.shazxrin.gemu.exception.StateException;
import me.shazxrin.gemu.exception.ValidationException;
import me.shazxrin.gemu.entity.LibraryGame;
import me.shazxrin.gemu.service.library.LibraryGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/game/library")
public class LibraryGameController {
    private final LibraryGameService libraryGameService;

    @Autowired
    public LibraryGameController(LibraryGameService libraryGameService) {
        this.libraryGameService = libraryGameService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{gameId}")
    public void postAddGameToLibrary(
        @PathVariable String gameId,
        @Valid @RequestBody AddLibraryGameDto addLibraryGameDto
    ) throws ValidationException, StateException {
        libraryGameService.addGameToLibrary(
            gameId,
            addLibraryGameDto.platformId(),
            addLibraryGameDto.status(),
            addLibraryGameDto.ownership(),
            addLibraryGameDto.hoursPlayed(),
            addLibraryGameDto.progress()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public PageDto<ListLibraryGameDto> getAllGamesInLibrary(
        @RequestParam(name = "page", required = false) Optional<Integer> pageOpt,
        @RequestParam(name = "platformId", required = false) Optional<String> platformIdOpt
    ) {
        int page = pageOpt.orElse(0);

        Page<LibraryGame> libraryGamePage;
        if (platformIdOpt.isPresent()) {
            String platformId = platformIdOpt.get();
            libraryGamePage = libraryGameService.getAllGamesInLibraryByPlatformId(platformId, page);
        } else {
            libraryGamePage = libraryGameService.getAllGamesInLibrary(page);
        }

        Page<ListLibraryGameDto> listLibraryGameDtoPage = libraryGamePage
            .map(libraryGame -> new ListLibraryGameDto(
                new ListLibraryGameGameDto(
                    libraryGame.getGame().getId(),
                    libraryGame.getGame().getName()
                ),
                new ListLibraryGamePlatformDto(
                    libraryGame.getPlatform().getId(),
                    libraryGame.getPlatform().getName()
                ),
                libraryGame.getStatus(),
                libraryGame.getOwnership(),
                libraryGame.getHoursPlayed(),
                libraryGame.getProgress()
            ));

        return new PageDto<>(
            listLibraryGameDtoPage.getNumber(),
            listLibraryGameDtoPage.getTotalPages(),
            listLibraryGameDtoPage.getContent()
        );
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<LibraryGameDto> getGameFromLibrary(@PathVariable String gameId) {
        Optional<LibraryGame> libraryGameOpt = libraryGameService.getGameInLibrary(gameId);

        if (libraryGameOpt.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                libraryGameOpt
                    .map(libraryGame -> new LibraryGameDto(
                        new LibraryGamePlatformDto(
                            libraryGame.getPlatform().getId(),
                            libraryGame.getPlatform().getName()
                        ),
                        libraryGame.getStatus(),
                        libraryGame.getOwnership(),
                        libraryGame.getHoursPlayed(),
                        libraryGame.getProgress()
                    ))
                    .get()
            );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{gameId}")
    public void putUpdateGameInLibrary(
        @PathVariable String gameId,
        @Valid @RequestBody UpdateLibraryGameDto updateLibraryGameDto
    ) throws ValidationException, NotFoundException {
        libraryGameService.updateGameInLibrary(
            gameId,
            updateLibraryGameDto.platformId(),
            updateLibraryGameDto.status(),
            updateLibraryGameDto.ownership(),
            updateLibraryGameDto.hoursPlayed(),
            updateLibraryGameDto.progress()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{gameId}")
    public void deleteRemoveGameFromLibrary(@PathVariable String gameId) throws NotFoundException {
        libraryGameService.removeGameFromLibrary(gameId);
    }
}
