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
//
//    }
//
//    static Type getSuperclassTypeParameter(Class<?> subClass){
//        Type class = subClass.getGenericSuperclass();
//        if(class instanceof Class){
//
//        }
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
