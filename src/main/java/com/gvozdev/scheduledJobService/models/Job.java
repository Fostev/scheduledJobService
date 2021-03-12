package com.gvozdev.scheduledJobService.models;

import com.gvozdev.scheduledJobService.helpers.JobState;
import com.gvozdev.scheduledJobService.helpers.JobType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    private String id;
    private JobType jobType;
    private JobState jobState;
    private String nextRunTime;
    private Integer timeLine;
}
