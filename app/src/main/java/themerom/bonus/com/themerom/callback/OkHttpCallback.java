package themerom.bonus.com.themerom.callback;

import com.squareup.okhttp.Request;

/**
 * Created by bonus on 11/13/15.
 * Class name ${type_name}
 */
public abstract class OkHttpCallback<T> {

//    public Type mType;
//
//    public OkHttpCallback() {
//        mType = getSuperclassTypeParameter(getClass());
//    }
//
//    static Type getSuperclassTypeParameter(Class<?> subClass){
//        Type superClass = subClass.getGenericSuperclass();
//        if(superClass instanceof Class){
//            new RuntimeException("miss class type parameter");
//        }
//        ParameterizedType parameterized = (ParameterizedType) superClass;
//        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//    }

    public void onBefore(Request request){

    }

    public void onAfter(){}
    public void inProgress(float progress){

    }

    public abstract void onError(Request request, Exception e);
    public abstract void onSuccess(T response);

    public static final OkHttpCallback DEFAULT_CALLBACK = new OkHttpCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onSuccess(String response) {

        }
    };
}
