package com.dating.flirtify.Fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.flirtify.Adapters.CardStackAdapter;
import com.dating.flirtify.Adapters.InterestAdapter;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Interfaces.OnCardActionListener;
import com.dating.flirtify.Models.Requests.LikeRequest;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.R;
import com.dating.flirtify.Services.SessionManager;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewFragment extends Fragment implements OnCardActionListener {
    private ApiService apiService;
    private View view;
    private CardView cvPreview, detailInformationWrapper;
    private LinearLayout bottomCardWrapper;
    private FloatingActionButton fabLike, fabDislike;
    private BottomNavigationView footerWrapper;
    private ConstraintLayout mConstraintLayout;
    private RecyclerView rvInterests;
    private ArrayList<UserResponse> itemsResponse;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    private TextView tvBio;
    private String accessToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);
        initViews(view);
        handlerEvent();
        getUsers();
        return view;
    }

    @Override
    public void onArrowUpClick(UserResponse user) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        HeaderFragment headerFragment = (HeaderFragment) fragmentManager.findFragmentById(R.id.fragment_header);

        footerWrapper.setVisibility(View.GONE);

        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomCardWrapper, "translationY", 0, 125f);
        animator.setDuration(300);
        animator.start();
        headerFragment.setHeaderType(2);
        headerFragment.setCurrentUser(user);

        SlideUp();
        ConstraintSet mConstraintSet = new ConstraintSet();
        mConstraintSet.clone(mConstraintLayout);
        mConstraintSet.constrainPercentHeight(R.id.content_wrapper, 0.92f);
        mConstraintSet.applyTo(mConstraintLayout);

        tvBio.setText(user.getBio());
        List<String> listInterests = user.getInterests();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        rvInterests.setLayoutManager(layoutManager);

        InterestAdapter interestAdapter = new InterestAdapter(listInterests);
        rvInterests.setAdapter(interestAdapter);
        manager.setCanScrollHorizontal(false);
        manager.setCanScrollVertical(false);
    }

    private void getUsers() {
        Call<ArrayList<UserResponse>> call = apiService.getUserToConnect(accessToken);
        call.enqueue(new Callback<ArrayList<UserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<UserResponse>> call, Response<ArrayList<UserResponse>> response) {
                if (response.isSuccessful()) {
                    ArrayList<UserResponse> arrMatcher = response.body();
                    if (arrMatcher != null) {
                        itemsResponse.addAll(arrMatcher);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Matches", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserResponse>> call, Throwable t) {
                Log.e("Matches", "Request failed: " + t.getMessage());
            }
        });
        Log.i("Matches", "Get users: " + itemsResponse.toString());
    }

    private void initViews(View view) {
        cvPreview = view.findViewById(R.id.cv_preview);
        detailInformationWrapper = view.findViewById(R.id.detail_information_wrapper);
        bottomCardWrapper = requireActivity().findViewById(R.id.bottom_card_wrapper);
        footerWrapper = requireActivity().findViewById(R.id.footer_wrapper);
        mConstraintLayout = requireActivity().findViewById(R.id.main);
        cardStackView = view.findViewById(R.id.card_stack_view);
        fabLike = requireActivity().findViewById(R.id.fab_like);
        fabDislike = requireActivity().findViewById(R.id.fab_dislike);
        tvBio = view.findViewById(R.id.tv_bio);
        rvInterests = view.findViewById(R.id.rv_interests);

        accessToken = SessionManager.getToken();
        apiService = ApiClient.getClient();
        itemsResponse = new ArrayList<>();
        adapter = new CardStackAdapter(itemsResponse, this);
        manager = new CardStackLayoutManager(view.getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                CardStackAdapter.ViewHolder currentViewHolder = getCurrentViewHolder();
                if (direction == Direction.Right) {
                    currentViewHolder.tvLike.setVisibility(View.VISIBLE);
                    currentViewHolder.tvDislike.setVisibility(View.GONE);
                } else if (direction == Direction.Left) {
                    currentViewHolder.tvLike.setVisibility(View.GONE);
                    currentViewHolder.tvDislike.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {
                if (direction == Direction.Right) {
                    UserResponse currentUser = itemsResponse.get(manager.getTopPosition() - 1);
                    LikeRequest likeRequest = new LikeRequest();
                    likeRequest.setId(currentUser.getId());

                    String accessToken = SessionManager.getToken();
                    Call<Void> call = apiService.storeUserLike(accessToken, likeRequest);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                if (response.code() == 200) {
                                    Dialog dialogMatched = new Dialog(getContext());
                                    dialogMatched.setContentView(R.layout.dialog_matched);
                                    dialogMatched.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    TextView tvName = dialogMatched.findViewById(R.id.dialog_tv_name);
                                    tvName.setText("Bạn đã tương hợp với " + currentUser.getFullname());
                                    dialogMatched.show();
                                } else if (response.code() == 201) {
                                    Toast.makeText(getContext(), "Like", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("API Error", "Request failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("API Error", "Request failed: " + t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCardRewound() {
                // Xử lý khi thẻ quay lại
            }

            @Override
            public void onCardCanceled() {
                CardStackAdapter.ViewHolder currentViewHolder = getCurrentViewHolder();
                currentViewHolder.tvLike.setVisibility(View.GONE);
                currentViewHolder.tvDislike.setVisibility(View.GONE);
            }

            @Override
            public void onCardAppeared(View view, int position) {
                // Xử lý khi thẻ xuất hiện
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                // Xử lý khi thẻ biến mất
            }
        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(12.0f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(45.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);

        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private void handlerEvent() {
        fabLike.setOnClickListener(v -> swipeCard(Direction.Right));
        fabDislike.setOnClickListener(v -> swipeCard(Direction.Left));
    }

    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().setDirection(direction).setDuration(Duration.Normal.duration).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    public void SlideUp() {
        this.DisabledRadius();
        this.VisibleInfo();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);
    }

    public void SlideDown() {
        this.EnableRadius();
        this.InvisibleInfo();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);
    }

    public void DisabledRadius() {
        if (cvPreview != null) {
            cvPreview.setRadius(0);
        } else {
            Log.w("PreviewFragment", "CardView chưa được khởi tạo.");
        }
    }

    public void EnableRadius() {
        if (cvPreview != null) {
            cvPreview.setRadius(24);
        } else {
            Log.w("PreviewFragment", "CardView chưa được khởi tạo.");
        }
    }

    public void VisibleInfo() {
        detailInformationWrapper.setVisibility(View.VISIBLE);
    }

    public void InvisibleInfo() {
        detailInformationWrapper.setVisibility(View.GONE);
    }

    public CardStackLayoutManager getManager() {
        return manager;
    }

    public CardStackAdapter getCardStackAdapter() {
        return adapter;
    }

    public CardStackAdapter.ViewHolder getCurrentViewHolder() {
        int currentPosition = manager.getTopPosition();
        RecyclerView.ViewHolder viewHolder = cardStackView.findViewHolderForAdapterPosition(currentPosition);
        if (viewHolder instanceof CardStackAdapter.ViewHolder) {
            return (CardStackAdapter.ViewHolder) viewHolder;
        }
        return null;
    }
}