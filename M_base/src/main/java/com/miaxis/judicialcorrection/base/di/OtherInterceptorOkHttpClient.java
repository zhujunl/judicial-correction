package com.miaxis.judicialcorrection.base.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * OtherInterceptorOkHttpClient
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface OtherInterceptorOkHttpClient {
}
