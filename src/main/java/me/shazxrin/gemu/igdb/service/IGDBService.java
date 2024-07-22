package me.shazxrin.gemu.igdb.service;

import me.shazxrin.gemu.igdb.dto.IGDBGameCountDto;
import me.shazxrin.gemu.igdb.dto.IGDBGameDto;
import me.shazxrin.gemu.igdb.dto.IGDBPlatformDto;
import me.shazxrin.gemu.igdb.exception.IGDBException;

public interface IGDBService {
    IGDBPlatformDto[] getPlatformsByName(String name) throws IGDBException;

    IGDBGameCountDto getGameCountByPlatformId(long platformId) throws IGDBException;

    IGDBGameDto[] getGamesByPlatformId(long platformId, long limit, long offset) throws IGDBException;
}
