Slim
====

![Logo](website/static/logo.png)

Introduction
------------
Slim is an annotation library whose main purpose is to remove common boilerplate associated with Android development.

Features
--------
+ Assignment of 'extras' passed through to an Activity or Fragment
+ Casting of an Activity to an interface callback within a Fragment
+ Easily call through to an Activity through an interface callback
+ Layout declaration at the top of the class
+ Pairs well with other Annotation-based libraries, such as [ButterKnife](https://github.com/JakeWharton/butterknife)

Pardon Our Progress
----

Slim is currently undergoing a complete rewrite to take advantage of annotation processors. Beta versions are available, but expect build-breaking changes until a final release is published.

Download
-----

To include Slim into your project, add it to your `build.gradle` dependencies. 

```
dependencies {
    compile 'com.metova:slim:2.0.0-beta.1'
    annotationProcessor 'com.metova:slim-compiler:2.0.0-beta.1'
}
```

Usage
-----

@Extra
------
```java

public class MyActivity extends Activity {

    public static final String EXTRA_DATA = "data";

    @Extra(EXTRA_DATA)
    Data mData;

    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Slim.bindExtras(this);
      // mData is now assigned its value from the Intent bundle and can be used
    }
}
```

An exception will be thrown by default if an extra is not found. You can mark an extra as optional by setting `optional = true` inside the annotation parameters. This will also allow you to set a default value:

```java
@Extra(value = EXTRA_DATA, optional = true)
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
        Slim.bindCallbacks(this);
        
        mMyCallback.doStuff();
    }

    public interface MyCallback {
        public void doStuff();
    }
}
```

This will cast the `Activity` to your callback for you automatically. If the `Activity` does not implement the `Fragment` interface, a `ClassCastException` will be thrown with an appropriate error message in the logs.

@Layout
-------
```java

@Layout(R.layout.activity_main)
public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slim.bindLayout(this);
    }
}
```

```java

@Layout(R.layout.fragment_main)
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return Slim.createLayout(this, inflater, container);
    }
}
```
