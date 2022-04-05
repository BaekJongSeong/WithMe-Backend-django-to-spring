package com.server.withme.configure;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.server.withme.entity.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

   	public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    public final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job exampleJob() throws Exception {

        Job exampleJob = jobBuilderFactory.get("exampleJob")
                .start(step())
                .build();

        return exampleJob;
    }

    @Bean
    @JobScope
    public Step step() throws Exception {
        return stepBuilderFactory.get("step")
                .<Account,Account>chunk(10)
                .reader(reader())
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Account> reader() throws Exception {

        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("amount", 10000);

        return new JpaPagingItemReaderBuilder<Account>()
                .pageSize(10)
                .parameterValues(parameterValues)
                .queryString("SELECT p FROM Member p WHERE p.amount >= :amount ORDER BY id ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Account, Account> processor(@Value("#{jobParameters[date]}")  String date){
    	log.info("jobParameters value : " + date);
        return new ItemProcessor<Account, Account>() {
            @Override
            public Account process(Account member) throws Exception {

                //1000원 추가 적립
                member.setName(member.getName() + 1000);

                return member;
            }
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Account> writer(@Value("#{jobParameters[date]}")  String date){

        return new JpaItemWriterBuilder<Account>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
    
    @Bean
    public Job readJob(String sql) throws Exception {

        Job exampleJob = jobBuilderFactory.get("readJob")
                .start(stepforRead(sql))
                .build();

        return exampleJob;
    }
    
    @Bean
    @JobScope
    public Step stepforRead(String sql) throws Exception {
        return stepBuilderFactory.get("Step")
                .<Account,Account>chunk(20)
                .reader(reader(sql))
                .build();
    }
    
    @Bean
    @StepScope
    public JpaPagingItemReader<Account> reader(@Value("#{jobParameters[sql]}")  String sql) throws Exception {

        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("amount", 10000);

        return new JpaPagingItemReaderBuilder<Account>()
                .pageSize(20)
                .parameterValues(parameterValues)
                .queryString(sql)
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }
}
