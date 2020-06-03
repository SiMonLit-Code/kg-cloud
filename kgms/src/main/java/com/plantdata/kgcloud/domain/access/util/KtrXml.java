package com.plantdata.kgcloud.domain.access.util;

public class KtrXml {
        static String xml = "<transformation>\n" +
                "  <info>\n" +
                "    <name>resourceNameQAQ</name>\n" +
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
                "    <created_date>2020/03/06 01:55:55.531</created_date>\n" +
                "    <modified_user>-</modified_user>\n" +
                "    <modified_date>2020/03/06 01:55:55.531</modified_date>\n" +
                "    <key_for_session_key>H4sIAAAAAAAAAAMAAAAAAAAAAAA=</key_for_session_key>\n" +
                "    <is_key_private>N</is_key_private>\n" +
                "  </info>\n" +
                "  <notepads>\n" +
                "  </notepads>\n";

        static String connectionXml = "  <connection>\n" +
                "    <name>access</name>\n" +
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
                "    </attributes>\n" +
                "  </connection>\n";

        static String orderXml = "<order>\n" +
                "    <hop>\n" +
                "      <from>获取变量</from>\n" +
                "      <to>JSON output</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>JSON output</from>\n" +
                "      <to>JavaScript代码 2</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>JavaScript代码 2</from>\n" +
                "      <to>JavaScript代码</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>" +
                "    <hop>\n" +
                "      <from>input</from>\n" +
                "      <to>获取变量</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>MongoDB input</from>\n" +
                "      <to>获取变量 2</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n"+
                "    <hop>\n" +
                "      <from>JavaScript代码</from>\n" +
                "      <to>Kafka_producer_QAQ_resourceNameQAQ_QAQ</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>JSON output 2</from>\n" +
                "      <to>JavaScript代码 2 2</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>获取变量 2</from>\n" +
                "      <to>JSON output 2</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n" +
                "    <hop>\n" +
                "      <from>JavaScript代码 2 2</from>\n" +
                "      <to>JavaScript代码 3</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n"+
                "    <hop>\n" +
                "      <from>JavaScript代码 3</from>\n" +
                "      <to>Kafka_producer_QAQ_resourceNameQAQ_QAQ 2</to>\n" +
                "      <enabled>Y</enabled>\n" +
                "    </hop>\n"+
                "  </order>\n";
        static String defaultStepXml = "  <step>\n" +
                "    <name>JSON output</name>\n" +
                "    <type>JsonOutput</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <outputValue>data_json</outputValue>\n" +
                "    <jsonBloc>data_json</jsonBloc>\n" +
                "    <nrRowsInBloc>1</nrRowsInBloc>\n" +
                "    <operation_type>outputvalue</operation_type>\n" +
                "    <compatibility_mode>N</compatibility_mode>\n" +
                "    <encoding>UTF-8</encoding>\n" +
                "    <addtoresult>N</addtoresult>\n" +
                "    <file>\n" +
                "      <name/>\n" +
                "      <extention>js</extention>\n" +
                "      <append>N</append>\n" +
                "      <split>N</split>\n" +
                "      <haspartno>N</haspartno>\n" +
                "      <add_date>N</add_date>\n" +
                "      <add_time>N</add_time>\n" +
                "      <create_parent_folder>N</create_parent_folder>\n" +
                "      <DoNotOpenNewFileInit>N</DoNotOpenNewFileInit>\n" +
                "      <servlet_output>N</servlet_output>\n" +
                "    </file>\n" +
                "    <fields>\n" +
                "       fieldsQAQ" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>304</xloc>\n" +
                "      <yloc>128</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step>\n" +
                "    <name>JavaScript代码</name>\n" +
                "    <type>ScriptValueMod</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <compatible>N</compatible>\n" +
                "    <optimizationLevel>9</optimizationLevel>\n" +
                "    <jsScripts>\n" +
                "      <jsScript>\n" +
                "        <jsScript_type>0</jsScript_type>\n" +
                "        <jsScript_name>Script 1</jsScript_name>\n" +
                "        <jsScript_script>//Script here\n" +
                "\n" +
                "\n" +
                "var re = {\n" +
                "\"data\" : JSON.parse(data_input),\n" +
                "\"operationType\":\"ADD\",\n" +
                "\"resourceConfig_\":resourceConfig_\n" +
                "}\n" +
                "\n" +
                "var value=JSON.stringify(re)</jsScript_script>\n" +
                "      </jsScript>\n" +
                "    </jsScripts>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>value</name>\n" +
                "        <rename>value</rename>\n" +
                "        <type>String</type>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <replace>N</replace>\n" +
                "      </field>\n" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>656</xloc>\n" +
                "      <yloc>128</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step>\n" +
                "    <name>JavaScript代码 3</name>\n" +
                "    <type>ScriptValueMod</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <compatible>N</compatible>\n" +
                "    <optimizationLevel>9</optimizationLevel>\n" +
                "    <jsScripts>\n" +
                "      <jsScript>\n" +
                "        <jsScript_type>0</jsScript_type>\n" +
                "        <jsScript_name>Script 1</jsScript_name>\n" +
                "        <jsScript_script>//Script here\n" +
                "\n" +
                "\n" +
                "var re = {\n" +
                "\"data\" : JSON.parse(data_input),\n" +
                "\"operationType\":\"ADD\",\n" +
                "\"resourceConfig_\":resourceConfig_\n" +
                "}\n" +
                "\n" +
                "var value=JSON.stringify(re)</jsScript_script>\n" +
                "      </jsScript>\n" +
                "    </jsScripts>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>value</name>\n" +
                "        <rename>value</rename>\n" +
                "        <type>String</type>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <replace>N</replace>\n" +
                "      </field>\n" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>656</xloc>\n" +
                "      <yloc>128</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step>\n" +
                "    <name>JavaScript代码 2</name>\n" +
                "    <type>ScriptValueMod</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <compatible>N</compatible>\n" +
                "    <optimizationLevel>9</optimizationLevel>\n" +
                "    <jsScripts>\n" +
                "      <jsScript>\n" +
                "        <jsScript_type>0</jsScript_type>\n" +
                "        <jsScript_name>Script 1</jsScript_name>\n" +
                "        <jsScript_script>var data_input = JSON.parse(data_json)[\"data_json\"][0]\n" +
                "\n" +
                "if ( data_input[\"json\"] !=null ) {\n" +
                "    data_input = data_input[\"json\"]\n" +
                "}else{\n" +
                "data_input=JSON.stringify(data_input)\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</jsScript_script>\n" +
                "      </jsScript>\n" +
                "    </jsScripts>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>data_input</name>\n" +
                "        <rename>data_input</rename>\n" +
                "        <type>String</type>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <replace>N</replace>\n" +
                "      </field>\n" +
                "    </fields>\n" +
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
                "  <step>\n" +
                "    <name>获取变量</name>\n" +
                "    <type>GetVariable</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>resourceName</name>\n" +
                "        <variable>${resourceName}</variable>\n" +
                "        <type>String</type>\n" +
                "        <format/>\n" +
                "        <currency/>\n" +
                "        <decimal/>\n" +
                "        <group/>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <trim_type>none</trim_type>\n" +
                "      </field>\n" +
                "      <field>\n" +
                "        <name>resourceConfig_</name>\n" +
                "        <variable>${resourceConfig_}</variable>\n" +
                "        <type>String</type>\n" +
                "        <format/>\n" +
                "        <currency/>\n" +
                "        <decimal/>\n" +
                "        <group/>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <trim_type>none</trim_type>\n" +
                "      </field>\n" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>759</xloc>\n" +
                "      <yloc>118</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n"+
                "  <step>\n" +
                "    <name>获取变量 2</name>\n" +
                "    <type>GetVariable</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>resourceName</name>\n" +
                "        <variable>${resourceName}</variable>\n" +
                "        <type>String</type>\n" +
                "        <format/>\n" +
                "        <currency/>\n" +
                "        <decimal/>\n" +
                "        <group/>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <trim_type>none</trim_type>\n" +
                "      </field>\n" +
                "      <field>\n" +
                "        <name>resourceConfig_</name>\n" +
                "        <variable>${resourceConfig_}</variable>\n" +
                "        <type>String</type>\n" +
                "        <format/>\n" +
                "        <currency/>\n" +
                "        <decimal/>\n" +
                "        <group/>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <trim_type>none</trim_type>\n" +
                "      </field>\n" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>759</xloc>\n" +
                "      <yloc>118</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n"+
                "  <step>\n" +
                "    <name>JSON output 2</name>\n" +
                "    <type>JsonOutput</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <outputValue>data_json</outputValue>\n" +
                "    <jsonBloc>data_json</jsonBloc>\n" +
                "    <nrRowsInBloc>1</nrRowsInBloc>\n" +
                "    <operation_type>outputvalue</operation_type>\n" +
                "    <compatibility_mode>N</compatibility_mode>\n" +
                "    <encoding>UTF-8</encoding>\n" +
                "    <addtoresult>N</addtoresult>\n" +
                "    <file>\n" +
                "      <name/>\n" +
                "      <extention>js</extention>\n" +
                "      <append>N</append>\n" +
                "      <split>N</split>\n" +
                "      <haspartno>N</haspartno>\n" +
                "      <add_date>N</add_date>\n" +
                "      <add_time>N</add_time>\n" +
                "      <create_parent_folder>N</create_parent_folder>\n" +
                "      <DoNotOpenNewFileInit>N</DoNotOpenNewFileInit>\n" +
                "      <servlet_output>N</servlet_output>\n" +
                "    </file>\n" +
                "    <fields>\n" +
                "       <field>\n" +
                "        <name>json</name>\n" +
                "        <element>json</element>\n" +
                "      </field>\n"+
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>384</xloc>\n" +
                "      <yloc>368</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n"+
                " <step>\n" +
                "    <name>JavaScript代码 2 2</name>\n" +
                "    <type>ScriptValueMod</type>\n" +
                "    <description/>\n" +
                "    <distribute>N</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <compatible>N</compatible>\n" +
                "    <optimizationLevel>9</optimizationLevel>\n" +
                "    <jsScripts>\n" +
                "      <jsScript>\n" +
                "        <jsScript_type>0</jsScript_type>\n" +
                "        <jsScript_name>Script 1</jsScript_name>\n" +
                "        <jsScript_script>var data_input = JSON.parse(data_json)[\"data_json\"][0]\n" +
                "\n" +
                "if ( data_input[\"json\"] !=null ) {\n" +
                "    data_input = data_input[\"json\"]\n" +
                "}else{\n" +
                "data_input=JSON.stringify(data_input)\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</jsScript_script>\n" +
                "      </jsScript>\n" +
                "    </jsScripts>\n" +
                "    <fields>\n" +
                "      <field>\n" +
                "        <name>data_input</name>\n" +
                "        <rename>data_input</rename>\n" +
                "        <type>String</type>\n" +
                "        <length>-1</length>\n" +
                "        <precision>-1</precision>\n" +
                "        <replace>N</replace>\n" +
                "      </field>\n" +
                "    </fields>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>656</xloc>\n" +
                "      <yloc>352</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>";

        static String mongoErrorInput = "<step>\n" +
                "    <name>MongoDB input</name>\n" +
                "    <type>MongoDbInput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <hostname>errorMongoIpQAQ</hostname>\n" +
                "    <port>errorMongoPortQAQ</port>\n" +
                "    <use_all_replica_members>N</use_all_replica_members>\n" +
                "    <db_name>errorMongoDbQAQ</db_name>\n" +
                "    <fields_name/>\n" +
                "    <collection>errorMongoCollectionQAQ</collection>\n" +
                "    <json_field_name>json</json_field_name>\n" +
                "    <json_query/>\n" +
                "    <auth_database/>\n" +
                "    <auth_user>errorMongoUserQAQ</auth_user>\n" +
                "    <auth_password>errorMongoPasswordQAQ</auth_password>\n" +
                "    <auth_mech/>\n" +
                "    <auth_kerberos>N</auth_kerberos>\n" +
                "    <connect_timeout/>\n" +
                "    <socket_timeout/>\n" +
                "    <use_ssl_socket_factory>N</use_ssl_socket_factory>\n" +
                "    <read_preference>primary</read_preference>\n" +
                "    <output_json>Y</output_json>\n" +
                "    <query_is_pipeline>N</query_is_pipeline>\n" +
                "    <execute_for_each_row>N</execute_for_each_row>\n" +
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
                "      <yloc>368</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>";

        static String jsonFieldxml = "<field>\n" +
                "        <name>fieldQAQ</name>\n" +
                "        <element>fieldQAQ</element>\n" +
                "      </field>\n";

        static String mysqlInputXml = "<step>\n" +
                "    <name>input</name>\n" +
                "    <type>TableInput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <connection>access</connection>\n" +
                "    <sql>queryQAQ</sql>\n" +
                "    <limit>0</limit>\n" +
                "    <lookup/>\n" +
                "    <execute_each_row>N</execute_each_row>\n" +
                "    <variables_active>Y</variables_active>\n" +
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
                "      <xloc>343</xloc>\n" +
                "      <yloc>118</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" ;

        static String kafkaxml = "  <step>\n" +
                "    <name>Kafka_producer_QAQ_resourceNameQAQ_QAQ</name>\n" +
                "    <type>KafkaProducerOutput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <connectionType>DIRECT</connectionType>\n" +
                "    <directBootstrapServers>kafkaQAQ</directBootstrapServers>\n" +
                "    <clusterName/>\n" +
                "    <topic>topicQAQ</topic>\n" +
                "    <clientId/>\n" +
                "    <keyField>resourceName</keyField>\n" +
                "    <messageField>value</messageField>\n" +
                "    <advancedConfig>\n" +
                "      <option property=\"compression.type\" value=\"none\"/>\n" +
                "      <option property=\"ssl.key.password\" value=\"\"/>\n" +
                "      <option property=\"ssl.keystore.location\" value=\"\"/>\n" +
                "      <option property=\"ssl.keystore.password\" value=\"\"/>\n" +
                "      <option property=\"ssl.truststore.location\" value=\"\"/>\n" +
                "      <option property=\"ssl.truststore.password\" value=\"\"/>\n" +
                "      <option property=\"partitioner.class\" value=\"com.hiekn.partitioner.ChannelPartitioner\"/>\n" +
                "    </advancedConfig>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>935</xloc>\n" +
                "      <yloc>118</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step>\n" +
                "    <name>Kafka_producer_QAQ_resourceNameQAQ_QAQ 2</name>\n" +
                "    <type>KafkaProducerOutput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <connectionType>DIRECT</connectionType>\n" +
                "    <directBootstrapServers>kafkaQAQ</directBootstrapServers>\n" +
                "    <clusterName/>\n" +
                "    <topic>topicQAQ</topic>\n" +
                "    <clientId/>\n" +
                "    <keyField>resourceName</keyField>\n" +
                "    <messageField>value</messageField>\n" +
                "    <advancedConfig>\n" +
                "      <option property=\"compression.type\" value=\"none\"/>\n" +
                "      <option property=\"ssl.key.password\" value=\"\"/>\n" +
                "      <option property=\"ssl.keystore.location\" value=\"\"/>\n" +
                "      <option property=\"ssl.keystore.password\" value=\"\"/>\n" +
                "      <option property=\"ssl.truststore.location\" value=\"\"/>\n" +
                "      <option property=\"ssl.truststore.password\" value=\"\"/>\n" +
                "      <option property=\"partitioner.class\" value=\"com.hiekn.partitioner.ChannelPartitioner\"/>\n" +
                "    </advancedConfig>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>935</xloc>\n" +
                "      <yloc>118</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>\n" +
                "  <step_error_handling>\n" +
                "  </step_error_handling>\n" +
                "  <slave-step-copy-partition-distribution>\n" +
                "  </slave-step-copy-partition-distribution>\n" +
                "  <slave_transformation>N</slave_transformation>\n" +
                "  <attributes/>\n" +
                "</transformation>\n";

        static String mongoInputXml = "<step>\n" +
                "    <name>input</name>\n" +
                "    <type>MongoDbInput</type>\n" +
                "    <description/>\n" +
                "    <distribute>Y</distribute>\n" +
                "    <custom_distribution/>\n" +
                "    <copies>1</copies>\n" +
                "    <partitioning>\n" +
                "      <method>none</method>\n" +
                "      <schema_name/>\n" +
                "    </partitioning>\n" +
                "    <hostname>ipQAQ</hostname>\n" +
                "    <port>portQAQ</port>\n" +
                "    <use_all_replica_members>N</use_all_replica_members>\n" +
                "    <db_name>dbnameQAQ</db_name>\n" +
                "    <fields_name/>\n" +
                "    <collection>tbNameQAQ</collection>\n" +
                "    <json_field_name>json</json_field_name>\n" +
                "    <json_query>queryQAQ</json_query>\n" +
                "    <auth_database/>\n" +
                "    <auth_user>usernameQAQ</auth_user>\n" +
                "    <auth_password>passwordQAQ</auth_password>\n" +
                "    <auth_mech/>\n" +
                "    <auth_kerberos>N</auth_kerberos>\n" +
                "    <connect_timeout/>\n" +
                "    <socket_timeout/>\n" +
                "    <use_ssl_socket_factory>N</use_ssl_socket_factory>\n" +
                "    <read_preference>primary</read_preference>\n" +
                "    <output_json>Y</output_json>\n" +
                "    <query_is_pipeline>N</query_is_pipeline>\n" +
                "    <execute_for_each_row>N</execute_for_each_row>\n" +
                "    <mongo_fields/>\n" +
                "    <attributes/>\n" +
                "    <cluster_schema/>\n" +
                "    <remotesteps>\n" +
                "      <input>\n" +
                "      </input>\n" +
                "      <output>\n" +
                "      </output>\n" +
                "    </remotesteps>\n" +
                "    <GUI>\n" +
                "      <xloc>96</xloc>\n" +
                "      <yloc>144</yloc>\n" +
                "      <draw>Y</draw>\n" +
                "    </GUI>\n" +
                "  </step>";

        static String mongoAllQueryXMl = "{\n" +
                "  \"_id\": {\n" +
                "    \"$exists\":${key}\n" +
                "  }\n" +
                "}\n";

        static String mongoTimeQueryXMl = "{\n" +
                "    \"timeFieldQAQ\": {\n" +
                "        \"$gte\": \"${StartTime}\",\n" +
                "        \"$lt\": \"${EndTime}\"\n" +
                "    },\n" +
                "  \"_id\": {\n" +
                "    \"$exists\":${key}\n" +
                "  }\n" +
                "}\n";

        static String mongoTimeQueryDateFieldXMl = "{\n" +
                "    \"timeFieldQAQ\": {\n" +
                "        \"$gte\": {\"$date\":\"${StartTime}\"},\n" +
                "        \"$lt\": {\"$date\":\"${EndTime}\"}\n" +
                "    },\n" +
                "  \"_id\": {\n" +
                "    \"$exists\":${key}\n" +
                "  }\n" +
                "}\n";
    }


