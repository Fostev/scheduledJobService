package com.gvozdev.scheduledJobService;

import com.gvozdev.scheduledJobService.helpers.JobState;
import com.gvozdev.scheduledJobService.helpers.JobType;
import com.gvozdev.scheduledJobService.models.Job;
import com.gvozdev.scheduledJobService.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@EnableAsync
class ScheduledJobServiceApplicationTests {

    @Autowired
    private JobService jobService;

    @Test
    void addingJobToTheQueueList() {
        jobService.addToQueueList(JobType.TYPE_1, 1, 0, 0);
        await().until(() -> jobService.getJobQueue().size(), is(1));
    }

    @Test
    void immediatelyAddedJobIsRunning() {
        cleanUpQueue();
        jobService.addToQueueList(JobType.TYPE_1, 1, 1, 1);
        await().until(() -> jobService.getJobQueue().size(), is(1));
        Job job = getFirstJob();
        assertEquals("Job state is: ", JobState.RUNNING, job.getJobState());
    }

    @Test
    void cancelJob() {
        cleanUpQueue();
        addingJobToTheQueueList();
        Job job = getFirstJob();
        assertEquals("Job count before cancel: ", 1, jobService.getJobQueue().size());
        jobService.cancelJob(job.getId());
        assertEquals("Job count after cancel: ", 0, jobService.getJobQueue().size());

    }

    @Test
    void scheduledTask() {
        cleanUpQueue();
        addingJobToTheQueueList();
        jobService.checkAndRunScheduledTasks();
        Job job = getFirstJob();
        assertEquals("Job state is: ", JobState.WAITING, job.getJobState());
    }

    void cleanUpQueue() {
        if (!jobService.getJobQueue().isEmpty()) {
            Map.Entry<String, Job> entry = jobService.getJobQueue().entrySet().iterator().next();
            String jobId = entry.getKey();
            jobService.cancelJob(jobId);
        }
    }

    Job getFirstJob() {
        Map.Entry<String, Job> entry = jobService.getJobQueue().entrySet().iterator().next();
        return entry.getValue();
    }
}
