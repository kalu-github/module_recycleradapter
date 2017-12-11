package lib.kalu.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class ScaleInAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_SCALE_FROM) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
        return arrayOf<Animator>(scaleX, scaleY)
    }

    companion object {

        private val DEFAULT_SCALE_FROM = .5f
    }
}
