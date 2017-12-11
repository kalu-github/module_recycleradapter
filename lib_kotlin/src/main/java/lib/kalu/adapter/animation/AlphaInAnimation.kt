package lib.kalu.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class AlphaInAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_ALPHA_FROM) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f))
    }

    companion object {

        private val DEFAULT_ALPHA_FROM = 0f
    }
}
