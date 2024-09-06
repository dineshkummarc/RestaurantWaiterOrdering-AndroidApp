package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.CustCuisineListPojo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Reeva on 10/10/2015.
 */
public interface CustCuisineListAPI {

    @GET("/getAllCusine.php")
    void getUserPosts(Callback<List<CustCuisineListPojo>> posts);

}
