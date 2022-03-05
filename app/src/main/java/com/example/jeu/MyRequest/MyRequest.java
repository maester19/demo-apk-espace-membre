package com.example.jeu.MyRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {

    private Context context;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }
    public void register(String name, String pseudo, String email, String password, RegisterCallback callback) {
        String url = "http://192.168.0.10/espace_membre/register.php";

       StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               Map<String, String> errors = new HashMap<>();

               try{
                   JSONObject json = new JSONObject(response);
                   Boolean error = json.getBoolean("error");

                   if(!error){
                        callback.onSuccess("Vous etes bien inscrit");
                   }else{

                       JSONObject messages = json.getJSONObject("message");
                       if(messages.has("pseudo")){
                           errors.put("pseudo", messages.getString("pseudo"));
                       }
                       if(messages.has("email")){
                           errors.put("email", messages.getString("email"));
                       }
                       callback.inputErrors(errors);
                   }

               }catch(JSONException e) {
                    e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

               if(error instanceof NetworkError) {
                   callback.onError("Impossible de se connecter");
               }else if(error instanceof VolleyError){
                   callback.onError("Une erreur s'est produite");
               }
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {

               Map<String, String>map = new HashMap<>();
               map.put("name", name);
               map.put("pseudo", pseudo);
               map.put("email", email);
               map.put("password", password);
               return map;
           }
       };
       queue.add(request);
    }

    public interface RegisterCallback{
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }

    public void connection(String pseudo, String password, LoginCallback callback){

        String url = "http://192.168.0.10/espace_membre/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try{
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        String id = json.getString("id");
                        String pseudo = json.getString("pseudo");
                        callback.onSuccess(id, pseudo);
                    }else{
                        callback.onError(json.getString("message"));
                    }

                }catch (JSONException e){
                    callback.onError("Une erreur s'est produite");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NetworkError) {
                    callback.onError("Impossible de se connecter");
                }else if(error instanceof VolleyError){
                    callback.onError("Une erreur s'est produite");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String>map = new HashMap<>();
                map.put("pseudo", pseudo);
                map.put("password", password);
                return map;
            }
        };
        queue.add(request);
    }

    public interface LoginCallback{
        void onSuccess(String id, String pseudo);
        void onError(String message);
    }
}

