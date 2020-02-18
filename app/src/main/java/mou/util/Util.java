package mou.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {
    //------------------------------------------------------
    //  http_get BEGAN
    //  Summary: input String of url, return String of response data
    public static void http_get(final String sz_url, final Callback_HTTP callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                String ret = "";
                try {
                    urlConnection = (HttpURLConnection) new URL(sz_url).openConnection();
                    int responseCode = urlConnection.getResponseCode();
                    Log.w("k", "code=" + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            Log.w("k", "line=" + line);
                            sb.append(line + "\n");
                        }
                        br.close();
                        ret = sb.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                callback.on_callback(ret);
            }
        }).start();
    }
    //  http_get ENDED
    //------------------------------------------------------

    //------------------------------------------------------
    //  http_post BEGAN
    //  Summary: input String of url, and String of json post body, return String of response data
    public static void http_post(final String sz_url, final String sz_json, final Callback_HTTP callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                String ret = "";
                try {
                    urlConnection = (HttpURLConnection) new URL(sz_url).openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);

                    OutputStream os = urlConnection.getOutputStream();
                    DataOutputStream writer = new DataOutputStream(os);
                    writer.writeBytes(sz_json);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = urlConnection.getResponseCode();
                    Log.w("k", "code=" + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            Log.w("k", "line=" + line);
                            sb.append(line + "\n");
                        }
                        br.close();
                        ret = sb.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                callback.on_callback(ret);
            }
        }).start();
    }
    //  http_post BEGAN
    //------------------------------------------------------

    public interface Callback_HTTP {
        public void on_callback(String ret);
    }
}