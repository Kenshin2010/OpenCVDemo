package vn.manroid.opencv.utils;

import vn.manroid.opencv.R;

public interface AnimationType {
    int[] ANIM_BOTTOM_TO_TOP = {R.anim.slide_in_bottom, R.anim.slide_out_bottom};
    int[] ANIM_TOP_TO_BOTTOM = {R.anim.slide_in_top, R.anim.slide_out_top};
    int[] ANIM_RIGHT_TO_LEFT = {R.anim.slide_in_right, R.anim.slide_out_left};
    int[] ANIM_LEFT_TO_RIGHT = {R.anim.slide_in_left, R.anim.slide_out_right};
    int[] ANIM_FADE_IN_FADE_OUT = {R.anim.fade_in, R.anim.fade_out};
}
