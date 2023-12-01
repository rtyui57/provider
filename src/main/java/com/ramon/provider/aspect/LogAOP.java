package com.ramon.provider.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class LogAOP {

    protected ObjectMapper mapper;
    protected static final String LOG_AOP = "LogAOP";

    public LogAOP() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StringLimitSerializer());
        mapper.registerModule(module);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }


    int maxPayLoadLength = 1000;

    int maxFieldLength = 100;

    boolean ignoreLongFields = false;
    static final String DEFAULT_SITE_PROP_NAME = "site";
    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAOP.class);

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
    )
    public Object setSiteLogger(ProceedingJoinPoint jp) throws Throwable {
        processSiteParam(DEFAULT_SITE_PROP_NAME, jp);
        printRestRequestMethodWithParameters(jp);
        Object res;
        try {
            res = jp.proceed();
            printRestResponseMethod(jp, res);
        } catch (Throwable ex) {
            printRestErrorMethod(jp, ex);
            throw ex;
        } finally {
        }
        return res;
    }


    @Around(value = "@annotation(a)", argNames = "jp, a")
    public Object setSiteLogger(ProceedingJoinPoint jp, LogSite a) throws Throwable {
        String prevSite = LoggerUtils.getSite();

        if (ObjectUtils.isEmpty(a.siteParam())) {
            LoggerUtils.setSite(a.site());
        } else {
            processSiteParam(a.siteParam(), jp);
        }
        Object res;
        try {
            res = jp.proceed();
        } finally {
            if (ObjectUtils.isEmpty(prevSite)) {
                LoggerUtils.clear();
            } else {
                LoggerUtils.setSite(prevSite);
            }
        }
        return res;

    }

    private void processSiteParam(String paramSiteName, ProceedingJoinPoint jp) throws InvocationTargetException {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Object[] arguments = jp.getArgs();
        Method method = signature.getMethod();
        Parameter[] params = method.getParameters();

        // Checks if the parameters syntax is complexProp + "." + subProperty
        String[] paramSiteNameSplit = paramSiteName.split("\\.");
        String paramSiteNameFirstLevl = paramSiteName;
        String paramSiteNameSecondLevl = null;
        if (paramSiteNameSplit.length > 1) {
            // The parameter is complex.
            paramSiteNameFirstLevl = paramSiteNameSplit[0];
            paramSiteNameSecondLevl = paramSiteNameSplit[1];
        }

        for (int index = 0; index < params.length; index++) {
            if (Objects.equals(params[index].getName(), paramSiteNameFirstLevl)) {
                Object site = arguments[index];
                if (paramSiteNameSecondLevl != null) {
                    // Method should be "get" + property name, capitalizing its first letter. E.g.: getSite
                    String methodName = "get" + paramSiteNameSecondLevl.substring(0, 1).toUpperCase() + paramSiteNameSecondLevl.substring(1);
                    try {
                        Method methodParam = arguments[index].getClass().getMethod(methodName);
                        site = methodParam.invoke(arguments[index]);
                    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException |
                             SecurityException ex) {
                        String errorMsg = MessageFormat.format("Error at processing complex field {0} at setting the Log site. Cause: {1}", paramSiteName, ex.getMessage());
                        LOGGER.error(errorMsg, ex);
                    }
                }
                if (site instanceof String || ObjectUtils.isEmpty(site)) {
                    LoggerUtils.setSite((String) site);
                    break;
                }
            }
        }
    }

    /**
     * Prints in logger the name of the method with its parameters (And values) to log a rest request.
     *
     * <b>NOTE</b>: Only will be printed if the log level is DEBUG AND the joining method is from a "com.tedial" package.
     *
     * @param jp
     */
    private void printRestRequestMethodWithParameters(ProceedingJoinPoint jp) {
        try {
            if (shouldLog(jp)) {
                MethodSignature signature = (MethodSignature) jp.getSignature();
                Object[] arguments = jp.getArgs();
                Method method = signature.getMethod();
                Parameter[] params = method.getParameters();

                StringBuilder logBuilder = new StringBuilder("REST REQUEST-->");
                appendsToLogBuilderWithMethodName(logBuilder, jp);
                logBuilder.append("(");
                appendsToLogBuilderMethodParameters(logBuilder, arguments, params);
                logBuilder.append(")");

                LOGGER.debug(logBuilder.toString());
            }
        } catch (Throwable t) {
            LOGGER.warn(LOG_AOP, t);
        }
    }

    /**
     * Prints in logger the name of the method to log a rest response.
     *
     * <b>NOTE</b>: Only will be printed if the log level is DEBUG AND the joining method is from a "com.tedial" package.
     *
     * @param jp
     */
    private void printRestResponseMethod(ProceedingJoinPoint jp, Object ret) {
        try {
            if (shouldLog(jp)) {
                StringBuilder logBuilder = new StringBuilder("REST RESPONSE<--");
                appendsToLogBuilderWithMethodName(logBuilder, jp);
                logBuilder.append(": ");
                String retStr = mapper.writeValueAsString(ret);
                if (retStr == null) {
                    retStr = "void";
                }
                if (retStr.length() > maxPayLoadLength) {
                    logBuilder.append(retStr.substring(0, maxPayLoadLength))
                            .append("...");
                } else {
                    logBuilder.append(retStr);
                }

                LOGGER.debug(logBuilder.toString());
            }
        } catch (Throwable t) {
            LOGGER.warn(LOG_AOP, t);
        }
    }

    /**
     * Prints in logger the name of the method to log a rest exception.
     *
     * <b>NOTE</b>: Only will be printed if the log level is DEBUG AND the joining method is from a "com.tedial" package.
     *
     * @param jp
     * @param ex
     */
    private void printRestErrorMethod(ProceedingJoinPoint jp, Throwable ex) {
        try {
            if (shouldLog(jp)) {
                StringBuilder logBuilder = new StringBuilder("REST ERROR<--");
                appendsToLogBuilderWithMethodName(logBuilder, jp);
                logBuilder.append(". Cause: ")
                        .append(ex.getMessage());

                LOGGER.debug(logBuilder.toString());
            }
        } catch (Throwable t) {
            LOGGER.warn(LOG_AOP, t);
        }
    }

    private void appendsToLogBuilderWithMethodName(StringBuilder logBuilder, ProceedingJoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        logBuilder.append("user=")
                .append("")
                .append(", method=")
                .append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName());
    }

    /**
     * Only will be logged the calls to classes from "com.tedial" packages AND the logger is in debug.
     *
     * @param jp
     * @return
     */
    private boolean shouldLog(ProceedingJoinPoint jp) {
        if (!LOGGER.isDebugEnabled()) {
            return false;
        }
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        return method.getDeclaringClass().getName().startsWith("com.tedial");
    }

    /**
     * Appends to the log the input parameters, with its values. Annotation NoLog could be present in parameters and it interact in two
     * different ways: (1) Without Property (@NoLog). It will hide the value of this property. (2) With property
     * (@NoLog(propName="property") ). This annotation must be setted to a complex object and this property must be refer to any of its
     * properties. In this case, the method will hide this property of the object.
     *
     * <b>NOTE</b>: It is limited the maximum length of the information to be included.
     *
     * @param logBuilder
     * @param arguments
     * @param params
     */
    private void appendsToLogBuilderMethodParameters(StringBuilder logBuilder, Object[] arguments, Parameter[] params) throws JsonProcessingException, JsonProcessingException {
        int maxPayLoadLengthCnt = maxPayLoadLength;
        for (int i = 0; i < arguments.length && maxPayLoadLengthCnt > 0; i++) {
            Annotation noLog = params[i].getDeclaredAnnotation(NoLog.class);
            String paramName = params[i].getName();
            logBuilder.append(paramName)
                    .append(" = ");
            String paramValue = null;

            if (noLog != null) {
                NoLog noLogObject = (NoLog) noLog;
                String propName = noLogObject.propName();
                if (StringUtils.hasText(propName)) {
                    paramValue = mapper.writeValueAsString(arguments[i]);
                    Object jsonObj;
                    if (arguments[i] instanceof List) {
                        List<Object> resultList = new ArrayList<>();
                        List<Object> objects = mapper.readValue(paramValue, List.class);
                        for (Object o : objects) {
                            resultList.add(updateMap(o, propName));
                        }
                        jsonObj = resultList;
                    } else {
                        Map<String, Object> object;
                        try {
                            object = mapper.readValue(paramValue, Map.class);
                        } catch (JsonProcessingException jpEx) {
                            object = null;
                        }
                        //In case that annotation was setted to a NO complex Object.
                        if (object == null) {
                            logBuilder.append("...");
                            continue;
                        }
                        jsonObj = updateMap(object, propName);
                    }
                    paramValue = mapper.writeValueAsString(jsonObj);
                } else {
                    logBuilder.append("...");
                    continue;
                }
            }

            maxPayLoadLengthCnt -= paramName.length() + 3; // Includes the length of the " = " string.
            if (maxPayLoadLengthCnt > 0) {
                if (paramValue == null) {
                    paramValue = mapper.writeValueAsString(arguments[i]);
                }
                if (paramValue != null) {
                    paramValue = paramValue.substring(0, Math.min(paramValue.length(), maxPayLoadLengthCnt));
                    maxPayLoadLengthCnt -= paramValue.length();
                }
                logBuilder.append(paramValue);
            }

            if (maxPayLoadLengthCnt <= 0) {
                logBuilder.append("...");
            } else if (i + 1 < arguments.length) {
                logBuilder.append(", ");
            }

        }
    }

    private Map<String, Object> updateMap(Object obj, String propName) {
        Map<String, Object> map = mapper.convertValue(obj, Map.class);
        if (map.containsKey(propName)) {
            map.put(propName, "...");
        }
        return map;
    }

    public class StringLimitSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.length() > maxFieldLength) {
                if (ignoreLongFields) {
                    value = "...";
                } else {
                    value = value.substring(0, maxFieldLength - 3) + "...";
                }
            }
            gen.writeString(value);
        }
    }
}
