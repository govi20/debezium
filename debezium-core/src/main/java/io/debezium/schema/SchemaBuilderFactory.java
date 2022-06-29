/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.schema;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

import io.debezium.connector.AbstractSourceInfo;
import io.debezium.heartbeat.HeartbeatImpl;
import io.debezium.pipeline.txmetadata.TransactionMonitor;
import io.debezium.util.SchemaNameAdjuster;

/**
 * A factory for creating {@link SchemaBuilder} structs.
 *
 * @author Anisha Mohanty
 */

public class SchemaBuilderFactory {

    private static final int SCHEMA_VERSION = 1;

    private static final String SERVER_NAME_CLASS = "io.debezium.connector.common.ServerNameKey";
    private static final String HEARTBEAT_CLASS = "io.debezium.connector.common.Heartbeat";

    private static final String TRANSACTION_BLOCK_NAME = "event.block";
    private static final String TRANSACTION_EVENT_COUNT_COLLECTION_NAME = "event.collection";
    private static final String TRANSACTION_METADATA_KEY_CLASS = "io.debezium.connector.common.TransactionMetadataKey";
    private static final String TRANSACTION_METADATA_VALUE_CLASS = "io.debezium.connector.common.TransactionMetadataValue";

    public static Schema heartbeatKeySchema(SchemaNameAdjuster adjuster) {
        return SchemaBuilder.struct()
                .name(adjuster.adjust(SERVER_NAME_CLASS))
                .version(SCHEMA_VERSION)
                .field(HeartbeatImpl.SERVER_NAME_KEY, Schema.STRING_SCHEMA)
                .build();
    }

    public static Schema heartbeatValueSchema(SchemaNameAdjuster adjuster) {
        return SchemaBuilder.struct()
                .name(adjuster.adjust(HEARTBEAT_CLASS))
                .version(SCHEMA_VERSION)
                .field(AbstractSourceInfo.TIMESTAMP_KEY, Schema.INT64_SCHEMA)
                .build();
    }

    public static Schema transactionBlockSchema() {
        return SchemaBuilder.struct().optional()
                .name(TRANSACTION_BLOCK_NAME)
                .version(SCHEMA_VERSION)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_ID_KEY, Schema.STRING_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_TOTAL_ORDER_KEY, Schema.INT64_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_DATA_COLLECTION_ORDER_KEY, Schema.INT64_SCHEMA)
                .build();
    }

    public static Schema transactionEventCountPerDataCollectionSchema() {
        return SchemaBuilder.struct().optional()
                .name(TRANSACTION_EVENT_COUNT_COLLECTION_NAME)
                .version(SCHEMA_VERSION)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_COLLECTION_KEY, Schema.STRING_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_EVENT_COUNT_KEY, Schema.INT64_SCHEMA)
                .build();
    }

    public static Schema transactionKeySchema(SchemaNameAdjuster adjuster) {
        return SchemaBuilder.struct()
                .name(adjuster.adjust(TRANSACTION_METADATA_KEY_CLASS))
                .version(SCHEMA_VERSION)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_ID_KEY, Schema.STRING_SCHEMA)
                .build();
    }

    public static Schema transactionValueSchema(SchemaNameAdjuster adjuster) {
        return SchemaBuilder.struct()
                .name(adjuster.adjust(TRANSACTION_METADATA_VALUE_CLASS))
                .version(SCHEMA_VERSION)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_STATUS_KEY, Schema.STRING_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_ID_KEY, Schema.STRING_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_EVENT_COUNT_KEY, Schema.OPTIONAL_INT64_SCHEMA)
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_DATA_COLLECTIONS_KEY, SchemaBuilder.array(transactionEventCountPerDataCollectionSchema()))
                .field(TransactionMonitor.DEBEZIUM_TRANSACTION_TS_MS, Schema.INT64_SCHEMA)
                .build();
    }
}
