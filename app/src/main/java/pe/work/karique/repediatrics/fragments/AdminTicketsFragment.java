package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.work.karique.repediatrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminTicketsFragment extends Fragment {


    public AdminTicketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_tickets, container, false);
    }

}
