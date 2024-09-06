package fortin.restaurant.digital.menu.webservices;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Reeva on 9/23/2015.
 */
public interface ChangeTableAPI {
    @FormUrlEncoded
    @POST("/changeTable.php")
    void changeTable(@Field("orderid") String orderid,
                     @Field("oldtableid") String oldtableid,
                      @Field("newtableid") String newtableid, Callback<String> res);
}
