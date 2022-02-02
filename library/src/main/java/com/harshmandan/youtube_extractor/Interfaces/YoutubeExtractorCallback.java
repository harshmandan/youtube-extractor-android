package com.harshmandan.youtube_extractor.Interfaces;

import com.harshmandan.youtube_extractor.Models.VideoStream;

import java.util.List;

public interface YoutubeExtractorCallback {

    void onSuccess(List<VideoStream> videoStreamList);

    void onError(String errorMessage);

}
