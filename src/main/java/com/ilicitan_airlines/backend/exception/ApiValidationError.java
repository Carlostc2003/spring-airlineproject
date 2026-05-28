package com.ilicitan_airlines.backend.exception;

public record ApiValidationError(String field, String message) {}
