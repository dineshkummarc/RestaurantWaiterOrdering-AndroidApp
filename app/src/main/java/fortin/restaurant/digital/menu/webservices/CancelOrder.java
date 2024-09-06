package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.CancelOrderPojo;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/29/2015.
 */
public interface CancelOrder {
    @FormUrlEncoded
    @POST("/orderCancel.php")
    void cancelorder(@Field("orderid") String orderid,
                     Callback<CancelOrderPojo> posts);
}
