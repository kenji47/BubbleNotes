package com.gnoemes.bubblenotes.instructions;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.azimolabs.conditionwatcher.Instruction;
import com.gnoemes.bubblenotes.App;
import com.gnoemes.bubblenotes.R;



/**
 * Created by F1sherKK on 15/04/16.
 */
public class HiddenLoadNotesProgressViewInstruction extends Instruction {
    @Override
    public String getDescription() {
        return "Load notes progress should";
    }

    @Override
    public boolean checkCondition() {
        Activity activity = ((App)
                InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();
        if (activity == null) return false;

        ProgressBar progressBar = activity.findViewById(R.id.loadNotesProgressBar);
        return progressBar.getVisibility() == View.INVISIBLE;
    }
}
