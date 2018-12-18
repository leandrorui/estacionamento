package com.example.leandro.estacionamento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    String urlWebServicesDesenvolvimento = "http://192.168.112.2/estacionamento/services/usuarios.php";
    String urlWebServicesProducao = "http://estacionamento.arielzayit.com/services/usuarios.php";
    String url = urlWebServicesProducao.toString();

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button btnEntrar;
    EditText editLogin;
    EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        btnEntrar = findViewById(R.id.btnEntrar);
        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);


        btnEntrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validado = true;

                if(editLogin.getText().length()==0){
                    editLogin.setError("Campo Login Obrigatório");
                    editLogin.requestFocus();
                    validado = false;
                }

                if(editSenha.getText().length()==0){
                    editSenha.setError("Campo Senha Obrigatório");
                    editSenha.requestFocus();
                    validado = false;
                }

                if(validado){

                    Toast.makeText(getApplicationContext(),"Validando seus dados... espere...", Toast.LENGTH_SHORT).show();

                    validarLogin();

                }
            }
        });
    }


    private void validarLogin() {

        stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("LogLogin", response);

                        try {


                            JSONObject jsonObject = new JSONObject(response);

                            boolean isErro = jsonObject.getBoolean("erro");
                            Log.v("LogLogin", jsonObject.getString("erro"));
                            if(isErro){

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("mensagem"),
                                        Toast.LENGTH_LONG).show();
                            }else{

                                int isAdmin = jsonObject.getInt("isAdmin");

                                if(isAdmin==1){

                                    Intent novaTela = new Intent(Login.this,
                                            InsereUsuario.class);
                                    startActivity(novaTela);
                                    finish();

                                }else if(isAdmin==0){

                                    Intent novaTela = new Intent(Login.this,
                                            Parking.class);
                                    startActivity(novaTela);
                                    finish();

                                }

                            }

                        }catch (Exception e){

                            Log.v("LogLogin", e.getMessage());

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogLogin", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("crud","login");
                params.put("login",editLogin.getText().toString());
                params.put("senha",editSenha.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }



}
