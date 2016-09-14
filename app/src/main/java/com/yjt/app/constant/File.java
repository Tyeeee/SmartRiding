package com.yjt.app.constant;

public enum File {

    FILE_NAME("profile"),
    TEMP_FOLDER_NAME("temp"),
    FILE_AUTHORITY(".content.fileprovider"),
    PICTURE_URI("picture_uri"),
    PICTURE_PATH("picture_path"),
    EMP_ID("emp_id"),
    EMP_ACCOUNT("emp_account"),
    ACCOUNT_NAME("account_name"),
    LOGIN_TICKET("login_ticket"),
    TICKET_TAG("ticket_tag"),
    HYCACHIER("hycachier");

    private String mContent;

    File(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
