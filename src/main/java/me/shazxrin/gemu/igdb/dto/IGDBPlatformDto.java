package me.shazxrin.gemu.igdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IGDBPlatformDto(
        @JsonProperty("id") long id
) { }
