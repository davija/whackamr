package whackamr.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Repeatable(AuthoritiesAllowed.class)
public @interface AuthorityAllowed
{
    /**
     * List of permissions that are permitted access.
     */
    Permissions[] value();
}
