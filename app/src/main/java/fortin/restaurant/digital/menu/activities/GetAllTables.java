package fortin.restaurant.digital.menu.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.reeva.restaurant.R;
import fortin.restaurant.digital.menu.model.DishOrderPojo;
import fortin.restaurant.digital.menu.model.InactivTPojo;
import fortin.restaurant.digital.menu.model.M;
import fortin.restaurant.digital.menu.model.NewOrderPojo;
import fortin.restaurant.digital.menu.model.OrderByOrderidPojo;
import fortin.restaurant.digital.menu.model.TablePojo;
import fortin.restaurant.digital.menu.webservices.APIService;
import fortin.restaurant.digital.menu.webservices.ActiveTableWithOrderAPI;
import fortin.restaurant.digital.menu.webservices.ChangeTableAPI;
import fortin.restaurant.digital.menu.webservices.GetOrderByOrderIDAPI;
import fortin.restaurant.digital.menu.webservices.NewOrederAPI;
import fortin.restaurant.digital.menu.webservices.TablesAPI;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Reeva on 9/21/2015.
 */
public class GetAllTables extends AppCompatActivity {
    public static String actorderdetailid;
    public static String activetblorderid;
    public static String acttblid;
    public static String orderid;
    public List<OrderByOrderidPojo> orderidbyorderidlist = new ArrayList<>();
    public List<InactivTPojo> inatablelist = new ArrayList<InactivTPojo>();
    public List<TablePojo> tablelist = new ArrayList<TablePojo>();
    public List<DishOrderPojo> dishorederlist = new ArrayList<DishOrderPojo>();
    GridView grd;
    String tblstatus, tableid, userid;
    String newtableid;
    ArrayList<String> oorderdetailidlist=new ArrayList<>();
    ArrayList<String> ostatuslist=new ArrayList<>();
    ArrayList<String> odishidlist = new ArrayList<String>();
    ArrayList<String> odishnamelist = new ArrayList<String>();
    ArrayList<String> oquantitylist = new ArrayList<String>();
    ArrayList<String> opricelist = new ArrayList<String>();
    ArrayList<String> tblidlistina = new ArrayList<String>();
    ArrayList<String> tblnamelistina = new ArrayList<String>();
    ArrayList<String> tblstatuslistina = new ArrayList<>();
    ArrayList<String> orderidlist = new ArrayList<String>();
    ArrayList<String> tblidlist = new ArrayList<String>();
    ArrayList<String> tblnamelist = new ArrayList<String>();
    String status1 = "0";
    TextView txttable, newtable;
    private TablesAdpater tbladapter;
    private InactivetableAdapter inactivetableadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_tables);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        grd = (GridView) findViewById(R.id.grd1);
        fab.attachToListView(grd);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(GetAllTables.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dialog_new_order);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));

                txttable = (TextView) dialog.findViewById(R.id.txttable);

                txttable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selecttale();
                    }
                });

                dialog.show();
                getInactiveTables();
                Button btnButton = (Button) dialog.findViewById(R.id.btnorder);
                Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
                btnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        M m = new M();
                        if (DataManager.dishorederlist != null) {
                            DataManager.dishorederlist.clear();
                        }
                        if(tableid !=null) {
                            userid = M.getID(GetAllTables.this);
                            Log.e("userid", M.getID(GetAllTables.this));
                            getOrderId();
                            dialog.dismiss();
                        }else
                        {
                            M.showToast(GetAllTables.this, "Please select Any Table");
                        }

                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

            }
        });

        if(DataManager.dishorederlist !=null)
        {
            DataManager.dishorederlist.clear();
        }
        getTables();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_active_tables, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {

            getTables();
            return true;
        }else if(id == R.id.logout)
        {
            M.setUsername(null, GetAllTables.this);
            M.setPassword(null, GetAllTables.this);
            Intent i = new Intent(this, Login.class);
            finish();
            startActivity(i);
            overridePendingTransition(0,0);
        }

        return super.onOptionsItemSelected(item);
    }


    void getOrderId() {
        M.showLoadingDialog(GetAllTables.this);
        NewOrederAPI mCommentsAPI = APIService.createService(NewOrederAPI.class);
        mCommentsAPI.getUserPosts(userid, tableid, new Callback<NewOrderPojo>() {

            @Override
            public void success(NewOrderPojo newOrderPojo, Response response) {
                M.hideLoadingDialog();
                Log.e("tableid", tableid);
                DataManager.currentorderid = newOrderPojo.getOrderid().toString();

                Intent intent = new Intent(getApplicationContext(), OrderPlaceScreen.class);
                finish();
                startActivity(intent);
                overridePendingTransition(0, 0);
                Log.e("response", response.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();
                Log.e("error", error.getMessage());
            }
        });


    }


    //Get Inactive tables list

    public void selecttale() {
        final ArrayAdapter<String> adapter;
        final Dialog myDialog = new Dialog(GetAllTables.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog.setContentView(R.layout.custom_spinner_dialog);
        myDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtheader = (TextView) myDialog.findViewById(R.id.txtheader);

        txtheader.setText("Select table");


        final ListView listview = (ListView) myDialog
                .findViewById(R.id.spinnerlist);

        adapter = new ArrayAdapter<String>(GetAllTables.this,
                android.R.layout.select_dialog_item, tblnamelistina);

        listview.setAdapter(adapter);

        if (tblidlistina.size() > 0) {
            tableid = tblidlistina.get(0).toString();
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    myDialog.dismiss();


                    tableid = tblidlistina.get(position).toString();
                    txttable.setText(tblnamelistina.get(position).toString());
                    Log.e("tablesize", tblidlistina.size() + "");

                    Log.e("tableid", tableid);
                }
            });
        }
        myDialog.show();

    }


//get ActiveTables list

    public void getInactiveTables() {

        M.showLoadingDialog(GetAllTables.this);
        TablesAPI mCommentsAPI = APIService.createService(TablesAPI.class);
        mCommentsAPI.getUserPosts(status1, new Callback<List<InactivTPojo>>() {

            @Override
            public void success(List<InactivTPojo> inactivTPojos, Response response) {
                inatablelist.clear();
                tblidlistina.clear();
                tblnamelistina.clear();
                tblstatuslistina.clear();
                Log.e("table", inatablelist.size() + "");
                if (inactivTPojos != null) {


                    inatablelist = inactivTPojos;


                    for (InactivTPojo tabledata : inatablelist) {

                        String id = tabledata.getId();
                        String tblname = tabledata.getName();
                        tblstatus = (tabledata.getStatus());
                        Log.e("statusin", tblstatus + "");


                        tblidlistina.add(id);
                        Log.e("tblinid", tabledata.getId() + "");
                        Log.e("tblinname", tabledata.getName() + "");
                        tblnamelistina.add(tblname);
                        tblstatuslistina.add(tblstatus);



                    }
                }




                M.hideLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();

                Log.e("error", error.getMessage());
            }

        });
    }

    // get orderid by order id

    private void getTables() {

        M.showLoadingDialog(GetAllTables.this);
        ActiveTableWithOrderAPI mCommentsAPI = APIService.createService(ActiveTableWithOrderAPI.class);
        mCommentsAPI.getUserPosts(new Callback<List<TablePojo>>() {
            @Override
            public void success(List<TablePojo> tablePojos, Response response) {
                tablelist.clear();
                Log.e("table", tablelist.size() + "");
                if (tablePojos != null) {

                    tablelist = tablePojos;

                    for (TablePojo tabledata : tablelist) {
                         String orderid = tabledata.getOrderid();
                         String tblid = tabledata.getTableid();
                         String tblname = tabledata.getTablename();


                         orderidlist.add(orderid);
                         tblidlist.add(tblid);
                         tblnamelist.add(tblname);

                        Log.e("activeorderid", tabledata.getOrderid() + "");
                        Log.e("tblid", tabledata.getTableid() + "");
                        Log.e("tblname", tabledata.getTablename() + "");

                    }
                }


                tbladapter = new TablesAdpater(getApplicationContext(), tablelist);
                tbladapter.notifyDataSetChanged();
                grd.setAdapter(tbladapter);
                grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataManager.currentorderid = orderidlist.get(position).toString();
                        DataManager.tableid = tblidlist.get(position).toString();


                        Log.e("acorderid", orderidlist.get(position).toString());
                        Log.e("actbl", tblidlist.get(position).toString());
                        DataManager.tablename = tblnamelist.get(position).toString();
                        getorderidByorderid();
                        Log.e("orderidsize", orderidbyorderidlist.size() + "");

                    }
                });

                grd.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                                   int position, long id) {
                        // TODO Auto-generated method stub

                        String currentorderid = orderidlist.get(position).toString();
                        String tableid = tblidlist.get(position).toString();


                        Log.e("acorderid", orderidlist.get(position).toString());
                        Log.e("actbl", tblidlist.get(position).toString());
                        String tablename = tblnamelist.get(position).toString();


                        changetable(tablename, currentorderid, tableid);
                        return false;
                    }
                });

                M.hideLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();

                Log.e("error", error.getMessage());
            }


        });
    }

    private void getorderidByorderid() {

        M.showLoadingDialog(GetAllTables.this);
        GetOrderByOrderIDAPI mCommentsAPI = APIService.createService(GetOrderByOrderIDAPI.class);
        mCommentsAPI.getUserPosts( DataManager.currentorderid, new Callback<List<OrderByOrderidPojo>>() {


            @Override
            public void success(List<OrderByOrderidPojo> orderByOrderidPojos, Response response) {
                Log.e("table", orderidbyorderidlist.size() + "");
                if (orderByOrderidPojos != null) {


                    orderidbyorderidlist = orderByOrderidPojos;

                    if( DataManager. dishorederlist!=null) {
                        DataManager.dishorederlist.clear();
                    }
                    for (OrderByOrderidPojo orderdata : orderidbyorderidlist) {
                        String orderdetailid=orderdata.getOrderdetailid();
                        String ostatus=orderdata.getStatus();

                        Log.e("status",ostatus);
                        String odishid = orderdata.getDishid();
                        String odishname=orderdata.getDishname();
                        String oquantity = orderdata.getQuantity();
                        String oprice = orderdata.getPrice();
                        oorderdetailidlist.add(orderdetailid);

                        ostatuslist.add(ostatus);

                        odishidlist.add(odishid);
                        odishnamelist.add(odishname);
                        oquantitylist.add(oquantity);
                        opricelist.add(oprice);

                        DishOrderPojo order=new DishOrderPojo();
                        order.setDishid(odishid);
                        order.setDishname(odishname);
                        order.setQty(oquantity);
                        order.setOrderdetailid(orderdetailid);
                        order.setStatus(ostatus);

                        order.setIsnew(false);



                        dishorederlist.add(order);


                        DataManager.dishorederlist=dishorederlist;

                        Intent intent=new Intent(getApplicationContext(),OrderPlaceScreen.class);
                        finish();
                        startActivity(intent);

                        overridePendingTransition(0, 0);


                    }
                }


                M.hideLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();

                Intent intent=new Intent(getApplicationContext(),OrderPlaceScreen.class);
                finish();
                startActivity(intent);
                overridePendingTransition(0, 0);
                Log.e("error", error.getMessage());
            }
        });
    }

    public void changetable(String currenttable, final String orderid,final String tblid)
    {
        final Dialog dialog = new Dialog(GetAllTables.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.change_table_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

         newtable = (TextView) dialog.findViewById(R.id.txtnewtable);
      final TextView txtcurrenttable  = (TextView) dialog.findViewById(R.id.txtcurrenttable);

        txtcurrenttable.setText(""+currenttable);

        newtable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectnewtable();
            }
        });

        dialog.show();
        getInactiveTables();
        Button btnButton = (Button) dialog.findViewById(R.id.btnorder);
        Button btncancel = (Button) dialog.findViewById(R.id.btncancel);
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changetableAPI(orderid, newtableid, tblid);

                dialog.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }

    void changetableAPI(String orderid, final String tblid, final String oldtableid) {
        M.showLoadingDialog(GetAllTables.this);
        ChangeTableAPI mCommentsAPI = APIService.createService(ChangeTableAPI.class);
        System.out.println("newtable"+newtableid);
        System.out.println("oldtableid"+oldtableid);
        mCommentsAPI.changeTable(orderid, oldtableid, newtableid, new Callback<String>() {

            @Override
            public void success(String resp, Response response) {
                M.hideLoadingDialog();
                Log.e("tblid", tblid);

                getTables();

                Log.e("response", resp.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                M.hideLoadingDialog();

                getTables();

                Log.e("error", error.getMessage());
            }
        });


    }

    public void selectnewtable() {
        final ArrayAdapter<String> adapter;
        final Dialog myDialog = new Dialog(GetAllTables.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog.setContentView(R.layout.custom_spinner_dialog);
        myDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtheader = (TextView) myDialog.findViewById(R.id.txtheader);

        txtheader.setText("Select table");


        final ListView listview = (ListView) myDialog
                .findViewById(R.id.spinnerlist);

        adapter = new ArrayAdapter<String>(GetAllTables.this,
                android.R.layout.select_dialog_item, tblnamelistina);

        listview.setAdapter(adapter);

        if (tblidlistina.size() > 0) {

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    myDialog.dismiss();


                    newtableid = tblidlistina.get(position).toString();
                    newtable.setText(tblnamelistina.get(position).toString());
                    Log.e("tablesize", tblidlistina.size() + "");

                }
            });
        }
        myDialog.show();

    }


    public class TablesAdpater extends BaseAdapter {

        public List<TablePojo> tablelist = new ArrayList<TablePojo>();
        Context context;
        public TablesAdpater(  Context context,List<TablePojo> tablelist  ) {


            this.tablelist=tablelist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return tablelist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;


            if(row==null)
            {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.activity_custom_view_grid, parent, false);

            } else {
                row = (View) convertView;
            }


            TextView txttname=(TextView)row.findViewById(R.id.txttblname);
            TextView txtamount=(TextView)row.findViewById(R.id.txttotalamt);
            TextView txtuser=(TextView)row.findViewById(R.id.txtuser);
            Button changetable = (Button)row.findViewById(R.id.btnchangetable);
            txttname.setText(tablelist.get(position).getTablename());

            String amount = tablelist.get(position).getAmount();
            if(amount == null)
            {
                amount = "0";
            }
            txtamount.setText(context.getResources().getString(R.string.amount)+ " "+ DataManager.currency+amount);
            txtuser.setText(context.getResources().getString(R.string.user)+ " "+tablelist.get(position).getUsername());

            changetable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentorderid = orderidlist.get(position).toString();
                    String tableid = tblidlist.get(position).toString();


                    Log.e("acorderid", orderidlist.get(position).toString());
                    Log.e("actbl", tblidlist.get(position).toString());
                    String tablename = tblnamelist.get(position).toString();

                    changetable(tablename, currentorderid, tableid);
                }
            });

            return row;
        }



    }

}



