package it.unimol.diffusiontool.threads;

import it.unimol.diffusiontool.interfaces.Stoppable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that defines a thread of the type StoppableThread. This thread is an alternative to the {@link Thread} class
 * natively included in Java by the default, with the particularity that it can be interrupted safely and without data loss
 * thanks to a new method {@link #stop(StoppableThread)} which replaces the deprecated one {@link Thread#stop()}.
 * To allow that, Countdown extends the {@link Thread} class and implements the {@link Stoppable} interface
 * with the signatures with the new methods to initialize and interrupt the execution. Each stoppable thread is
 * composed of an atomic boolean variable, representing a {@code flag} that indicates the state of the thread to check
 * if it's running or not, and of an instance of {@link Runnable}, that allows the execution of the method {@link Runnable#run()}.
 *
 * @author Angelo Trotta
 * @version 1.0
 */

public class StoppableThread extends Thread implements Stoppable {
    private final AtomicBoolean running = new AtomicBoolean(); // A thread needs to be stopped in an atomic way
    /* An atomic termination makes sure that the thread is executed fully before being stopped. */
    private Runnable instance;
    private static StoppableThread thisThread;

    /**
     * It creates a stoppable thread, by calling the constructor of the superclass {@link Thread}, and initializing {@link #instance}.
     *
     * @param instance Instance of the {@link Runnable} class.
     */
    public StoppableThread(Runnable instance) {
        super();

        this.instance = instance;
        thisThread = this;
    }

    public Runnable getRunnable() {
        return this.instance;
    }

    public static StoppableThread currentThread() {
        return thisThread;
    }

    /**
     * It gives information on the state of the countdown.
     *
     * @return A casting of the atomic boolean variable to a boolean one through the method {@link AtomicBoolean#get()}
     * of the execution flag.
     */
    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    /**
     * It defines the thread's execution job. Once set the execution flag to {@code true}, the instance's method
     * {@code instance.run()} is executed.
     */
    @Override
    public void run() {
        this.running.set(true);
        this.instance.run();
    }

    /**
     * It allows for the termination of a {@link StoppableThread} thread and sets its execution flag to {@code false}.
     *
     * @param thread Thread to terminate.
     */
    @Override
    public void stop(StoppableThread thread) {
        this.running.set(false);
    }
}