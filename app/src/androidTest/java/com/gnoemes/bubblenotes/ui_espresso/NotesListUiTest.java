package com.gnoemes.bubblenotes.ui_espresso;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.gnoemes.bubblenotes.App;
import com.gnoemes.bubblenotes.R;
import com.gnoemes.bubblenotes.instructions.HiddenLoadNoteProgressViewInstruction;
import com.gnoemes.bubblenotes.instructions.HiddenLoadNotesProgressViewInstruction;
import com.gnoemes.bubblenotes.instructions.ListNotesRecyclerViewSizeOver0Instruction;
import com.gnoemes.bubblenotes.instructions.ListNotes_CheckListIsEmpty;
import com.gnoemes.bubblenotes.ui.main.MainDrawerActivity;
import com.gnoemes.bubblenotes.utils.MyMatches;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.gnoemes.bubblenotes.R.id.prioritySpinner;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by kenji1947 on 03.10.2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotesListUiTest {
    @Rule
    public ActivityTestRule<MainDrawerActivity> mainDrawerActivityActivityTestRule
            = new ActivityTestRule<MainDrawerActivity>(MainDrawerActivity.class);

    @Before
    public void before() {
        clearDb();
    }

    @After
    public void after() {

    }

    private void clearDb() {
        ((App) (mainDrawerActivityActivityTestRule.getActivity().getApplication()))
                .clearAllEntities();
    }

    @Test
    public void addNewNote_ShouldAddNoteToList() throws Exception{
        String name = "New note 1";
        boolean complete = true;
        String description = "Description 1";
        String lowPriority = "Low";

        String newName = "New";
        boolean newComplete = false;
        String newDescription = "Description new";
        String newPiority = "High";

        int commentsCount = 3;

        //ADD

        //wait for first load
        ConditionWatcher.waitForCondition(new HiddenLoadNotesProgressViewInstruction());

        //check if empty adapter showing
        onView(withId(R.id.emptyAdapterTextView)).check(matches(isDisplayed()));

        //open NoteDetailActivity
        onView(withId(R.id.addNoteFab)).perform(click());

        //fill note fields
        onView(withId(R.id.nameEditText)).perform(typeText(name));
        onView(withId(R.id.completeCheckBox)).perform(click());
        onView(withId(R.id.descriptionEditText)).perform(typeText(description));
        Espresso.closeSoftKeyboard();
        onView(withId(prioritySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(lowPriority))).perform(click());
        onView(withId(prioritySpinner)).check(matches(withSpinnerText(containsString(lowPriority))));

        //click save button
        onView(withId(R.id.saveNoteFab)).perform(click());

        //wait for saving
        ConditionWatcher.waitForCondition(new ListNotesRecyclerViewSizeOver0Instruction());

        //check empty adapter not showing
        onView(withId(R.id.emptyAdapterTextView)).check(matches(not(isDisplayed())));

        //check list element
        onView(withId(R.id.listRecyclerView))
                .perform(RecyclerViewActions
                        .scrollToHolder(MyMatches.findHolderNotesList(complete, lowPriority, name, 0)));

        //click on created note in NotesList
        onView(withId(R.id.listRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //wait for note loading
        ConditionWatcher.waitForCondition(new HiddenLoadNoteProgressViewInstruction());

        //check note fields
        onView(withId(R.id.nameEditText)).check(matches(withText(name)));
        onView(withId(R.id.descriptionEditText)).check(matches(withText(description)));
        onView(withId(R.id.completeCheckBox)).check(matches(isChecked()));
        onView(withId(prioritySpinner)).check(matches(withSpinnerText(containsString(lowPriority))));

        //UPDATE

        //update note fields
        onView(withId(R.id.nameEditText)).perform(clearText());
        onView(withId(R.id.nameEditText)).perform(typeText(newName));
        onView(withId(R.id.completeCheckBox)).perform(click());
        onView(withId(R.id.descriptionEditText)).perform(clearText());
        onView(withId(R.id.descriptionEditText)).perform(typeText(newDescription));
        Espresso.closeSoftKeyboard();
        onView(withId(prioritySpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(newPiority))).perform(click());
        onView(withId(prioritySpinner)).check(matches(withSpinnerText(containsString(newPiority))));

        //add 3 comments
        for (int i = 0; i <3; i++) {
            onView(withId(R.id.addCommentButton)).perform(click());
        }

        //click save button
        onView(withId(R.id.saveNoteFab)).perform(click());

        //wait for saving
        ConditionWatcher.waitForCondition(new ListNotesRecyclerViewSizeOver0Instruction());

        //check list element
        onView(withId(R.id.listRecyclerView))
                .perform(RecyclerViewActions
                        .scrollToHolder(MyMatches.findHolderNotesList(newComplete, newPiority, newName, commentsCount)));

        //click on created note in NotesList
        onView(withId(R.id.listRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //wait for note loading
        ConditionWatcher.waitForCondition(new HiddenLoadNoteProgressViewInstruction());

        //check note fields
        onView(withId(R.id.nameEditText)).check(matches(withText(newName)));
        onView(withId(R.id.descriptionEditText)).check(matches(withText(newDescription)));
        onView(withId(R.id.completeCheckBox)).check(matches(isNotChecked()));
        onView(withId(prioritySpinner)).check(matches(withSpinnerText(containsString(newPiority))));

        //DELETE

        //press delete button
        onView(withId(R.id.menu_delete)).perform(click());

        //wait for deleting
        ConditionWatcher.waitForCondition(new ListNotes_CheckListIsEmpty());

        //check empty adapter is showing
        onView(withId(R.id.emptyAdapterTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteNewNote_ShouldAddNoteToList() throws Exception{

    }
}
