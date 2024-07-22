package me.shazxrin.gemu.dto.library;

import me.shazxrin.gemu.entity.LibraryGameOwnership;
import me.shazxrin.gemu.entity.LibraryGameStatus;

/**
 * DTO for {@link me.shazxrin.gemu.entity.LibraryGame}
 */
public record ListLibraryGameDto(
    ListLibraryGameGameDto game,
    ListLibraryGamePlatformDto platform,
    LibraryGameStatus status,
    LibraryGameOwnership ownership,
    Integer hoursPlayed,
    Integer progress
) {
}
