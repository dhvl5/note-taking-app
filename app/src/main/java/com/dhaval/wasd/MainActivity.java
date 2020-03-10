package com.dhaval.wasd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    static long backPressed;

    EditText titleEditText, descEditText;
    FloatingActionButton floatingActionButton;

    RelativeLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    MaterialButton createNoteBtn;

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteList;
    MaterialButton getSelectedBtn;

    View dimBackgroundView;

    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;

        dimBackgroundView = findViewById(R.id.dimBackground);
        recyclerView = findViewById(R.id.rv);
        getSelectedBtn = findViewById(R.id.getSelectedBtn);
        lottieAnimationView = findViewById(R.id.lottie);
        bottomSheet = findViewById(R.id.bottom_sheet);
        createNoteBtn = findViewById(R.id.createNoteBtn);
        titleEditText = findViewById(R.id.titleNoteEditText);
        descEditText = findViewById(R.id.descNoteEditText);
        floatingActionButton = findViewById(R.id.floating_btn);

        dimBackgroundView.setVisibility(View.GONE);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);

        floatingActionButton.setImageResource(R.drawable.ic_add_black);

        if(inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0))
            titleEditText.clearFocus();
        if(inputMethodManager.hideSoftInputFromWindow(descEditText.getWindowToken(), 0))
            descEditText.clearFocus();

        ClearFocus(titleEditText, inputMethodManager);
        ClearFocus(descEditText, inputMethodManager);

        //region getSelectedBtn click listener
        getSelectedBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(noteAdapter.getSelected().size() > 0)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < noteAdapter.getSelected().size(); i++)
                    {
                        stringBuilder.append(noteAdapter.getSelected().get(i).getNoteTitle());
                        stringBuilder.append("\n");
                    }
                    ShowToast(stringBuilder.toString().trim());
                }
                else
                    ShowToast("No Selection!!!");
            }
        });
        //endregion

        //region floatingActionButton click listener
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(noteAdapter.getSelected().size() > 0)
                {
                    for(int i = 0; i < noteAdapter.getSelected().size(); i++)
                    {
                        noteList.remove(noteAdapter.getSelected().get(i));
                        noteAdapter.notifyItemRemoved(i);
                    }
                    noteAdapter.notifyDataSetChanged();
                    return;
                }

                HideFloatingButton();
                dimBackgroundView.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimBackgroundView, "Alpha", .7f);
                objectAnimator.setDuration(1000);
                objectAnimator.start();
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
        //endregion

        //region createNoteBtn click listener
        createNoteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(titleEditText.getText().toString().matches("") && descEditText.getText().toString().matches(""))
                {
                    ShowToast("Empty note discarded!");
                }
                else
                {
                    Note a = new Note(titleEditText.getText().toString(), descEditText.getText().toString());
                    noteList.add(a);
                    noteAdapter.notifyDataSetChanged();

                    int centerX = lottieAnimationView.getWidth() / 2;
                    int centerY = lottieAnimationView.getHeight() / 2;

                    float radius = (float) Math.hypot(centerX, centerY);

                    Animator animator = ViewAnimationUtils.createCircularReveal(lottieAnimationView, centerX, centerY, radius, 0);
                    animator.setDuration(1000);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();

                    inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0);
                    titleEditText.clearFocus();
                    titleEditText.getText().clear();

                    inputMethodManager.hideSoftInputFromWindow(descEditText.getWindowToken(), 0);
                    descEditText.clearFocus();
                    descEditText.getText().clear();
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                RevealFloatingButton();
            }
        });
        //endregion

        //region noteAdapter single click listener
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(int position, View view, View v, NoteAdapter.MyViewHolder holder)
            {
                if(noteAdapter.getSelected().size() > 0)
                {
                    noteList.get(position).setChecked(!noteList.get(position).isChecked());
                    if(noteList.get(position).isChecked())
                        holder.noteCard.setCardBackgroundColor(Color.BLACK);
                    else
                        holder.noteCard.setCardBackgroundColor(Color.BLUE);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                intent.putExtra("edit_note", noteList.get(position).getNote());
                intent.putExtra("edit_title", noteList.get(position).getNoteTitle());
                Pair<View, String> pair1 = new Pair<>(view, ViewCompat.getTransitionName(view));
                Pair<View, String> pair2 = new Pair<>(v, ViewCompat.getTransitionName(v));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2);
                startActivity(intent, options.toBundle());
            }
        });
        //endregion

        //region noteAdapter long click listener
        noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener()
        {
            @Override
            public void OnLongClick(int position, NoteAdapter.MyViewHolder holder)
            {
                floatingActionButton.setRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
                floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                floatingActionButton.setImageResource(R.drawable.ic_delete_white);
                floatingActionButton.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                noteList.get(position).setChecked(!noteList.get(position).isChecked());
                if(noteList.get(position).isChecked())
                    holder.noteCard.setCardBackgroundColor(Color.BLACK);
                else
                    holder.noteCard.setCardBackgroundColor(Color.BLUE);
            }
        });
        //endregion

        //region dimBackgroundView click listener
        dimBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        //endregion

        //region bottomSheetBehavior callback
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                if(newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    if(floatingActionButton.getVisibility() != View.VISIBLE)
                        RevealFloatingButton();
                    dimBackgroundView.setVisibility(View.GONE);
                    dimBackgroundView.setAlpha(0);

                    if(inputMethodManager.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0))
                        titleEditText.clearFocus();
                    if(inputMethodManager.hideSoftInputFromWindow(descEditText.getWindowToken(), 0))
                        descEditText.clearFocus();
                }
                /*else if(newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        });
        //endregion
    }

    private void RevealFloatingButton()
    {
        int centerX = floatingActionButton.getWidth() / 2;
        int centerY = floatingActionButton.getHeight() / 2;

        float radius = (float) Math.hypot(centerX, centerY);

        Animator animator = ViewAnimationUtils.createCircularReveal(floatingActionButton, centerX, centerY, 0, radius);
        animator.setDuration(500);
        floatingActionButton.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void HideFloatingButton()
    {
        int centerX = floatingActionButton.getWidth() / 2;
        int centerY = floatingActionButton.getHeight() / 2;

        float radius = (float) Math.hypot(centerX, centerY);

        Animator animator = ViewAnimationUtils.createCircularReveal(floatingActionButton, centerX, centerY, radius, 0);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    private void ClearFocus(final EditText editText, final InputMethodManager inputMethodManager)
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                }
            }
        });
    }

    private void ShowToast(String msg)
    {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }

    @Override
    public void onBackPressed()
    {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
        {
            if(backPressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
            {
                ShowToast("Press again to exit!!!");
                backPressed = System.currentTimeMillis();
            }
        }
    }
}