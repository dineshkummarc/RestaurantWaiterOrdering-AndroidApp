package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.activities.Order;

import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/25/2015.
 */
public interface UpdateAPI {

    @POST("/updateorder.php")
    void getUserPosts(@Body List<Order> orderlist, Callback<JSONObject> posts);

}
