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

package com.liferay.mobile.sdk.java;

import com.liferay.mobile.android.http.Response;
import com.liferay.mobile.android.http.file.UploadData;
import com.liferay.mobile.android.service.JSONObjectWrapper;
import com.liferay.mobile.sdk.util.LanguageUtil;

import org.apache.commons.lang.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Bruno Farache
 */
public class JavaUtil extends LanguageUtil {

	public static final String INTEGER = "Integer";

	public static final String JSON_ARRAY = "JSONArray";

	public static final String JSON_OBJECT = "JSONObject";

	public static final String JSON_OBJECT_WRAPPER = "JSONObjectWrapper";

	public static final String UPLOAD_DATA = "UploadData";

	public String getReturnType(String type) {
		type = getType(type);

		if (type.equals(VOID)) {
			return type;
		}

		if (type.equals(INT)) {
			return INTEGER;
		}

		if (type.equals(JSON_OBJECT_WRAPPER) || type.equals(UPLOAD_DATA)) {
			return JSON_OBJECT;
		}

		if (type.equals(BYTE_ARRAY)) {
			return JSON_ARRAY;
		}

		return WordUtils.capitalize(type);
	}

	public String getType(String type) {
		type = super.getType(type);

		if (type.equals(BOOLEAN) || type.equals(BYTE_ARRAY) ||
			type.equals(DOUBLE) || type.equals(INT) || type.equals(LONG) ||
			type.equals(VOID)) {

			return type;
		}

		if (isArray(type)) {
			return JSON_ARRAY;
		}

		if (type.equals(STRING)) {
			return "String";
		}

		if (type.equals(FILE)) {
			return UPLOAD_DATA;
		}

		if (type.startsWith(OBJECT_PREFIX)) {
			return JSON_OBJECT_WRAPPER;
		}

		return JSON_OBJECT;
	}

	public boolean isPrimitive(String type) {
		if (type.equals(BOOLEAN) || type.equals(DOUBLE) || type.equals(INT) ||
			type.equals(LONG)) {

			return true;
		}

		return false;
	}

	public Class type(String type) {
		type = super.getType(type);

		if (type.equals(BOOLEAN)) {
			return boolean.class;
		}

		if (type.equals(BYTE_ARRAY)) {
			return byte[].class;
		}

		if (type.equals(DOUBLE)) {
			return double.class;
		}

		if (type.equals(INT)) {
			return int.class;
		}

		if (type.equals(LONG)) {
			return long.class;
		}

		if (type.equals(VOID)) {
			return Response.class;
		}

		if (isArray(type)) {
			return JSONArray.class;
		}

		if (type.equals(STRING)) {
			return String.class;
		}

		if (type.equals(FILE)) {
			return UploadData.class;
		}

		if (type.startsWith(OBJECT_PREFIX)) {
			return JSONObjectWrapper.class;
		}

		return JSONObject.class;
	}

}