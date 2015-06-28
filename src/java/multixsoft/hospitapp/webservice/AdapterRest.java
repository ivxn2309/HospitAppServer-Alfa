package multixsoft.hospitapp.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 13/May/2015
 */
public class AdapterRest {

    private String base = "http://localhost:8080/HospitAppServerCiclo1/webresources/";

    public AdapterRest(String address) {
        base = address;
    }
    public AdapterRest() {
       // base = address;
    }

    public Object get(String path) {
        try {
            URL url = new URL(base + path);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setRequestMethod("GET");
            
            int codigo = conexion.getResponseCode();
            if (codigo == HttpURLConnection.HTTP_OK) {
                InputStream is = conexion.getInputStream();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(is));
                String respuesta = entrada.readLine();

                Object obj = JSONValue.parse(respuesta);
                entrada.close();
                return obj;
            }
        } 
        catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean delete(String path) {
        boolean resultado = false;                
        try {
            URL url = new URL(base+path);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            int codigo = conexion.getResponseCode();
            if (codigo/100 == 2) {
                resultado = true;
            }
        } 
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public boolean post(String path, String jsonObject){
        byte [] pack = jsonObject.getBytes();
        try {
            URL url = new URL(base + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(pack.length));
            OutputStream os = conn.getOutputStream();
            os.write(pack);

            int codigo = conn.getResponseCode();
            //System.out.println("Codigo recibido" + codigo);
            if (codigo / 100 == 2) {
                //System.out.println("Error en Codigo recibido" + codigo);
                return true;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public boolean put(String path, String jsonObject){
        byte [] pack = jsonObject.getBytes();
        try {
            URL url = new URL(base + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("PUT");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(pack.length));
            OutputStream os = conn.getOutputStream();
            os.write(pack);

            int codigo = conn.getResponseCode();
            if (codigo / 100 == 2) {
                return true;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
