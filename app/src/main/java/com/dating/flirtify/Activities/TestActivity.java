package com.dating.flirtify.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dating.flirtify.Adapters.ImagePagerAdapter;
import com.dating.flirtify.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImagePagerAdapter adapter;
    private LinearLayout indicatorLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager = findViewById(R.id.vp_card);
        indicatorLayout = findViewById(R.id.custom_indicator_layout);

        List<String> images = new ArrayList<>();
        images.add("https://scontent.fhan18-1.fna.fbcdn.net/v/t39.30808-6/448220955_421112774228441_1840011718993680812_n.jpg?_nc_cat=1&ccb=1-7&_nc_sid=5f2048&_nc_ohc=OG1m9JE9eP8Q7kNvgECuYsJ&_nc_ht=scontent.fhan18-1.fna&oh=00_AYAda2vSkGAhCB1ZVU2GpwhonTXtdJ5xH-neZD7WtXf47Q&oe=6671C3D6");
        images.add("https://scontent.fhan18-1.fna.fbcdn.net/v/t39.30808-6/448220682_421112720895113_1239173085403670151_n.jpg?_nc_cat=1&ccb=1-7&_nc_sid=5f2048&_nc_ohc=lFAljvKCca8Q7kNvgFwFm_I&_nc_ht=scontent.fhan18-1.fna&oh=00_AYD3wFmiGrHQcLmdb12LRXbeVYPQpYXO2AdYJQOmSO1voQ&oe=6671E494");
        images.add("https://scontent.fhan18-1.fna.fbcdn.net/v/t39.30808-6/448190459_421112760895109_6014066479778337541_n.jpg?_nc_cat=1&ccb=1-7&_nc_sid=5f2048&_nc_ohc=EEbCWXwBo_4Q7kNvgFixg5t&_nc_ht=scontent.fhan18-1.fna&oh=00_AYDv37W_2dSdEIr4QeqGCU86poSpqvwDtVyz-BR4gsgLwA&oe=6671E6A0");
        images.add("https://scontent.fhan18-1.fna.fbcdn.net/v/t39.30808-6/448220641_421112837561768_366775219374721906_n.jpg?_nc_cat=1&ccb=1-7&_nc_sid=5f2048&_nc_ohc=b7ONTAu2x34Q7kNvgFpM-TX&_nc_ht=scontent.fhan18-1.fna&oh=00_AYD6Za_WOmXjjb9U0r7fCxbO1c81FRDljGqAD2HVKaC3ag&oe=6671C88A");
        adapter = new ImagePagerAdapter(this, images);

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        viewPager.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() < v.getWidth() / 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
            return true;
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndicators(position);
            }
        });

        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateIndicators(viewPager.getCurrentItem());
            }
        });
    }

    private void updateIndicators(int position) {
        // Xóa tất cả các chỉ báo hiện có
        indicatorLayout.removeAllViews();

        // Lấy số lượng hình ảnh hiện tại từ adapter của ViewPager2
        int count = adapter.getItemCount();

        // Lấy kích thước hiện tại của ViewPager2
        int viewPagerWidth = viewPager.getWidth();

        // Tính toán kích thước của mỗi chỉ báo
        int indicatorWidth = viewPagerWidth / count;
        int indicatorHeight = 8;

        // Tạo và thiết lập lại các chỉ báo
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicatorWidth, indicatorHeight);
            layoutParams.setMargins(8, 0, 8, 0);
            imageView.setLayoutParams(layoutParams);

            if (i == position) {
                imageView.setImageResource(R.drawable.indicator_active); // Chỉ báo được chọn
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive); // Chỉ báo không được chọn
            }

            indicatorLayout.addView(imageView);
        }
    }
}
