package com.ateliopti.lapplicationpti.futuremind.recyclerviewfastscroll.viewprovider;

/**
 * Created by Michal on 11/08/16.
 */
public class DefaultBubbleBehavior implements com.futuremind.recyclerviewfastscroll.viewprovider.ViewBehavior {

    private final VisibilityAnimationManager animationManager;

    public DefaultBubbleBehavior(VisibilityAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void onHandleGrabbed() {
        animationManager.show();
    }

    @Override
    public void onHandleReleased() {
        animationManager.hide();
    }

    @Override
    public void onScrollStarted() {

    }

    @Override
    public void onScrollFinished() {

    }

}
