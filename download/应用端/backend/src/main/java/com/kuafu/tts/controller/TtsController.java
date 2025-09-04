package com.kuafu.tts.controller;

import com.kuafu.tts.dto.TtsRequest;
import com.kuafu.tts.service.TtsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/tts")
@Slf4j
public class TtsController {

    private final TtsService ttsService;

    public TtsController(TtsService ttsService) {
        this.ttsService = ttsService;
    }

    @PostMapping(value = "/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> stream(@RequestBody TtsRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getSegments())) {
            return ResponseEntity.badRequest().build();
        }
        log.info("start tts stream, segments:{}", request.getSegments().size());
        StreamingResponseBody body = outputStream -> {
            long start = System.currentTimeMillis();
            int index = 0;
            for (String segment : request.getSegments()) {
                try {
                    byte[] audio = ttsService.synthesize(segment, request.getVoice(), request.getSpeed(), request.getEmotion());
                    outputStream.write(audio);
                    outputStream.flush();
                    log.debug("segment {} written, bytes:{}", index++, audio.length);
                } catch (Exception e) {
                    log.error("streaming tts failed", e);
                    break;
                }
            }
            log.info("tts stream finished cost:{}ms", System.currentTimeMillis() - start);
        };
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(body);
    }
}