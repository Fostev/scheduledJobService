package com.gvozdev.scheduledJobService.service;

import com.gvozdev.scheduledJobService.helpers.JobState;
import com.gvozdev.scheduledJobService.helpers.JobType;
import com.gvozdev.scheduledJobService.models.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final Map<String, Job> jobQueue = new ConcurrentHashMap<>();
    private final List<String> jobToCancel = new ArrayList<>();

    public Map<String, Job> getJobQueue() {
        return jobQueue;
    }

    public void cancelJob(String jobId) {
        if (jobQueue.containsKey(jobId)) {
            if (jobQueue.get(jobId).getJobState().equals(JobState.RUNNING)) {
                jobToCancel.add(jobId);
                logger.info("Job with id {} was stopped", jobId);
                logger.info("Job with id {} successfully deleted from queue", jobId);
            }
            jobQueue.remove(jobId);
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void checkAndRunScheduledTasks() {
        if (jobQueue.size() >= 1) {
            logger.info("checking and running jobs from queue");
            for (Map.Entry<String, Job> data : jobQueue.entrySet()) {
                Job job = data.getValue();
                if (job.getJobState() == JobState.RUNNING) continue;
                if (job.getJobState() == JobState.WAITING) {
                    String nextRunTime = job.getNextRunTime();
                    if (nextRunTime == null && job.getJobState().equals(JobState.WAITING)) {
                        job.setJobState(JobState.RUNNING);
                        runJob(job);
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        Date nextRunTimeFormatted = null;
                        try {
                            nextRunTimeFormatted = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(nextRunTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date currentTimeFormatted = calendar.getTime();
                        logger.info("currentTimeFormatted : {}", currentTimeFormatted);
                        logger.info("nextRunTimeFormatted : {}", nextRunTimeFormatted);
                        if (nextRunTimeFormatted.before(currentTimeFormatted)) {
                            job.setJobState(JobState.RUNNING);
                            job.setNextRunTime(getNextRunTime(job.getTimeLine()));
                            runJob(job);
                        }
                    }
                }
            }
        } else {
            logger.info("queue is empty");
        }
    }

    @Async
    public void runJob(Job job) {
        String jobId = job.getId();
        try {
            logger.info("Running job " + job);
            int i = 10;
            while (i-- > 0) {
                if (jobToCancel.remove(jobId)) {
                    break;
                }
                TimeUnit.SECONDS.sleep(1);
                logger.info("Thread {} is running job type {} with id {} ", Thread.currentThread().getName(), job.getJobType(), jobId);
            }
        } catch (Exception e) {
            logger.error("runJob method failed on job {} with error {} ", job, e.getMessage());
        }
        CompletableFuture<Job> jobCompletableFuture = CompletableFuture.completedFuture(job);
        if (jobCompletableFuture.isDone() && job.getNextRunTime() == null) {
            jobQueue.remove(jobId);
            logger.info("Job type {} with id {} was completed", job.getJobType(), jobId);
        } else if (job.getNextRunTime() != null && jobCompletableFuture.isDone()) {
            job.setJobState(JobState.WAITING);
            jobQueue.put(jobId, job);
        }
    }

    @Async
    public void addToQueueList(JobType type, Integer time, Integer immediately, Integer runOnce) {
        Job job = new Job();
        String jobId = String.valueOf(UUID.randomUUID());
        job.setId(jobId);
        if (type == null) {
            job.setJobType(JobType.TYPE_1);
        } else {
            job.setJobType(type);
        }
        if (runOnce == 1 || time == null) {
            job.setNextRunTime(null);
        } else {
            String nextRunTime = getNextRunTime(time);
            job.setNextRunTime(nextRunTime);
            job.setTimeLine(time);
        }
        logger.info("JOB type {} with id {} successfully added to the queue", job.getJobType(), job.getId());
        if (immediately == 1) {
            job.setJobState(JobState.RUNNING);
            jobQueue.put(jobId, job);
            runJob(job);
        } else {
            jobQueue.put(jobId, job);
            job.setJobState(JobState.WAITING);
        }

    }

    private String getNextRunTime(Integer time) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

}
