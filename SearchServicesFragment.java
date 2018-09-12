package hr.nmsolutions.ez_bookermobile.services;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import hr.nmsolutions.ez_bookermobile.Helper;
import hr.nmsolutions.ez_bookermobile.IdValue;
import hr.nmsolutions.ez_bookermobile.MainApplication;
import hr.nmsolutions.ez_bookermobile.R;
import hr.nmsolutions.ez_bookermobile.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchServicesFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private LinearLayout parentLayout;

    private ProgressBar mProgressBar;

    private Spinner categoriesSpinner;
    private Spinner locationsSpinner;
    private Spinner servicesSpinner;
    private EditText calendarEditText;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;

    ArrayAdapter<IdValue> servicesSpinnerAdapter;
    ArrayList<IdValue> servicesSpinnerData;
    ArrayList<IdValue> servicesSpinnerDataMasterCopy;

    Button tryAgainButton;

    public SearchServicesFragment() {
        // Required empty public constructor
    }

    public static SearchServicesFragment newInstance() {
        SearchServicesFragment fragment = new SearchServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_services_fragment, container, false);

        parentLayout = view.findViewById(R.id.search_services_parent_layout);
        mProgressBar = view.findViewById(R.id.progress_bar_load_search_filters);
        tryAgainButton = view.findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tryAgainButton.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        getServiceFilters();
                    }
                }, 1000);
            }
        });

        categoriesSpinner = (Spinner) view. findViewById(R.id.categories_spinner);
        locationsSpinner = (Spinner) view. findViewById(R.id.locations_spinner);
        servicesSpinner = (Spinner) view. findViewById(R.id.services_spinner);

        myCalendar = Calendar.getInstance();

        calendarEditText = (EditText) view.findViewById(R.id.search_date_et);
        calendarEditText.setInputType(InputType.TYPE_NULL);
        updateLabel(new Date());

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar.getTime());
            }

        };

        calendarEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("qwe", "bla");
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // **********************
        tryAgainButton.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                getServiceFilters();
            }
        }, 1000);


        view.findViewById(R.id.search_services_parent_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(getActivity());
                return false;
            }
        });

        return view;
    }

    private void updateLabel(Date date) {
        String myFormat = "MM/dd/yy  EEE";
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);

        calendarEditText.setText(sdf.format(date));
    }

    private void getServiceFilters() {

        tryAgainButton.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        User user = MainApplication.user;

        MainApplication.apiManager.getServiceFilters(user.getEmail(), user.getPassword(), new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response.body() != null) {

                    JsonObject json = response.body();

                    JsonObject obj = json.getAsJsonObject("EZMobileServiceFilterRS");
                    if(obj == null){
                        handleError();
                        return;
                    }

                    if(obj.getAsJsonObject("fail") != null){
                        obj = obj.getAsJsonObject("fail");

                        Log.e("api", obj.getAsJsonArray("errors").toString());
                        handleError();

                        return;
                    } else {
                        obj = obj.getAsJsonObject("success");

                        if(obj == null){
                            handleError();
                            return;
                        }
                    }

                    parentLayout.setVisibility(View.VISIBLE);

                    // SERVICES
                    JsonObject filterServices = obj.getAsJsonObject("filter_services");
                    ArrayList<String> servicesKeys = new ArrayList(filterServices.keySet());

                    servicesSpinnerData = new ArrayList<IdValue>();
                    servicesSpinnerDataMasterCopy = new ArrayList<IdValue>();

                    for(String key: servicesKeys){
                        JsonElement je = filterServices.get(key);
                        if(je instanceof JsonObject){
                            JsonObject serviceJsonObject = (JsonObject) je;
                            IdValue iv = new IdValue(key, serviceJsonObject.get("service_name").getAsString(), serviceJsonObject.get("category_id").getAsString(), serviceJsonObject.get("location_id").getAsString());
                            servicesSpinnerData.add(iv);
                            servicesSpinnerDataMasterCopy.add(iv);
                        } else {
                            String value = je.getAsString();
                            if(key.equals("filter_all")){
                                value = "All";
                                servicesSpinnerData.add(new IdValue(key, value));
                            }
                        }
                    }

                    // SERVICES SPINNER
                    servicesSpinnerAdapter = new ArrayAdapter<IdValue>(getActivity(), R.layout.custom_spinner_first_item, servicesSpinnerData);

                    servicesSpinner.setAdapter(servicesSpinnerAdapter);
                    servicesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                    servicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });// *****************************************************************************************************************

                    // CATEGORIES
                    JsonObject filterCategories = obj.getAsJsonObject("filter_categories");
                    ArrayList<String> categoriesKeys = new ArrayList(filterCategories.keySet());

                    ArrayList<IdValue> categoriesSpinnerData = new ArrayList<IdValue>();
                    for(String key: categoriesKeys){
                        String value = filterCategories.get(key).getAsString();
                        if(key.equals("filter_all")){
                            value = "All";
                        }

                        categoriesSpinnerData.add(new IdValue(key, value));
                    }

                    // IZBRISI OVO
                    categoriesSpinnerData.add(new IdValue("5", "Kategorijski kurac"));

                    // CATEGORIES SPINNER
                    ArrayAdapter<IdValue> categoriesSpinnerAdapter = new ArrayAdapter<IdValue>(getActivity(), R.layout.custom_spinner_first_item, categoriesSpinnerData);

                    categoriesSpinner.setAdapter(categoriesSpinnerAdapter);
                    categoriesSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                    categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            IdValue item = (IdValue) parent.getItemAtPosition(position);
                            String categoryID = item.getId();

                            servicesSpinnerData.clear();
                            servicesSpinnerData.addAll(servicesSpinnerDataMasterCopy);

                            if(!categoryID.equals("filter_all")){
                                for (Iterator<IdValue> iterator = servicesSpinnerData.iterator(); iterator.hasNext();) {
                                    IdValue listItem = iterator.next();
                                    Log.d("qwe", (listItem.getCategoryId()) + "");
                                    if (listItem.getCategoryId() != null && !listItem.getCategoryId().equals(categoryID)) {
                                        iterator.remove();
                                    }
                                }
                            }

                            if(servicesSpinnerData.size() > 1){
                                servicesSpinnerData.add(0, new IdValue("filter_all", "All"));
                            }

                            if(servicesSpinnerData.size() == 0){
                                servicesSpinnerData.add(0, new IdValue("no_data", "No Services for this category"));
                            }

                            servicesSpinnerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });// *****************************************************************************************************************

                    // LOCATIONS
                    JsonObject filterLocations = obj.getAsJsonObject("filter_locations");
                    ArrayList<String> locationsKeys = new ArrayList(filterLocations.keySet());

                    ArrayList<IdValue> locationsSpinnerData = new ArrayList<>();
                    for(String key: locationsKeys){
                        String value = filterLocations.get(key).getAsString();
                        if(key.equals("filter_all")){
                            value = "All";
                        }

                        locationsSpinnerData.add(new IdValue(key, value));
                    }

                    // IZBRISI OVO
                    locationsSpinnerData.add(new IdValue("5", "Picka mila materina"));

                    // LOCATIONS SPINNER
                    ArrayAdapter<IdValue> locationsSpinnerAdapter = new ArrayAdapter<IdValue>(getActivity(), R.layout.custom_spinner_first_item, locationsSpinnerData);

                    locationsSpinner.setAdapter(locationsSpinnerAdapter);
                    locationsSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
                    locationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            IdValue item = (IdValue) parent.getItemAtPosition(position);
                            String locationId = item.getId();

                            servicesSpinnerData.clear();
                            servicesSpinnerData.addAll(servicesSpinnerDataMasterCopy);

                            if(!locationId.equals("filter_all")){
                                for (Iterator<IdValue> iterator = servicesSpinnerData.iterator(); iterator.hasNext();) {
                                    IdValue listItem = iterator.next();
                                    if (listItem.getLocationId() != null && !listItem.getLocationId().equals(locationId)) {
                                        iterator.remove();
                                    }
                                }
                            }

                            if(servicesSpinnerData.size() > 1){
                                servicesSpinnerData.add(0, new IdValue("filter_all", "All"));
                            }

                            if(servicesSpinnerData.size() == 0){
                                servicesSpinnerData.add(0, new IdValue("no_data", "No Services for this location"));
                            }

                            servicesSpinnerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else {
                    Log.e("api", "Error in login:" + response.code() + " " + response.message());
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("api", "Response Error " + t.getLocalizedMessage());
                handleError();

            }
        });
    }

    private void handleError(){
        tryAgainButton.setVisibility(View.VISIBLE);
        parentLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
