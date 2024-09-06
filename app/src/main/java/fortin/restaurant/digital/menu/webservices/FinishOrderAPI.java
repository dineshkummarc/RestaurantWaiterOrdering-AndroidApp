package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.FinishorderPojo;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/29/2015.
 */
public interface FinishOrderAPI {
    @FormUrlEncoded
    @POST("/finishOrder.php")
    void finishorder(@Field("orderid") String orderid,
                     @Field("tableid") String tableid, Callback<FinishorderPojo> posts);
}
