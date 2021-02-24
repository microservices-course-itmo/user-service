package com.wine.to.up.user.service.controller.util;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
    @ApiImplicitParam(
        name = "page", dataType = "int", paramType = "query", value = "Number of page to retrieve. Starts from 0"
    ),
    @ApiImplicitParam(
        name = "size", dataType = "int", paramType = "query", value = "Number of elements in one page"
    )
})
public @interface ApiPageParams {
}
