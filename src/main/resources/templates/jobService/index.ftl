<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <title>Scheduled Job Service</title>
</head>
<body>

<h1 class="text-center">Job Service</h1>
<div class="container-fluid">
    <div class="d-flex justify-content-between w-100">
        <form action="/job/TYPE_1/1/0/0" method="get">
            <button type="submit" class="btn btn-sm btn-primary">add job type 1 (run every 1 hr)</button>
        </form>
        <form action="/job/TYPE_2/2/0/0" method="get">
            <button type="submit" class="btn btn-sm btn-primary">add job type 2 (run every 2 hr)</button>
        </form>
        <form action="/job/TYPE_3/6/0/0" method="get">
            <button type="submit" class="btn btn-sm btn-primary">add job type 3 (run every 6 hr)</button>
        </form>
        <form action="/job/TYPE_4/12/0/0" method="get">
            <button type="submit" class="btn btn-sm btn-primary">add job type 4 (run every 12 hr)</button>
        </form>
        <form action="/job/TYPE_3/1/1/1" method="get">
            <button type="submit" class="btn btn-sm btn-primary">add job type 3 (run immediately once)</button>
        </form>
        <button class="btn btn-sm btn-info" onClick="window.location.reload();">refresh queue status</button>
    </div>
    <div class="d-flex w-50 flex-column pt-3">
        <b>Pool Capacity: ${poolCapacity}</b>
        <b>Currently in queue: ${currentlyInQueue}</b>
        <b>Concurrent max workers: ${concurrentMaxWorkers}</b>
    </div>
    <#if jobQueueList?has_content>
        <table id="jobsListTable" class="table table-striped table-bordered table-sm" cellspacing="0"
               width="100%">
            <thead>
            <tr>
                <th class="th-sm">ID</th>
                <th class="th-sm">Type</th>
                <th class="th-sm">Job State</th>
                <th class="th-sm">Next Run Time</th>
                <th class="th-sm">Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list jobQueueList as jobId, job>
                <tr>
                    <td>${jobId}</td>
                    <td>${job.getJobType()}</td>
                    <td>${job.getJobState()}</td>
                    <td>${(job.getNextRunTime())!}</td>
                    <td class="text-center">
                        <form action="/job/cancel/${jobId}"
                              onclick="return confirm('are you sure you want to delete job from queue?')" method="get">
                            <button type="submit" class="btn btn-sm btn-danger">delete job from queue
                            </button>
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
            <tfoot>
            <tr>
                <th class="th-sm">ID</th>
                <th class="th-sm">Type</th>
                <th class="th-sm">Status</th>
                <th class="th-sm">Next Run Time</th>
                <th class="th-sm">Actions</th>
            </tr>
            </tfoot>
        </table>
    <#else>
        <hr>
        <h3 class="text-center">Queue is empty.</h3>
        <h3 class="text-center">Please, add new job</h3>
        <hr>
    </#if>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
</html>