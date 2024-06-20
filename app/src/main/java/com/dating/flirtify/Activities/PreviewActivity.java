package com.dating.flirtify.Activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dating.flirtify.Adapters.CardStackAdapter;
import com.dating.flirtify.Fragments.HeaderFragment;
import com.dating.flirtify.Fragments.MatcherFragment;
import com.dating.flirtify.Fragments.PreviewFragment;
import com.dating.flirtify.Fragments.AccountFragment;
import com.dating.flirtify.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

public class PreviewActivity extends AppCompatActivity {

    private BottomNavigationView footerWrapper;
    private ImageButton ibArrowDown;
    private ConstraintLayout headerWrapper, mConstraintLayout;
    private LinearLayout bottomCardWrapper, layoutSpacer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeView();
        handlerEvent();
    }

    private void initializeView() {
        ibArrowDown = findViewById(R.id.ib_arrow_down);
        footerWrapper = findViewById(R.id.footer_wrapper);
        headerWrapper = findViewById(R.id.header_wrapper);
        mConstraintLayout = findViewById(R.id.main);
        bottomCardWrapper = findViewById(R.id.bottom_card_wrapper);
        layoutSpacer = findViewById(R.id.ll_spacer);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void handlerEvent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PreviewFragment previewFragment = new PreviewFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, previewFragment).commit();
        HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);
        headerFragment.setHeaderType(1);


        ibArrowDown.setOnClickListener(v -> {
            footerWrapper.setVisibility(View.VISIBLE);
            headerWrapper.setVisibility(View.VISIBLE);

            CardStackLayoutManager manager = previewFragment.getManager();
            manager.setCanScrollHorizontal(true);
            manager.setCanScrollVertical(true);

            CardStackAdapter adapter = previewFragment.getCardStackAdapter();
            CardStackAdapter.ViewHolder currentViewHolder = previewFragment.getCurrentViewHolder();
            if (currentViewHolder != null) {
                currentViewHolder.ibArrowUp.setVisibility(View.VISIBLE);
                currentViewHolder.infoWrapper.setVisibility(View.VISIBLE);
                adapter.notifyItemChanged(currentViewHolder.getAdapterPosition());
            }

            ObjectAnimator animator = ObjectAnimator.ofFloat(bottomCardWrapper, "translationY", 125f, 0f);
            animator.setDuration(300);
            animator.start();
            headerFragment.setHeaderType(1);

            previewFragment.SlideDown();
            ConstraintSet mConstraintSet = new ConstraintSet();
            mConstraintSet.clone(mConstraintLayout);
            mConstraintSet.constrainPercentHeight(R.id.content_wrapper, 0.78f);
            mConstraintSet.applyTo(mConstraintLayout);
        });

        footerWrapper.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_preview) {
                selectedFragment = new PreviewFragment();
                headerFragment.setHeaderType(1);
                bottomCardWrapper.setVisibility(View.VISIBLE);
                layoutSpacer.setVisibility(View.VISIBLE);
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new MatcherFragment();
                headerFragment.setHeaderType(3);
                bottomCardWrapper.setVisibility(View.GONE);
                layoutSpacer.setVisibility(View.GONE);
            } else if (itemId == R.id.nav_account) {
                selectedFragment = new AccountFragment();
                headerFragment.setHeaderType(3);
                bottomCardWrapper.setVisibility(View.GONE);
                layoutSpacer.setVisibility(View.GONE);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });
    }
}