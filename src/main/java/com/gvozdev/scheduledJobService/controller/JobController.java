package com.gvozdev.scheduledJobService.controller;

import com.gvozdev.scheduledJobService.helpers.JobType;
import com.gvozdev.scheduledJobService.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class JobController {

    @Value("${pool.capacity:3}")
    private int poolCapacity;
    @Value("${core.pool.size:3}")
    private int concurrentMaxWorkers;
    @Autowired
    private JobService jobService;

    @GetMapping
    public String index(Model model) {

        model.addAttribute("jobQueueList", jobService.getJobQueue());
        model.addAttribute("currentlyInQueue", jobService.getJobQueue().size());
        model.addAttribute("concurrentMaxWorkers", concurrentMaxWorkers);
        model.addAttribute("poolCapacity", poolCapacity);
        return "jobService/index";
    }

    @GetMapping(path = "/job/{type}/{time}/{immediately}/{oneRun}")
    public String addToQueueList(@PathVariable JobType type, @PathVariable Integer time, @PathVariable Integer immediately, @PathVariable Integer oneRun) {
        jobService.addToQueueList(type, time, immediately, oneRun);
        return "redirect:/";
    }

    @GetMapping(path = "/job/cancel/{jobId}")
    public String cancelJob(@PathVariable String jobId) {
        jobService.cancelJob(jobId);
        return "redirect:/";
    }
}
