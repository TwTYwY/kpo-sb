package hse.bank.patterns;

import hse.bank.commands.Command;

public class TimingDecorator implements Command {
    private final Command wrappedCommand;

    public TimingDecorator(Command wrappedCommand) {
        this.wrappedCommand = wrappedCommand;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        wrappedCommand.execute();
        long endTime = System.currentTimeMillis();
        System.out.println("Command '" + getName() + "' executed in " + (endTime - startTime) + "ms");
    }

    @Override
    public String getName() {
        return wrappedCommand.getName();
    }
}