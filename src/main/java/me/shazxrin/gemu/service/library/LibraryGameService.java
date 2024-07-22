package me.shazxrin.gemu.service.library;

import me.shazxrin.gemu.exception.NotFoundException;
import me.shazxrin.gemu.exception.StateException;
import me.shazxrin.gemu.exception.ValidationException;
import me.shazxrin.gemu.entity.LibraryGame;
import me.shazxrin.gemu.entity.LibraryGameOwnership;
import me.shazxrin.gemu.entity.LibraryGameStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface LibraryGameService {
    void addGameToLibrary(
        String gameId,
        String platformId,
        LibraryGameStatus status,
        LibraryGameOwnership ownership,
        int hoursPlayed,
        int progress
    ) throws ValidationException, StateException;

    Page<LibraryGame> getAllGamesInLibrary(int page);

    Page<LibraryGame> getAllGamesInLibraryByPlatformId(String platformId, int page);

    Optional<LibraryGame> getGameInLibrary(String gameId);

    void updateGameInLibrary(
        String gameId,
        String platformId,
        LibraryGameStatus status,
        LibraryGameOwnership ownership,
        int hoursPlayed,
        int progress
    ) throws NotFoundException, ValidationException;

    void removeGameFromLibrary(String gameId) throws NotFoundException;
}
