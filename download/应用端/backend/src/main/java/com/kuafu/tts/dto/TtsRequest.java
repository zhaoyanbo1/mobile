package com.kuafu.tts.dto;

import lombok.Data;

import java.util.List;

@Data
public class TtsRequest {
    private List<String> segments;
    private String voice;
    private Double speed;
    private String emotion;
}