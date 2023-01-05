package dao.thecocktaildb;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

public class GsonQueryRequest<T> extends Request<T> {
    // Volley request of a json which will be parsed by a Gson object
    private final Gson gsonParser;
    private final Class<T> containerClass;
    private final Response.Listener<T> listener;

    public GsonQueryRequest(int method, String url, @Nullable Response.ErrorListener errorListener, Class<T> containerClass, Response.Listener<T> listener) {
        super(method, url, errorListener);
        this.containerClass = containerClass;
        this.listener = listener;
        this.gsonParser = new Gson();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        if (response == null){
            return Response.error(new NetworkError());
        }
        String jsonQueryResult;
        try {
            jsonQueryResult = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i(this.getClass().getName(), String.format("response data is %s", jsonQueryResult));
            return Response.success(
                    gsonParser.fromJson(jsonQueryResult, containerClass),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            Log.e(this.getClass().getName(), e.toString());
            return Response.error(new NetworkError());
        }

    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}