package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.InactivTPojo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by Reeva on 9/21/2015.
 */
public interface TablesAPI {
    @FormUrlEncoded
    @POST("/getActiveTables.php")
    void getUserPosts(@Field("status") String status,Callback<List<InactivTPojo>> posts);

}
