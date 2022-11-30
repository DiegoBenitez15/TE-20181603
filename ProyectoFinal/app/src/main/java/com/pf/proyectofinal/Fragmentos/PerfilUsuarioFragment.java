package com.pf.proyectofinal.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pf.proyectofinal.Entidades.Usuario;
import com.pf.proyectofinal.Modelos.ProductoViewModel;
import com.pf.proyectofinal.Modelos.UsuarioViewModel;
import com.pf.proyectofinal.R;
import com.pf.proyectofinal.Servicios.FirebaseServicios;
import com.pf.proyectofinal.databinding.FragmentPerfilUsuarioBinding;
import com.pf.proyectofinal.databinding.RepasswordLayoutBinding;

import java.util.UUID;


public class PerfilUsuarioFragment extends Fragment {
    private UsuarioViewModel  usuarioViewModel;
    private FirebaseServicios firebaseServicios;
    private FragmentPerfilUsuarioBinding binding;
    private String usuario;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        firebaseServicios = new FirebaseServicios();



        binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usuario = getActivity().getIntent().getExtras().getString("user");
        usuarioViewModel.getUser(null,usuario).observe(getViewLifecycleOwner(),usuario1 -> {
            binding.usrPerfil.setText(usuario1.getUsuario());
            binding.nmbrPerfil.setText(usuario1.getNombre());
            binding.corPerfil.setText(usuario1.getCorreo());
            binding.cntPerfil.setText(usuario1.getContacto());
            firebaseServicios.downloadImage(usuario1.getUrl_img(),binding.imgPerfil);
        });

        binding.cmbContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = requireActivity().getLayoutInflater();
              View layout =  inflater.inflate(R.layout.repassword_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setView(layout)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String old_pass = ((EditText)layout.findViewById(R.id.password_old)).getText().toString();
                                String new_pass = ((EditText)layout.findViewById(R.id.password_new)).getText().toString();
                                String new_repass = ((EditText)layout.findViewById(R.id.repassword_new)).getText().toString();

                                if(!old_pass.equals("") && !new_pass.equals("") && !new_repass.equals("")) {
                                    usuarioViewModel.getUser(null,usuario).observe(getViewLifecycleOwner(),usuario1 -> {
                                        if(usuario1.getContrasena().equals(old_pass)){
                                            if(!new_pass.equals(old_pass)){
                                                if(new_pass.equals(new_repass)){
                                                    usuario1.setContrasena(new_pass);
                                                    usuarioViewModel.update(usuario1);
                                                    Toast.makeText(getContext(), "Se ha establecido la contraseña adecuada", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getContext(), "La confirmacion no es igual a la contraseña nueva", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getContext(), "Es la misma contraseña establecida", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getContext(), "No es la contraseña establecida", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else{
                                    Toast.makeText(getContext(), "No se pueden dejar los campos vacios", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();
            }
        });

        binding.edtPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("user",getActivity().getIntent().getExtras().getString("user"));
                NavHostFragment.findNavController(PerfilUsuarioFragment.this).navigate(R.id.action_nav_perfil_to_nav_editar_perfil);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}