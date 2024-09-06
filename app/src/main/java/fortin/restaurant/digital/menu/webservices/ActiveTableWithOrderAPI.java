package fortin.restaurant.digital.menu.webservices;

import fortin.restaurant.digital.menu.model.TablePojo;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Reeva on 9/29/2015.
 */
public interface ActiveTableWithOrderAPI {
    @GET("/activeTablesWithOrderId.php")
    void getUserPosts(Callback<List<TablePojo>> posts);

}
