package com.github.nebula.graphics.data;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * @param dataType
 * @param name
 * @author Anton Schoenfeld
 * @since 22.03.2024
 */
public record UniformAttribute(@NonNull GLDataType dataType, @NonNull String name) {}
