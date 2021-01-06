/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.core.storage.query;

import java.io.IOException;
import java.util.List;
import org.apache.skywalking.oap.server.core.analysis.DownSampling;
import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.core.query.entity.TopNEntity;
import org.apache.skywalking.oap.server.core.storage.DAO;

public interface IAggregationQueryDAO extends DAO {

    List<TopNEntity> getServiceTopN(final String indName, String valueCName, final int topN,
                                    final DownSampling downsampling, final long startTB, final long endTB, final Order order) throws IOException;

    List<TopNEntity> getAllServiceInstanceTopN(final String indName, String valueCName, final int topN,
                                               final DownSampling downsampling, final long startTB, final long endTB, final Order order) throws IOException;

    List<TopNEntity> getServiceInstanceTopN(final String serviceId, final String indName, String valueCName,
                                            final int topN, final DownSampling downsampling, final long startTB, final long endTB,
                                            final Order order) throws IOException;

    List<TopNEntity> getAllEndpointTopN(final String indName, String valueCName, final int topN,
                                        final DownSampling downsampling, final long startTB, final long endTB, final Order order) throws IOException;

    List<TopNEntity> getEndpointTopN(final String serviceId, final String indName, String valueCName, final int topN,
                                     final DownSampling downsampling, final long startTB, final long endTB, final Order order) throws IOException;
}
