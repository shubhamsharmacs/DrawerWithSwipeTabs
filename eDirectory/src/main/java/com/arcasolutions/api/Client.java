package com.arcasolutions.api;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.arcasolutions.api.annotation.ApiModule;
import com.arcasolutions.api.annotation.ApiResource;
import com.arcasolutions.api.constant.Resource;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.EdirectoryConf;
import com.arcasolutions.api.model.EdirectoryConfResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.api.model.ModuleConf;
import com.arcasolutions.api.model.ModuleConfResult;
import com.arcasolutions.api.model.ModuleFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Client {

    private static final String BASE_URL = "http://demodirectory.com";

    private static final String API_URL = BASE_URL + "/API/api2.php";

    private final RestTemplate mRestTemplate = new RestTemplate();

    private static EdirectoryConf mEdirectoryConf;

    private static ModuleConf mModuleConf;

    public Client() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        MappingJackson2HttpMessageConverter jconverter
                = new MappingJackson2HttpMessageConverter();
        jconverter.setObjectMapper(objectMapper);

        mRestTemplate.getMessageConverters().add(jconverter);
    }

    private <T> T getResult(Class<T> clazz, Map<String, Object> map)
            throws RestClientException {

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

}
