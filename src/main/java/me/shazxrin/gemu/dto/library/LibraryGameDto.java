package me.shazxrin.gemu.dto.library;

import me.shazxrin.gemu.model.LibraryGameOwnership;
import me.shazxrin.gemu.model.LibraryGameStatus;

/**
 * DTO for {@link me.shazxrin.gemu.model.LibraryGame}
 */
public record LibraryGameDto(
        LibraryGamePlatformDto platform,
        LibraryGameStatus status,
        LibraryGameOwnership ownership,
        Integer hoursPlayed,
        Integer progress
) { }
