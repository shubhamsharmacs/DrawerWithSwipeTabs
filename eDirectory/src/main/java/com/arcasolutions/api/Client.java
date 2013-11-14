package com.arcasolutions.api;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.arcasolutions.api.annotation.ApiModule;
import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.EdirectoryConf;
import com.arcasolutions.api.model.EdirectoryConfResult;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.api.model.ModuleConf;
import com.arcasolutions.api.model.ModuleConfResult;
import com.arcasolutions.api.model.ModuleFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.internal.ac;
import com.google.android.gms.internal.bo;
import com.google.android.gms.internal.bu;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Client {

    private static final String BASE_URL = "http://demodirectory.com";

    private static final String API_URL = BASE_URL + "/API/api2.php";
    private static final String IAPP_URL = BASE_URL + "/iapp/m6400";


    private final RestTemplate mRestTemplate = new RestTemplate();

    private static EdirectoryConf mEdirectoryConf;

    private static ModuleConf mModuleConf;

    public Client() {

        mRestTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        mRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    IappResult<Account> getAccountResult(IappBuilder builder) throws RestClientException, JSONException {
        ResponseEntity<String> responseEntity;
        if (IappBuilder.Method.POST.equals(builder.mMethod)) {
            HttpEntity<?> requestEntity = getHttpEntity(builder.mParams);
            responseEntity = mRestTemplate.postForEntity(builder.mUrl, requestEntity, String.class);
        } else {
            Map<String, String> params = Maps.newHashMap();
            for (Map.Entry<String, List<String>> entry : builder.mParams.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            responseEntity = mRestTemplate.exchange(builder.mUrl, HttpMethod.GET, getHttpEntity(), String.class, params);
        }

        String body = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(body);

        boolean success = jsonObject.has("validate")
                ? jsonObject.getBoolean("validate")
                : jsonObject.has("authenticateAccount")
                    ? jsonObject.getBoolean("authenticateAccount")
                    : jsonObject.has("member_id");

        String message = jsonObject.has("message")
                ? jsonObject.getString("message")
                : jsonObject.has("authmessage")
                    ? jsonObject.getString("authmessage")
                    : null;

        IappResult<Account> result = new IappResult<Account>(success, message);
        Account account = null;
        if (jsonObject.has("authenticateAccount") && success) {
            account = new Account();
            account.setId(jsonObject.getLong("id"));
            account.setEmail(jsonObject.getString("username"));
            account.setFirstName(jsonObject.getString("first_name"));
            account.setLastName(jsonObject.getString("last_name"));

        } else if (jsonObject.has("member_id")) {
            account = new Account();
            account.setId(jsonObject.getLong("member_id"));
            account.setUid(builder.mParams.getFirst("uid"));
            account.setEmail(builder.mParams.getFirst("email"));
            account.setFirstName(builder.mParams.getFirst("first_name"));
            account.setLastName(builder.mParams.getFirst("last_name"));
        }

        result.setResult(account);
        return result;
    }

    IappResult<Object> getReviewResult(IappBuilder builder) throws RestClientException {
        HttpEntity<?> request = getHttpEntity(builder.mParams);
        ResponseEntity<String> responseEntity = mRestTemplate.postForEntity(builder.mUrl, request, String.class);
        return new IappResult<Object>(true, null);
    }

    IappResult<String> getRedeemResult(IappBuilder builder) throws RestClientException, JSONException {
        HttpEntity<?> request = getHttpEntity(builder.mParams);
        ResponseEntity<String> responseEntity = mRestTemplate.postForEntity(builder.mUrl, request, String.class);

        String body = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(body);

        boolean success = jsonObject.has("result")
                ? jsonObject.getInt("result") == 0
                : false;

        String redeemCode = jsonObject.has("redeem_code")
                ? jsonObject.getString("redeem_code")
                : null;

        IappResult<String> result = new IappResult<String>(success, null);
        if (!TextUtils.isEmpty(redeemCode)) {
            result.setResult(redeemCode);
        }

        return result;
    }

    IappResult<Object> getCheckInResult(IappBuilder builder) throws RestClientException {
        HttpEntity<?> request = getHttpEntity(builder.mParams);
        ResponseEntity<String> responseEntity = mRestTemplate.exchange(builder.mUrl, HttpMethod.POST, request, String.class);
        return  new IappResult<Object>(true, null);
    }

    private HttpEntity<?> getHttpEntity() {
        return new HttpEntity<Object>(getIappHttpHeaders());
    }

    private HttpEntity<?> getHttpEntity(MultiValueMap<String, String> body) {
        HttpHeaders headers = getIappHttpHeaders();
        return new HttpEntity<MultiValueMap<String, String>>(body, headers);
    }

    private HttpHeaders getIappHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setUserAgent("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_2 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8H7 Safari/6533.18.5");
        return headers;
    }

    private <T> T getResult(Class<T> clazz, Map<String, Object> map)
            throws RestClientException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        MappingJackson2HttpMessageConverter jconverter = new MappingJackson2HttpMessageConverter();
        jconverter.setObjectMapper(objectMapper);

        mRestTemplate.getMessageConverters().add(jconverter);

        List<String> keys = Lists.newArrayList();

        for (String key : map.keySet())
            keys.add(key + "={" + key + "}");

        String query = TextUtils.join("&", keys);
        String url = TextUtils.join("?", new String[]{API_URL, query});

        ResponseEntity<T> responseEntity = mRestTemplate.getForEntity(url, clazz, map);

        return responseEntity.getBody();
    }

    public static class Builder {
        Map<String, Object> mMap = Maps.newHashMap();
        Class mClass;

        public Builder(Class<? extends BaseResult> clazz) {
            mClass = clazz;

            if (mClass == null)
                throw new IllegalArgumentException("clazz can not be null");


            ApiResource apiResource = null;
            Annotation[] annotations = mClass.getDeclaredAnnotations();
            if (annotations != null) {
                for (Annotation a : annotations) {
                    if (a instanceof ApiResource) {
                        apiResource = (ApiResource) a;
                        break;
                    }
                }
            }

            if (apiResource == null)
                throw new IllegalArgumentException(mClass.getName() + " must be annotated with " + ApiResource.class.getName());

            Resource resource = apiResource.value();
            if (resource == null)
                throw new IllegalArgumentException("ApiResource value can not be null");

            mMap.put("resource", resource.toString());
        }

        public Builder categoryId(long categoryId) {
            mMap.put("category_id", categoryId);
            return this;
        }

        public Builder searchBy(SearchBy searchBy) {
            mMap.put("searchBy", searchBy.toString());
            return this;
        }

        public Builder region(double nearLeftLatitude,
                              double nearLeftLongitude,
                              double farRightLatitude,
                              double farRightLongitude) {

            mMap.put("drawLat0", String.format(Locale.US, "%.9f", nearLeftLatitude));
            mMap.put("drawLong0", String.format(Locale.US, "%.9f", nearLeftLongitude));
            mMap.put("drawLat1", String.format(Locale.US, "%.9f", farRightLatitude));
            mMap.put("drawLong1", String.format(Locale.US, "%.9f", farRightLongitude));

            return this;
        }

        public Builder myLocation(double latitude, double longitude) {
            mMap.put("myLat", String.format(Locale.US, "%.9f", latitude));
            mMap.put("myLong", String.format(Locale.US, "%.9f", longitude));
            return this;
        }

        public Builder fatherId(long categoryId) {
            mMap.put("father_id", categoryId);
            return this;
        }

        public Builder page(int page) {
            mMap.put("page", page);
            return this;
        }

        public Builder id(long id) {
            mMap.put("id", id);
            return this;
        }

        public Builder orderBy(String orderBy) {
            mMap.put("orderBy", orderBy);
            return this;
        }

        public Builder orderSequence(String orderSequence) {
            mMap.put("orderSequence", orderSequence);
            return this;
        }

        public Builder module(ReviewModule module) {
            mMap.put("type", module.toString());
            return this;
        }

        public <T extends BaseResult> T get(Class<T> clazz) throws Exception {
            T result = new Client().getResult(clazz, mMap);
            List results = result.getResults();
            if (results != null) {
                for (Object item : results) {
                    if (item instanceof Module) {
                        applyModuleConf((Module) item);
                    }
                }
            }
            return result;
        }

        public <T extends BaseResult> void execAsync(RestListener<T> listener) {
            new Task<T>(this, listener).execute(mClass);
        }
    }

    public static class IappBuilder {

        private enum Method { POST, GET };

        private final String mUrl;
        private final MultiValueMap<String, String> mParams;
        private final Method mMethod;
        private final IappTask.Service mService;

        private IappBuilder(String url, Method method, IappTask.Service service) {
            mUrl = url;
            mMethod = method;
            mParams = new LinkedMultiValueMap<String, String>();
            mService = service;
        }

        private void put(String param, String value) {
            mParams.add(param, value);
        }

        public static IappBuilder newCreateAcccountBuilder(String email, String password, String firstName, String lastName) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/profile/put.json.php", Method.POST, IappTask.Service.ACCOUNT);
            builder.put("username", email);
            builder.put("first_name", firstName);
            builder.put("last_name", lastName);
            builder.put("password", password);
            builder.put("retype_password", password);
            return builder;
        }

        public static IappBuilder newAuthenticateAccountBuilder(String username, String password) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/profile/login.json.php", Method.POST, IappTask.Service.ACCOUNT);
            builder.put("username", username);
            builder.put("password", password);
            return builder;
        }

        public static IappBuilder newAuthenticateFacebookBuilder(String uid, String email, String firstName, String lastName) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/profile/facebookauth.json.php?uid={uid}&email={email}&first_name={first_name}&last_name={last_name}", Method.GET, IappTask.Service.ACCOUNT);
            builder.put("uid", uid);
            builder.put("email", email);
            builder.put("first_name", firstName);
            builder.put("last_name", lastName);
            return builder;
        }

        public static IappBuilder newCreateReviewBuilder(long listingId, long accountId, String title, String comment, String name, String email, String location, float rating) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/reviews/reviewadd.php", Method.POST, IappTask.Service.REVIEW);
            builder.put("item_id", listingId + "");
            builder.put("account_id", accountId + "");
            builder.put("review_title", title);
            builder.put("review", comment);
            builder.put("name", name);
            builder.put("email", email);
            builder.put("location", location);
            builder.put("rating", rating + "");
            return builder;
        }

        public static IappBuilder newRedeemBuilder(String username, long dealId , boolean facebook) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/deal/deal.json.php", Method.POST, IappTask.Service.REDEEM);
            builder.put("user_name", username);
            builder.put("profile", facebook ? "no" : "yes");
            builder.put("facebook", facebook ? "yes" : "no");
            builder.put("promotion_id", dealId + "");
            return builder;
        }

        public static IappBuilder newCheckInBuilder(long listingId, long accountId, String name, String tip) {
            IappBuilder builder = new IappBuilder(IAPP_URL + "/checkin/checkinadd.php", Method.POST, IappTask.Service.CHECKIN);
            builder.put("item_id", listingId + "");
            builder.put("account_id", accountId + "");
            builder.put("checkin_name", name);
            builder.put("quick_tip", tip);
            return builder;
        }

        public void execAsync(RestIappListener listener) {
            new IappTask(this, listener).execute(mService);
        }

    }

    private static class IappTask extends AsyncTask<IappTask.Service, Void, Object> {
        private RestIappListener<?> mListener;
        private IappBuilder mBuilder;

        public static enum Service { ACCOUNT, REDEEM, CHECKIN, REVIEW };

        public IappTask(IappBuilder builder, RestIappListener listener) {
            mListener = listener;
            mBuilder = builder;
        }

        @Override
        protected Object doInBackground(IappTask.Service... services) {
            Service s = services[0];
            try {
                switch (s) {
                    case ACCOUNT:
                        return new Client().getAccountResult(mBuilder);

                    case REDEEM:
                        return new Client().getRedeemResult(mBuilder);

                    case CHECKIN:
                        return new Client().getCheckInResult(mBuilder);

                    case REVIEW:
                        return new Client().getReviewResult(mBuilder);

                    default:
                        throw new IllegalArgumentException("Unknown iapp service requested.");
                }
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o == null) {
                mListener.onFail(new Exception("Result is null"));
            } else if (o instanceof Exception) {
                mListener.onFail((Exception) o);
            } else if (o instanceof IappResult) {
                mListener.onSuccess((IappResult)o);
            }
        }
    }

    private static class Task<T extends BaseResult> extends AsyncTask<Class<T>, Void, Object> {

        private RestListener<T> mListener;
        private Builder mBuilder;

        public Task(Builder builder, RestListener<T> listener) {
            mBuilder = builder;
            mListener = listener;
        }

        @Override
        protected Object doInBackground(Class<T>... classes) {
            try {
                return mBuilder.get(classes[0]);
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result == null) {
                mListener.onFail(new Exception("Result is null"));
            } else if (result instanceof Exception) {
                mListener.onFail((Exception) result);
            } else if (result instanceof BaseResult) {
                mListener.onComplete((T) result);
            }
        }
    }

    public interface RestIappListener<T> {
        void onSuccess(IappResult<T> iappResult);
        void onFail(Exception ex);
    }

    public interface RestListener<T extends BaseResult> {
        void onComplete(T result);

        void onFail(Exception ex);
    }

    private static <T extends Module> void applyModuleConf(T module) throws Exception {
        ApiModule apiModule = null;
        Annotation[] annotations = module.getClass().getDeclaredAnnotations();
        if (annotations != null) {
            for (Annotation a : annotations) {
                if (a instanceof ApiModule) {
                    apiModule = (ApiModule) a;
                    break;
                }
            }
        }

        if (apiModule == null) return;

        ModuleConf moduleConf = getModuleConf();
        ModuleFeature features = moduleConf.get(apiModule.value(), module.getLevel());
        if (features != null) {
            module.keepGeneralFields(features.getGeneral());
        }
    }

    private static EdirectoryConf getEdirectoryConf() throws Exception {
        if (mEdirectoryConf == null) {
            Builder builder = new Builder(EdirectoryConfResult.class);
            EdirectoryConfResult result = builder.get(EdirectoryConfResult.class);
            List<EdirectoryConf> results = result.getResults();
            mEdirectoryConf = results.get(0);
        }
        return mEdirectoryConf;
    }

    private static ModuleConf getModuleConf() throws Exception {
        if (mModuleConf == null) {
            Builder builder = new Builder(ModuleConfResult.class);
            ModuleConfResult result = builder.get(ModuleConfResult.class);
            List<ModuleConf> results = result.getResults();
            mModuleConf = results.get(0);
        }
        return mModuleConf;
    }

    public static EdirectoryConf getConf() {
        return mEdirectoryConf;
    }
}
