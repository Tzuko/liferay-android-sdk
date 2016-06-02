/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.sdk;

import com.liferay.mobile.sdk.annotation.Param;
import com.liferay.mobile.sdk.annotation.Params;
import com.liferay.mobile.sdk.annotation.Path;
import com.liferay.mobile.sdk.http.ContentType;
import com.liferay.mobile.sdk.rx.OnCallSubscribe;
import com.liferay.mobile.sdk.util.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;

/**
 * @author Bruno Farache
 */
public class ServiceBuilder {

	public static <T> T build(Class<T> clazz) {
		Object proxy = Proxy.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] { clazz },
			new Handler(clazz));

		return (T)proxy;
	}

	static class Handler implements InvocationHandler {

		public Handler(Class<?> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			JSONObject body = new JSONObject();
			body.put(path(method), params(method, args));

			Path annotation = method.getAnnotation(Path.class);
			ContentType contentType = ContentType.JSON;

			if (annotation != null) {
				contentType = annotation.contentType();
			}

			Type returnType = method.getReturnType();
			Type genericReturnType = genericReturnType(method);
			Call call = new Call(body, genericReturnType, contentType);

			if (returnType == Call.class) {
				return call;
			}

			if (returnType == Observable.class) {
				return Observable.create(new OnCallSubscribe<>(call));
			}

			return null;
		}

		protected String methodPath(Method method) {
			Path annotation = method.getAnnotation(Path.class);

			if (annotation != null) {
				return annotation.value();
			}
			else {
				String methodName = method.getName();
				String regex = "([a-z])([A-Z]+)";
				String replacement = "$1-$2";
				methodName = methodName.replaceAll(regex, replacement);

				return "/" + methodName.toLowerCase();
			}
		}

		protected JSONObject params(Method method, Object[] args)
			throws JSONException {

			JSONObject params = new JSONObject();
			addDefaultParams(method, params);

			Annotation[][] annotations = method.getParameterAnnotations();

			for (int i = 0; i < annotations.length; i++) {
				for (Annotation annotation : annotations[i]) {
					Param param = (Param)annotation;
					String name = param.name();
					String className = param.className();
					Object value = args[i];

					if (Validator.isNull(className)) {
						if (value == null) {
							value = JSONObject.NULL;
						}

						params.put(name, value);
					}
					else {
						if (value != null) {
							mangle(name, className, (JSONObject)value, params);
						}
						else if (!className.equals(SERVICE_CONTEXT)) {
							params.put(name, JSONObject.NULL);
						}
					}
				}
			}

			return params;
		}

		protected void addDefaultParams(Method method, JSONObject jsonObject)
			throws JSONException {

			Param defaultParam = method.getAnnotation(Param.class);

			if (defaultParam != null) {
				addDefaultParam(jsonObject, defaultParam);
			}

			Params defaultParams = method.getAnnotation(Params.class);

			if (defaultParams != null) {
				Param[] params = defaultParams.value();

				for (Param param : params) {
					addDefaultParam(jsonObject, param);
				}
			}
		}

		protected void addDefaultParam(JSONObject params, Param defaultParam)
			throws JSONException {

			params.put(defaultParam.name(), defaultParam.value());
		}

		protected String path(Method method) {
			return rootPath() + methodPath(method);
		}

		protected String rootPath() {
			Path annotation = clazz.getAnnotation(Path.class);

			if (annotation != null) {
				return annotation.value();
			}
			else {
				String className = clazz.getSimpleName();

				if (className.endsWith("Service")) {
					className = className.substring(0, className.length() - 7);

					return "/" + className.toLowerCase();
				}

				return "";
			}
		}

		protected Type genericReturnType(Method method) {
			ParameterizedType returnType =
				(ParameterizedType)method.getGenericReturnType();

			return returnType.getActualTypeArguments()[0];
		}

		protected void mangle(
				String name, String className, JSONObject param,
				JSONObject params)
			throws JSONException {

			params.put("+" + name, className);

			Iterator<String> it = param.keys();

			while (it.hasNext()) {
				String key = it.next();
				Object value = param.get(key);
				params.put(name + "." + key, value);
			}
		}

		protected Class<?> clazz;

		protected static final String SERVICE_CONTEXT =
			"com.liferay.portal.service.ServiceContext";

	}

}