package me.shazxrin.gemu.igdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record IGDBGameDto(
    @JsonProperty("id") long id,
    @JsonProperty("name") String name,
    @JsonProperty("summary") Optional<String> summary,
    @JsonProperty("storyline") Optional<String> storyline
) {
}
