package com.example.advancedboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "CreatedBy")

})
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 내용

    @Setter
    private String hashtag; // 해시태그

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", cascade = CascadeType.REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article buildArticle(String title, String content, String hashtag) {
        return Article.builder()
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .build();
    }

    // Article 1 <-> N Comment
    // 양방향 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);

        if (comment.getArticle() != this) {
            comment.assignArticle(this);
        }
    }
}
