package com.manager.control.finance.schedules;

import com.manager.control.finance.dtos.ResponseMessage;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.JobManager;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.services.ExpensesService;
import com.manager.control.finance.services.JobManagerService;
import com.manager.control.finance.services.RevenuesService;
import com.manager.control.finance.utils.GlobalMessages;
import com.manager.control.finance.utils.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseSchedule {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseSchedule.class);

    @Autowired
    private RevenuesService revenuesService;

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private JobManagerService jobManagerService;

    // Calculo 12 horas é igual a 12 * 60 minutos * 60 segundos * 1000 milissegundos:
    // 2 minutes em milissegundos = 120000 (2 * 60 * 1000 )
    // 6 horas em milissegundos = 21600000 (6 * 60 * 60 * 1000).
    // 12 horas em milissegundos = 43200000 (12 * 60 * 60 * 1000).
    @Scheduled(fixedRate = 43200000)
        public void checkAndUpdateDatabase() {

            var dateHourNow = LocalDateTime.now();

            JobManager manager = new JobManager();
            manager.setName("ExpenseSchedule");
            manager.setExecutorMethod("checkAndUpdateDatabase");
            manager.setStartExecution(dateHourNow);
            manager.setNextExecution(dateHourNow.plusHours(12));
            logger.info("[ExpenseSchedule - checkAndUpdateDatabase] Start: {}", LocalDateUtil.formatterLocalDateTime(dateHourNow));

            int monthNow = dateHourNow.getMonth().getValue();
            int yearNow = dateHourNow.getYear();
            Optional<Revenues> result = revenuesService.hasReferenceAdvanceInMonthAndYear(monthNow,yearNow);

            if(result.isPresent()){
                var receivingDate =  result.get().getReceivingDate();
                List<Expenses> expensesList =  expensesService.findAllExpensesBasedReceivingDate(receivingDate);

                List<Expenses> updateExpensesList = new ArrayList<>();
                expensesList.forEach(item -> {
                    var year = item.getReferenceYear();
                    var month = item.getReferenceMonth();

                    month++;

                    if (month > GlobalMessages.DECEMBER) {
                        year++;
                        month = GlobalMessages.ONE;
                    }

                    item.setReferenceYear(year);
                    item.setReferenceMonth(month);
                    updateExpensesList.add(item);

                });
                ResponseMessage message = expensesService.updateAllExpensesByAfterDateNow(updateExpensesList);
                logger.debug("[ExpenseSchedule - checkAndUpdateDatabase] {}", message.getMessage());
                result.get().setUpdateJob(true);
                revenuesService.updateRevenue(result.get());
                manager.setStatusExecution("Success - " + message.getMessage());
            }

            var finishDate = LocalDateTime.now();
            manager.setFinishExecution(finishDate);
            jobManagerService.saveJobManager(manager);
            logger.info("[ExpenseSchedule - checkAndUpdateDatabase] Finish: {}", LocalDateUtil.formatterLocalDateTime(finishDate));
        }
    }