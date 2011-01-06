/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.ec2.compute.strategy;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.aws.util.AWSUtils;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.strategy.GetNodeMetadataStrategy;
import org.jclouds.compute.strategy.ResumeNodeStrategy;
import org.jclouds.ec2.EC2Client;
import org.jclouds.ec2.services.InstanceClient;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class EC2ResumeNodeStrategy implements ResumeNodeStrategy {
   private final InstanceClient client;
   private final GetNodeMetadataStrategy getNode;

   @Inject
   protected EC2ResumeNodeStrategy(EC2Client client, GetNodeMetadataStrategy getNode) {
      this.client = client.getInstanceServices();
      this.getNode = getNode;
   }

   @Override
   public NodeMetadata resumeNode(String id) {
      String[] parts = AWSUtils.parseHandle(id);
      String region = parts[0];
      String instanceId = parts[1];
      client.startInstancesInRegion(region, instanceId);
      return getNode.getNode(id);
   }

}