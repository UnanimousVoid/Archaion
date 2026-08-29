package com.ratrod.archaion.misc;

import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

public final class DynamicWeightedList<E> {
    private final List<Entry<E>> entries;

    private DynamicWeightedList(List<Entry<E>> entries) {
        this.entries = List.copyOf(entries);
    }

    public static <E> DynamicWeightedList<E> of() {
        return new DynamicWeightedList<>(List.of());
    }

    public static <E> DynamicWeightedList<E> of(List<Entry<E>> entries) {
        return new DynamicWeightedList<>(entries);
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public Optional<E> getRandom(RandomSource random) {
        int totalWeight = 0;

        for (Entry<E> entry : this.entries) {
            totalWeight += Math.max(0, entry.weight().applyAsInt(entry.value()));
        }

        if (totalWeight <= 0) {
            return Optional.empty();
        }

        int selection = random.nextInt(totalWeight);

        for (Entry<E> entry : this.entries) {
            selection -= Math.max(0, entry.weight().applyAsInt(entry.value()));

            if (selection < 0) {
                return Optional.of(entry.value());
            }
        }

        return Optional.empty();
    }

    public E getRandomOrThrow(RandomSource random) {
        return this.getRandom(random)
                .orElseThrow(() -> new IllegalStateException("Dynamic weighted list has no entries with positive weight"));
    }

    public List<Entry<E>> unwrap() {
        return this.entries;
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public boolean contains(E value) {
        return this.entries.stream()
                .anyMatch(entry -> entry.value().equals(value));
    }

    public record Entry<E>(E value, ToIntFunction<E> weight) {
    }

    public static class Builder<E> {
        private final List<Entry<E>> entries = new ArrayList<>();

        public Builder<E> add(E value, int weight) {
            return this.add(value, ignored -> weight);
        }

        public Builder<E> add(E value, ToIntFunction<E> weight) {
            this.entries.add(new Entry<>(value, weight));
            return this;
        }

        public Builder<E> add(Entry<E> entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder<E> remove(E value) {
            this.entries.removeIf(entry -> entry.value().equals(value));
            return this;
        }

        public Builder<E> remove(Entry<E> entry) {
            this.entries.remove(entry);
            return this;
        }

        public Builder<E> removeIf(java.util.function.Predicate<Entry<E>> filter) {
            this.entries.removeIf(filter);
            return this;
        }

        public List<Entry<E>> getList() {
            return List.copyOf(this.entries);
        }

        public DynamicWeightedList<E> build() {
            return new DynamicWeightedList<>(this.entries);
        }
    }
}