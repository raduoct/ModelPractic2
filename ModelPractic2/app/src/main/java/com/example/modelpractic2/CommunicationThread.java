package com.example.modelpractic2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (currency / information type!");
            String currency = bufferedReader.readLine();
            String informationType = bufferedReader.readLine();
            if (currency == null || currency.isEmpty() || informationType == null || informationType.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (currency / information type!");
                return;
            }

            HashMap<String, BpiInformation> data = serverThread.getData();
            BpiInformation bpiInformation;
            if (data.containsKey(currency)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                bpiInformation = data.get(currency);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";
                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + currency);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }

                JSONObject content = new JSONObject(pageSourceCode);

                JSONObject bpi = content.getJSONObject(Constants.BPI);

                JSONObject main = bpi.getJSONObject(currency);
                String code = main.getString(Constants.CODE);
                String rate = main.getString(Constants.RATE);
                String description = main.getString(Constants.DESCRIPTION);
                String rate_float = main.getString(Constants.RATE_FLOAT);

                bpiInformation = new BpiInformation(
                        code, rate, description, rate_float
                );
                serverThread.setData(currency, bpiInformation);
            }
            if (bpiInformation == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast Information is null!");
                return;
            }
            String result;
            switch(informationType) {
                case Constants.ALL:
                    result = bpiInformation.toString();
                    break;
                case Constants.CODE:
                    result = bpiInformation.getCode();
                    break;
                case Constants.RATE:
                    result = bpiInformation.getRate();
                    break;
                case Constants.DESCRIPTION:
                    result = bpiInformation.getDescription();
                    break;
                case Constants.RATE_FLOAT:
                    result = bpiInformation.getRate_float();
                    break;
                default:
                    result = "[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!";
            }
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

}