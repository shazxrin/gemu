package me.shazxrin.gemu.igdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IGDBGameCountDto(
    @JsonProperty("count") int count
) {
}
