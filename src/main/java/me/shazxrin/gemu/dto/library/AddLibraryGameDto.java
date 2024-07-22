package me.shazxrin.gemu.dto.library;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import me.shazxrin.gemu.entity.LibraryGameOwnership;
import me.shazxrin.gemu.entity.LibraryGameStatus;

/**
 * DTO for {@link me.shazxrin.gemu.entity.LibraryGame}
 */
public record AddLibraryGameDto(
    String platformId,
    @NotNull LibraryGameStatus status,
    @NotNull LibraryGameOwnership ownership,
    @Min(0) Integer hoursPlayed,
    @Min(0) @Max(100) Integer progress
) {
}
