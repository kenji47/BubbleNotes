package com.gnoemes.bubblenotes.utils;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gnoemes.bubblenotes.R;
import com.gnoemes.bubblenotes.ui.notes_list.NotesListAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import timber.log.Timber;

/**
 * Created by kenji1947 on 06.10.2017.
 */

public class MyMatches {
    public static Matcher<RecyclerView.ViewHolder> findHolderNotesList(boolean isComplete,
                                                                           String priority,
                                                                           String name,
                                                                           int commentsCount) {
        return new BoundedMatcher<RecyclerView.ViewHolder, NotesListAdapter.NoteHolder>(
                NotesListAdapter.NoteHolder.class
        ) {
            @Override
            public void describeTo(Description description) {
                description.appendText("ViewHolder found with text: " + name);
            }

            @Override
            protected boolean matchesSafely(NotesListAdapter.NoteHolder item) {
                Timber.d(
                        "MATCHER ViewHolder.getAdapterPosition:" + item.getAdapterPosition()
                                + " ViewHolder.getLayoutPosition:" + item.getLayoutPosition()
                );

                TextView nameTextView = item.view.findViewById(R.id.nameTextView);
                CheckBox isCompleteCheckBox = item.view.findViewById(R.id.completeCheckBox);
                TextView priorityTextView = item.view.findViewById(R.id.priorityTextView);
                TextView commentsNumber = item.view.findViewById(R.id.commentsNumberTextview);

                if (nameTextView == null || isCompleteCheckBox == null || priorityTextView == null || commentsNumber == null) {
                    return false;
                }
                boolean isChecked = isCompleteCheckBox.isChecked();


                return ((nameTextView.getText().toString().equals(name)) &&
                        (isChecked == isComplete) &&
                        (priorityTextView.getText().toString().equals(priority)) &&
                        (Integer.parseInt(commentsNumber.getText().toString()) == commentsCount)
                        );
            }
        };
    }
}
