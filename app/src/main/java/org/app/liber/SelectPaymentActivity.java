package org.app.liber;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.app.liber.helper.AlertReceiver;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.DateUtil;
import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.PaytmOrderPojo;
import org.app.liber.pojo.TransactionPojo;
import org.app.liber.pojo.WalletPojo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPaymentActivity extends AppCompatActivity {

    private ImageView orderImg;
    private TextView orderTitle;
    private Button gpayBtn;
    private Button codBtn;
    private Button paytmBtn;
    private ProgressDialog progressDialog;
    private Intent j;
    private BookshelfPojo l;
    private CheckBox walletChckBox;
    private TextView totalAmntTxt;
    private TextView walletAmntTxt;
    private TextView walletblncAmntTxt;
    private TextView returnDateTxt;
    private TextView dateExtFeesTxt;
    private TextView gstTxt;
    private int bookAmnt = 70;
    private int walletAmnt = 0;
    private LinearLayout linearLayout;
    private String tenureSelected;
    private Date today;
    private Date tomorrow;
    private int noofdays = 0;
    private Calendar calendar;
    private Calendar tomorrowCal;
    private SharedPreferences pref;
    private String userLocation;
    private double gstAmount = 0.0;
    private LiberEndpointInterface service;
    private int totalWalletAmount = 0;
    private PaytmOrder paytmOrder;
    private HashMap<String, String> paramMap;
    private PaytmPGService paytmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayForSnackBar);
        orderTitle = (TextView)findViewById(R.id.os_title_id);
        codBtn = (Button)findViewById(R.id.cod_bttn_id);
        gpayBtn = (Button)findViewById(R.id.gpay_bttn_id);
        paytmBtn = (Button)findViewById(R.id.paytm_bttn_id);
        returnDateTxt = (TextView)findViewById(R.id.due_date_id);
        walletChckBox = (CheckBox)findViewById(R.id.wallet_chckbox_id);
        walletAmntTxt = (TextView)findViewById(R.id.wallet_amount_id);
        totalAmntTxt = (TextView)findViewById(R.id.total_payable_amount_id);
        walletblncAmntTxt = (TextView)findViewById(R.id.wallet_blnc_amnt_id);
        dateExtFeesTxt = (TextView)findViewById(R.id.date_ext_fees_id);
        gstTxt = (TextView)findViewById(R.id.gst_amount_id);

        progressDialog = new ProgressDialog(SelectPaymentActivity.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.processing_txn_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        userLocation = pref.getString("USER_CITY","unknown");
        getWalletAmountFromServer();

//        if(userLocation.equals("unknown") || userLocation == null){
//            codBtn.setEnabled(false);
//            gpayBtn.setEnabled(false);
//            paytmBtn.setEnabled(false);
//        }else{
//            codBtn.setEnabled(true);
//            gpayBtn.setEnabled(true);
//            paytmBtn.setEnabled(true);
//        }

        today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(today);

        tomorrow = new Date();
        tomorrowCal = calendar;
        tomorrowCal.setTime(today);
        tomorrowCal.add(Calendar.DATE, 1);
        tomorrow = tomorrowCal.getTime();

        final DateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yy");

        Intent i = getIntent();

        l = (BookshelfPojo)i.getSerializableExtra("order_book2");
        tenureSelected = i.getStringExtra("tenure_selected");

        if(tenureSelected.equals("1")){
            noofdays = 14;
            dateExtFeesTxt.setText("0");
            calendar.add(Calendar.DAY_OF_YEAR, noofdays);
            returnDateTxt.setText(dateFormat.format(calendar.getTime()));
        }else if(tenureSelected.equals("2")){
            bookAmnt += 10;
            dateExtFeesTxt.setText("10");
            noofdays = 30;
            calendar.add(Calendar.DAY_OF_YEAR, noofdays);
            returnDateTxt.setText(dateFormat.format(calendar.getTime()));
        }else{
            bookAmnt += 15;
            dateExtFeesTxt.setText("15");
            noofdays = 60;
            calendar.add(Calendar.DAY_OF_YEAR, noofdays);
            returnDateTxt.setText(dateFormat.format(calendar.getTime()));
        }
        //Picasso.with(getApplicationContext()).load(l.getSmallThumbnailLink()).resize(100,150).into(orderImg);

        orderTitle.setText(l.getTitle());

        walletAmntTxt.setText("0");
        totalAmntTxt.setText(finalAmountRvrs());

        codBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    j = new Intent(getApplicationContext(), OrderCompleteActivity.class);
                    j.putExtra("tx_mode","Cash");
                    Date c = Calendar.getInstance().getTime();

                    TransactionPojo usrTx = new TransactionPojo();
                    usrTx.setTxId(String.valueOf(c.getTime()));
                    usrTx.setTxPaymentMode("CASH");
                    usrTx.setTxDate(DateUtil.getDate(today));
                    usrTx.setTxDeliverySts("Pending");
                    usrTx.setTxAmount(totalAmntTxt.getText().toString());
                    usrTx.setTxDelete("N");
                    usrTx.setTxMob(pref.getString("USER_MOB","unknown"));
                    usrTx.setTxDeliveryDate(DateUtil.getDate(tomorrow));
                    usrTx.setTxType("Renter");
                    usrTx.setTxUser(pref.getString("USER_NAME","unknown"));
                    usrTx.setTxReturnDate(returnDateTxt.getText().toString());
                    usrTx.setTxBook(orderTitle.getText().toString());
                    usrTx.setTxBookOwner(l.getU_id());
                    usrTx.setTxBookOwnerMob(l.getMobile());
                    saveTxn(usrTx);
                    addMoneyToLenderWallet(usrTx);
                    setNotificationAlarm(usrTx);
                    startActivity(j);
            }
        });


        paytmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Date c = Calendar.getInstance().getTime();
                String orderId = String.valueOf(c.getTime());
                paytmService = PaytmPGService.getStagingService();
                paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , "ZaOkaN15773078382332");
                paramMap.put( "ORDER_ID" , orderId);
                paramMap.put( "CUST_ID" , pref.getString("USER_NAME","unknown").replaceAll("\\s+",""));
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , "1.00");
                paramMap.put( "WEBSITE" , "APPSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId);

                PaytmOrderPojo paytmOrderPojo = new PaytmOrderPojo();
                paytmOrderPojo.setCallback(paramMap.get("CALLBACK_URL"));
                paytmOrderPojo.setChannelID(paramMap.get("CHANNEL_ID"));
                paytmOrderPojo.setMid(paramMap.get("MID"));
                paytmOrderPojo.setOrderID(paramMap.get("ORDER_ID"));
                paytmOrderPojo.setTxnAMOUNT(paramMap.get("TXN_AMOUNT"));
                paytmOrderPojo.setIndustryTYPEID(paramMap.get("INDUSTRY_TYPE_ID"));
                paytmOrderPojo.setWebsite(paramMap.get("WEBSITE"));
                paytmOrderPojo.setCustID(paramMap.get("CUST_ID"));
                paytmOrderPojo.setMobileNO(paramMap.get("MOBILE_NO"));
                paytmOrderPojo.setEmail(paramMap.get("EMAIL"));

                Call<PaytmOrderPojo> call = service.getOrderChecksum(paramMap.get("ORDER_ID"),paramMap.get("CUST_ID"),paramMap.get("TXN_AMOUNT"));
                call.enqueue(new Callback<PaytmOrderPojo>() {

                    @Override
                    public void onResponse(Call<PaytmOrderPojo> call, Response<PaytmOrderPojo> response) {

                            if(response.body().getChecksum().equals(null) || response.body().getChecksum().equals("")){
                                Toast.makeText(getApplicationContext(), "Checksum is null " , Toast.LENGTH_LONG).show();
                            }else{
                                paramMap.put( "CHECKSUMHASH" , response.body().getChecksum());
                                paytmOrder = new PaytmOrder(paramMap);

                                paytmService.initialize(paytmOrder,null);

                                paytmService.startPaymentTransaction(SelectPaymentActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                                    /*Call Backs*/
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "Payment UIError response " + inErrorMessage, Toast.LENGTH_LONG).show();
                                    }
                                    public void onTransactionResponse(Bundle inResponse) {
                                      progressDialog.dismiss();
                                        if(inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                                            j = new Intent(getApplicationContext(), OrderCompleteActivity.class);
                                            j.putExtra("tx_mode","Paytm");
                                            Date c = Calendar.getInstance().getTime();

                                            TransactionPojo usrTx = new TransactionPojo();
                                            usrTx.setTxId(String.valueOf(c.getTime()));
                                            usrTx.setTxPaymentMode("PAYTM");
                                            usrTx.setTxDate(DateUtil.getDate(today));
                                            usrTx.setTxDeliverySts("Pending");
                                            usrTx.setTxAmount(totalAmntTxt.getText().toString());
                                            usrTx.setTxDelete("N");
                                            usrTx.setTxMob(pref.getString("USER_MOB","unknown"));
                                            usrTx.setTxDeliveryDate(DateUtil.getDate(tomorrow));
                                            usrTx.setTxType("Renter");
                                            usrTx.setTxUser(pref.getString("USER_NAME","unknown"));
                                            usrTx.setTxReturnDate(returnDateTxt.getText().toString());
                                            usrTx.setTxBook(orderTitle.getText().toString());
                                            usrTx.setTxBookOwner(l.getU_id());
                                            usrTx.setTxBookOwnerMob(l.getMobile());
                                            saveTxn(usrTx);
                                            addMoneyToLenderWallet(usrTx);
                                            setNotificationAlarm(usrTx);
                                            startActivity(j);
                                        }
                                    }
                                    public void networkNotAvailable() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                    }
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Error loading web page:" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }
                                    public void onBackPressedCancelTransaction() {
                                        progressDialog.dismiss();
                                    }
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "TXN Canceled" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                    }

                    @Override
                    public void onFailure(Call<PaytmOrderPojo> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        gpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Uri uri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa","prashantm61289-1@okaxis")
                        .appendQueryParameter("pn","Prashant Mishra")
                        .appendQueryParameter("tn","Liber test transaction")
                        .appendQueryParameter("am", "01.00")
                        .appendQueryParameter("cu", "INR")
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
 ///               intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                intent.setData(uri);
                Intent chooser = Intent.createChooser(intent,"Pay using any UPI app");
                if(chooser.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, 0);
                }else{
                    Toast.makeText(getApplicationContext(),"Install any UPI app to continue.",Toast.LENGTH_LONG).show();
                }

            }
        });

        walletChckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(Integer.parseInt(walletblncAmntTxt.getText().toString())<50) {
                Toast.makeText(getApplicationContext(),"Minimum wallet balance needed is ₹50.",Toast.LENGTH_LONG).show();
                return;
            }else {
                if (((CheckBox) view).isChecked()) {
                    totalAmntTxt.setText(finalAmount());
                    walletAmntTxt.setText(String.format("%.2f", Double.valueOf(bookAmnt) - Double.parseDouble(totalAmntTxt.getText().toString())));
                } else {
                    totalAmntTxt.setText(finalAmountRvrs());
                    walletAmntTxt.setText("0");
                }
            }
            }
        });
    }

    private void setNotificationAlarm(TransactionPojo txn) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parse = null;
        try {
            parse = sdf.parse(txn.getTxReturnDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        c.set(Calendar.DATE,c.getTime().getMonth(),c.getTime().getDay(),11,1,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),1, intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
    }

    private void getWalletAmountFromServer() {

        Call<ArrayList<WalletPojo>> call = service.getUserWalletData(pref.getString("USER_MOB","unknown"));
        call.enqueue(new Callback<ArrayList<WalletPojo>>() {

            @Override
            public void onResponse(Call<ArrayList<WalletPojo>> call, Response<ArrayList<WalletPojo>> response) {
                Double amount = 0.0;
               // Toast.makeText(getApplicationContext(),response.body().get(0).getWamntAdded(),Toast.LENGTH_LONG).show();

                for(WalletPojo wallet:response.body()){
                    amount += Double.parseDouble(wallet.getWamntAdded());
                }
                totalWalletAmount = amount.intValue();
                walletAmnt = totalWalletAmount;
                walletblncAmntTxt.setText(String.valueOf(totalWalletAmount));
            }

            @Override
            public void onFailure(Call<ArrayList<WalletPojo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveTxn(TransactionPojo transactionPojo){

        Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertUserTxn(transactionPojo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(),"Transaction Saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed uploading transaction details : "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            if(data!= null){
                Bundle bundle = data.getExtras();
                Set<String> keys = bundle.keySet();
                for (String key : keys) {

                }
                String txn = data.getStringExtra("response");
                String txnId="";


                if(txn.toLowerCase().contains("success") || txn.contains("SUCCESS")){
                     j = new Intent(getApplicationContext(), OrderCompleteActivity.class);
                     j.putExtra("tx_mode","UPI");

                    String pattern = "(?<=txnId=)(.*)(?=&responseCode)";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(txn);
                    while(m.find()) {
                        txnId = m.group();
                    }
                    Date c = Calendar.getInstance().getTime();

                    TransactionPojo usrTx = new TransactionPojo();
                    usrTx.setTxId(String.valueOf(c.getTime()));
                    usrTx.setTxPaymentMode("UPI");
                    usrTx.setTxDate(DateUtil.getDate(today));
                    usrTx.setTxDeliverySts("Pending");
                    usrTx.setTxAmount(totalAmntTxt.getText().toString());
                    usrTx.setTxDelete("N");
                    usrTx.setTxMob(pref.getString("USER_MOB","unknown"));
                    usrTx.setTxDeliveryDate(DateUtil.getDate(tomorrow));
                    usrTx.setTxType("Renter");
                    usrTx.setTxUser(pref.getString("USER_NAME","unknown"));
                    usrTx.setTxReturnDate(returnDateTxt.getText().toString());
                    usrTx.setTxBook(orderTitle.getText().toString());
                    usrTx.setTxBookOwner(l.getU_id());
                    usrTx.setTxBookOwnerMob(l.getMobile());
                    saveTxn(usrTx);
                    setNotificationAlarm(usrTx);
                    addMoneyToLenderWallet(usrTx);
                    progressDialog.dismiss();
                    startActivity(j);
                }
            }
        }
    }

    public String finalAmount(){
        String finalAmount;
        if(walletAmnt>=0 && walletAmnt<=bookAmnt){
            finalAmount = String.valueOf(bookAmnt - walletAmnt);
            gstAmount = calculateTotalGST(finalAmount);
            return  String.valueOf(Double.parseDouble(finalAmount)+gstAmount);
        }else if(walletAmnt>bookAmnt){
            walletAmntTxt.setText(String.format("%.2f",bookAmnt));
            finalAmount = String.valueOf(0);
            return finalAmount;
        } else{
            finalAmount = String.valueOf(bookAmnt);
            gstAmount = calculateTotalGST(finalAmount);
            return  String.valueOf(Double.parseDouble(finalAmount)+gstAmount);
        }
    }

    private double calculateTotalGST(String beforeGST) {
        double gstPerc = 0.05;
        double b4GST = Double.valueOf(beforeGST);
        double afterGST = gstPerc * b4GST;

        gstTxt.setText(String.valueOf(afterGST));
        return afterGST;
    }

    public String finalAmountRvrs(){
        int walletAmnt = 0;
        //int bookAmnt = 70;
       gstAmount = calculateTotalGST(String.valueOf(bookAmnt));
       Double doubleBookAmnt = Double.valueOf(bookAmnt);
        return String.valueOf(doubleBookAmnt+gstAmount);
    }

    @Override
    public void onBackPressed() {

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Are you sure to cancel this order?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SelectPaymentActivity.super.onBackPressed();
                    }
                });

        snackbar.show();
    }

    public void addMoneyToLenderWallet(TransactionPojo txn){

        WalletPojo money = new WalletPojo();
        money.setWuid(txn.getTxBookOwner());
        money.setWmob(txn.getTxBookOwnerMob());
        money.setWamntAdded(String.valueOf(Double.parseDouble(txn.getTxAmount())*0.40));
        money.setWbookDate(DateUtil.getDate(Calendar.getInstance().getTime()));
        money.setWdelete("N");
        money.setWbookName(txn.getTxBook());

        Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertMoneyInToWallet(money);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(),"Amount added to lender's Liber acount",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Error :"+response.errorBody(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Error :"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
