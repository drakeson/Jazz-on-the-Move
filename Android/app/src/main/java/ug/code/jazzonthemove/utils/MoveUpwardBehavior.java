package ug.code.jazzonthemove.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

import ug.code.jazzonthemove.R;

/**
 * Created by Ajay on 07-03-2017.
 */

//Use this class only if you want toslide up the FAB whenever the seekbar appear, if u don't want this funcionality delete this file
public class MoveUpwardBehavior extends CoordinatorLayout.Behavior<View> {
    public MoveUpwardBehavior() {
        super();
    }

    public MoveUpwardBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        if (child.getVisibility() == View.VISIBLE) {
            if (child.getId() == R.id. fabLayout2) {
                translationY += -child.getContext().getResources().getDimension(R.dimen.standard_100);
            } else if (child.getId() == R.id. fabLayout3) {
                translationY += -child.getContext().getResources().getDimension(R.dimen.standard_145);
            }
        }
        ViewCompat.setTranslationY(child, translationY);
        return true;
    }

    //you need this when you swipe the snackbar(thanx to ubuntudroid's comment)
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        if (child.getVisibility() == View.VISIBLE) {
            if (child.getId() == R.id.fabLayout2) {
                translationY += -child.getContext().getResources().getDimension(R.dimen.standard_100);
            } else if (child.getId() == R.id.fabLayout3) {
                translationY += -child.getContext().getResources().getDimension(R.dimen.standard_145);
            }
        }
        ViewCompat.animate(child).translationY(translationY).start();
    }
}