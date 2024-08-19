package com.example.advancedboard.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "CreatedBy")

})
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article; // 게시글 (id)

    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 내용

    @Builder
    private Comment(Article article, String content) {
        this.assignArticle(article);
        this.content = content;
    }

    public static Comment buildComment(Article article, String content) {
        return Comment.builder()
                .article(article)
                .content(content)
                .build();
    }

    // Comment N <-> 1 Article
    // 양방향 연관관계 편의 메서드
    public void assignArticle(Article article) {
        if (this.getArticle() != null) {
            this.article.getComments().remove(this);
        }

        this.article = article;

        if (!article.getComments().contains(this)) {
            article.addComment(this);
        }
    }
}
