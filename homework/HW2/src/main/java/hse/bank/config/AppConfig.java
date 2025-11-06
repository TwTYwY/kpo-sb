package hse.bank.config;

import hse.bank.facade.*;
import hse.bank.factories.DomainFactory;
import hse.bank.patterns.observer.BalanceLogger;
import hse.bank.patterns.observer.BalanceNotifier;
import hse.bank.patterns.proxy.BankAccountRepository;
import hse.bank.patterns.proxy.BankAccountRepositoryProxy;
import hse.bank.patterns.proxy.DatabaseBankAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DomainFactory domainFactory() {
        return new DomainFactory();
    }

    @Bean
    public BankAccountRepository bankAccountRepository() {
        return new DatabaseBankAccountRepository();
    }

    @Bean
    public BankAccountRepositoryProxy bankAccountRepositoryProxy() {
        return new BankAccountRepositoryProxy(bankAccountRepository());
    }

    @Bean
    public BalanceLogger balanceLogger() {
        return new BalanceLogger();
    }

    @Bean
    public BalanceNotifier balanceNotifier() {
        BalanceNotifier notifier = new BalanceNotifier();
        notifier.addObserver(balanceLogger());
        return notifier;
    }

    @Bean
    public BankAccountFacade bankAccountFacade() {
        return new BankAccountFacade(domainFactory(), balanceNotifier());
    }

    @Bean
    public CategoryFacade categoryFacade() {
        return new CategoryFacade(domainFactory());
    }

    @Bean
    public OperationFacade operationFacade() {
        return new OperationFacade(domainFactory(), bankAccountFacade());
    }

    @Bean
    public AnalyticsFacade analyticsFacade() {
        return new AnalyticsFacade(operationFacade(), categoryFacade());
    }
}