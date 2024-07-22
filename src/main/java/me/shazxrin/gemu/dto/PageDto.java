package me.shazxrin.gemu.dto;

import java.util.List;

public record PageDto<T>(
        Integer page,
        Integer totalPages,
        List<T> items
) { }
