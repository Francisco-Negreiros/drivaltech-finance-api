package com.drivaltech.finance.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public class PaginationResponse<T> {

    private final List<T> data;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNext;

    public PaginationResponse(Page<T> pageObj) {
        this.data = pageObj.getContent();
        this.page = pageObj.getNumber();
        this.size = pageObj.getSize();
        this.totalElements = pageObj.getTotalElements();
        this.totalPages = pageObj.getTotalPages();
        this.hasNext = pageObj.hasNext();
    }

    public List<T> getData() { return data; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public boolean isHasNext() { return hasNext; }
}
