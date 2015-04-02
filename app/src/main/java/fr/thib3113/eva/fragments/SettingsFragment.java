package fr.thib3113.eva.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import fr.thib3113.eva.MainActivity;
import fr.thib3113.eva.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends android.support.v4.app.Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        EditText LocaleIpField = (EditText) super.getActivity().findViewById(R.id.localeip);
        EditText DistantIpField = (EditText) super.getActivity().findViewById(R.id.distantip);
        EditText DsernameField = (EditText) super.getActivity().findViewById(R.id.username);
        EditText PasswordField = (EditText) super.getActivity().findViewById(R.id.passwd);

        String config_localeIp = MainActivity.config_localeIp;
        String config_distantIp = MainActivity.config_distantIp;
        String config_username = MainActivity.config_username;
        String config_password = MainActivity.config_password;
        if(config_localeIp != null)
            LocaleIpField.setText(config_localeIp);
        if(config_distantIp != null)
            DistantIpField.setText(config_distantIp);
        if(config_username != null)
            DsernameField.setText(config_username);
        if(config_password != null)
            PasswordField.setText(config_password);
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = super.onCreateView(inflater, container, savedInstanceState);
//
//        Toast.makeText(MainActivity.getAppContext(), "on create", Toast.LENGTH_SHORT).show();
//        View myView=getActivity().findViewById(R.id.setting_container);
//        myView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                final Button mButton = (Button) getActivity().findViewById(R.id.checkConfigButton);
//                Toast.makeText(MainActivity.getAppContext(), "listener OK", Toast.LENGTH_SHORT).show();
//                mButton.setOnClickListener(
//                        new View.OnClickListener() {
//                            public String extract(EditText s){
//                                return s.getText().toString();
//                            }
//
//                            public void onClick(View view) {
//                                //get EditText
//                                String localeip = extract((EditText) getActivity().findViewById(R.id.localeip));
//                                String distantip = extract((EditText) getActivity().findViewById(R.id.distantip));
//                                String username = extract((EditText) getActivity().findViewById(R.id.username));
//                                String password = extract((EditText) getActivity().findViewById(R.id.passwd));
//
//                                String[] mStrings  =  {localeip, distantip, username, password};
//                                boolean one_empty = false;
//                                for( int i=0; i < mStrings.length; i++){
//                                    if(mStrings[i].length() < 1){
//                                        one_empty = true;
//                                    }
//                                }
//
//                                if(one_empty){
//                                    Toast.makeText(MainActivity.getAppContext(), "Vous devez remplir tous les champs", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//
//                                ((MainActivity)getActivity()).config(localeip,  distantip,  username,  password);
////                                config_handler.sendMessage(config_handler.obtainMessage());
//                            }
//                        }
//                );
//            }
//        });
//        return v;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
