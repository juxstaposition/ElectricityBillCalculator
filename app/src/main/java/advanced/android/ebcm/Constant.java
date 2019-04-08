package advanced.android.ebcm;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Constant {
    public static final String FAVOURITE_DEVICE = "FAVOURITE_DEVICE";
    public static final String PROFILE_DEVICES = "PROFILE_DEVICES";
    public static final String EDIT_PROFILE = "EDIT_PROFILE";
    public static final String DELETE_PROFILE = "DELETE_PROFILE";
    public static final String UPDATE_PROFILE = "UPDATE_PROFILE";


    public static final int UPDATE_PROFILE_ACTIVITY_REQ_CODE = 100;
    public static final int CREATE_PROFILE_ACTIVITY_REQ_CODE = 200;
    public static final int EDIT_PROFILE_ACTIVITY_REQ_CODE = 300;
    public static final int DELETE_PROFILE_ACTIVITY_REQ_CODE = 400;

    public static final int ADD_DEVICE_TO_PROFILE_REQ_CODE = 500;


    public void startAnimation(View view, int animationId, Context context){

        Animation animation= AnimationUtils.loadAnimation(context,animationId);
        view.startAnimation(animation);

    }

}
