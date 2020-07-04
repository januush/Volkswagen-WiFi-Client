package com.example.kfgclient;

import android.content.res.Resources;

/**
 * Helper class for Android app specific things, like calculating pixels to given dp based on display metrics.
 * https://stackoverflow.com/questions/3166501/getting-the-screen-density-programmatically-in-android
 */
public class AndroidViewHelper {
    /**
     * Calculates DP to PX.
     *
     * @param dp        value to be calculated to pixel
     * @param resources application resources
     * @return pixels based on the given DP value
     */
    public static int dpToPx(int dp, Resources resources) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}