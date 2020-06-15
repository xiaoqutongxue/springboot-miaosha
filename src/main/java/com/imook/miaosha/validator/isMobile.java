package com.imook.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {isMobileValidator.class}
)
/**
 * validatedBy = {isMobileValidator.class} 系统去找isMobileValidator实现方法
 */
public @interface isMobile {

    // 必须要输入的意思
    boolean required() default true;

    // 自定义选择器 必须的：意思是如果检验不通过 应该输出什么信息
    String message() default "手机号码格式不正确";
    // 自定义选择器 必须的
    Class<?>[] groups() default {};
    // 自定义选择器 必须的
    Class<? extends Payload>[] payload() default {};
}
