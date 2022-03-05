package com.example.jeu;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.jeu.MyRequest.MyRequest;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_create;
    private TextInputLayout til_name,til_pseudo, til_email, til_password;
    private ProgressBar pb_loader;
    private RequestQueue queue;
    private MyRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_create = (Button) findViewById(R.id.btn_create);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);
        til_name = (TextInputLayout) findViewById(R.id.til_name);
        til_pseudo = (TextInputLayout) findViewById(R.id.til_pseudo);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_password = (TextInputLayout) findViewById(R.id.til_password);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_loader.setVisibility(View.VISIBLE);
                String name = til_name.getEditText().getText().toString().trim();
                String pseudo = til_pseudo.getEditText().getText().toString().trim();
                String email = til_email.getEditText().getText().toString().trim();
                String password = til_password.getEditText().getText().toString().trim();
                 if(name.length() > 0 && pseudo.length() > 0 && email.length() > 0 && password.length() > 0) {
                     request.register(name, pseudo, email, password, new MyRequest.RegisterCallback() {
                         @Override
                         public void onSuccess(String message) {
                             pb_loader.setVisibility(View.GONE);
                             Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                             intent.putExtra("REGISTER", message);
                             startActivity(intent);
                             finish();

                         }

                         @Override
                         public void inputErrors(Map<String, String> errors) {
                            pb_loader.setVisibility(View.GONE);
                             if(errors.get("pseudo") != null){
                                til_pseudo.setError(errors.get("pseudo"));
                            }else{
                                til_pseudo.setErrorEnabled(false);
                            }
                             if(errors.get("email") != null){
                                 til_email.setError(errors.get("email"));
                             }else{
                                 til_email.setErrorEnabled(false);
                             }
                         }

                         @Override
                         public void onError(String message) {
                             pb_loader.setVisibility(View.GONE);
                             Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                         }
                     });
                 }else{
                     Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }
}