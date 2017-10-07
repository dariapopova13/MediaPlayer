package com.example.mediaplayer.data.enums;

import com.example.mediaplayer.R;

/**
 * Created by Daria Popova on 07.10.17.
 */

public enum RepeatTypeEnum {

    NO_REPEAT(0, R.drawable.ic_repeat_black_24dp, android.R.color.darker_gray, R.string.no_repeat_message),
    REPEAT_ONE(1, R.drawable.ic_repeat_one_black_24dp, R.color.colorPrimary, R.string.repeat_one_message),
    REPEAT_ALL(2, R.drawable.ic_repeat_black_24dp, R.color.colorPrimary, R.string.repeat_all_message);

    private int code;
    private int iconId;
    private int colorId;
    private int messageId;

    RepeatTypeEnum(int code, int iconId, int colorId, int messageId) {

        this.code = code;
        this.iconId = iconId;
        this.colorId = colorId;
        this.messageId = messageId;
    }

    public static RepeatTypeEnum getNext(RepeatTypeEnum repeatTypeEnum) {
        if (repeatTypeEnum.getCode() == 0)
            return REPEAT_ONE;
        else if (repeatTypeEnum.getCode() == 1)
            return REPEAT_ALL;
        else if (repeatTypeEnum.getCode() == 2)
            return NO_REPEAT;
        return null;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getCode() {
        return code;
    }

    public int getIconId() {
        return iconId;
    }

    public int getColorId() {
        return colorId;
    }
}
