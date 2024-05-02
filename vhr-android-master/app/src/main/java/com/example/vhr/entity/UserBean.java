package com.example.vhr.entity;

import java.util.List;

public class UserBean {
        private int id;
        private String jobSeekerName;
        private int jobId;
        private String jobSeekerEmail;
        private String jobSeekerTel;
        private String jobName;
        private String idNumber;
        private int take;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJobSeekerName() {
            return jobSeekerName;
        }

        public void setJobSeekerName(String jobSeekerName) {
            this.jobSeekerName = jobSeekerName;
        }

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public String getJobSeekerEmail() {
            return jobSeekerEmail;
        }

        public void setJobSeekerEmail(String jobSeekerEmail) {
            this.jobSeekerEmail = jobSeekerEmail;
        }

        public String getJobSeekerTel() {
            return jobSeekerTel;
        }

        public void setJobSeekerTel(String jobSeekerTel) {
            this.jobSeekerTel = jobSeekerTel;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public int getTake() {
            return take;
        }

        public void setTake(int take) {
            this.take = take;
        }
}
