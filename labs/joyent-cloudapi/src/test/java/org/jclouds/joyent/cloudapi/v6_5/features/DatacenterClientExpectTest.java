/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unles required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either expres or implied.  See the License for the
 * specific language governing permisions and limitations
 * under the License.
 */
package org.jclouds.joyent.cloudapi.v6_5.features;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.jclouds.http.HttpResponse;
import org.jclouds.joyent.cloudapi.v6_5.JoyentCloudClient;
import org.jclouds.joyent.cloudapi.v6_5.internal.BaseJoyentCloudClientExpectTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "DatacenterClientExpectTest")
public class DatacenterClientExpectTest extends BaseJoyentCloudClientExpectTest {

   public void testGetDatacentersWhenResponseIs2xx() {

      JoyentCloudClient clientWhenDatacentersExists = requestSendsResponse(getDatacenters, getDatacentersResponse);

      assertEquals(
            clientWhenDatacentersExists.getDatacenterClient().getDatacenters(),
            ImmutableMap.<String, URI> builder().put("us-east-1", URI.create("https://us-east-1.api.joyentcloud.com"))
                  .put("us-west-1", URI.create("https://us-west-1.api.joyentcloud.com"))
                  .put("us-sw-1", URI.create("https://us-sw-1.api.joyentcloud.com"))
                  .put("eu-ams-1", URI.create("https://eu-ams-1.api.joyentcloud.com")).build());
   }

   public void testGetDatacentersWhenResponseIs404() {
      HttpResponse getDatacentersResponse = HttpResponse.builder().statusCode(404).build();

      JoyentCloudClient getDatacentersWhenNone = requestSendsResponse(getDatacenters, getDatacentersResponse);

      assertEquals(getDatacentersWhenNone.getDatacenterClient().getDatacenters(), ImmutableMap.of());
   }
}
