package com.plantdata.kgcloud.domain.dataset.kettle;

public class KettleXml {
    /**
     * Date: 2019-05-08
     * Time: 下午6:47
     * Author: logic
     * Description:
     */
        static String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transformation>\n" +
                "  <info>\n" +
                "    <name>test-run</name>\n" +
                "    <description/>\n" +
                "    <extended_description/>\n" +
                "    <trans_version/>\n" +
                "    <trans_type>Normal</trans_type>\n" +
                "    <directory>/</directory>\n" +
                "    <parameters>\n" +
                "    </parameters>\n" +
                "    <log>\n" +
                "      <trans-log-table>\n" +
                "        <connection/>\n" +
                "        <schema/>\n" +
                "        <table/>\n" +
                "        <size_limit_lines/>\n" +
                "        <interval/>\n" +
                "        <timeout_days/>\n" +
                "        <field>\n" +
                "          <id>ID_BATCH</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ID_BATCH</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>TRANSNAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>TRANSNAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STATUS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STATUS</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_READ</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_READ</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_WRITTEN</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_WRITTEN</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_UPDATED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_UPDATED</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_INPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_INPUT</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_OUTPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_OUTPUT</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_REJECTED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_REJECTED</name>\n" +
                "          <subject/>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>ERRORS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ERRORS</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STARTDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STARTDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>ENDDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ENDDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOGDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOGDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>DEPDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>DEPDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>REPLAYDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>REPLAYDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOG_FIELD</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOG_FIELD</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>EXECUTING_SERVER</id>\n" +
                "          <enabled>N</enabled>\n" +
                "          <name>EXECUTING_SERVER</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>EXECUTING_USER</id>\n" +
                "          <enabled>N</enabled>\n" +
                "          <name>EXECUTING_USER</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>CLIENT</id>\n" +
                "          <enabled>N</enabled>\n" +
                "          <name>CLIENT</name>\n" +
                "        </field>\n" +
                "      </trans-log-table>\n" +
                "      <perf-log-table>\n" +
                "        <connection/>\n" +
                "        <schema/>\n" +
                "        <table/>\n" +
                "        <interval/>\n" +
                "        <timeout_days/>\n" +
                "        <field>\n" +
                "          <id>ID_BATCH</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ID_BATCH</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>SEQ_NR</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>SEQ_NR</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOGDATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOGDATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>TRANSNAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>TRANSNAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STEPNAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STEPNAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STEP_COPY</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STEP_COPY</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_READ</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_READ</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_WRITTEN</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_WRITTEN</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_UPDATED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_UPDATED</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_INPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_INPUT</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_OUTPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_OUTPUT</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_REJECTED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_REJECTED</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>ERRORS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ERRORS</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>INPUT_BUFFER_ROWS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>INPUT_BUFFER_ROWS</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>OUTPUT_BUFFER_ROWS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>OUTPUT_BUFFER_ROWS</name>\n" +
                "        </field>\n" +
                "      </perf-log-table>\n" +
                "      <channel-log-table>\n" +
                "        <connection/>\n" +
                "        <schema/>\n" +
                "        <table/>\n" +
                "        <timeout_days/>\n" +
                "        <field>\n" +
                "          <id>ID_BATCH</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ID_BATCH</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOG_DATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOG_DATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOGGING_OBJECT_TYPE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOGGING_OBJECT_TYPE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>OBJECT_NAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>OBJECT_NAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>OBJECT_COPY</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>OBJECT_COPY</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>REPOSITORY_DIRECTORY</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>REPOSITORY_DIRECTORY</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>FILENAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>FILENAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>OBJECT_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>OBJECT_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>OBJECT_REVISION</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>OBJECT_REVISION</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>PARENT_CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>PARENT_CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>ROOT_CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ROOT_CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "      </channel-log-table>\n" +
                "      <step-log-table>\n" +
                "        <connection/>\n" +
                "        <schema/>\n" +
                "        <table/>\n" +
                "        <timeout_days/>\n" +
                "        <field>\n" +
                "          <id>ID_BATCH</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ID_BATCH</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOG_DATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOG_DATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>TRANSNAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>TRANSNAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STEPNAME</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STEPNAME</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>STEP_COPY</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>STEP_COPY</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_READ</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_READ</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_WRITTEN</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_WRITTEN</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_UPDATED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_UPDATED</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_INPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_INPUT</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_OUTPUT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_OUTPUT</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LINES_REJECTED</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LINES_REJECTED</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>ERRORS</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ERRORS</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOG_FIELD</id>\n" +
                "          <enabled>N</enabled>\n" +
                "          <name>LOG_FIELD</name>\n" +
                "        </field>\n" +
                "      </step-log-table>\n" +
                "      <metrics-log-table>\n" +
                "        <connection/>\n" +
                "        <schema/>\n" +
                "        <table/>\n" +
                "        <timeout_days/>\n" +
                "        <field>\n" +
                "          <id>ID_BATCH</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>ID_BATCH</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>CHANNEL_ID</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>CHANNEL_ID</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>LOG_DATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>LOG_DATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_DATE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_DATE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_CODE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_CODE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_DESCRIPTION</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_DESCRIPTION</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_SUBJECT</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_SUBJECT</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_TYPE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_TYPE</name>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "          <id>METRICS_VALUE</id>\n" +
                "          <enabled>Y</enabled>\n" +
                "          <name>METRICS_VALUE</name>\n" +
                "        </field>\n" +
                "      </metrics-log-table>\n" +
                "    </log>\n" +
                "    <maxdate>\n" +
                "      <connection/>\n" +
                "      <table/>\n" +
                "      <field/>\n" +
                "      <offset>0.0</offset>\n" +
                "      <maxdiff>0.0</maxdiff>\n" +
                "    </maxdate>\n" +
                "    <size_rowset>10000</size_rowset>\n" +
                "    <sleep_time_empty>50</sleep_time_empty>\n" +
                "    <sleep_time_full>50</sleep_time_full>\n" +
                "    <unique_connections>N</unique_connections>\n" +
                "    <feedback_shown>Y</feedback_shown>\n" +
                "    <feedback_size>50000</feedback_size>\n" +
                "    <using_thread_priorities>Y</using_thread_priorities>\n" +
                "    <shared_objects_file/>\n" +
                "    <capture_step_performance>N</capture_step_performance>\n" +
                "    <step_performance_capturing_delay>1000</step_performance_capturing_delay>\n" +
                "    <step_performance_capturing_size_limit>100</step_performance_capturing_size_limit>\n" +
                "    <dependencies>\n" +
                "    </dependencies>\n" +
                "    <partitionschemas>\n" +
                "    </partitionschemas>\n" +
                "    <slaveservers>\n" +
                "    </slaveservers>\n" +
                "    <clusterschemas>\n" +
                "    </clusterschemas>\n" +
                "    <created_user>-</created_user>\n" +
                "    <created_date>2019/05/08 12:03:29.007</created_date>\n" +
                "    <modified_user>-</modified_user>\n" +
                "    <modified_date>2019/05/08 12:03:29.007</modified_date>\n" +
                "    <key_for_session_key/>\n" +
                "    <is_key_private>N</is_key_private>\n" +
                "  </info>\n" +
                "  <notepads>\n" +
                "  </notepads>\n";

        static String connectionxml = "  <connection>\n" +
                "    <name>mysql</name>\n" +
                "    <server>ipQAQ</server>\n" +
                "    <type>typeQAQ</type>\n" +
                "    <access>Native</access>\n" +
                "    <database>dbnameQAQ</database>\n" +
                "    <port>portQAQ</port>\n" +
                "    <username>usernameQAQ</username>\n" +
                "    <password>passwordQAQ</password>\n" +
                "    <servername/>\n" +
                "    <data_tablespace/>\n" +
                "    <index_tablespace/>\n" +
                "    <attributes>\n" +
                "      <attribute>\n" +
                "        <code>FORCE_IDENTIFIERS_TO_LOWERCASE</code>\n" +
                "        <attribute>N</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>FORCE_IDENTIFIERS_TO_UPPERCASE</code>\n" +
                "        <attribute>N</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>IS_CLUSTERED</code>\n" +
                "        <attribute>N</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>PORT_NUMBER</code>\n" +
                "        <attribute>portQAQ</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>PRESERVE_RESERVED_WORD_CASE</code>\n" +
                "        <attribute>Y</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>QUOTE_ALL_FIELDS</code>\n" +
                "        <attribute>N</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>STREAM_RESULTS</code>\n" +
                "        <attribute>Y</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>SUPPORTS_BOOLEAN_DATA_TYPE</code>\n" +
                "        <attribute>Y</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>SUPPORTS_TIMESTAMP_DATA_TYPE</code>\n" +
                "        <attribute>Y</attribute>\n" +
                "      </attribute>\n" +
                "      <attribute>\n" +
                "        <code>USE_POOLING</code>\n" +
                "        <attribute>N</attribute>\n" +
                "      </attribute>\n" +
                "    </attributes>\n";

        static String sqlxml = "  </connection>\n" +
                "  <order>\n" +
                "    <hop>\n" +
                "      <from>表输入</from>\n" +
                "      <to>MongoDB output</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "  </order>\n" +
                "  <step>\n" +
                "    <name>表输入</name>\n" +
                "    <type>TableInput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <connection>mysql</connection>\n" +
                "    <sql>select * from test</sql>\n" +
                "    <limit>0</limit>\n" +
                "    <lookup/>\n" +
                "    <execute_each_row>N</execute_each_row>\n" +
                "    <variables_active>N</variables_active>\n" +
                "    <lazy_conversion_active>N</lazy_conversion_active>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>208</xloc>\n" +
                "      <yloc>128</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n";

        static String mongoxml = "  <step>\n" +
                "    <name>MongoDB output</name>\n" +
                "    <type>MongoDbOutput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <mongo_host>192.168.4.11</mongo_host>\n" +
                "    <mongo_port>19130</mongo_port>\n" +
                "    <use_all_replica_members>N</use_all_replica_members>\n" +
                "    <auth_mech/>\n" +
                "    <auth_kerberos>N</auth_kerberos>\n" +
                "    <mongo_db>zdss_test</mongo_db>\n" +
                "    <mongo_collection>test4</mongo_collection>\n" +
                "    <batch_insert_size>1000</batch_insert_size>\n" +
                "    <connect_timeout/>\n" +
                "    <socket_timeout/>\n" +
                "    <use_ssl_socket_factory>N</use_ssl_socket_factory>\n" +
                "    <read_preference>primary</read_preference>\n" +
                "    <write_concern/>\n" +
                "    <w_timeout/>\n" +
                "    <journaled_writes>N</journaled_writes>\n" +
                "    <truncate>N</truncate>\n" +
                "    <update>N</update>\n" +
                "    <upsert>N</upsert>\n" +
                "    <multi>N</multi>\n" +
                "    <modifier_update>N</modifier_update>\n" +
                "    <write_retries>5</write_retries>\n" +
                "    <write_retry_delay>10</write_retry_delay>\n" +
                "    <mongo_fields>\n" +
                "    </mongo_fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>400</xloc>\n" +
                "      <yloc>128</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step_error_handling>\n" +
                "  </step_error_handling>\n" +
                "  <slave-step-copy-partition-distribution>\n" +
                "  </slave-step-copy-partition-distribution>\n" +
                "  <slave_transformation>N</slave_transformation>\n" +
                "  <attributes/>\n" +
                "</transformation>";
    }


