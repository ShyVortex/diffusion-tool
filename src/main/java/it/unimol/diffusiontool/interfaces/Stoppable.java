package it.unimol.diffusiontool.interfaces;

import it.unimol.diffusiontool.threads.StoppableThread;

/**
 * Interface that defines the signatures of the new methods to start or stop the execution of a thread of the type
 * {@link StoppableThread}.
 *
 * @author Angelo Trotta
 * @version 1.0
 */

public interface Stoppable {
    boolean isRunning();

    /**
     * It defines the stop of execution of a {@link StoppableThread}.
     *
     * @param thread The thread to stop.
     */
    void stop(StoppableThread thread);
}
