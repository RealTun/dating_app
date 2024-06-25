package com.dating.flirtify.Fragments;

import static com.dating.flirtify.Services.DistanceCalculator.calculateDistanceForAddress;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dating.flirtify.Adapters.CardStackAdapter;
import com.dating.flirtify.Adapters.InterestAdapter;
import com.dating.flirtify.Api.ApiClient;
import com.dating.flirtify.Api.ApiService;
import com.dating.flirtify.Listeners.OnCardActionListener;
import com.dating.flirtify.Models.Requests.LikeRequest;
import com.dating.flirtify.Models.Requests.UserLocationRequest;
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
    private final ArrayList<UserResponse> itemsResponse = new ArrayList<>();
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private CardStackLayoutManager manager;
    private TextView tvBio, tvRelationShip;
    private String accessToken;

    public interface UserResponseCallback {
        void onUserResponseReceived(UserResponse userResponse);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preview, container, false);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String address = SessionManager.getLocationUser();
                initViews(view);
                initCardStackView(address);
                handlerEvent();
                getUsers(address);
//                updateUserLocation(address);
            }
        }, 1000);

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
        tvRelationShip.setText(user.getRelationship());
        List<String> listInterests = user.getInterests();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        rvInterests.setLayoutManager(layoutManager);

        InterestAdapter interestAdapter = new InterestAdapter(listInterests);
        rvInterests.setAdapter(interestAdapter);
        manager.setCanScrollHorizontal(false);
        manager.setCanScrollVertical(false);
    }

    private void getUsers(String currentUserLocation) {
        getCurrentUser(currentUser -> {
            if (currentUser != null) {
                Call<ArrayList<UserResponse>> call = apiService.getUserToConnect(accessToken);
                call.enqueue(new Callback<ArrayList<UserResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserResponse>> call, Response<ArrayList<UserResponse>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<UserResponse> arrMatcher = response.body();
                            if (arrMatcher != null) {
                                int minAge = currentUser.getMin_age();
                                int maxAge = currentUser.getMax_age();
                                int maxDistance = currentUser.getMax_distance();
                                for (UserResponse item : arrMatcher) {
                                    String matcherLocation = item.getLocation();
                                    double distance = calculateDistanceForAddress(getActivity(), currentUserLocation, matcherLocation) / 1000;
                                    if (distance < maxDistance && item.getAge() > minAge && item.getAge() < maxAge) {
                                        itemsResponse.add(item);
                                    }
                                }
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
            } else {
                // Xử lý trường hợp không lấy được currentUser
                Log.e("getUsers", "Failed to get current user");
            }
        });
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
        tvRelationShip = view.findViewById(R.id.tv_relationship);
        rvInterests = view.findViewById(R.id.rv_interests);

        apiService = ApiClient.getClient();
        accessToken = SessionManager.getToken();
    }

    private void initCardStackView(String location) {
        adapter = new CardStackAdapter(getActivity(), itemsResponse, this, location);
        manager = new CardStackLayoutManager(getActivity(), new CardStackListener() {
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
                handleCardSwipe(direction);
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

    private void handleCardSwipe(Direction direction) {
        if (direction == Direction.Right) {
            UserResponse currentUser = itemsResponse.get(manager.getTopPosition() - 1);
            LikeRequest likeRequest = new LikeRequest(currentUser.getId(), 1);

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
        } else if (direction == Direction.Left) {
            UserResponse currentUser = itemsResponse.get(manager.getTopPosition() - 1);
            LikeRequest likeRequest = new LikeRequest(currentUser.getId(), 0);

            Call<Void> call = apiService.storeUserLike(accessToken, likeRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
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

    private void handlerEvent() {
        fabLike.setOnClickListener(v -> swipeCard(Direction.Left));
        fabDislike.setOnClickListener(v -> swipeCard(Direction.Right));
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

    private void updateUserLocation(String location) {
        UserLocationRequest userLocation = new UserLocationRequest(location);
        Call<Void> call = apiService.updateUserLocation(accessToken, userLocation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
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

    // Sửa đổi getCurrentUser để sử dụng callback
    // Sửa đổi getCurrentUser để sử dụng callback
    private void getCurrentUser(UserResponseCallback callback) {
        Call<UserResponse> call = apiService.getUser(accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    callback.onUserResponseReceived(response.body());
                } else {
                    Log.e("getUser", response.message());
                    callback.onUserResponseReceived(null); // Gọi callback với null khi thất bại
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
                callback.onUserResponseReceived(null); // Gọi callback với null khi thất bại
            }
        });
    }
}