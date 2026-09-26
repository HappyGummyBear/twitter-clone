package com.business.twitter.service;

import com.business.twitter.dto.CompletedMediaUploadRequest;
import com.business.twitter.dto.MediaResponse;
import com.business.twitter.dto.MediaUploadUrlRequest;
import com.business.twitter.dto.MediaUploadUrlResponse;
import com.business.twitter.entity.Media;
import com.business.twitter.entity.Tweet;
import com.business.twitter.repository.MediaRepository;
import com.business.twitter.repository.TweetRepository;
import com.business.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {

    private final TweetRepository tweetRepository;
    private final MediaRepository mediaRepository;
    private final S3StorageService s3StorageService;

    public MediaService(TweetRepository tweetRepository, MediaRepository mediaRepository, UserRepository userRepository, S3StorageService s3StorageService) {
        this.tweetRepository = tweetRepository;
        this.mediaRepository = mediaRepository;
        this.s3StorageService = s3StorageService;
    }

    public MediaUploadUrlResponse generateMediaUploadUrl(MediaUploadUrlRequest request) {
        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found."));

        String contentType = request.contentType();
        Media.MediaType mediaType = Media.MediaType.fromContentType(contentType);

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/bmp" -> ".bmp";
            case "video/mp4" -> ".mp4";
            case "video/webm" -> ".webm";
            case "video/quicktime" -> ".mov";
            default -> throw new IllegalArgumentException(
                    "Unsupported content type: " + contentType
            );
        };

        String prefix = mediaType == Media.MediaType.IMAGE
                ? "images/"
                : "videos/";

        String mediaKey = prefix + UUID.randomUUID() + extension;

        Media media = new Media();
        media.setTweet(tweet);
        media.setMediaType(mediaType);
        media.setPendingMediaKey(mediaKey);
        media.setContentType(contentType);

        Media savedMedia = mediaRepository.save(media);

        URL uploadUrl = s3StorageService.generatePresignedUploadUrl(mediaKey);

        return new MediaUploadUrlResponse(
                savedMedia.getId(),
                mediaKey,
                uploadUrl.toString()
        );
    }

    public void completedMediaUpload(CompletedMediaUploadRequest completedMediaUploadRequest) {
        Media media = mediaRepository.findById(completedMediaUploadRequest.mediaId())
                .orElseThrow(() -> new RuntimeException("Media not found."));
        String mediaKey = media.getPendingMediaKey();

        if (mediaKey == null) {
            throw new IllegalArgumentException("No pending media upload.");
        }
        if (!s3StorageService.objectExistsInS3(mediaKey)) {
            throw new IllegalArgumentException("Media was not uploaded to S3.");
        }

        media.setMediaKey(mediaKey);
        media.setPendingMediaKey(null);

        mediaRepository.save(media);
    }

    public List<MediaResponse> getMediaForTweet(Tweet tweet) {
        return mediaRepository.findByTweet(tweet)
                .stream()
                .filter(media -> media.getMediaKey() != null)
                .map(media -> {
                    String url = s3StorageService.generatePresignedGetUrl(media.getMediaKey())
                            .toString();

                    return new MediaResponse(
                            media.getId(),
                            media.getMediaType().name(),
                            media.getContentType(),
                            url
                    );
                }).toList();
    }

    public void deleteTweetMedia(Tweet tweet) {
        mediaRepository.findByTweet(tweet)
                .parallelStream()
                .filter(media -> media.getMediaKey() != null)
                .forEach(media -> s3StorageService.deleteObjectFromS3(media.getMediaKey()));
    }
}
