package com.lqt.scheduler;

import com.lqt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
    @Autowired
    private UserRepository userRepository;

    @Async
    @Scheduled(cron = "0 */10 * * * *")
    public void lockLecturerIfDoNotChangeDefaultPassword(){
        logger.debug("RUN JOB: Start update lecturer");
        userRepository.lockLecturerWithoutChangeDefaultPassword();
        logger.debug("END JOB: End update lecturer");
    }

}
