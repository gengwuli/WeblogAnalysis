This project aims to analyze the web log.
The logger is in the form of 
:remote-addr - :remote-user [:date] ":method :url HTTP/:http-version" :status :res[content-length] ":referrer" ":user-agent"

So the primary aim of this project is extracting useful information and organize them in structure and display in various graphs so we can get a better idea of our website and the user behaviors. 

Since in reality the data required is tremendous, this project only simulates the real scenario with a simple frontend and backend dedicated to generate the real data.

A python script should be run by clicking buttons in the frontend and make enough queries to the backend for analysis.

So the whole workflow works like this.

1. User visit the frontend, make queries to the backend where morgan will log those queries in details and writes them in to a access.log file. In reality the log file might be rotated according to date. But for simplicity, a single access.log file is used here.

2. While the log file is being updated, Flume(Apache Framework) will monitor the updates and collecting the appended date into memory once a threshold is reached, it will then copy the aggregated data to hadoop hdfs.

3. Once the aggregated data is big enough in hdfs, three Mapreduce jobs will be created accordingly to screen the data and expand the log data further into finer grained details. 

4. The three outputs from map reduce jobs will be imported to Apache Hive for further processing.

5. In Hive, various tables will be generated for different analysis purpose.

6. Once all the tables are populated, the Apache Sqoop will be come and export those tables to MySQL.

7. Once the all the tables are imported to MySQL, the backend, having a mysql connector will now query the database and send interested data to the frontend displaying the result.

8. The end.

In details.

1. Run weblog.py to generate log data to the backend.
2. At flume home directory, Run
	bin/flume-ng agent -c conf -f agentconf/tail-hdfs.conf -n a1
   This will start a new agent and collecting logging data with 
   user defined tail-hdfs.conf configuration.
3. Run sh movetopreworkdir.sh to move data from flume directory to preprocess directory.

4. Run log_click.sh and log_preprocess.sh to screen the initial log data.

5. Run etl_detail.sh to import data to hive and generate the various tables.

6. Run sqoop_export.sh to export those tables to MySQL.

The whole process is controlled by Apache Azkaban and can be scheduled periodically. 

The password of mysql connection in sqoop_export.sh and nodemysql/index.js has been changed to root
