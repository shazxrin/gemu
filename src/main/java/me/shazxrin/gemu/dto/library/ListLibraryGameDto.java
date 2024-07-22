package me.shazxrin.gemu.dto.library;

import me.shazxrin.gemu.model.LibraryGameOwnership;
import me.shazxrin.gemu.model.LibraryGameStatus;

/**
 * DTO for {@link me.shazxrin.gemu.model.LibraryGame}
 */
public record ListLibraryGameDto(
        ListLibraryGameGameDto game,
        ListLibraryGamePlatformDto platform,
        LibraryGameStatus status,
        LibraryGameOwnership ownership,
        Integer hoursPlayed,
        Integer progress
) { }
