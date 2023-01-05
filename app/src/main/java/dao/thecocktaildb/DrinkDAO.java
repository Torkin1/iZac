package dao.thecocktaildb;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import entity.DrinkPack;

public class DrinkDAO {

    // Singleton
    private static DrinkDAO ref = null;
    public static DrinkDAO getInstance(Context context){
        if (ref == null){
            ref = new DrinkDAO(context);
        }
        return  ref;
    }
    private Context context;
    private RequestQueue requestQueue;

    private DrinkDAO(Context context){
        this.context = context.getApplicationContext();
        this.requestQueue = RequestQueueHolder.getInstance(context).getRequestQueue();
    }

    public void queryDrinksByFirstLetter(String firstLetter, Response.ErrorListener errorListener, Response.Listener<DrinkPack> listener){
        // Sanitize input parameter
        firstLetter = firstLetter.substring(0, 1);
        String query = (new StringBuilder())
                .append(QueryTemplate.DRINK_BY_FIRST_LETTER.toString())
                .append(firstLetter)
                .toString();
        GsonQueryRequest<DrinkPack> request = new GsonQueryRequest<>(
                Request.Method.GET,
                query,
                errorListener,
                DrinkPack.class,
                listener
        );
        this.requestQueue.add(request);
    }
    public void queryDrinkThumbnail(String strDrinkThumb, DrinkThumbnailSize size, Response.ErrorListener errorListener, Response.Listener<Bitmap> listener){
        if (strDrinkThumb == null){
            return;
        }
        this.requestQueue.add(
                new ImageRequest(
                        (new StringBuilder())
                                .append(strDrinkThumb)
                                .append(size.toString())
                                .toString(),
                        listener,
                        0,
                        0,
                        ImageView.ScaleType.FIT_CENTER,
                        Bitmap.Config.ARGB_8888,
                        errorListener
                )
        );
    }

    public void queryRandomDrink(Response.ErrorListener errorListener, Response.Listener<DrinkPack> listener){
        this.requestQueue.add(new GsonQueryRequest<DrinkPack>(
                Request.Method.GET,
                QueryTemplate.DRINK_RANDOM.toString(),
                errorListener,
                DrinkPack.class,
                listener
                )
        );
    }
}
