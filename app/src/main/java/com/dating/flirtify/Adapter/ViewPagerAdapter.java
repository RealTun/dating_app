package com.dating.flirtify.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dating.flirtify.UI.Fragment.RegisterStep1Fragment;
import com.dating.flirtify.UI.Fragment.RegisterStep2Fragment;
import com.dating.flirtify.UI.Fragment.RegisterStep3Fragment;
import com.dating.flirtify.UI.Fragment.RegisterStep4Fragment;
import com.dating.flirtify.UI.Fragment.RegisterStep5Fragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new RegisterStep1Fragment();
                break;
            case 1:
                fragment = new RegisterStep2Fragment();
                break;
            case 2:
                fragment = new RegisterStep3Fragment();
                break;
            case 3:
                fragment = new RegisterStep4Fragment();
                break;
            case 4:
                fragment = new RegisterStep5Fragment();
                break;
            default:
                fragment = new RegisterStep1Fragment();
                break;
        }
        fragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5; // Số lượng các trang
    }

    public Fragment getFragment(int position) {
        return fragmentMap.get(position);
    }
}
