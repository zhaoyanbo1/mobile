package com.kuafu.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simple pagination wrapper for API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private long current;
    private long size;
    private long total;
    private long pages;
    private boolean hasNext;
    private List<T> records;
}