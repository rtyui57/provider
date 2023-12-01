package com.ramon.provider.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogSite {

    /**
     * Specifies the value of the site to include in the Log.
     * <b>NOTE</b>: Only is processed if "siteParam" is not definded.
     *
     * @return
     */
    String site() default "";

    /**
     * Name of the method input parameter that contains the site value, to be included in the log. If the site value is a subproperty, can
     * be indicated with the syntax: "property" + "." + "subproperty". E.g.: "template.site"
     *
     * @return
     */
    String siteParam() default "";

}