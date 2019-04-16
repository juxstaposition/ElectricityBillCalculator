package advanced.android.ebcm;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Constant {
    public static final String PROFILE_DEVICES = "PROFILE_DEVICES";
    public static final String NEW_PROFILE = "NEW_PROFILE";
    public static final String EDIT_PROFILE = "EDIT_PROFILE";
    public static final String DELETE_PROFILE = "DELETE_PROFILE";


    public static final String NEW_DEVICE = "NEW_DEVICE";
    public static final String EDIT_DEVICE = "EDIT_DEVICE";
    public static final String DELETE_DEVICE = "DELETE_DEVICE";


    public static final int UPDATE_PROFILE_ACTIVITY_REQ_CODE = 100;
    public static final int CREATE_PROFILE_ACTIVITY_REQ_CODE = 200;
    public static final int EDIT_PROFILE_ACTIVITY_REQ_CODE = 300;
    public static final int DELETE_PROFILE_ACTIVITY_REQ_CODE = 400;

    public static final int ADD_DEVICE_TO_PROFILE_REQ_CODE = 500;
    public static final int EDIT_DEVICE_REQ_CODE = 600;
    public static final int DELETE_DEVICE_REQ_CODE = 700;
    public static final int PICK_AN_ITEM_REQ_CODE = 800;

    public static final int VIEW_RESULTS = 900;


    public void startAnimation(View view, int animationId, Context context){

        Animation animation= AnimationUtils.loadAnimation(context,animationId);
        view.startAnimation(animation);

    }

}
