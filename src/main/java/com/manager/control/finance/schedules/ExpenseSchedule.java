package com.manager.control.finance.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpenseSchedule {

    @Scheduled(fixedRate = 120000) // 6 horas em milissegundos fixedRate = 21600000 (6 * 60 * 60 * 1000).
    public void checkAndUpdateDatabase() {
        System.out.println("Job executado em " + LocalDateTime.now());

        }
    }