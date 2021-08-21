package com.luckyzz.guilds.guild;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ExecutingConditionSet {

    private final Set<ExecutingLevelCondition<?>> conditions;

    ExecutingConditionSet(@NotNull final Set<ExecutingLevelCondition<?>> conditions) {
        this.conditions = conditions;
    }

    public void forEach(@NotNull final Consumer<ExecutingLevelCondition<?>> consumer) {
        conditions.forEach(consumer);
    }

    public @NotNull Stream<ExecutingLevelCondition<?>> stream() {
        return conditions.stream();
    }

    public boolean isExecuted() {
        return stream().allMatch(ExecutingLevelCondition::isExecuted);
    }

    @Override
    public @NotNull String toString() {
        return "ExecutingConditionSet{" +
                "conditions=" + conditions +
                '}';
    }

}
