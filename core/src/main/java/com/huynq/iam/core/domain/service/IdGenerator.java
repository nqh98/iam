package com.huynq.iam.core.domain.service;

/**
 * Pluggable strategy for generating unique identifiers (e.g., Snowflake).
 */
@FunctionalInterface
public interface IdGenerator {

    long nextId();
}

