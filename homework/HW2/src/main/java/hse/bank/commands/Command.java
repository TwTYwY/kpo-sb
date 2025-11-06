package hse.bank.commands;

public interface Command {
    void execute();
    String getName();
}