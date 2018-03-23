package game;

/**
 * Created by Alek on 3/23/2018.
 */
public class Animation {

    private static long animationEndTime = 0;
    private static boolean showAnimation = false;
    private static Piece animationPiece;
    private static final int ANIMATION_TIME = 2000;

    public static void playAnimation(Piece piece) {
        animationPiece = piece;
        animationEndTime = System.currentTimeMillis() + ANIMATION_TIME;
        showAnimation = true;
    }

    public static boolean shouldAnimate() {
        if (!showAnimation)
            return false;
        showAnimation = animationEndTime - System.currentTimeMillis() > 0;
        return showAnimation;
    }

    public static Piece getAnimationPiece() {
        return animationPiece;
    }


}
