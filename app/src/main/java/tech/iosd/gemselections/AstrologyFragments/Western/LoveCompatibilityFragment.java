package tech.iosd.gemselections.AstrologyFragments.Western;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.iosd.gemselections.R;
import tech.iosd.gemselections.Retrofit.AstrologyApiClient;
import tech.iosd.gemselections.Retrofit.AstrologyApiInterface;
import tech.iosd.gemselections.Retrofit.RequestModels.WesternAstrologyComplexRequest;
import tech.iosd.gemselections.Retrofit.ResponseModels.KarmaDestinyResponse;
import tech.iosd.gemselections.Retrofit.ResponseModels.LoveCompatibilityResponse;
import tech.iosd.gemselections.Utils.Constants;

/**
 * Created by anubhavmalik on 26/03/18.
 */

public class LoveCompatibilityFragment extends Fragment {
    Retrofit retrofit;
    ArrayList<String> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_report_frag, container, false);

        AstrologyApiClient astrologyApiClient = new AstrologyApiClient();
        retrofit = astrologyApiClient.getRetrofit();

        AstrologyApiInterface astrologyApiInterface = retrofit.create(AstrologyApiInterface.class);
        final TextView responseTextView = view.findViewById(R.id.general_report_text_view);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading ... ");
        progressDialog.show();

        Bundle bundle = this.getArguments();
        bundle = new Bundle();
        if (bundle != null) {

            WesternAstrologyComplexRequest westernAstrologyComplexRequest = new WesternAstrologyComplexRequest(20, 2, 1992, 12, 12, Constants.PRIMARY_LAT, Constants.PRIMARY_LNG, Constants.TIMEZONE, 20, 2, 1992, 12, 12, Constants.PRIMARY_LAT, Constants.PRIMARY_LNG, Constants.TIMEZONE);
//                = new WesternAstrologySimpleRequest(bundle.getInt(Constants.PRIMARY_DAY,1)
//                ,bundle.getInt(Constants.PRIMARY_MONTH,1)
//                ,bundle.getInt(Constants.PRIMARY_YEAR,2018)
//                ,bundle.getInt(Constants.PRIMARY_HOUR,1)
//                ,bundle.getInt(Constants.PRIMARY_MIN,1)
//                ,Constants.PRIMARY_LAT
//                ,Constants.PRIMARY_LNG
//                ,Constants.TIMEZONE);


            Call<LoveCompatibilityResponse> call = astrologyApiInterface
                    .getLoveCompatibilityReport(AstrologyApiInterface.HEADER_TOKEN, westernAstrologyComplexRequest);

            call.enqueue(new Callback<LoveCompatibilityResponse>() {
                @Override
                public void onResponse(Call<LoveCompatibilityResponse> call, Response<LoveCompatibilityResponse> response) {
                    Log.d("TAGGER", "RESPONSE CODE : " + response.code());
                    Log.d("TAGGER", "RESPONSE BODY : " + response.body());
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
//                        String responseString = "";
                        Log.d("TAGGER", "RESPONSE SUCCESS");
                        arrayList.addAll(response.body().getLoveReport());
                        for (int i = 0; i < response.body().getLoveReport().size(); i++) {
//                            responseString = responseString.concat(response.body().getReport().get(i));
                            responseTextView.append("\n" + response.body().getLoveReport().get(i)+"\n");
                        }
//                        responseTextView.append("\n Key Quality : "+response.body().getKeyQuality());
//                        responseTextView.append("\n Spiritual Lesson : "+ response.body().getSpiritualLesson());

                    }
                }

                @Override
                public void onFailure(Call<LoveCompatibilityResponse> call, Throwable t) {

                    Log.d("TAGGER", "RESPONSE FAILURE");
                    Log.d("TAGGER", t.getMessage());
                    Snackbar.make(responseTextView, "PLEASE RETRY", Snackbar.LENGTH_INDEFINITE);

                    progressDialog.dismiss();
                }
            });


        } else {
            progressDialog.dismiss();
            Snackbar.make(responseTextView, "PLEASE RETRY", Snackbar.LENGTH_INDEFINITE);
        }
        return view;
    }
}
