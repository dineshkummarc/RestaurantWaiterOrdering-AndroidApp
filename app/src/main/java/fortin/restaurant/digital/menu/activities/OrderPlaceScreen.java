package fortin.restaurant.digital.menu.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reeva.restaurant.R;
import fortin.restaurant.digital.menu.model.CancelOrderPojo;
import fortin.restaurant.digital.menu.model.DishOrderPojo;
import fortin.restaurant.digital.menu.model.DishiesPojo;
import fortin.restaurant.digital.menu.model.FeedbackPojo;
import fortin.restaurant.digital.menu.model.FinishorderPojo;
import fortin.restaurant.digital.menu.model.M;
import fortin.restaurant.digital.menu.model.OrderByOrderidPojo;
import fortin.restaurant.digital.menu.webservices.APIService;
import fortin.restaurant.digital.menu.webservices.CancelOrder;
import fortin.restaurant.digital.menu.webservices.FeedbackAPI;
import fortin.restaurant.digital.menu.webservices.FinishOrderAPI;
import fortin.restaurant.digital.menu.webservices.GetAllDishesAPI;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderPlaceScreen extends AppCompatActivity {
    public List<DishiesPojo> disheslist = new ArrayList<DishiesPojo>();
    public List<DishOrderPojo> dishorederlist = new ArrayList<DishOrderPojo>();
    public List<OrderByOrderidPojo> dishordernewlist=new ArrayList<>();
    AutoCompleteTextView autocomdishes;
    Button btnadddish, btnUpdateoreder, btnfinish,btnordercancel, btndec, btninc;
    EditText edtqty;
    String dishqty;
    Button btncustmode;
    String customername,contactno,rating1,feedback;
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> cusineidlist = new ArrayList<String>();
    ArrayList<String> dishtypelist = new ArrayList<>();
    ArrayList<String> discriplist = new ArrayList<>();
    ArrayList<String> pricelist = new ArrayList<>();
    ArrayList<String> dishimglist = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    DishOrderPojo order;
    ArrayList<String> dishnamelist = new ArrayList<String>();
    ArrayAdapter adapter;
    String dishid, dishname, qty, price;
    DbRestaurant db;
    OrderByOrderidPojo orderbidbyorderidpojo;
    String acttblorderid= DataManager.currentorderid;
    String acttblid= DataManager.tableid;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter recycleradapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    int quan = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_dishies);
        btncustmode=(Button)findViewById(R.id.btncustmode);
        btncustmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPlaceScreen.this, CustCuisine.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        autocomdishes = (AutoCompleteTextView) findViewById(R.id.autodish);
        btnadddish = (Button) findViewById(R.id.btnadddish);
        edtqty = (EditText) findViewById(R.id.edtquantity);
        btnUpdateoreder = (Button) findViewById(R.id.btnorderupdate);
        btnfinish=(Button)findViewById(R.id.btnfinish);
        btninc=(Button)findViewById(R.id.btninc);
        btndec=(Button)findViewById(R.id.btndec);
        dishqty = edtqty.getText().toString();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Table Name: " + DataManager.tablename);
        getSupportActionBar().setSubtitle("Order By: " + M.getUsername(this));
        btnordercancel = (Button) findViewById(R.id.cancelorder);
        if(DataManager.dishorederlist!=null)
        {

            dishorederlist= DataManager.dishorederlist;
            if(dishorederlist.size()==0)
            {
                btnordercancel.setVisibility(View.VISIBLE);
                btnfinish.setVisibility(View.GONE);
            }
        }
        else {


                btnordercancel.setVisibility(View.VISIBLE);
                btnfinish.setVisibility(View.GONE);

                btnordercancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCancelOrder();
                    }
                });
            }



        db = new DbRestaurant(getApplicationContext());


        if (db.getTempPhotos().size() < 1) {
            getAllDishes();
            Log.e("db", "online");
        } else {

            Log.e("db", "local");
            getlocaldishes();
        }


        btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quan = Integer.valueOf(edtqty.getText().toString());

                quan++;
                edtqty.setText(quan + "");

            }

        });
        btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quan = Integer.valueOf(edtqty.getText().toString());
                if (quan == 1) {
                    return;
                } else {
                    quan--;
                    edtqty.setText(quan + "");
                }
            }
        });

        btnadddish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qty = edtqty.getText().toString();
                dishname = autocomdishes.getText().toString();
                if (edtqty.length() == 0 && autocomdishes.length() == 0) {
                    Toast.makeText(getApplicationContext(), "please enter values!!", Toast.LENGTH_LONG).show();
                    return;
                }

                String selected = dishname;
                int pos = 0;
                try {
                    pos = dishnamelist.indexOf(selected);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "please select proper dish from dropdown!!", Toast.LENGTH_LONG).show();
                }
                Log.e("pos--", pos + "");
                if (pos > -1) {

                    if (idlist.get(pos).toString() != null) {
                        dishid = idlist.get(pos).toString();
                        Log.e("dishid--", dishid);
                        Log.e("dishname--", dishname);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please select proper dish from dropdown!!", Toast.LENGTH_LONG).show();
                    return;
                }


                order = new DishOrderPojo();
                order.setDishid(dishid);
                order.setDishname(dishname);
                order.setQty(qty);
                order.setIsnew(true);
                order.setStatus("0");
                dishorederlist.add(order);
                recycleradapter.notifyDataSetChanged();
                edtqty.setText("1");
                autocomdishes.setText("");
                dishname = "";
                qty = "1";


            }

        });





        btnUpdateoreder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dishorederlist.size() < 1)
                {
                 M.showToast(OrderPlaceScreen.this, "Please select Any order or Cancel it.");
                }else {
                    buildjson();
                    M.hideLoadingDialog();
                }

            }
        });


        recycleradapter = new RecyclerViewAdapter(getApplicationContext(), dishorederlist);
        mRecyclerView.setAdapter(recycleradapter);

        btnordercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OrderPlaceScreen.this);
                alertDialog2.setTitle("Are you sure you want to Cancel this order ?");

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                getCancelOrder();

                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });

                alertDialog2.show();

            }
        });


        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // btnordercancel.setVisibility(View.GONE);


                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OrderPlaceScreen.this);
                alertDialog2.setTitle("Are you sure you want to finish ?");

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                getFinish();
                                showInputDialog();
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });

                alertDialog2.show();


            }


        });




    }
    //create JSON Object

    public void buildjson() {

        M m = new M();
        String userid = M.getID(OrderPlaceScreen.this);

        GetAllTables getalltables = new GetAllTables();

        acttblorderid= DataManager.currentorderid;

        jsonObject = new JSONObject();
        jsonArray = new JSONArray();


        for (int i = 0; i < RecyclerViewAdapter.dishorederlist.size(); i++) {

            JSONObject dishes = new JSONObject();

            if (RecyclerViewAdapter.dishorederlist.get(i).isnew()) {
                try {

                    dishes.put("dishid", RecyclerViewAdapter.dishorederlist.get(i).getDishid());
                    dishes.put("quantity", RecyclerViewAdapter.dishorederlist.get(i).getQty());
                    dishes.put("userid", userid);

                    dishes.put("orderid", acttblorderid);
                    jsonArray.put(dishes);


                    Log.e("dishid", RecyclerViewAdapter.dishorederlist.get(i).getDishid());

                    jsonObject.put("order", jsonArray);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();


                }
            }

            }
        M.hideLoadingDialog();

            passJsonObject(jsonObject);

            Log.e("json", jsonObject.toString());

    }


    public void passJsonObject(JSONObject jsonObject) {
        // send jsonobject using httpclient
        M.showLoadingDialog(OrderPlaceScreen.this);
        try {
            ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            AsyncHttpClient client = new AsyncHttpClient();

            System.out.println("entity--"+entity.toString());
            System.out.println("url--"+ DataManager.MAIN+"updateOrder.php");
            client.post(getApplicationContext(), DataManager.MAIN+"updateOrder.php", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
//
                    Log.i("SER", obj.toString());
//
//                    // TODO Auto-generated catch block
//
                    Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(OrderPlaceScreen.this, GetAllTables.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                    M.hideLoadingDialog();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    M.hideLoadingDialog();
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "404 - Nie odnaleziono serwera!", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "500 - Coś poszło nie tak po stronie serwera!", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 403) {
                        Toast.makeText(getApplicationContext(), "Podano niepoprawne dane!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OrderPlaceScreen.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(OrderPlaceScreen.this, GetAllTables.class);
        startActivity(i);
        overridePendingTransition(0, 0);
    }


    private void getlocaldishes() {

        disheslist = db.getTempPhotos();

        for (DishiesPojo dishesdata : disheslist) {


            String cusineid = dishesdata.getDishid();
            String dishname = dishesdata.getDishname();
            String dishtype = dishesdata.getDishtype();
            String discription = dishesdata.getDescription();
            String price = dishesdata.getPrice();
            String image = dishesdata.getDishimage();
                 idlist.add(cusineid);
            dishnamelist.add(dishname);
            dishtypelist.add(dishtype);
            discriplist.add(discription);
            pricelist.add(price);
            dishimglist.add(image);

        }

        adapter = new ArrayAdapter(OrderPlaceScreen.this, android.R.layout.simple_list_item_1, dishnamelist);
        adapter.notifyDataSetChanged();
        autocomdishes.setAdapter(adapter);


    }


    //get All Dishes


    private void getAllDishes() {

        M.showLoadingDialog(OrderPlaceScreen.this);
        GetAllDishesAPI mCommentsAPI = APIService.createService(GetAllDishesAPI.class);

        mCommentsAPI.getUserPosts(new Callback<List<DishiesPojo>>() {

            @Override
            public void success(List<DishiesPojo> dishiesPojos, Response response) {
                Log.e("response", response.toString());
                Log.e("dishlist", disheslist.size() + "");
                if (dishiesPojos != null) {


                    disheslist = dishiesPojos;


                    for (DishiesPojo dishesdata : disheslist) {

                        String dishid = dishesdata.getId();
                        String cusineid = dishesdata.getDishid();
                        String dishname = dishesdata.getDishname();
                        String dishtype = dishesdata.getDishtype();
                        String discription = dishesdata.getDescription();
                        String price = dishesdata.getPrice();
                        String image = dishesdata.getDishimage();
                        idlist.add(dishid);
                        cusineidlist.add(cusineid);
                        dishnamelist.add(dishname);
                        dishtypelist.add(dishtype);
                        discriplist.add(discription);
                        pricelist.add(price);
                        dishimglist.add(image);


                        db.addDishes(new DishiesPojo(dishid, dishname, dishtype, discription, price, image));


                        //db.addTables(new TablePojo(id,tblname,tblstatus));

                    }
                }

                Log.e("dishid--", idlist.toString());
                Log.e("dishname--", dishnamelist.toString());

                adapter = new ArrayAdapter(OrderPlaceScreen.this, android.R.layout.simple_list_item_1, dishnamelist);
                adapter.notifyDataSetChanged();
                autocomdishes.setAdapter(adapter);


                M.hideLoadingDialog();

            }

            @Override
            public void failure(RetrofitError error) {

                M.hideLoadingDialog();
                Log.e("error", error.getMessage());

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_cust_dishes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            Intent i = new Intent(OrderPlaceScreen.this, GetAllTables.class);

            startActivity(i);
            overridePendingTransition(0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //get finish

    void getFinish() {
        M.showLoadingDialog(OrderPlaceScreen.this);
        FinishOrderAPI mCommentsAPI = APIService.createService(FinishOrderAPI.class);
        mCommentsAPI.finishorder(acttblorderid, acttblid, new Callback<FinishorderPojo>() {


            @Override
            public void success(FinishorderPojo finishorderPojo, Response response) {
                M.hideLoadingDialog();
                Log.e("response", response.toString());
                Log.e("success", finishorderPojo.getSuccess());
                if (finishorderPojo.getSuccess().equals("0")) {
                    Toast.makeText(OrderPlaceScreen.this, "finish", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderPlaceScreen.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();
                Log.e("error", error.getMessage());
            }
        });


    }



    // feedback dialog function


    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(OrderPlaceScreen.this);
        View promptView = layoutInflater.inflate(R.layout.feedback, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderPlaceScreen.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Do you want to give Feedback??");
        final EditText edtcustname=(EditText)promptView.findViewById(R.id.edtcusname);

      //  edtcustname.setText(customername);

        final EditText edtcontactno=(EditText)promptView.findViewById(R.id.edtphonenum);
        final Button btnok=(Button)promptView.findViewById(R.id.btnok);
        final Button btnskip=(Button)promptView.findViewById(R.id.btnskip);
      //  edtcontactno.setText(contactno);

        final TextView txtrating=(TextView)promptView.findViewById(R.id.txtrating);
        final RatingBar rating=(RatingBar)promptView.findViewById(R.id.rat);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               rating1=String.valueOf((int)rating);

                Log.e("rating",rating1+"");
            }
        });


        final EditText edtfeedback=(EditText)promptView.findViewById(R.id.edtfeedback);


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customername=edtcustname.getText().toString();
                contactno = edtcontactno.getText().toString();
                if(customername.length()< 2)
                {
                    M.showToast(OrderPlaceScreen.this, "Enter Valid customer name");
                }else if(contactno.length()<7)
                {
                    M.showToast(OrderPlaceScreen.this, "Enter valid Mobile Number");
                }else {
                    Log.e("customername", customername);

                    Log.e("contactno", contactno);
                    feedback = edtfeedback.getText().toString();
                    Log.e("feedback", feedback);
                    getfeedback();
                }
            }
        });

        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent=new Intent(getApplicationContext(),GetAllTables.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


   // get service of feedback

    void getfeedback() {
        M.showLoadingDialog(OrderPlaceScreen.this);
        FeedbackAPI mCommentsAPI = APIService.createService(FeedbackAPI.class);
        mCommentsAPI.getUserPosts(customername,contactno,rating1,feedback,acttblorderid, new Callback<FeedbackPojo>() {

            @Override
            public void success(FeedbackPojo feedbackPojo, Response response) {
                M.hideLoadingDialog();
                Log.e("response", response.toString());
                Log.e("success", feedbackPojo.getSuccess());
                if (feedbackPojo.getSuccess().equals("0")) {
                    Toast.makeText(OrderPlaceScreen.this, "finish", Toast.LENGTH_SHORT).show();

                    finish();
                    Intent intent=new Intent(getApplicationContext(),GetAllTables.class);

                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                else
                {
                    Toast.makeText(OrderPlaceScreen.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();
                Log.e("error", error.getMessage());
            }


        });




    }

    // cancel Order


    void getCancelOrder() {
        M.showLoadingDialog(OrderPlaceScreen.this);
        CancelOrder mCommentsAPI = APIService.createService(CancelOrder.class);
        mCommentsAPI.cancelorder(acttblorderid, new Callback<CancelOrderPojo>() {


            @Override
            public void success(CancelOrderPojo cancelOrderPojo, Response response) {
                M.hideLoadingDialog();
                Log.e("response", response.toString());
                Log.e("success", cancelOrderPojo.getSuccess());
                if (cancelOrderPojo.getSuccess().equals("0")) {
                 Intent i = new Intent(OrderPlaceScreen.this, GetAllTables.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0,0);

                } else {
                    Toast.makeText(OrderPlaceScreen.this, "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();
                Log.e("error", error.getMessage());
            }


        });


    }



}


