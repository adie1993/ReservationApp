package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.adie.reservation.activity.PesanTiket;
import app.adie.reservation.R;


public class TabFragment extends BaseFragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);


        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        return x;

    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position){
              case 0 : return PesanTiket.newInstance();
              case 1 : return new HistoryFragment();
              case 2 : return new PromoFragment();
          }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Pesan Tiket";
                case 1 :
                    return "My Ticket";
                case 2 :
                    return "Updates";
            }
                return null;
        }
    }

}
