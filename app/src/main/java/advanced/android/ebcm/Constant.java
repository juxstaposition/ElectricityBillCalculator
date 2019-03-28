package advanced.android.ebcm;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Constant {
    public static final String FAVOURITE_DEVICE = "FAVOURITE_DEVICE";
    public static final String PROFILE_DEVICE = "PROFILE_DEVICE";

    public void startAnimation(View view, int animationId, Context context){

        Animation animation= AnimationUtils.loadAnimation(context,animationId);
        view.startAnimation(animation);

    }

}
