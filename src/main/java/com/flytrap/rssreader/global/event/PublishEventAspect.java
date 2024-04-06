package com.flytrap.rssreader.global.event;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PublishEventAspect implements ApplicationEventPublisherAware {

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final String spelRegex = "#\\{(.*)\\}";

    private ApplicationEventPublisher eventPublisher;

    @Pointcut("@annotation(publishEvent)")
    public void publishEventPointcut(PublishEvent publishEvent) {
    }

    /**
     * return값 없으면 new eventType() params값 없으면 new eventType(returnValue) params값이 문자열이면 new
     * eventType(params) params값이 SpEL이면 parse 후에 eventType(params)
     */
    @Before(value = "publishEventPointcut(publishEvent)", argNames = "joinPoint,publishEvent")
    public void before(JoinPoint joinPoint, PublishEvent publishEvent)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] args = joinPoint.getArgs();
        Map<String, Object> paramMap = new HashMap<>();

        // 클래스 이름을 키로 사용하여 매개변수를 맵에 추가
        for (Object arg : args) {
            paramMap.put(arg.getClass().getName(), arg);
        }

        Object event;

        // TODO: PARM이 안 들어와 있을경우 구현
        if (isSpel(publishEvent.params())) {
            String spel = publishEvent.params().replaceAll(spelRegex, "$1");
            StandardEvaluationContext context = getEvaluationContext(joinPoint, spel);

            Object constructArg = expressionParser.parseExpression(spel).getValue(context);
            event = publishEvent.eventType()
                    .getDeclaredConstructor(constructArg.getClass())
                    .newInstance(constructArg);
        } else if (publishEvent.params() == null || publishEvent.params().isEmpty()) {
            event = publishEvent.eventType().getDeclaredConstructor().newInstance();
        } else {
            event = publishEvent.eventType()
                    .getConstructor(String.class)
                    .newInstance(publishEvent.params());
        }

        eventPublisher.publishEvent(event);
    }

    private StandardEvaluationContext getEvaluationContext(JoinPoint joinPoint, String SpEL) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        setVariables(evaluationContext, joinPoint, SpEL);

        return evaluationContext;
    }

    private void setVariables(StandardEvaluationContext evaluationContext, JoinPoint joinPoint,
            String SpEL)
            throws SpelParseException {
        int variableCount = SpEL.length() - SpEL.replace("#", "").length();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        HashMap<String, Object> map = new HashMap<>();

        for (int i = 0; i < variableCount; i++) {
            String parameterName = parameterNames[i];
            Object arg = args[i];

            map.put(parameterName, arg);
        }

        evaluationContext.setVariables(map);
    }


    private boolean isSpel(String params) {
        Pattern spelPattern = Pattern.compile(spelRegex);
        return spelPattern.matcher(params).matches();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
