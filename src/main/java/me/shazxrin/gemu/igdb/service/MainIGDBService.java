package me.shazxrin.gemu.igdb.service;

import me.shazxrin.gemu.igdb.dto.IGDBGameCountDto;
import me.shazxrin.gemu.igdb.dto.IGDBGameDto;
import me.shazxrin.gemu.igdb.dto.IGDBPlatformDto;
import me.shazxrin.gemu.igdb.dto.TwitchTokenDto;
import me.shazxrin.gemu.igdb.exception.IGDBException;
import me.shazxrin.gemu.igdb.entity.IGDBInfo;
import me.shazxrin.gemu.igdb.repository.IGDBInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class MainIGDBService implements IGDBService {
    private final String clientId;
    private final String clientSecret;
    private final RestClient twitchRestClient;
    private final IGDBInfoRepository igdbInfoRepository;

    private IGDBInfo igdbInfo;
    private RestClient igdbRestClient;

    @Autowired
    public MainIGDBService(
        @Value("${gemu.igdb.client-id}") String clientId,
        @Value("${gemu.igdb.client-secret}") String clientSecret,
        IGDBInfoRepository igdbInfoRepository
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.twitchRestClient = RestClient.builder()
            .baseUrl("https://id.twitch.tv")
            .build();
        this.igdbInfoRepository = igdbInfoRepository;
    }

    private RestClient buildIGDBRestClient() {
        return RestClient.builder()
            .baseUrl("https://api.igdb.com/v4")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add("Content-Type", "application/json");
                httpHeaders.add("Client-ID", this.clientId);
                httpHeaders.add("Authorization", "Bearer " + this.igdbInfo.getAccessToken());
            })
            .build();
    }

    private void prepareIGDBRestClient() throws IGDBException {
        // Fetch IGDB info for the first time
        if (this.igdbInfo == null) {
            this.igdbInfo = igdbInfoRepository.findFirstByIdNotNull();
        }

        // Check if access token has expired
        if (this.igdbInfo != null && LocalDateTime.now().isBefore(this.igdbInfo.getExpiryDateTime())) {
            if (this.igdbRestClient == null) {
                this.igdbRestClient = buildIGDBRestClient();
            }
            return;
        }

        // Refetch access token
        ResponseEntity<TwitchTokenDto> twitchTokenResponse = this.twitchRestClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/oauth2/token")
                .queryParam("client_id", this.clientId)
                .queryParam("client_secret", this.clientSecret)
                .queryParam("grant_type", "client_credentials")
                .build()
            )
            .retrieve()
            .toEntity(TwitchTokenDto.class);
        if (twitchTokenResponse.getStatusCode() != HttpStatus.OK) {
            throw new IGDBException("Unable to fetch access token");
        }

        TwitchTokenDto twitchTokenDto = twitchTokenResponse.getBody();

        if (this.igdbInfo == null) {
            this.igdbInfo = new IGDBInfo(
                LocalDateTime.now().plusSeconds(twitchTokenDto.expiresIn()),
                twitchTokenDto.accessToken()
            );
        } else {
            this.igdbInfo.setExpiryDateTime(LocalDateTime.now().plusSeconds(twitchTokenDto.expiresIn()));
            this.igdbInfo.setAccessToken(twitchTokenDto.accessToken());
        }
        igdbInfoRepository.save(this.igdbInfo);

        this.igdbRestClient = buildIGDBRestClient();
    }

    private <T> T call(String endpoint, String query, Class<T> entityClass) throws IGDBException {
        prepareIGDBRestClient();

        ResponseEntity<T> responseEntity = this.igdbRestClient.post()
            .uri(endpoint)
            .contentType(MediaType.TEXT_PLAIN)
            .body(query)
            .retrieve()
            .toEntity(entityClass);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IGDBException("Error on calling " + endpoint);
        }

        return responseEntity.getBody();
    }

    @Override
    public IGDBPlatformDto[] getPlatformsByName(String name) throws IGDBException {
        return call(
            "/platforms",
            String.format("fields id; where name = \"%s\";", name),
            IGDBPlatformDto[].class
        );
    }

    @Override
    public IGDBGameCountDto getGameCountByPlatformId(long platformId) throws IGDBException {
        return call(
            "/games/count",
            String.format("where platforms = (%d);", platformId),
            IGDBGameCountDto.class
        );
    }

    @Override
    public IGDBGameDto[] getGamesByPlatformId(long platformId, long limit, long offset) throws IGDBException {
        return call(
            "/games",
            String.format(
                "fields id, name, summary, storyline; where platforms = (%d); limit %d; offset %d;",
                platformId,
                limit,
                offset
            ),
            IGDBGameDto[].class
        );
    }
}
