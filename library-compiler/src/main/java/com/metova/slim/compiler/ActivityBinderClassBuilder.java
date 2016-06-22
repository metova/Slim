package com.metova.slim.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ActivityBinderClassBuilder extends BinderClassBuilder {

    public ActivityBinderClassBuilder(String packageName, TypeElement classElement) {
        super(packageName, classElement);
    }
}
