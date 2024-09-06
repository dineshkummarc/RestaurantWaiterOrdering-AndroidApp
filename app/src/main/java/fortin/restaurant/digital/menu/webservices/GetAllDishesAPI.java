package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.DishiesPojo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Reeva on 9/23/2015.
 */
public interface GetAllDishesAPI {

    @GET("/getAllDish.php")
    void getUserPosts(Callback<List<DishiesPojo>> posts);
}
