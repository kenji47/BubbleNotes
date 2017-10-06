package com.gnoemes.bubblenotes.instructions;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.RecyclerView;

import com.azimolabs.conditionwatcher.Instruction;
import com.gnoemes.bubblenotes.R;
import com.gnoemes.bubblenotes.utils.App;


/**
 * Created by F1sherKK on 15/04/16.
 */
public class ShowListRecyclerViewInstruction extends Instruction {
    @Override
    public String getDescription() {
        return "FAB add Note should shown";
    }

    @Override
    public boolean checkCondition() {
        Activity activity = ((App)
                InstrumentationRegistry.getTargetContext().getApplicationContext()).getCurrentActivity();
        if (activity == null) return false;

        RecyclerView recyclerView = activity.findViewById(R.id.listRecyclerView);
        return recyclerView != null;
    }
}
