package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.NewOrderPojo;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/23/2015.
 */
public interface NewOrederAPI {
    @FormUrlEncoded
    @POST("/startNewOrder.php")
    void getUserPosts(@Field("userid") String userid,
                      @Field("tableid") String tableid , Callback<NewOrderPojo> res);
}
