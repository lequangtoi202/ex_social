package com.lqt.scheduler;

import com.lqt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerTask {
    @Autowired
    private UserRepository userRepository;

    @Async
    @Scheduled(cron = "0 */10 * * * *")
    public void lockLecturerIfDoNotChangeDefaultPassword(){
        userRepository.lockLecturerWithoutChangeDefaultPassword();
    }

}
