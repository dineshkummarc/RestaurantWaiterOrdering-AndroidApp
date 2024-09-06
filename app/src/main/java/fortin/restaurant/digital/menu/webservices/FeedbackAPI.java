package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.FeedbackPojo;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/30/2015.
 */
public interface FeedbackAPI {
    @FormUrlEncoded
    @POST("/feedback.php")
    void getUserPosts(@Field("customername") String customername,
                      @Field("phone") String phone ,
                      @Field("rating") String rating,
                      @Field("feedback") String feedback,
                      @Field("orderid") String orderid,Callback<FeedbackPojo> posts);
}
