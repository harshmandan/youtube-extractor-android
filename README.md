# youtube-extractor-android

# Usage

Put this inside your project level Gradle:

```
repositories {
	maven { url 'https://jitpack.io' }
}

```	

And put this inside your app level gradle.

```  
dependencies {
	  implementation 'com.github.harshmandan:youtube-extractor-android:1.0.0'
}
	
```

# How to Use:

```
YoutubeExtractor youtubeExtractor = new YoutubeExtractor();
youtubeExtractor.Extract("https://www.youtube.com/watch?v=Z2F67Ji6Jos", "StreamExtractor", new YoutubeExtractorCallback() {
    @Override
    public void onSuccess(List<VideoStream> videoStreamList) {
       List containing youtube data.
    }

    @Override
    public void onError(String errorMessage) {
	runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
		Toast.makeText(MainActivity.this, ""+errorMessage, Toast.LENGTH_SHORT).show();
	    }
	});

    }
});
```
