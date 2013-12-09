package com.grootcode.base.util;

import android.net.Uri;

public class ContractUtils {
    public static String CONTENT_AUTHORITY;
    public static Uri BASE_CONTENT_URI;

    public static void init(String contentAuthority, Uri baseContentUri) {
        CONTENT_AUTHORITY = contentAuthority;
        BASE_CONTENT_URI = baseContentUri;
    }
}
