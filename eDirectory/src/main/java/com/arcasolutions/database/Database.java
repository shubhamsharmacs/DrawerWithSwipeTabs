package com.arcasolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.arcasolutions.api.model.Account;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "edirectory.db";

    private static final int OLD_DATABASE_VERSION = 1;
    private static final int NEW_DATABASE_VERSION = 2;
    private static final int DATABASE_VERSION_7_1_1 = 3;
    private static final int DATABASE_VERSION_7_1_2 = 4;


    private static final int DATABASE_VERSION = DATABASE_VERSION_7_1_2;

    private interface OldTables {
        String COUNTRY_TABLE_NAME = "Country";
        String STATE_TABLE_NAME = "State";
        String CITY_TABLE_NAME = "City";
        String LOGIN_TABLE_NAME = "LoginAccount";
        String LOGIN_FACEBOOK_TABLE_NAME = "LoginFacebook";
        String SETTING_TABLE_NAME = "Setting";
    }

    public interface Tables {
        String ARTICLES = "articles";
        String CLASSIFIEDS = "classifieds";
        String DEALS = "deals";
        String EVENTS = "events";
        String LISTINGS = "listings";
        String NOTIFICATIONS = "notifications";
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        createNewDatabaseTables(db);
        changeDataBase_V7_1_1(db);
        changeDatabase_V7_1_2(source);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        // Update old database to new database dropping old tables
        if (oldVersion == OLD_DATABASE_VERSION
                && newVersion == NEW_DATABASE_VERSION) {

            // Drop all old tables
            db.execSQL("DROP TABLE " + OldTables.COUNTRY_TABLE_NAME);
            db.execSQL("DROP TABLE " + OldTables.STATE_TABLE_NAME);
            db.execSQL("DROP TABLE " + OldTables.CITY_TABLE_NAME);
            db.execSQL("DROP TABLE " + OldTables.LOGIN_TABLE_NAME);
            db.execSQL("DROP TABLE " + OldTables.LOGIN_FACEBOOK_TABLE_NAME);
            db.execSQL("DROP TABLE " + OldTables.SETTING_TABLE_NAME);

            createNewDatabaseTables(db);
        }

        if (newVersion == DATABASE_VERSION_7_1_1) {
            changeDataBase_V7_1_1(db);
        }

        if (newVersion >= DATABASE_VERSION_7_1_2) {
            changeDatabase_V7_1_2(source);
        }
    }

    private void changeDatabase_V7_1_2(ConnectionSource source) {
        try {
            TableUtils.createTableIfNotExists(source, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeDataBase_V7_1_1(SQLiteDatabase db) {
        // Add columns lat lng to classifieds
        db.execSQL("ALTER TABLE " + Tables.CLASSIFIEDS
                + " ADD COLUMN "
                + ClassifiedsColumns.CLASSIFIED_LATITUDE + " DOUBLE");
        db.execSQL("ALTER TABLE " + Tables.CLASSIFIEDS
                + " ADD COLUMN "
                + ClassifiedsColumns.CLASSIFIED_LONGITUDE + " DOUBLE");

        // Add columns lat lng to events
        db.execSQL("ALTER TABLE " + Tables.EVENTS
                + " ADD COLUMN "
                + EventsColumns.EVENT_LATITUDE + " DOUBLE");
        db.execSQL("ALTER TABLE " + Tables.EVENTS
                + " ADD COLUMN "
                + EventsColumns.EVENT_LONGITUDE + " DOUBLE");

        // Add columns lat lng to listings
        db.execSQL("ALTER TABLE " + Tables.LISTINGS
                + " ADD COLUMN "
                + ListingsColumns.LISTING_LATITUDE + " DOUBLE");
        db.execSQL("ALTER TABLE " + Tables.LISTINGS
                + " ADD COLUMN "
                + ListingsColumns.LISTING_LONGITUDE + " DOUBLE");

        // create notification table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.NOTIFICATIONS + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NotificationsColumns.NOTIFICATION_ID + " INTEGER,"
                + NotificationsColumns.NOTIFICATION_TITLE + " TEXT,"
                + NotificationsColumns.NOTIFICATION_DESCRIPTION + " TEXT,"
                + "UNIQUE ("
                + NotificationsColumns.NOTIFICATION_ID + ","
                + NotificationsColumns.NOTIFICATION_TITLE + ","
                + NotificationsColumns.NOTIFICATION_DESCRIPTION + "))");
    }

    private void createNewDatabaseTables(SQLiteDatabase db) {
        // Create new tables
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.ARTICLES + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ArticlesColumns.ARTICLE_ID + " INTEGER,"
                + ArticlesColumns.ARTICLE_ICON + " TEXT,"
                + ArticlesColumns.ARTICLE_TITLE + " TEXT,"
                + ArticlesColumns.ARTICLE_RATE + " DOUBLE,"
                + ArticlesColumns.ARTICLE_PUBLISHER + " TEXT,"
                + ArticlesColumns.ARTICLE_PUBLISHED + " INTEGER,"
                + "UNIQUE (" + ArticlesColumns.ARTICLE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.CLASSIFIEDS + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ClassifiedsColumns.CLASSIFIED_ID + " INTEGER,"
                + ClassifiedsColumns.CLASSIFIED_ICON + " TEXT,"
                + ClassifiedsColumns.CLASSIFIED_TITLE + " TEXT,"
                + ClassifiedsColumns.CLASSIFIED_ADDRESS1 + " TEXT,"
                + ClassifiedsColumns.CLASSIFIED_ADDRESS2 + " TEXT,"
                + ClassifiedsColumns.CLASSIFIED_PRICE + " DOUBLE,"
                + "UNIQUE (" + ClassifiedsColumns.CLASSIFIED_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.EVENTS + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsColumns.EVENT_ID + " INTEGER,"
                + EventsColumns.EVENT_ICON + " TEXT,"
                + EventsColumns.EVENT_TITLE + " TEXT,"
                + EventsColumns.EVENT_ADDRESS + " TEXT,"
                + EventsColumns.EVENT_START_DATE + " INTEGER,"
                + "UNIQUE (" + EventsColumns.EVENT_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.DEALS + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DealsColumns.DEAL_ID + " INTEGER,"
                + DealsColumns.DEAL_ICON + " TEXT,"
                + DealsColumns.DEAL_LISTING_ID + " INTEGER,"
                + DealsColumns.DEAL_TITLE + " TEXT,"
                + DealsColumns.DEAL_LISTING_TITLE + " TEXT,"
                + DealsColumns.DEAL_LATITUDE + " DOUBLE,"
                + DealsColumns.DEAL_LONGITUDE + " DOUBLE,"
                + DealsColumns.DEAL_RATE + " DOUBLE,"
                + DealsColumns.DEAL_REAL_VALUE + " DOUBLE,"
                + DealsColumns.DEAL_VALUE + " DOUBLE,"
                + "UNIQUE (" + DealsColumns.DEAL_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.LISTINGS + "("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ListingsColumns.LISTING_ID + " INTEGER,"
                + ListingsColumns.LISTING_ICON + " TEXT,"
                + ListingsColumns.LISTING_TITLE + " TEXT,"
                + ListingsColumns.LISTING_ADDRESS + " TEXT,"
                + ListingsColumns.LISTING_HAS_DEAL + " INTEGER,"
                + ListingsColumns.LISTING_RATE + " DOUBLE,"
                + "UNIQUE (" + ListingsColumns.LISTING_ID + ") ON CONFLICT REPLACE)");
    }

    public interface ArticlesColumns {
        String ARTICLE_ID = "article_id";
        String ARTICLE_ICON = "article_icon";
        String ARTICLE_TITLE = "article_title";
        String ARTICLE_PUBLISHED = "article_published";
        String ARTICLE_PUBLISHER = "article_publisher";
        String ARTICLE_RATE = "article_rate";
    }

    public interface ClassifiedsColumns {
        String CLASSIFIED_ID = "classified_id";
        String CLASSIFIED_ICON = "classified_icon";
        String CLASSIFIED_TITLE = "classified_title";
        String CLASSIFIED_ADDRESS1 = "classified_address1";
        String CLASSIFIED_ADDRESS2 = "classified_address2";
        String CLASSIFIED_PRICE = "classified_price";
        String CLASSIFIED_LATITUDE = "classified_latitude";
        String CLASSIFIED_LONGITUDE = "classified_longitude";
    }

    public interface DealsColumns {
        String DEAL_ID = "deal_id";
        String DEAL_ICON = "deal_icon";
        String DEAL_LISTING_ID = "deal_listing_id";
        String DEAL_TITLE = "deal_title";
        String DEAL_LISTING_TITLE = "deal_listing_title";
        String DEAL_LATITUDE = "deal_latitude";
        String DEAL_LONGITUDE = "deal_longitude";
        String DEAL_RATE = "deal_rate";
        String DEAL_REAL_VALUE = "deal_real_value";
        String DEAL_VALUE = "deal_value";
    }

    public interface EventsColumns {
        String EVENT_ID = "event_id";
        String EVENT_ICON = "event_icon";
        String EVENT_TITLE = "event_title";
        String EVENT_ADDRESS = "event_address";
        String EVENT_START_DATE = "event_start_date";
        String EVENT_LATITUDE = "event_latitude";
        String EVENT_LONGITUDE = "event_longitude";
    }

    public interface ListingsColumns {
        String LISTING_ID = "listing_id";
        String LISTING_ICON = "listing_icon";
        String LISTING_TITLE = "listing_title";
        String LISTING_ADDRESS = "listing_address";
        String LISTING_RATE = "listing_rate";
        String LISTING_HAS_DEAL = "listing_has_deal";
        String LISTING_LATITUDE = "listing_latitude";
        String LISTING_LONGITUDE = "listing_longitude";
    }

    public interface NotificationsColumns {
        String NOTIFICATION_ID = "notification_id";
        String NOTIFICATION_TITLE = "notification_title";
        String NOTIFICATION_DESCRIPTION = "notification_description";
    }

}
