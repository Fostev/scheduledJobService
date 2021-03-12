# Job System


###Setup steps:

1. clone repository to you IDE
2. install maven dependencies
3. make sure that you have installed Java SDK (1.8)
4. in application.properties file, if needed, please change the server port. (default is 8085)
5. run the app and open any browser with address **localhost:8085** (don't forget about the correct port)


###Tests:

**location is in folder** _test -> java -> com -> gvozdev -> scheduledJobService -> ScheduledJobServiceApplicationTests_

Just run the file **ScheduledJobServiceApplicationTests**

**Tests coverage 88% Classes**

Test cases:
* addingJobToTheQueueList
* immediatelyAddedJobIsRunning
* cancelJob
* scheduledTask

**Service is supporting:**
1. job types
2. job state
3. scheduler
4. concurrency
5. job uniqueness
6. cancel feature
7. simple UI

**Can / Should be improved:**

1. remove hardcoded values like Job State, Job Types
2. add availability to see pending job while they reach pool max size
3. add flexible scheduler (each minute,5 minutes, months etc...)
4. add a history of running jobs with all information.

Technology stack:
1. Java
2. spring boot
3. freemarker
4. lombok
5. junit
6. awaitility



