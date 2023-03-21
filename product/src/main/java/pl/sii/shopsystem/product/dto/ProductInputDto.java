package pl.sii.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record ProductInputDto(
        String type,
        String title,
        String publishingHouse,
        String price) {
}
