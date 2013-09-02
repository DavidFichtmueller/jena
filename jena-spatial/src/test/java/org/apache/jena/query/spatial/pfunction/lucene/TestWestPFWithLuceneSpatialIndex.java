/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.query.spatial.pfunction.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.jena.query.spatial.SpatialSearchUtil;
import org.apache.jena.query.spatial.pfunction.AbstractTestWestPF;
import org.junit.After;
import org.junit.Before;

/**
 * This abstract class defines a setup configuration for a dataset with a Lucene
 * index.
 */
public class TestWestPFWithLuceneSpatialIndex extends
		AbstractTestWestPF {

	@Before
	public void init() throws IOException {
		dataset = SpatialSearchUtil.initInMemoryDatasetWithLuceneSpatitalIndex();
	}

	@After
	public void destroy() {
		SpatialSearchUtil.deleteOldLuceneIndexDir();
	}
}
