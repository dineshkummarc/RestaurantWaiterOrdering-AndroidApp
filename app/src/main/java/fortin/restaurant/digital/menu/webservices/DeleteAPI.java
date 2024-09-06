package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.DeletePojo;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/30/2015.
 */
public interface DeleteAPI {
    @FormUrlEncoded
    @POST("/deleteDish.php")
    void getUserPosts(@Field("orderdetailid") String orderdetailid, Callback<DeletePojo> posts);
}
