package com.business.twitter.controller;

import com.business.twitter.dto.CompletedMediaUploadRequest;
import com.business.twitter.dto.MediaUploadUrlRequest;
import com.business.twitter.dto.MediaUploadUrlResponse;
import com.business.twitter.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload-url")
    public ResponseEntity<MediaUploadUrlResponse> generateUploadUrl(
            @Valid @RequestBody MediaUploadUrlRequest mediaUploadUrlRequest) {
        MediaUploadUrlResponse response = mediaService.generateMediaUploadUrl(mediaUploadUrlRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete")
    public ResponseEntity<Void> completeVideoUpload(
            @Valid @RequestBody CompletedMediaUploadRequest completedMediaUploadRequest) {

        mediaService.completedMediaUpload(completedMediaUploadRequest);

        return ResponseEntity.ok().build();
    }
}
