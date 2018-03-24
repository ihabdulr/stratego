package game;

import client.Global;

/**
 * Created by Alek on 3/23/2018.
 */
public class Animation {

    private static long animationEndTime = 0;
    private static boolean showAnimation = false;
    private static final int ANIMATION_TIME = 2000;
    private static int animationIndex;
    private static int x, y;


    public static void playAnimation(int dx, int dy, int index) {
        x = dx;
        y = dy;
        animationIndex = index;
        animationEndTime = System.currentTimeMillis() + ANIMATION_TIME;
        showAnimation = true;
    }

    public static boolean shouldAnimate() {
        if (!showAnimation) {

            return false;
        }
        showAnimation = animationEndTime - System.currentTimeMillis() > 0;
        return showAnimation;
    }

    public static int getAnimationIndex() {
        return animationIndex;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

}
