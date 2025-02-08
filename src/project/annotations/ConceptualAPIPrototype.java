package project.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ConceptualAPIPrototype {
  // Marker annotation, should be applied to a method within a prototype class
}
