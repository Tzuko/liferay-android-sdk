package com.liferay.mobile.sdk.v62.dlfileversion;

import com.liferay.mobile.sdk.Call;
import com.liferay.mobile.sdk.annotation.Param;
import com.liferay.mobile.sdk.annotation.Path;
import java.lang.Integer;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/dlfileversion")
public interface DLFileVersionService {
  @Path("/get-file-version")
  Call<JSONObject> getFileVersion(@Param("fileVersionId") long fileVersionId);

  @Path("/get-file-versions")
  Call<JSONArray> getFileVersions(@Param("fileEntryId") long fileEntryId, @Param("status") int status);

  @Path("/get-file-versions-count")
  Call<Integer> getFileVersionsCount(@Param("fileEntryId") long fileEntryId, @Param("status") int status);

  @Path("/get-latest-file-version")
  Call<JSONObject> getLatestFileVersion(@Param("fileEntryId") long fileEntryId);
}
