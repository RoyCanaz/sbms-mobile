package org.totalit.sbms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.thomashaertel.widget.MultiSpinner;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.totalit.sbms.CallListerner.BrandListener;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Repository.GetSupplierByName;
import org.totalit.sbms.Repository.SupplierList;
import org.totalit.sbms.Utils.ProductValidations;
import org.totalit.sbms.domain.Product;
import org.totalit.sbms.domain.Stock;
import org.totalit.sbms.domain.Supplier;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.StockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddComputerActivity extends AppCompatActivity {

    @BindView(R.id.btn_add_product)
    Button buttonAddProduct;

    @BindView(R.id.product_brand)
    SearchableSpinner selectBrand;

    @BindView(R.id.suppliers)
    MultiSpinner supplierSpinner;

    @BindView(R.id.product_monthyear)
    SearchableSpinner selectMonthYear;

    @BindView(R.id.product_os)
    SearchableSpinner selectOs;

    @BindView(R.id.product_harddrive)
    SearchableSpinner selectHarddrive;

    @BindView(R.id.product_memory)
    SearchableSpinner selectMemory;

    @BindView(R.id.product_processor)
    SearchableSpinner selectProcessor;

    @BindView(R.id.pro_layout_model)
    TextInputLayout textModel;

    @BindView(R.id.pro_layout_description)
    TextInputLayout textDescription;

    @BindView(R.id.pro_layout_display)
    TextInputLayout textDisplay;

    @BindView(R.id.pro_layout_landing)
    TextInputLayout textLanding;

    @BindView(R.id.pro_layout_quantity)
    TextInputLayout textQuantity;

    @BindView(R.id.pro_layout_selling)
    TextInputLayout textSelling;

    @BindView(R.id.pro_layout_reorderL)
    TextInputLayout textReorderLevel;

    @BindView(R.id.pro_layout_reorderQ)
    TextInputLayout textReorderQuantity;

    @BindView(R.id.input_pro_warranty)
    EditText inputWarranty;

    @BindView(R.id.input_pro_model)
    EditText inputModel;

    @BindView(R.id.input_pro_description)
    EditText inputDescription;

    @BindView(R.id.input_pro_display)
    EditText inputDisplay;

    @BindView(R.id.input_pro_landingcost)
    EditText inputLandingCost;

    @BindView(R.id.input_pro_quantity)
    EditText inputQuantity;

    @BindView(R.id.input_pro_selling)
    EditText inputSelling;

    @BindView(R.id.input_pro_reorderL)
    EditText inputReorderLevel;

    @BindView(R.id.input_pro_reorderQ)
    EditText inputReorderQuantity;

    String categoryName;
    long categoryId;
    long companyId;

    SharedPreferences sharedPreferences;
    String username;
    String password;
    AppDatabase mdb;
    ArrayAdapter<String> supplierAdapter;
    List<Supplier> supplierList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_computer);
        ButterKnife.bind(this);
        mdb = AppDatabase.getFileDatabase(this);
        sharedPreferences = getApplicationContext().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        Intent i = getIntent();
        categoryId = i.getLongExtra("categoryId", 0);
        Log.e("Category Id", categoryId+"");
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0);
        categoryName = i.getStringExtra("categoryName");
        try {
            populateArrays();
        } catch (ExecutionException | InterruptedException e) {

        }

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Add " + categoryName);

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       onBackPressed();
       return true;

    }
    public void populateArrays() throws ExecutionException, InterruptedException {
        selectBrand.setTitle("Select Brand");
        selectBrand.setPositiveButton("OK");

        selectMonthYear.setTitle("Select Month/Year");
        selectMonthYear.setPositiveButton("OK");
        selectHarddrive.setTitle("Select Harddrive");
        selectHarddrive.setPositiveButton("OK");
        selectMemory.setTitle("Select Memory");
        selectMemory.setPositiveButton("OK");
        selectProcessor.setTitle("Select Processor");
        selectProcessor.setPositiveButton("OK");
        final ArrayList<String> strings = new ArrayList<>();
        getBrands(new BrandListener() {
            @Override
            public void brands(ArrayList<String> i) {
                strings.addAll(i);

            }
        });
        ArrayAdapter adapter = new ArrayAdapter(AddComputerActivity.this, R.layout.support_simple_spinner_dropdown_item, strings);
        selectBrand.setAdapter(adapter);


        List<Supplier> suppliers = new SupplierList(mdb).execute().get();
        ArrayList<String> arrayList = new ArrayList<>();
        for(Supplier supplier : suppliers){
            arrayList.add(supplier.getName());
        }

       supplierAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);

        supplierSpinner.setAdapter(supplierAdapter, false, onSelectedListener);

    /*    boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[0] = true;
        supplierSpinner.setSelected(selectedItems);*/

    }
    public void getBrands(final BrandListener brandListener){
        StockService stockService = ServiceGenerator.createService(StockService.class, username, password);
        Call<ArrayList<String>> stockCall = stockService.getBrands();
        stockCall.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()){
                    brandListener.brands(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(AddComputerActivity.this, "Failed to Retrieve Brands !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            StringBuilder builder = new StringBuilder();
             supplierList.clear();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                  //  builder.append(supplierAdapter.getItem(i)).append(" ");
                    try {
                        supplierList.add(new GetSupplierByName(mdb, supplierAdapter.getItem(i)).execute().get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

           // Toast.makeText(AddComputerActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }
    };

             public void buttonAction(){
                  StringBuilder builder = new StringBuilder();
                  for(Supplier s : supplierList){
                      builder.append(s.getName()).append("");
                  }
                 getProduct();
                }

                public void getProduct(){


                        ProductValidations.setSpinnerError(selectBrand, getApplicationContext());


                 if(!ProductValidations.validate(inputModel.getText().toString(), textModel, getWindow()) |
                     !ProductValidations.validate(inputQuantity.getText().toString(), textQuantity, getWindow()) |
                     !ProductValidations.validate(inputSelling.getText().toString(), textSelling, getWindow()) |
                         !ProductValidations.setSpinnerError(selectBrand, getApplicationContext())|
                    !ProductValidations.setSpinnerError(selectHarddrive, getApplicationContext()) |
                    !ProductValidations.setSpinnerError(selectMemory, getApplicationContext()) |
                    !ProductValidations.setSpinnerError(selectProcessor, getApplicationContext())|
                    !ProductValidations.setSpinnerError(selectOs, getApplicationContext())){
                     return;
                 }


                    final String brand = selectBrand.getSelectedItem().toString();
                    final String model = inputModel.getText().toString();
                    String warranty = inputWarranty.getText().toString();
                    String monthYear = selectMonthYear.getSelectedItem().toString();
                    String description = inputDescription.getText().toString();
                    String quantityDelivered = inputQuantity.getText().toString();
                    Double landingCost = inputLandingCost.getText().toString().isEmpty()? null : Double.parseDouble(inputLandingCost.getText().toString());

                    Double sellingPrice = Double.parseDouble(inputSelling.getText().toString());
                    Long reOrderLevel = inputReorderLevel.getText().toString().isEmpty()? null : Long.parseLong(inputReorderLevel.getText().toString());
                    Long reOrderQuantity =inputReorderQuantity.getText().toString().isEmpty()? null : Long.parseLong(inputReorderQuantity.getText().toString());
                    String display = inputDisplay.getText().toString();
                    String processor = selectProcessor.getSelectedItem().toString();
                    String memory = selectMemory.getSelectedItem().toString();
                    String hardDrive = selectHarddrive.getSelectedItem().toString();
                    String os = selectOs.getSelectedItem().toString();
                    String productCode = generateProductCode(model, brand, categoryName, quantityDelivered);
                    Product p  = new Product();
                    p.setBrand(brand);
                    p.setModel(model);
                    p.setDescription(description);
                    p.setAvailableStock(Long.valueOf(quantityDelivered));
                    p.setSuppliers(supplierList);
                    p.setMonthYear(monthYear);
                    p.setWarranty(warranty);
                    p.setProductCode(productCode);
                    p.setQuantityDelivered(quantityDelivered);


                    p.setOs(os);
                    p.setProcessor(processor);
                    p.setMemory(memory);
                    p.setHardDrive(hardDrive);
                    p.setDisplay(display);
                    p.setSellingPrice(sellingPrice);
                    p.setReOrderLevel(reOrderLevel);
                    p.setReOrderQuantity(reOrderQuantity);

                    p.setCategoryId(categoryId);
                    p.setCompanyId(companyId);
                      StockService stockService  = ServiceGenerator.createService(StockService.class, username, password);
                      Call<List<Stock>> productCall = stockService.addProduct(p);
                      productCall.enqueue(new Callback<List<Stock>>() {
                          @Override
                          public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                              if(response.isSuccessful()){

                                 List<Stock> list= response.body();
                                  ArrayList<Stock> stockList = new ArrayList<Stock>();
                                  for(Stock stock : list){
                                     stockList.add(stock);
                                  }
                                  Log.e("Data", ServiceGenerator.gson.toJson(response.body()));
                                  Toast.makeText(AddComputerActivity.this, "Product Success", Toast.LENGTH_SHORT).show();
                                  Intent intent = new Intent(AddComputerActivity.this, SerialNumberActivity.class);
                                  intent.putExtra("brand", brand);
                                  intent.putExtra("model", model);
                                  Bundle bundle = new Bundle();
                                  bundle.putParcelableArrayList("StockList", stockList);
                                  intent.putExtras(bundle);
                                  startActivity(intent);
                              }
                          }

                          @Override
                          public void onFailure(Call<List<Stock>> call, Throwable t) {
                                 Log.e("Add Product Error", t.getMessage());
                          }
                      });

                    Log.e("Json", ServiceGenerator.gson.toJson(p));
                }

                public String generateProductCode(String model, String brand, String category, String quantity){
                    Random rand = new Random();
                    int value = rand.nextInt(99);
                    String newCate = category.substring(1,4);
                    String newBrand = brand.substring(0, 2);
                    String newModel = "MOD";
                    if(model.length()>3){
                        newModel = model.substring(0, 3);
                    }
                    String finalCode = newCate.concat(newBrand).concat(newModel);
                    String code = finalCode.substring(0, 6);
                    String productCode = String.valueOf(value).concat(code).concat(quantity);
                    return productCode.toUpperCase();
                }




}
