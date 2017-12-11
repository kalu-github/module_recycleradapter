package lib.kalu.adapter.animation

import android.animation.Animator
import android.view.View

interface BaseAnimation {

    fun getAnimators(view: View): Array<Animator>

    companion object {

        const val ALPHAIN = 0x00000001
        const val SCALEIN = 0x00000002
        const val SLIDEIN_BOTTOM = 0x00000003
        const val SLIDEIN_LEFT = 0x00000004
        const val SLIDEIN_RIGHT = 0x00000005
    }
}
