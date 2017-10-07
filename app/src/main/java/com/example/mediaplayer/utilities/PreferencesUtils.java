package com.example.mediaplayer.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mediaplayer.data.enums.RepeatTypeEnum;
import com.example.mediaplayer.data.enums.ShuffleEnum;

/**
 * Created by Daria Popova on 07.10.17.
 */

public final class PreferencesUtils {

    private static final String PREF_SHAFFLED_KEY = "shaffeled";
    private static final String PREF_REPEAT_KEY = "repeat";


    public static void setIsRepeated(Context context, RepeatTypeEnum repeatTypeEnum) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(PREF_REPEAT_KEY, repeatTypeEnum.getCode());
        editor.apply();
    }

    public static int getRepeatType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_REPEAT_KEY, RepeatTypeEnum.NO_REPEAT.getCode());
    }

    public static RepeatTypeEnum getRepeatTypeEnum(Context context) {
        if (isNoRepeat(context))
            return RepeatTypeEnum.NO_REPEAT;
        else if (isRepeatOne(context))
            return RepeatTypeEnum.REPEAT_ONE;
        else if (isRepeatAll(context))
            return RepeatTypeEnum.REPEAT_ALL;
        return null;
    }

    public static boolean isNoRepeat(Context context) {
        return RepeatTypeEnum.NO_REPEAT.getCode() ==
                getRepeatType(context);
    }

    public static boolean isRepeatOne(Context context) {
        return RepeatTypeEnum.REPEAT_ONE.getCode() ==
                getRepeatType(context);
    }

    public static boolean isRepeatAll(Context context) {
        return RepeatTypeEnum.REPEAT_ALL.getCode() ==
                getRepeatType(context);
    }

    public static void setShuffle(Context context, ShuffleEnum value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(PREF_SHAFFLED_KEY, value.isShuffled());
        editor.apply();
    }

    public static ShuffleEnum getShuffleEnum(Context context) {
        if (isShuffled(context))
            return ShuffleEnum.SHUFFLE;
        else return ShuffleEnum.NO_SHUFFLE;
    }

    public static boolean isShuffled(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_SHAFFLED_KEY, false);
    }

}
