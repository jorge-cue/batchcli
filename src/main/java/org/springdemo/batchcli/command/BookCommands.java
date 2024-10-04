package org.springdemo.batchcli.command;

import org.jline.terminal.Terminal;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.ShellRunner;
import org.springframework.shell.standard.ShellComponent;

import java.time.LocalDate;

@ShellComponent
public class BookCommands implements ShellRunner {

    public static final String INPUT_FILE_NAME = "inputFileName";
    public static final String EFFECTIVE_DATE = "effectiveDate";

    private final JobLauncher jobLauncher;

    private final Job job;

    @Autowired
    public BookCommands(JobLauncher jobLauncher,
                        @Qualifier("importBooksJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public boolean run(String[] args) throws
            IllegalArgumentException,
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        var jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(INPUT_FILE_NAME, "sample-data.csv", true);
        var effectiveDate = LocalDate.now();
        jobParametersBuilder.addLocalDate(EFFECTIVE_DATE, effectiveDate);
        jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        return true;
    }
}
