package pl.sii.validation.demo.kafka;

import lombok.Builder;

@Builder
public record TestModel(String name, int number) {
}
