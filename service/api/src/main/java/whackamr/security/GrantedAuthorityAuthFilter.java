package whackamr.security;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.Pointcuts;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthoritiesAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.method.AuthorizationAdvisor;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;
import org.springframework.security.authorization.method.ThrowingMethodAuthorizationDeniedHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.lang.Arrays;

@Component
public class GrantedAuthorityAuthFilter implements AuthorizationAdvisor
{
    private static final Logger logger = LoggerFactory.getLogger(GrantedAuthorityAuthFilter.class);

    private AuthoritiesAuthorizationManager authManager = new AuthoritiesAuthorizationManager();
    private MethodAuthorizationDeniedHandler defaultHandler = new ThrowingMethodAuthorizationDeniedHandler();
    private Supplier<SecurityContextHolderStrategy> securityContextHolderStrategy = SecurityContextHolder::getContextHolderStrategy;

    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        logger.debug("Checking for granted authorities");
        var annotations = getAnnotations(invocation);

        if (annotations.isEmpty())
        {
            logger.debug("no annotations...");
            return defaultHandler.handleDeniedInvocation(invocation,
                    new AuthorizationDeniedException("No annotations found on method"));
        }

        try
        {
            annotations.forEach(annotation -> {
                var authorities = Arrays.asList(annotation.value()).stream().map(ga -> ga.toString()).toList();

                logger.debug("Looking for authorities: " + authorities.toString());

                var result = authManager.authorize(this::getAuthentication, authorities);

                logger.debug("Got result from auth manager: " + result);

                if (result != null && !result.isGranted())
                {
                    logger.debug("Failed to authorize " + invocation + " with result " + result);

                    throw new AuthorizationDeniedException("access denied", result);
                }
            });
        } catch (

        AuthorizationDeniedException ade)
        {
            return defaultHandler.handleDeniedInvocation(invocation, ade);
        }

        try
        {
            return invocation.proceed();
        } catch (AuthorizationDeniedException ade)
        {
            return defaultHandler.handleDeniedInvocation(invocation, ade);
        }
    }

    @Override
    public Pointcut getPointcut()
    {
        return forAnnotations(AuthorityAllowed.class, AuthoritiesAllowed.class);
    }

    @Override
    public Advice getAdvice()
    {
        return this;
    }

    @SafeVarargs
    static Pointcut forAnnotations(Class<? extends Annotation>... annotations)
    {
        ComposablePointcut pointcut = null;
        for (Class<? extends Annotation> annotation : annotations)
        {
            if (pointcut == null)
            {
                pointcut = new ComposablePointcut(classOrMethod(annotation));
            } else
            {
                pointcut.union(classOrMethod(annotation));
            }
        }
        return pointcut;
    }

    private List<AuthorityAllowed> getAnnotations(MethodInvocation invocation)
    {
        var annotations = new ArrayList<AuthorityAllowed>();
        var classAnnotations = Arrays
                .asList(invocation.getMethod().getDeclaringClass().getAnnotationsByType(AuthorityAllowed.class));
        var methodAnnotations = Arrays.asList(invocation.getMethod().getAnnotationsByType(AuthorityAllowed.class));

        logger.debug("Class: " + invocation.getClass().getName());
        logger.debug("Annotation count: " + classAnnotations.size());
        logger.debug("Method: " + invocation.getMethod().getName());
        logger.debug("Annotation count: " + methodAnnotations.size());

        annotations.addAll(classAnnotations);
        annotations.addAll(methodAnnotations);

        return annotations;
    }

    private Authentication getAuthentication()
    {
        Authentication authentication = this.securityContextHolderStrategy.get().getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
        {
            throw new AuthenticationCredentialsNotFoundException(
                    "An Authentication object was not found in the SecurityContext");
        }

        return authentication;
    }

    private static Pointcut classOrMethod(Class<? extends Annotation> annotation)
    {
        return Pointcuts.union(new AnnotationMatchingPointcut(null, annotation, true),
                new AnnotationMatchingPointcut(annotation, true));
    }
}
