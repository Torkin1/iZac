package dao.thecocktaildb;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHolder {
    private static RequestQueueHolder ref = null;
    public static RequestQueueHolder getInstance(Context context){
        if (ref == null){
            ref = new RequestQueueHolder(context);
        }
        return ref;
    }
    private RequestQueue requestQueue;
    private RequestQueueHolder(Context context){
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
