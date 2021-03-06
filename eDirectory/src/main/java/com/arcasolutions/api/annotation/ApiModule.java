package com.arcasolutions.api.annotation;

import com.arcasolutions.api.constant.ModuleName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ApiModule {
    ModuleName value();
}
