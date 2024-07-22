package me.shazxrin.gemu.service.library;

import me.shazxrin.gemu.exception.NotFoundException;
import me.shazxrin.gemu.exception.StateException;
import me.shazxrin.gemu.exception.ValidationException;
import me.shazxrin.gemu.model.Game;
import me.shazxrin.gemu.model.Platform;
import me.shazxrin.gemu.model.LibraryGame;
import me.shazxrin.gemu.model.LibraryGameOwnership;
import me.shazxrin.gemu.model.LibraryGameStatus;
import me.shazxrin.gemu.repository.GameRepository;
import me.shazxrin.gemu.repository.LibraryGameRepository;
import me.shazxrin.gemu.repository.PlatformRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MainLibraryGameService implements LibraryGameService {
    private final LibraryGameRepository libraryGameRepository;
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;

    public MainLibraryGameService(
            LibraryGameRepository libraryGameRepository,
            GameRepository gameRepository,
            PlatformRepository platformRepository
    ) {
        this.libraryGameRepository = libraryGameRepository;
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
    }

    @Override
    public void addGameToLibrary(
            String gameId,
            String platformId,
            LibraryGameStatus status,
            LibraryGameOwnership ownership,
            int hoursPlayed,
            int progress
    ) throws ValidationException, StateException {
        boolean isLibraryGameExists = libraryGameRepository.existsByGameId(gameId);
        if (isLibraryGameExists) {
            throw new StateException("Library game already exists.");
        }

        Optional<Game> gameOpt = gameRepository.findById(gameId);
        Game game = gameOpt.orElseThrow(() -> new ValidationException("Game does not exist."));

        Optional<Platform> platformOpt = platformRepository.findById(platformId);
        Platform platform = platformOpt.orElseThrow(() -> new ValidationException("Platform does not exist."));

        LibraryGame libraryGameToAdd = new LibraryGame(
               game,
               platform,
               status,
               ownership,
               hoursPlayed,
               progress
        );
        libraryGameRepository.save(libraryGameToAdd);
    }

    @Override
    public Page<LibraryGame> getAllGamesInLibrary(int page) {
        return libraryGameRepository.findAllByIdNotNull(PageRequest.of(page, 10));
    }

    @Override
    public Page<LibraryGame> getAllGamesInLibraryByPlatformId(String platformId, int page) {
        return libraryGameRepository.findAllByPlatformId(platformId, PageRequest.of(page, 10));
    }

    @Override
    public Optional<LibraryGame> getGameInLibrary(String gameId) {
        return libraryGameRepository.findByGameId(gameId);
    }

    @Override
    public void updateGameInLibrary(
            String gameId,
            String platformId,
            LibraryGameStatus status,
            LibraryGameOwnership ownership,
            int hoursPlayed,
            int progress
    ) throws NotFoundException, ValidationException {
        Optional<LibraryGame> libraryGameOpt = libraryGameRepository.findById(gameId);
        LibraryGame libraryGame = libraryGameOpt.orElseThrow(() -> new NotFoundException("Library game not found."));

        if (!libraryGame.getGame().getId().equals(platformId)) {
            Optional<Platform> platformOpt = platformRepository.findById(platformId);
            Platform platform = platformOpt.orElseThrow(() -> new ValidationException("Platform does not exist."));
            libraryGame.setPlatform(platform);
        }

        libraryGame.setStatus(status);
        libraryGame.setOwnership(ownership);
        libraryGame.setHoursPlayed(hoursPlayed);
        libraryGame.setProgress(progress);

        libraryGameRepository.save(libraryGame);
    }

    @Transactional
    @Override
    public void removeGameFromLibrary(String gameId) throws NotFoundException {
        boolean isLibraryGameExists = libraryGameRepository.existsByGameId(gameId);
        if (!isLibraryGameExists) {
            throw new NotFoundException("Library game does not exist.");
        }

        libraryGameRepository.deleteByGameId(gameId);
    }
}
