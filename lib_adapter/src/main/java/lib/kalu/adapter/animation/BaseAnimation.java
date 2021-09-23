package lib.kalu.adapter.animation;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;

public interface BaseAnimation {

    int ALPHAIN = 0x00000001;
    int SCALEIN = 0x00000002;
    int SLIDEIN_BOTTOM = 0x00000003;
    int SLIDEIN_LEFT = 0x00000004;
    int SLIDEIN_RIGHT = 0x00000005;

    Animator[] getAnimators(@NonNull View view);
}
