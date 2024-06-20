package in.softment.exampracticeadmin.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import in.softment.exampracticeadmin.Fragment.Admin.AddFragment;
import in.softment.exampracticeadmin.Fragment.Admin.AdminSettingsFragment;
import in.softment.exampracticeadmin.Fragment.Admin.NotificationFragment;
import in.softment.exampracticeadmin.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import in.softment.exampracticeadmin.Util.NonSwipeAbleViewPager;

public class AdminDashboardActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] tabIcons = {
          //  R.drawable.ic_baseline_notifications_active_24,
            R.drawable.ic_round_add_circle_24
           // R.drawable.ic_round_settings_24,

    };

    private static String[] titles = {

            "Add",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.setCurrentItem(1);
    }
    public void setPagerItem(int item){
        viewPager.setCurrentItem(item);
    }

    private void setupTabIcons() {

     //   tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
   //     tabLayout.getTabAt(2).setIcon(tabIcons[2]);




    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //viewPagerAdapter.addFrag(new NotificationFragment());
        viewPagerAdapter.addFrag(new AddFragment());
        //viewPagerAdapter.addFrag(new AdminSettingsFragment());
        viewPager.setAdapter(viewPagerAdapter);

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull @NotNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }



        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
