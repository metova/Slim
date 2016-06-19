package com.metova.slim.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class BinderMetadata {

    private Class<?> mBinderClass;
    private TypeElement mTypeElement;

    public BinderMetadata(Class<?> binderClass, TypeElement typeElement) {
        mBinderClass = binderClass;
        mTypeElement = typeElement;
    }

    public Class<?> getBinderClass() {
        return mBinderClass;
    }

    public ExecutableElement getMethodElement() {
        for (Element enclosedElement : mTypeElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                return (ExecutableElement) enclosedElement;
            }
        }

        return null;
    }
}
