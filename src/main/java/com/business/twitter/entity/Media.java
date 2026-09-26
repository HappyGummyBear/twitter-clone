package com.business.twitter.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="media_id")
    private Long id;

    @Column(name="media_key")
    private String mediaKey;

    @Column(name="pending_media_key")
    private String pendingMediaKey;

    @Column(name="content_type", nullable=false)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tweet_id", nullable=false)
    private Tweet tweet;

    public Media() {

    }

    public enum MediaType {
        IMAGE,
        VIDEO;

        public static MediaType fromContentType(String contentType) {
            if (contentType.startsWith("image/")) {
                return IMAGE;
            }

            if (contentType.startsWith("video/")) {
                return VIDEO;
            }

            throw new IllegalArgumentException(
                    "Unsupported content type: " + contentType
            );
        }
    }
}
