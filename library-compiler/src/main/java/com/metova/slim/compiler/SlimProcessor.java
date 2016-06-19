package com.metova.slim.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;

import com.metova.slim.annotation.Extra;
import com.metova.slim.annotation.Layout;
import com.metova.slim.binder.ExtraBinder;
import com.metova.slim.binder.LayoutIdProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
    private Map<Class<? extends Annotation>, BinderMetadata> mBinderMetadataMap = new HashMap<>();

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

        addBinderAssociation(Layout.class, LayoutIdProvider.class);
        addBinderAssociation(Extra.class, ExtraBinder.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        List<BinderClassBuilder> builders = createBinderClassBuilders(env);
        for (BinderClassBuilder builder : builders) {
            try {
                builder.buildJavaFile().writeTo(mFiler);
            } catch (IOException e) {
                error("Unable to write binder class %s: %s", builder.getCanonicalName(), e.getMessage());
            }
        }

        return true;
    }

    private void addBinderAssociation(Class<? extends Annotation> annotationCls, Class<?> binderCls) {
        TypeElement binderElement = processingEnv.getElementUtils().getTypeElement(binderCls.getCanonicalName());
        mBinderMetadataMap.put(annotationCls, new BinderMetadata(binderCls, binderElement));
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
            BinderMetadata metadata = mBinderMetadataMap.get(Layout.class);
            builder.writeLayout(metadata.getBinderClass(), metadata.getMethodElement(), id);
        }

        return new ArrayList<>(builderMap.values());
    }

    private BinderClassBuilder getOrCreateBinderClassBuilder(Map<Element, BinderClassBuilder> builderMap, Element element) {
        element = getEnclosingElement(element);
        BinderClassBuilder builder;
        if (builderMap.containsKey(element)) {
            builder = builderMap.get(element);
        } else {
            builder = new BinderClassBuilder(getPackageName(element), getClassName(element));
            builderMap.put(element, builder);
        }

        return builder;
    }

    private String getClassName(Element type) {
        return type.getSimpleName().toString() + BINDING_CLASS_SUFFIX;
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

    private static Set<String> arrayToSet(String[] array) {
        assert array != null;

        Set<String> set = new HashSet<>(array.length);
        Collections.addAll(set, array);

        return set;
    }

    private static Element getEnclosingElement(Element type) {
        if (type.getKind() != CLASS && type.getKind() != INTERFACE) {
            type = type.getEnclosingElement();
        }
        return type;
    }
}
