package com.business.twitter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tweets")
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    private Long id;

    @NonNull
    @Column(name = "content", nullable = false, length = 280)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NonNull
    private User user;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(
            mappedBy = "tweet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Media> media = new ArrayList<>();

    public Tweet() {

    }

    public Tweet(@NonNull String content, @NonNull User user, @NonNull Instant createdAt) {
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }
}
