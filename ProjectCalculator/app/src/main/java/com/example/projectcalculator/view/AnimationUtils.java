package com.example.projectcalculator.view;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AnimationUtils {
    public static void animateIndicator(View indicatorBar, Button targetButton, Resources resources) {
        targetButton.post(() -> {
            ViewGroup.LayoutParams layoutParams = indicatorBar.getLayoutParams();
            layoutParams.width = targetButton.getWidth();
            indicatorBar.setLayoutParams(layoutParams);

            int[] buttonLocation = new int[2];
            targetButton.getLocationOnScreen(buttonLocation);

            int buttonLeft = buttonLocation[0];
            int screenWidth = resources.getDisplayMetrics().widthPixels;
            float targetX = buttonLeft - (float) (screenWidth - targetButton.getWidth()) / 2;

            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(indicatorBar, "translationX", targetX);
            moveAnimator.setDuration(200);
            moveAnimator.start();
        });
    }
}

