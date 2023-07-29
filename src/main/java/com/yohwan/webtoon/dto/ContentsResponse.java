package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.Contents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentsResponse {
    private Long id;
    private String contentsName;
    private String author;

    public ContentsResponse(Contents contents) {
        this(contents.getId(), contents.getContentsName(), contents.getAuthor());
    }

    public ContentsResponse(Long id, String contentsName, String author) {
        this.id = id;
        this.contentsName = contentsName;
        this.author = author;
    }
}
