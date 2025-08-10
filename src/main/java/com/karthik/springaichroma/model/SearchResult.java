package com.karthik.springaichroma.model;

import java.util.Map;

public record SearchResult(String id, String content, double score, Map<String, Object> metadata) {}