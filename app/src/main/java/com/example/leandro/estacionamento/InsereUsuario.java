package com.example.leandro.estacionamento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class InsereUsuario extends AppCompatActivity {


    String urlWebServicesDesenvolvimento = "http://192.168.112.2/estacionamento/services/usuarios.php";
    String urlWebServicesProducao = "http://estacionamento.arielzayit.com/services/usuarios.php";
    String url = urlWebServicesProducao.toString();

    StringRequest stringRequest;
    RequestQueue requestQueue;

    EditText edtNome, edtSobrenome, edtLogin, edtSenha;
    CheckBox isAdmin, isMobile, isVisitante;
    Button btnCancelar, btnInserir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insere_usuario);

        requestQueue = Volley.newRequestQueue(this);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnInserir = findViewById(R.id.btnInserir);

        edtNome = findViewById(R.id.edtNome);
        edtSobrenome = findViewById(R.id.edtSobrenome);
        edtLogin = findViewById(R.id.edtLogin);
        edtSenha = findViewById(R.id.edtSenha);

        isAdmin = findViewById(R.id.chkIsAdmin);
        isMobile = findViewById(R.id.chkIsMobile);
        isVisitante = findViewById(R.id.chkIsVisitante);

        btnInserir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirUsuario();
            }
        });


    }

    public String booleanToString(boolean valor){
        String retorno = "0";
        if(valor) retorno = "1";
        return retorno;
    }

    private void inserirUsuario() {

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


                                    Intent novaTela = new Intent(InsereUsuario.this,
                                            Login.class);
                                    startActivity(novaTela);
                                    finish();

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
                params.put("crud","insert");

                params.put("login",edtLogin.getText().toString());
                params.put("senha",edtSenha.getText().toString());
                params.put("nome",edtNome.getText().toString());
                params.put("sobrenome",edtSobrenome.getText().toString());
                params.put("isAdmin", booleanToString(isAdmin.isChecked()));
                params.put("isMobile", booleanToString(isMobile.isChecked()));
                params.put("isVisitante", booleanToString(isVisitante.isChecked()));

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


}
