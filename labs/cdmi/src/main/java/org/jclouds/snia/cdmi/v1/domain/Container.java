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
package org.jclouds.snia.cdmi.v1.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jclouds.domain.JsonBall;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * 
 * @author Kenneth Nagin
 */
public class Container extends CDMIObject {

	public static Builder<?> builder() {
		return new ConcreteBuilder();
	}

	@Override
	public Builder<?> toBuilder() {
		return builder().fromContainer(this);
	}

	public static class Builder<B extends Builder<B>> extends
			CDMIObject.Builder<B> {

		private Set<String> children = ImmutableSet.of();
		private Map<String, JsonBall> metadata = Maps.newHashMap();

		/**
		 * @see Container#getChildren()
		 */
		public B children(String... children) {
			return children(ImmutableSet.copyOf(checkNotNull(children,
					"children")));
		}

		/**
		 * @see Container#getChildren()
		 */
		public B children(Set<String> children) {
			this.children = ImmutableSet.copyOf(checkNotNull(children,
					"children"));
			return self();
		}

		/**
		 * @see Container#getMetadata()
		 */
		public B metadata(Map<String, JsonBall> metadata) {
			this.metadata = ImmutableMap.copyOf(checkNotNull(metadata,
					"metadata"));
			return self();
		}

		@Override
		public Container build() {
			return new Container(this);
		}

		public B fromContainer(Container in) {
			return fromCDMIObject(in).children(in.getChildren()).metadata(
					in.getMetadata());
		}
	}

	private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
	}

	private final Set<String> children;
	private final Map<String, JsonBall> metadata;

	private Map<String, String> userMetaDataIn;
	private Map<String, String> systemMetaDataIn;
	private List<Map<String, String>> aclMetaDataIn;

	protected Container(Builder<?> builder) {
		super(builder);
		this.children = ImmutableSet.copyOf(checkNotNull(builder.children,
				"children"));
		this.metadata = ImmutableMap.copyOf(checkNotNull(builder.metadata,
				"metadata"));
	}

	/**
	 * Names of the children objects in the container object. Child container
	 * objects end with "/".
	 */
	public Set<String> getChildren() {
		return children;
	}

	/**
	 * Metadata for the container object. This field includes any user and
	 * system metadata specified in the request body metadata field, along with
	 * storage system metadata generated by the cloud storage system.
	 */
	public Map<String, JsonBall> getMetadata() {
		return metadata;
	}

	/**
	 * Parse Metadata for the container object from the original JsonBall.
	 * System metadata data is prefixed with cdmi. System ACL metadata data is
	 * prefixed with cdmi_acl
	 * 
	 */
	private void parseMetadata() {
		userMetaDataIn = new HashMap<String, String>();
		systemMetaDataIn = new HashMap<String, String>();
		aclMetaDataIn = new ArrayList<Map<String, String>>();
		Iterator<String> keys = metadata.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			JsonBall value = metadata.get(key);
			if (key.startsWith("cdmi")) {
				if (key.matches("cdmi_acl")) {
					String[] cdmi_acl_array = value.toString().split("[{}]");
					for (int i = 0; i < cdmi_acl_array.length; i++) {
						if (!(cdmi_acl_array[i].startsWith("[")
								|| cdmi_acl_array[i].startsWith("]") || cdmi_acl_array[i]
								.startsWith(","))) {
							HashMap<String, String> aclMap = new HashMap<String, String>();
							String[] cdmi_acl_member = cdmi_acl_array[i]
									.split(",");
							for (String s : cdmi_acl_member) {
								String cdmi_acl_key = s.substring(0,
										s.indexOf(":"));
								String cdmi_acl_value = s.substring(s
										.indexOf(":") + 1);
								cdmi_acl_value.replace('"', ' ').trim();
								aclMap.put(cdmi_acl_key, cdmi_acl_value);
							}
							aclMetaDataIn.add(aclMap);
						}
					}
				} else {
					systemMetaDataIn.put(key, value.toString()
							.replace('"', ' ').trim());
				}
			} else {
				userMetaDataIn.put(key, value.toString().replace('"', ' ')
						.trim());
			}
		}
	}

	/**
	 * Get User Metadata for the container object. This field includes any user
	 * metadata
	 */
	public Map<String, String> getUserMetadata() {
		if (userMetaDataIn == null) {
			parseMetadata();
		}
		return userMetaDataIn;
	}

	/**
	 * Get System Metadata for the container object excluding ACL related
	 * metadata
	 */
	public Map<String, String> getSystemMetadata() {
		if (systemMetaDataIn == null) {
			parseMetadata();
		}
		return systemMetaDataIn;
	}

	/**
	 * Get System Metadata for the container object excluding ACL related
	 * metadata
	 */
	public List<Map<String, String>> getACLMetadata() {
		if (aclMetaDataIn == null) {
			parseMetadata();
		}
		return aclMetaDataIn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Container that = Container.class.cast(o);
		return super.equals(that) && equal(this.children, that.children)
				&& equal(this.metadata, that.metadata);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), children, metadata);
	}

	@Override
	public ToStringHelper string() {
		return super.string().add("children", children)
				.add("metadata", metadata);
	}

}
