package dao.thecocktaildb;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class IngredientDAO {
    private static IngredientDAO ref = null;
    public static IngredientDAO getInstance(Context context){
        if (ref == null){
            ref = new IngredientDAO(context);
        }
        return ref;
    }
    private Context context;
    private RequestQueue requestQueue;

    private IngredientDAO(Context context){
        this.context = context.getApplicationContext();
        this.requestQueue = RequestQueueHolder.getInstance(context).getRequestQueue();
    }
    public void queryIngredientThumbnail(String ingredientName, IngredientThumbnailSize size, Response.ErrorListener errorListener, Response.Listener<Bitmap> listener){
        if (ingredientName == null){
            return;
        }
        this.requestQueue.add(
                new ImageRequest(
                        (new StringBuilder())
                                .append(QueryTemplate.INGREDIENT_THUMB.toString())
                                .append(ingredientName)
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
}
