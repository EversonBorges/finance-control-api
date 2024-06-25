package com.manager.control.finance.dtos;

public record CategoryRequestDTO(
        Integer id,
        String description,
        Character type
) {
}
