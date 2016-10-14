package com.metova.slim.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;

import com.metova.slim.annotation.Extra;
import com.metova.slim.annotation.Layout;
import com.metova.slim.binder.ActivityBinder;
import com.metova.slim.binder.FragmentBinder;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public class SlimProcessor extends AbstractProcessor {

    private static final String BINDING_CLASS_SUFFIX = "$$SlimBinder";

    private Filer mFiler;
    private TypeName mActivityIBinderElement;
    private TypeName mFragmentIBinderElement;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return arrayToSet(new String[]{
                Extra.class.getCanonicalName(),
                Layout.class.getCanonicalName()
        });
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mActivityIBinderElement = TypeName.get(processingEnv.getElementUtils().getTypeElement(ActivityBinder.class.getCanonicalName()).asType());
        mFragmentIBinderElement = TypeName.get(processingEnv.getElementUtils().getTypeElement(FragmentBinder.class.getCanonicalName()).asType());
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        List<BinderClassBuilder> builders = createBinderClassBuilders(env);
        for (BinderClassBuilder builder : builders) {
            try {
                builder.buildJavaFile(BINDING_CLASS_SUFFIX).writeTo(mFiler);
            } catch (IOException e) {
                error("Unable to write binder class %s: %s", builder.getCanonicalName(), e.getMessage());
            }
        }

        return true;
    }

    private List<BinderClassBuilder> createBinderClassBuilders(RoundEnvironment env) {
        Map<Element, BinderClassBuilder> builderMap = new HashMap<>();
        for (Element element : env.getElementsAnnotatedWith(Layout.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            Layout annotation = element.getAnnotation(Layout.class);
            int id = annotation.value();

            BinderClassBuilder builder = getOrCreateBinderClassBuilder(builderMap, element);
            builder.writeLayout(id);
        }

        for (Element element : env.getElementsAnnotatedWith(Extra.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            Extra extra = element.getAnnotation(Extra.class);
            String key = extra.value();
            boolean optional = extra.optional(); // TODO

            BinderClassBuilder builder = getOrCreateBinderClassBuilder(builderMap, element);
            builder.writeExtra(element.getSimpleName().toString(), key);
        }

        return new ArrayList<>(builderMap.values());
    }

    private BinderClassBuilder getOrCreateBinderClassBuilder(Map<Element, BinderClassBuilder> builderMap, Element element) {
        TypeElement typeElement = getEnclosingElement(element);
        BinderClassBuilder builder;
        if (builderMap.containsKey(typeElement)) {
            builder = builderMap.get(typeElement);
        } else {
            builder = new BinderClassBuilder(getPackageName(element), typeElement);
            if (isActivity(typeElement)) {
                builder.addInterfaceTypeName(mActivityIBinderElement);
            } else if (isFragment(typeElement)) {
                builder.addInterfaceTypeName(mFragmentIBinderElement);
            } else {
                error(typeElement, "%s is not an Activity or Fragment", typeElement.getQualifiedName());
            }

            builderMap.put(typeElement, builder);
        }

        return builder;
    }

    private String getPackageName(Element type) {
        return processingEnv.getElementUtils().getPackageOf(type).getQualifiedName().toString();
    }

    private void error(String message, Object... args) {
        processingEnv.getMessager().printMessage(ERROR, String.format(message, args));
    }

    private void error(Element element, String message, Object... args) {
        processingEnv.getMessager().printMessage(ERROR, String.format(message, args), element);
    }

    private boolean isActivity(TypeElement element) {
        return isAssignable(element, "android.app.Activity");
    }

    private boolean isFragment(TypeElement element) {
        return isAssignable(element, "android.support.v4.app.Fragment");
    }

    private boolean isAssignable(TypeElement element, String className) {
        return processingEnv.getTypeUtils().isAssignable(element.asType(), processingEnv.getElementUtils().getTypeElement(className).asType());
    }

    private static Set<String> arrayToSet(String[] array) {
        assert array != null;

        Set<String> set = new HashSet<>(array.length);
        Collections.addAll(set, array);

        return set;
    }

    private static TypeElement getEnclosingElement(Element type) {
        if (type.getKind() != CLASS && type.getKind() != INTERFACE) {
            type = type.getEnclosingElement();
        }
        return (TypeElement) type;
    }
}
