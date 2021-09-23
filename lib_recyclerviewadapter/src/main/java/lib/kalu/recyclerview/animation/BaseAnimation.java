package lib.kalu.recyclerview.animation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

public interface BaseAnimation {

    int ALPHAIN = 0x00000001;
    int SCALEIN = 0x00000002;
    int SLIDEIN_BOTTOM = 0x00000003;
    int SLIDEIN_LEFT = 0x00000004;
    int SLIDEIN_RIGHT = 0x00000005;

    default boolean isOnce() {
        return false;
    }

    default long getDuration() {
        return 200;
    }

    default Interpolator getInterpolator() {
        return null;
    }

    default Animator[] getAnimators(@NonNull View view) {
        return null;
    }

    //@Retention表示这个注解保留的范围，SOURCE=注解将被编译器编译的时候丢弃，不在代码运行时存在，这个注解只是希望IDE警告限定值的范围并不需要保留到VM或者运行时
    @Retention(SOURCE)
    //@Target 这个注解需要使用的地方 PARAMETER=注解将被使用到方法的参数中
    @Target({PARAMETER})
    //显式声明被定义的整数值，除了@IntDef还有@LongDef @StringDef等等
    @IntDef(value = {BaseAnimation.ALPHAIN, BaseAnimation.SCALEIN, BaseAnimation.SLIDEIN_BOTTOM, BaseAnimation.SLIDEIN_LEFT, BaseAnimation.SLIDEIN_RIGHT})
    @interface BaseAnimationType {

    }
}
