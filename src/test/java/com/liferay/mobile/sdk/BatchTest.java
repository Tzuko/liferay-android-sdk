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

import com.liferay.mobile.sdk.http.Response;
import com.liferay.mobile.sdk.json.JSONParser;
import com.liferay.mobile.sdk.service.CustomGroupService;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bruno Farache
 */
public class BatchTest extends BaseTest {

	public BatchTest() throws IOException {
		super();
	}

	@Test
	public void getUserSites() throws Exception {
		CustomGroupService service = new CustomGroupService();
		Response response = Batch.execute(
			service.getUserSitesGroups(), service.getUserSitesGroups());

		List sites = JSONParser.fromJSON(response.bodyAsString(), List.class);
		assertEquals(2, sites.size());

		GroupServiceTest.assertUserSitesAsMap(
			(List<Map<String, Object>>)sites.get(0));

		GroupServiceTest.assertUserSitesAsMap(
			(List<Map<String, Object>>)sites.get(1));
	}

	@Test
	public void getUserSitesAsync() throws Exception {
		final CountDownLatch lock = new CountDownLatch(1);

		CustomGroupService service = new CustomGroupService();

		TestCallback<Response> callback = new TestCallback<>(lock);

		Batch.async(
			callback, service.getUserSitesGroups(),
			service.getUserSitesGroups());

		await(lock);

		List sites = JSONParser.fromJSON(
			callback.result.bodyAsString(), List.class);

		assertEquals(2, sites.size());

		GroupServiceTest.assertUserSitesAsMap(
			(List<Map<String, Object>>)sites.get(0));

		GroupServiceTest.assertUserSitesAsMap(
			(List<Map<String, Object>>)sites.get(1));
	}

}