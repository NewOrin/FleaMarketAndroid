package fleamarket.neworin.com.fleamarket.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fleamarket.neworin.com.fleamarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FocusFragment extends Fragment {

    private View view;

    public FocusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_focus, container, false);
    }

}
