package com.metova.slim.compiler;

import com.metova.slim.binder.LayoutBinder;
import com.metova.slim.provider.CallbackProvider;
import com.metova.slim.provider.ExtraProvider;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

class BinderClassBuilder {

    private static final int NO_LAYOUT_ID = -1;

    private final Target mTarget;

    private final String mPackageName;
    private final TypeElement mClassElement;

    private HashSet<TypeName> mInterfaceTypeNameSet = new HashSet<>();
    private int mLayoutId = NO_LAYOUT_ID;
    private Map<String, String> mExtraMap = new HashMap<>();
    private Map<String, String> mCallbackMap = new HashMap<>();

    BinderClassBuilder(Target target, String packageName, TypeElement classElement) {
        mTarget = target;
        mPackageName = packageName;
        mClassElement = classElement;
    }

    void addInterfaceTypeName(TypeName interfaceTypeName) {
        mInterfaceTypeNameSet.add(interfaceTypeName);
    }

    void setLayout(int layoutId) {
        mLayoutId = layoutId;
    }

    void addExtra(String fieldName, String extraKey) {
        mExtraMap.put(fieldName, extraKey);
    }

    void addCallback(String fieldName, String fieldClassName) {
        mCallbackMap.put(fieldName, fieldClassName);
    }

    // void bindLayout(Object target, LayoutBinder binder);
    private MethodSpec createActivityLayoutMethodSpec(int layoutId) {
        final String target = "target";
        final String binder = "binder";

        return MethodSpec.methodBuilder("bindLayout")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.OBJECT, target)
                .addParameter(TypeName.get(LayoutBinder.class), binder)
                .addCode("$L.bindLayout($L, $L);\n", binder, target, layoutId)
                .build();
    }

    // int getLayoutId();
    private MethodSpec createFragmentLayoutMethodSpec(int layoutId) {
        return MethodSpec.methodBuilder("getLayoutId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode("return $L;\n", layoutId)
                .build();
    }

    // void bindExtras(Object target, ExtraProvider provider);
    private MethodSpec createExtrasMethodSpec() {
        final String target = "target";
        final String provider = "provider";

        final MethodSpec.Builder builder = MethodSpec.methodBuilder("bindExtras")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.OBJECT, target)
                .addParameter(TypeName.get(ExtraProvider.class), provider)
                .addCode("final $T obj = ($T) target;\n", mClassElement, mClassElement);

        for (Map.Entry<String, String> entry : mExtraMap.entrySet()) {
            builder.addCode("obj.$L = provider.getExtra(target, \"$L\");\n", entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    // void bindCallbacks(Object target);
    private MethodSpec createCallbacksMethodSpec() {
        final String target = "target";
        final String provider = "provider";

        final MethodSpec.Builder builder = MethodSpec.methodBuilder("bindCallbacks")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.OBJECT, target)
                .addParameter(TypeName.get(CallbackProvider.class), provider)
                .addCode("final $T obj = ($T) target;\n", mClassElement, mClassElement);

        for (Map.Entry<String, String> entry : mCallbackMap.entrySet()) {
            builder.addCode("obj.$L = provider.getCallback(target, $L.class);", entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    JavaFile buildJavaFile(String classSuffix) {
        final MethodSpec layoutMethodSpec;
        switch (mTarget) {
            case ACTIVITY:
                layoutMethodSpec = createActivityLayoutMethodSpec(mLayoutId);
                break;
            case FRAGMENT:
                layoutMethodSpec = createFragmentLayoutMethodSpec(mLayoutId);
                break;
            default:
                throw new RuntimeException("Unhandled target: " + mTarget.name());
        }

        final TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(mClassElement.getSimpleName() + classSuffix)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterfaces(mInterfaceTypeNameSet)
                .addMethod(layoutMethodSpec)
                .addMethod(createExtrasMethodSpec());

        if (mTarget == Target.FRAGMENT) {
            typeSpecBuilder.addMethod(createCallbacksMethodSpec());
        }

        return JavaFile.builder(mPackageName, typeSpecBuilder.build())
                .addFileComment("Generated by Slim. Do not modify!\n")
                .addFileComment(new Date(System.currentTimeMillis()).toString())
                .build();
    }

    String getCanonicalName() {
        return mPackageName + "." + mClassElement.getSimpleName();
    }
}
