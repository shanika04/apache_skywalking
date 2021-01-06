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

package org.apache.skywalking.oap.server.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.skywalking.oap.server.core.cache.NetworkAddressAliasCache;
import org.apache.skywalking.oap.server.core.cache.ProfileTaskCache;
import org.apache.skywalking.oap.server.core.command.CommandService;
import org.apache.skywalking.oap.server.core.config.ConfigService;
import org.apache.skywalking.oap.server.core.config.DownSamplingConfigService;
import org.apache.skywalking.oap.server.core.config.IComponentLibraryCatalogService;
import org.apache.skywalking.oap.server.core.profile.ProfileTaskMutationService;
import org.apache.skywalking.oap.server.core.query.AggregationQueryService;
import org.apache.skywalking.oap.server.core.query.AlarmQueryService;
import org.apache.skywalking.oap.server.core.query.LogQueryService;
import org.apache.skywalking.oap.server.core.query.MetadataQueryService;
import org.apache.skywalking.oap.server.core.query.MetricQueryService;
import org.apache.skywalking.oap.server.core.query.ProfileTaskQueryService;
import org.apache.skywalking.oap.server.core.query.TopNRecordsQueryService;
import org.apache.skywalking.oap.server.core.query.TopologyQueryService;
import org.apache.skywalking.oap.server.core.query.TraceQueryService;
import org.apache.skywalking.oap.server.core.remote.RemoteSenderService;
import org.apache.skywalking.oap.server.core.remote.client.RemoteClientManager;
import org.apache.skywalking.oap.server.core.server.GRPCHandlerRegister;
import org.apache.skywalking.oap.server.core.server.JettyHandlerRegister;
import org.apache.skywalking.oap.server.core.source.SourceReceiver;
import org.apache.skywalking.oap.server.core.storage.model.IModelManager;
import org.apache.skywalking.oap.server.core.storage.model.IModelOverride;
import org.apache.skywalking.oap.server.core.storage.model.INewModel;
import org.apache.skywalking.oap.server.core.worker.IWorkerInstanceGetter;
import org.apache.skywalking.oap.server.core.worker.IWorkerInstanceSetter;
import org.apache.skywalking.oap.server.library.module.ModuleDefine;

/**
 * Core module definition. Define all open services to other modules.
 */
public class CoreModule extends ModuleDefine {
    public static final String NAME = "core";
    private static int ENDPOINT_NAME_MAX_LENGTH = 150;

    public CoreModule() {
        super(NAME);
    }

    @Override
    public Class[] services() {
        List<Class> classes = new ArrayList<>();
        classes.add(ConfigService.class);
        classes.add(DownSamplingConfigService.class);
        classes.add(IComponentLibraryCatalogService.class);

        classes.add(IWorkerInstanceGetter.class);
        classes.add(IWorkerInstanceSetter.class);

        addServerInterface(classes);
        addReceiverInterface(classes);
        addInsideService(classes);
        addCacheService(classes);
        addQueryService(classes);
        addProfileService(classes);

        classes.add(CommandService.class);

        return classes.toArray(new Class[] {});
    }

    /**
     * Format endpoint name by using the length config in the core module. This is a global rule, every place including
     * endpoint as the {@link org.apache.skywalking.oap.server.core.source.Source} should follow this for any core
     * module implementation.
     *
     * @param endpointName raw data, literal string.
     * @return the string, which length less than or equals {@link #ENDPOINT_NAME_MAX_LENGTH};
     */
    public static String formatEndpointName(String endpointName) {
        if (endpointName.length() > ENDPOINT_NAME_MAX_LENGTH) {
            return endpointName.substring(0, ENDPOINT_NAME_MAX_LENGTH);
        } else {
            return endpointName;
        }
    }

    public static void setEndpointNameMaxLength(final int endpointNameMaxLength) {
        ENDPOINT_NAME_MAX_LENGTH = endpointNameMaxLength;
    }

    private void addProfileService(List<Class> classes) {
        classes.add(ProfileTaskMutationService.class);
        classes.add(ProfileTaskQueryService.class);
        classes.add(ProfileTaskCache.class);
    }

    private void addQueryService(List<Class> classes) {
        classes.add(TopologyQueryService.class);
        classes.add(MetricQueryService.class);
        classes.add(TraceQueryService.class);
        classes.add(LogQueryService.class);
        classes.add(MetadataQueryService.class);
        classes.add(AggregationQueryService.class);
        classes.add(AlarmQueryService.class);
        classes.add(TopNRecordsQueryService.class);
    }

    private void addServerInterface(List<Class> classes) {
        classes.add(GRPCHandlerRegister.class);
        classes.add(JettyHandlerRegister.class);
    }

    private void addInsideService(List<Class> classes) {
        classes.add(INewModel.class);
        classes.add(IModelManager.class);
        classes.add(IModelOverride.class);
        classes.add(RemoteClientManager.class);
        classes.add(RemoteSenderService.class);
    }

    private void addCacheService(List<Class> classes) {
        classes.add(NetworkAddressAliasCache.class);
    }

    private void addReceiverInterface(List<Class> classes) {
        classes.add(SourceReceiver.class);
    }
}
