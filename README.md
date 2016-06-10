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
+ Base class implementations that remove having to call through to the `Slim` methods
+ Pairs well with other Annotation-based libraries, such as [ButterKnife](https://github.com/JakeWharton/butterknife)

Download
-----

To include Slim into your project, add it to your `build.gradle` dependencies. 

```
dependencies {
    compile 'com.metova:slim:1.2'
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
      Slim.injectExtras(getIntent().getExtras(), this);
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
        Slim.injectCallback(this);
    }

    public interface MyCallback {
        public void doStuff();
    }
}
```

This will cast the `Activity` to your callback for you automatically. If the `Activity` does not implement the `Fragment` interface, a `ClassCastException` will be thrown with an appropriate error message in the logs.

@CallbackClick
--------------
```java

public class MyFragment extends Fragment {

    @Callback
    MyCallback mMyCallback;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Slim.injectCallbacks(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Slim.injectCallbacksMethods(this);
    }

    public interface MyCallback {
        @CallbackClick(R.id.do_stuff)
        public void doStuff();
    }
}
```

This will automatically assign a click listener from a View represented by its id to your callback's methods. Due to limitations with Java annotations, this will only work with zero argument methods.


@Layout
-------
```java

@Layout(R.layout.fragment_main)
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return Slim.createLayout(getActivity(), this, container);
    }
}
```

This will work anywhere you have access to a `Context`.

```java

@Layout(R.layout.list_item)
public class MyAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = Slim.createLayout(parent.getContext(), this, parent);
        }

        // ...

        return view;
    }
}
```

Other
-----

Alternatively, `SlimActivity`, `SlimFragment`, `SlimListFragment`, and `SlimDialogFragment` are provided as base classes that will call the `Slim` methods in the correct places.

Here's an example that showcases everything:

```java

@Layout(R.layout.fragment_example)
public class ExampleFragment extends SlimFragment {

    public static final String EXTRA_TEXT = "extra_text";

    @Extra(EXTRA_TEXT)
    String mText;

    @Callback
    ExampleCallback mCallback;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(mText);
    }

    public interface ExampleCallback {

        @CallbackClick(R.id.example_Button)
        public void goToNextScreen();
    }
}

```
