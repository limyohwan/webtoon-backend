package com.yohwan.webtoon.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(String message) {
        this(null, message);
    }

    public ApiResponse(T data) {
        this(data, null);
    }

    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
