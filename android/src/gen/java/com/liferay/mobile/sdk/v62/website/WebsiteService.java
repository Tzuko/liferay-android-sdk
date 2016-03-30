package com.liferay.mobile.sdk.v62.website;

import com.liferay.mobile.sdk.http.Response;
import com.liferay.mobile.sdk.v2.Call;
import com.liferay.mobile.sdk.v2.JsonObject;
import com.liferay.mobile.sdk.v2.Param;
import com.liferay.mobile.sdk.v2.Path;
import java.lang.String;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/website")
public interface WebsiteService {
  @Path("/add-website")
  Call<JSONObject> addWebsite(@Param("className") String className, @Param("classPK") long classPK, @Param("url") String url, @Param("typeId") int typeId, @Param("primary") boolean primary);

  @Path("/add-website")
  Call<JSONObject> addWebsite(@Param("className") String className, @Param("classPK") long classPK, @Param("url") String url, @Param("typeId") int typeId, @Param("primary") boolean primary, @JsonObject(name = "serviceContext", className = "com.liferay.portal.service.ServiceContext") JSONObject serviceContext);

  @Path("/delete-website")
  Call<Response> deleteWebsite(@Param("websiteId") long websiteId);

  @Path("/get-website")
  Call<JSONObject> getWebsite(@Param("websiteId") long websiteId);

  @Path("/get-websites")
  Call<JSONArray> getWebsites(@Param("className") String className, @Param("classPK") long classPK);

  @Path("/update-website")
  Call<JSONObject> updateWebsite(@Param("websiteId") long websiteId, @Param("url") String url, @Param("typeId") int typeId, @Param("primary") boolean primary);
}