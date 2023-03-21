package kafka.dto;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        LocalDateTime creationTime,
        String title,
        String type,
        String publishingHouse,
        BigDecimal price,
        boolean isDeleted
) {

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("type", type)
                .append("publishingHouse", publishingHouse)
                .append("price", price)
                .append("creationTime", creationTime)
                .append("isDeleted", isDeleted)
                .toString();
    }
}