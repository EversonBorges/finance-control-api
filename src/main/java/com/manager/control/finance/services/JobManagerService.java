package com.manager.control.finance.services;

import com.manager.control.finance.Mappers.JobManagerMapper;
import com.manager.control.finance.dtos.JobManagerResponseDTO;
import com.manager.control.finance.dtos.ResponseMessageDTO;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.JobManager;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.CategoryRepository;
import com.manager.control.finance.repositories.JobManagerRepository;
import com.manager.control.finance.repositories.RevenuesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import com.manager.control.finance.utils.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobManagerService {

    @Autowired
    private JobManagerMapper jobManagerMapper;

    @Autowired
    private JobManagerRepository jobManagerRepository;

    @Autowired
    private RevenuesRepository revenuesRepository;

    @Autowired
    private RevenuesService revenuesService;

    @Autowired
    private ExpensesService expensesService;

    @Autowired
    private CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(JobManagerService.class);

    public JobManagerResponseDTO convertJobManagerToDTO(JobManager jobManager){
        return jobManagerMapper.toDTO(jobManager);
    }

    private void saveJobManager(JobManager dto){
        jobManagerRepository.save(dto);
    }

    public List<JobManagerResponseDTO> findAll() {
        return jobManagerRepository.findAll().stream().map(jobManagerMapper::toDTO).toList();
    }

    public List<JobManagerResponseDTO> findByName(String name) {
        List<Optional<JobManager>> result = jobManagerRepository.findByName(name);

        return result.stream().map(item -> jobManagerMapper.toDTO(
                item.orElseThrow(DataNotFoundException::new))).toList();
    }

    public String executeJob(int monthNow, int yearNow, JobManager manager) {
        Optional<List<Revenues>> result = revenuesService.hasReferenceAdvanceInMonthAndYear(monthNow, yearNow);
        var finishDate = LocalDateTime.now();

        try {
            result.ifPresent(revenues -> revenues.forEach(resul -> {
                var receivingDate = resul.getReceivingDate();
                List<Expenses> expensesList = expensesService.findAllExpensesBasedReceivingDate(receivingDate);

                if (!expensesList.isEmpty()) {
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

                    ResponseMessageDTO message = expensesService.updateAllExpensesByAfterDateNow(updateExpensesList);
                    logger.debug("[JobManagerService - executeJob] {}", message.getMessage());
                    resul.setUpdateJob(true);
                    revenuesService.updateRevenue(resul);
                    manager.setStatusExecution(message.getMessage());
                }
            }));

            manager.setFinishExecution(finishDate);
            saveJobManager(manager);
            logger.info("[JobManagerService - executeJob] Finish: {}", LocalDateUtil.formatterLocalDateTime(finishDate));
            return "Job Executado com sucesso";
        } catch (Exception e) {
            logger.error("[JobManagerService - executeJob] Finish: {}", LocalDateUtil.formatterLocalDateTime(finishDate));
            return "Falha ao executar o Job";
        }
    }

    public JobManager buildJobManager() {
        JobManager manager = new JobManager();
        var dateHourNow = LocalDateTime.now();
        manager.setName("executeJob");
        manager.setExecutorMethod("executeJob");
        manager.setStartExecution(dateHourNow);
        manager.setAutomaticExecution(false);
        return manager;
    }

    private BigDecimal getMonthlyBalance(int year, int month){
        return jobManagerRepository.getMonthlyBalance(year,month);
    }

    private static boolean isLastDayOfMonth(LocalDate date) {
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return date.equals(lastDayOfMonth);
    }

    public String executeBalance(LocalDate dateNow){
        var dateHourNow = LocalDateTime.now();
        logger.info("[JobManagerService - executeBalance] Start: {}", LocalDateUtil.formatterLocalDateTime(dateHourNow));
        try {
            if(isLastDayOfMonth(dateNow)){
                var month = dateNow.getMonthValue();
                var year = dateNow.getYear();
                BigDecimal balance = getMonthlyBalance(year,month);

                if(balance != null && balance.compareTo(BigDecimal.ZERO) > 0){
                    Category category = categoryService.findByDescription("Saldo anterior");
                    month ++;

                    if(month > GlobalMessages.DECEMBER) {
                        year ++;
                        month = GlobalMessages.ONE;
                    }
                    Revenues revenues = new Revenues();
                    revenues.setReferenceYear(year);
                    revenues.setReferenceMonth(month);
                    revenues.setUpdateJob(true);
                    revenues.setCategory(category);
                    revenues.setAmount(balance);
                    revenues.setReceivingDate(dateNow.plusDays(GlobalMessages.ONE));
                    revenuesRepository.save(revenues);
                    return "Job Executado com sucesso - Dados incluídos";
                }
                return "Job Executado com sucesso - Não há dados a serem  incluídos";
            }

            logger.info("[JobManagerService - executeBalance] Finish: {}", LocalDateUtil.formatterLocalDateTime(dateHourNow));
            return "Job Executado com sucesso - Não é o último dia do mês";
        } catch (Exception e) {
            logger.error("[JobManagerService - executeBalance] Finish: {}", LocalDateUtil.formatterLocalDateTime(dateHourNow));
            return "Falha ao executar o Job";
        }
    }
}
