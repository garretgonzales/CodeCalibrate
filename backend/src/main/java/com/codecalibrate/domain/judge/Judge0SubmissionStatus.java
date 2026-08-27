package com.codecalibrate.domain.judge;

public record Judge0SubmissionStatus(int statusId) {
    public boolean isFinished() {
        // judge0 status 1 = queued, status 2 = processing
        return statusId != 1 && statusId != 2;
    }

    public boolean isAccepted() {
        return statusId == 3; //3 = accepted
    }


}
