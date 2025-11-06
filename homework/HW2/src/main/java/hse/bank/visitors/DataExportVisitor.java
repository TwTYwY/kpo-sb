package hse.bank.visitors;

import hse.bank.domain.BankAccount;
import hse.bank.domain.Category;
import hse.bank.domain.Operation;

public interface DataExportVisitor {
    void visit(BankAccount account);
    void visit(Category category);
    void visit(Operation operation);
}