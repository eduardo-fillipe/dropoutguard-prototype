package br.ufs.dcomp.dropoutguard.shared.application;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a class is a use case in the application's architecture.
 * <p>
 * This annotation is used to label classes that represent specific use cases or
 * business logic operations within the system. 
 * It is retained at runtime and applicable to types (classes or interfaces).
 */
@Service
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCase {
}
