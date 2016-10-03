package com.yjt.app.constant;

public enum File {

    FILE_NAME("profile"),
    NAVIGATION_DEMONSTRATION_PATTERN("navigation_demonstration_pattern"),
    TEMP_FOLDER_NAME("temp"),
    FILE_AUTHORITY(".content.fileprovider"),
    PICTURE_URI("picture_uri"),
    PICTURE_PATH("picture_path");

    private String mContent;

    File(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
