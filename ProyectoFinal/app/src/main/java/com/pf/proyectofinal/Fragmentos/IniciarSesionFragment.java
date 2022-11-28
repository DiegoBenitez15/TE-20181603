package com.pf.proyectofinal.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pf.proyectofinal.Actividades.MainActivity;
import com.pf.proyectofinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IniciarSesionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IniciarSesionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public IniciarSesionFragment() {
        // Required empty public constructor
    }

    public static IniciarSesionFragment newInstance() {
        IniciarSesionFragment fragment = new IniciarSesionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_iniciar_sesion2, container, false);
    }
}