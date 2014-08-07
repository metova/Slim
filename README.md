Slim
====

Introduction
------------
Slim is an annotation library whose main purpose is to remove common boilerplate associated with Android development.
There are currently two features:

+ Assignment of 'extras' passed through to an Activity or Fragment
+ Casting of an activity to an interface callback within a Fragment

Usage
-----

@Extra
------
```java

public class MyActivity extends Activity {

    public static final String EXTRA_DATA = "data";
    
    @Extra(key = EXTRA_DATA)
    Data mData;
    
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Slim.injectExtras(getIntent().getExtras(), this);
    }
}
```

An exception will be thrown by default if an extra is not found. You can mark an extra as optional by setting `optional = true` inside the annotation parameters. This will also allow you to set a default value:

```java
@Extra(key = EXTRA_DATA, optional = true)
Data mData = Data.newInstance();
```

`mData` will be set to `Data.newInstance()` if an extra was not found matching key `EXTRA_DATA`.

@Callback
---------
```java

public class MyFragment extends Fragment {
    
    @Callback
    MyCallback mMyCallback;
        
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Slim.injectCallback(this);
    }
        
    public interface MyCallback {
        public void doStuff();
    }
}
```

This will cast the `Activity` to your callback for you automatically. If the `Activity` does not implement the `Fragment` interface, a `ClassCastException` will be thrown with an appropriate error message in the logs.

Other
-----

Alternatively, `SlimActivity`, `SlimFragment`, and `SlimListFragment` is provided as base classes that will call the `Slim` code in the correct places.
