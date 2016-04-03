package fleamarket.neworin.com.fleamarket.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fleamarket.neworin.com.fleamarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistrictFragment extends Fragment {


    public DistrictFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_district, container, false);
    }

}
