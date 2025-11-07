package com.medac.ejercicio10;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Emulador: 10.0.2.2 | Móvil físico: pon la IP de tu PC
    private static final String BASE = "http://10.0.2.2:8080/";
    private static final String URL_LISTAR = BASE + "listar.php";
    private static final String URL_INSERTAR = BASE + "insertar.php";

    private EditText etNombre, etEdad, etBuscar;
    private Button btnInsertar, btnBuscar;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.editTextText);
        etEdad = findViewById(R.id.editTextNumber);
        etBuscar = findViewById(R.id.editTextText2);
        btnInsertar = findViewById(R.id.button);
        btnBuscar = findViewById(R.id.button2);

        //objeto que permite trabajar con la conexión
        queue = Volley.newRequestQueue(this);

        //abreviación para hacer un evento
        btnInsertar.setOnClickListener(v -> insertar());
        btnBuscar.setOnClickListener(v -> buscarPorNombre());
    }

    private void insertar() {
        //trim por si hay espacio
        String nombre = etNombre.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();

        if (nombre.isEmpty()) {
            toast("El nombre es obligatorio");
            return;
        }
        if (edadStr.isEmpty()) {
            toast("La edad es obligatoria");
            return;
        }

        StringRequest req = new StringRequest(
                //Enviar datos
                Request.Method.POST,
                URL_INSERTAR,
                response -> {
                    // El PHP devuelve {"resultado":"OK"} en éxito
                    toast("Insertado correctamente");
                    etNombre.setText("");
                    etEdad.setText("");
                },
                error -> {
                    Log.e("VOLLEY", "Error insertar: " + error);
                    toast("Error al insertar");
                }
        ) {
            //Manda los datos que quiere insertar
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("nombre", nombre);
                p.put("edad", edadStr);
                return p;
            }
        };

        queue.add(req);
    }
    private void buscarPorNombre() {
        String target = etBuscar.getText().toString().trim();
        if (target.isEmpty()) {
            toast("Introduce un nombre para buscar");
            return;
        }
        // Pedimos listar.php (JSON) y filtramos por nombre en cliente.
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                URL_LISTAR,
                null,
                (JSONArray response) -> {
                    String targetLower = target.toLowerCase(Locale.ROOT);
                    Integer edadEncontrada = null;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String nombre = obj.optString("nombre", "");
                            if (nombre.toLowerCase(Locale.ROOT).equals(targetLower)) {
                                // "edad" en PHP puede venir como String -> convertir con optInt
                                int edad = obj.optInt("edad", -1);
                                edadEncontrada = edad;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (edadEncontrada != null && edadEncontrada >= 0) {
                        toast("Edad de " + target + ": " + edadEncontrada);
                    } else {
                        toast("No se encontró a " + target);
                    }
                },
                error -> {
                    Log.e("VOLLEY", "Error listar/buscar: " + error);
                    toast("Error al buscar");
                }
        );

        queue.add(req);

    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
