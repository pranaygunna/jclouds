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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.aws.ec2.compute;

import org.jclouds.aws.ec2.AWSEC2AsyncClient;
import org.jclouds.aws.ec2.AWSEC2Client;
import org.jclouds.aws.ec2.compute.internal.AWSEC2ComputeServiceContextImpl;
import org.jclouds.ec2.compute.EC2ComputeServiceContext;

import com.google.inject.ImplementedBy;

/**
 * @author Adrian Cole
 */
@ImplementedBy(AWSEC2ComputeServiceContextImpl.class)
public interface AWSEC2ComputeServiceContext extends EC2ComputeServiceContext<AWSEC2Client, AWSEC2AsyncClient> {
   @Override
   AWSEC2ComputeService getComputeService();
}