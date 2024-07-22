package me.shazxrin.gemu.igdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TwitchTokenDto(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") Long expiresIn
) {
}
