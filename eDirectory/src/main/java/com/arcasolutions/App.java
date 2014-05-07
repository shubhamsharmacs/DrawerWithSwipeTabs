package com.arcasolutions;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.arcasolutions.api.Client;
import com.weedfinder.R;

import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

public class App extends Application {

    private ConnectionFactoryRegistry mConnectionFactoryRegistry;
    private ConnectionRepository mConnectionRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectionFactoryRegistry = new ConnectionFactoryRegistry();
        mConnectionFactoryRegistry.addConnectionFactory(
                new FacebookConnectionFactory(getFacebookAppId(), getFacebookAppSecret()));

        SQLiteOpenHelper mRepositoryHelper = new SQLiteConnectionRepositoryHelper(this);
        mConnectionRepository = new SQLiteConnectionRepository(
                mRepositoryHelper,
                mConnectionFactoryRegistry,
                AndroidEncryptors.text("password", "5c0744940b5c369b"));

        Client.EDIR_LANG = getEdirLang();
    }

    public ConnectionRepository getConnectionRepository() {
        return mConnectionRepository;
    }

    public FacebookConnectionFactory getFacebookConnectionFactory() {
        return (FacebookConnectionFactory) mConnectionFactoryRegistry
                .getConnectionFactory(Facebook.class);
    }

    public boolean isFacebookConnected() {
        return mConnectionRepository.findPrimaryConnection(Facebook.class) != null;
    }

    public void facebookDisconnect() {
        mConnectionRepository.removeConnections(getFacebookConnectionFactory().getProviderId());
    }

    public Facebook getFacebook() {
        return mConnectionRepository.findPrimaryConnection(Facebook.class).getApi();
    }

    private String getFacebookAppId() {
        return getString(R.string.facebookAppId);
    }

    private String getFacebookAppSecret() {
        return getString(R.string.facebookAppSecret);
    }

    private String getEdirLang() {
        return getString(R.string.edir_lang);
    }
}
