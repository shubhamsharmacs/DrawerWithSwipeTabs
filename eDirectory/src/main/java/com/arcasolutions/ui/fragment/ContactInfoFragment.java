package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.implementation.IContactInfo;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.util.CheckInHelper;
import com.arcasolutions.util.IntentUtil;

public class ContactInfoFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_CONTACT_INFO = "contact_info";
    private LayoutInflater mInflater;
    private LinearLayout mLinearLayout;
    private CheckInHelper mCheckInHelper;

    public ContactInfoFragment() {
    }

    public static ContactInfoFragment newInstance(IContactInfo contactInfo) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_CONTACT_INFO, contactInfo);

        final ContactInfoFragment f = new ContactInfoFragment();
        f.setArguments(args);
        return f;
    }

    public IContactInfo getShownContactInfo() {
        return getArguments() != null
                ? (IContactInfo) getArguments().getParcelable(ARG_CONTACT_INFO)
                : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = LayoutInflater.from(getActivity());

        IContactInfo contact = getShownContactInfo();
        if (contact instanceof  Listing) {
            Listing listing = (Listing) contact;
            mCheckInHelper = new CheckInHelper(this, listing);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_contact_info, container, false);
        return mLinearLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IContactInfo contactInfo = getShownContactInfo();
        if (contactInfo != null) {

            if (!TextUtils.isEmpty(contactInfo.getPhoneNumber()) && !(contactInfo instanceof Listing)) {
                addContactInfo(
                        R.id.contactPhoneNumber,
                        R.drawable.ic_action_device_access_call,
                        getString(R.string.call),
                        contactInfo.getPhoneNumber());
            }

            if (!TextUtils.isEmpty(contactInfo.getEmail())) {
                addContactInfo(
                        R.id.contactEmail,
                        R.drawable.ic_action_content_email,
                        getString(R.string.send_email),
                        contactInfo.getEmail());
            }

            if (!TextUtils.isEmpty(contactInfo.getUrl())) {
                addContactInfo(
                        R.id.contactUrl,
                        R.drawable.ic_action_location_web_site,
                        getString(R.string.visit_webpage),
                        contactInfo.getUrl());
            }

            if (mCheckInHelper != null) {
                View v = addContactInfo(
                        R.id.contactCheckIn,
                        R.drawable.ic_action_marker,
                        getString(R.string.check_in_here),
                        null
                );
                v.setClickable(true);
                v.setOnClickListener(mCheckInHelper);
            }
        }
    }

    private View addContactInfo(int id, int drawableId, CharSequence text1, CharSequence text2) {
        View view = mInflater.inflate(R.layout.simple_list_item_contact, mLinearLayout, false);
        view.setId(id);
        AQuery aq = new AQuery(view);
        aq.id(android.R.id.icon).image(drawableId);
        aq.id(android.R.id.text1).text(text1);
        aq.id(android.R.id.text2).text(text2, true);
        aq.tag(text2);
        mLinearLayout.addView(view);
        mLinearLayout.requestLayout();
        view.setClickable(true);
        view.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactPhoneNumber:
                IntentUtil.call(getActivity(), getShownContactInfo().getPhoneNumber());
                break;

            case R.id.contactEmail:
                IntentUtil.email(getActivity(),
                        getShownContactInfo().getEmail(),
                        getString(R.string.contact), null);
                break;

            case R.id.contactUrl:
                IntentUtil.website(getActivity(), getShownContactInfo().getUrl());
                break;

            case R.id.contactCheckIn:

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckInHelper.onActivityResult(requestCode, resultCode, data);
    }
}
