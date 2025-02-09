package project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Annotation for marking prototype classes at runtime
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProcessAPIPrototype {}

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// Annotation for marking prototype methods
@Target(ElementType.METHOD)
public @interface ProcessAPIPrototype {
    // Marker annotation, should be applied to a method within a prototype class
}
