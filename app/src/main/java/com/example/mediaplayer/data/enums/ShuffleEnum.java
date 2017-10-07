package com.example.mediaplayer.data.enums;

import com.example.mediaplayer.R;

/**
 * Created by Daria Popova on 07.10.17.
 */

public enum ShuffleEnum {

    SHUFFLE(true, R.color.colorPrimary, R.string.shuffe_is_on_message),
    NO_SHUFFLE(false, android.R.color.darker_gray, R.string.shuffe_is_off_message);
    private int colorId;
    private boolean isShuffled;
    private int messageId;

    ShuffleEnum(boolean isShuffled, int colorId, int messageId) {
        this.colorId = colorId;
        this.isShuffled = isShuffled;
        this.messageId = messageId;

    }

    public static ShuffleEnum getOpposite(ShuffleEnum shuffleEnum) {
        if (shuffleEnum.isShuffled())
            return NO_SHUFFLE;
        else return SHUFFLE;
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {

        this.colorId = colorId;
    }
}
