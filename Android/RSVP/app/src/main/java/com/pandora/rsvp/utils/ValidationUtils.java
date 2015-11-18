package com.pandora.rsvp.utils;

import android.text.TextUtils;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class ValidationUtils {
    public static boolean isValidEmail(CharSequence email) {
        return email != null && !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
