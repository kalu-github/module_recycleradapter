package lib.kalu.recyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public final class SlideInRightAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {

        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0);
        return new Animator[]{animator};
    }
}
