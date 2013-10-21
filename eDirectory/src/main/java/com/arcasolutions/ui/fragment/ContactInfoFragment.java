package com.arcasolutions.ui.fragment;

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

public class ContactInfoFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_CONTACT_INFO = "contact_info";
    private LayoutInflater mInflater;
    private LinearLayout mLinearLayout;

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

            if (!TextUtils.isEmpty(contactInfo.getPhoneNumber())) {
                addContactInfo(
                        R.id.contactPhoneNumber,
                        R.drawable.ic_action_device_access_call,
                        "Call",
                        contactInfo.getPhoneNumber());
            }

            if (!TextUtils.isEmpty(contactInfo.getEmail())) {
                addContactInfo(
                        R.id.contactEmail,
                        R.drawable.ic_action_content_email,
                        "Send Email",
                        contactInfo.getEmail());
            }

            if (!TextUtils.isEmpty(contactInfo.getUrl())) {
                addContactInfo(
                        R.id.contactUrl,
                        R.drawable.ic_action_location_web_site,
                        "Visit Webpage",
                        contactInfo.getUrl());
            }
        }
    }

    private void addContactInfo(int id, int drawableId, CharSequence text1, CharSequence text2) {
        View view = mInflater.inflate(R.layout.simple_list_item_contact, mLinearLayout, false);
        view.setId(id);
        AQuery aq = new AQuery(view);
        aq.id(android.R.id.icon).image(drawableId);
        aq.id(android.R.id.text1).text(text1);
        aq.id(android.R.id.text2).text(text2);
        aq.tag(text2);
        aq.clicked(this);
        mLinearLayout.addView(view);
        mLinearLayout.requestLayout();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactPhoneNumber:
                break;

            case R.id.contactEmail:
                break;

            case R.id.contactUrl:
                break;
        }
    }
}
