package lib.kalu.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class SlideInBottomAnimation : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f))
    }
}
