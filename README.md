# youtube-extractor-android

Usage

put This inside your project level Gradle.
allprojects {
		repositories {
    
			maven { url 'https://jitpack.io' }
      
	
  And put this inside your app level gradle.
  
	dependencies {
		implementation 'com.github.User:Repo:Tag'
	}
  
  # How to Use.
  
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

  
