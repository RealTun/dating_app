// package com.dating.flirtify.Adapters;
//package com.dating.flirtify.Adapter;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//import com.dating.flirtify.UI.Fragment.RegisterSearchOptionsFragment;
//import com.dating.flirtify.UI.Fragment.RegisterStep1Fragment;
//import com.dating.flirtify.UI.Fragment.RegisterStep2Fragment;
//import com.dating.flirtify.UI.Fragment.RegisterStep3Fragment;
//import com.dating.flirtify.UI.Fragment.RegisterStep4Fragment;
//import com.dating.flirtify.UI.Fragment.RegisterStep5Fragment;
//import com.dating.flirtify.UI.Fragment.RegisterWantToSeeFragment;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ViewPagerAdapter extends FragmentStateAdapter {
//
//    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();
//
//    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        Fragment fragment;
//        switch (position) {
//            case 0:
//                fragment = new RegisterStep1Fragment();
//                break;
//            case 1:
//                fragment = new RegisterStep2Fragment();
//                break;
//            case 2:
//                fragment = new RegisterStep3Fragment();
//                break;
//            case 3:
//                fragment = new RegisterStep4Fragment();
//                break;
//            case 4:
//                fragment = new RegisterStep5Fragment();
//                break;
//            case 5:
//                fragment = new RegisterWantToSeeFragment();
//                break;
//            case 6:
//                fragment = new RegisterSearchOptionsFragment();
//                break;
//            default:
//                fragment = new RegisterStep1Fragment();
//                break;
//        }
//        fragmentMap.put(position, fragment);
//        return fragment;
//    }
//
//    @Override
//    public int getItemCount() {
//        return 7; // Số lượng các trang
//    }
//
//    public Fragment getFragment(int position) {
//        return fragmentMap.get(position);
//    }
//}

package com.dating.flirtify.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.dating.flirtify.Fragments.ProcessingFragment;
import com.dating.flirtify.Fragments.RegisterSearchOptionsFragment;
import com.dating.flirtify.Fragments.RegisterStep1Fragment;
import com.dating.flirtify.Fragments.RegisterStep2Fragment;
import com.dating.flirtify.Fragments.RegisterStep3Fragment;
import com.dating.flirtify.Fragments.RegisterStep4Fragment;
import com.dating.flirtify.Fragments.RegisterStep5Fragment;
import com.dating.flirtify.Fragments.RegisterWantToSeeFragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private static final int TOTAL_PAGES = 8;
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        initFragments();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragmentMap.get(position);
        if (fragment == null) {
            fragment = new RegisterStep1Fragment(); // Default fragment
            Log.e("ViewPagerAdapter", "Fragment at position " + position + " is null. Returning default fragment.");
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        Log.d("ViewPagerAdapter", "Total pages: " + TOTAL_PAGES);
        return TOTAL_PAGES;
    }

    public Fragment getFragment(int position) {
        return fragmentMap.get(position);
    }

    private void initFragments() {
        fragmentMap.put(0, new RegisterStep1Fragment());
        fragmentMap.put(1, new RegisterStep2Fragment());
        fragmentMap.put(2, new RegisterStep3Fragment());
        fragmentMap.put(3, new RegisterStep4Fragment());
        fragmentMap.put(4, new RegisterWantToSeeFragment());
        fragmentMap.put(5, new RegisterSearchOptionsFragment());
        fragmentMap.put(6, new RegisterStep5Fragment());
        fragmentMap.put(7, new ProcessingFragment());

        // Log each fragment to check if it's initialized correctly
        for (int i = 0; i < TOTAL_PAGES; i++) {
            Log.d("ViewPagerAdapter", "Fragment at position " + i + ": " + fragmentMap.get(i));
        }
    }
}
