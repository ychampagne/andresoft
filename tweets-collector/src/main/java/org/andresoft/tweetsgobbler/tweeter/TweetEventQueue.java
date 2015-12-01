package org.andresoft.tweetsgobbler.tweeter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.andresoft.tweetsgobbler.dto.TweetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetEventQueue
{

    private static final Logger LOG = LoggerFactory.getLogger(TweetEventQueue.class);

    private final BlockingQueue<TweetEvent> sharedQueue = new LinkedBlockingQueue<TweetEvent>();

    public boolean put(
                       TweetEvent event)
    {

        sharedQueue.add(event);
        return true;
    }

    /**
     * Get a single event off the queue
     * 
     * Only used by junit test
     * 
     * @return
     * @throws InterruptedException
     */
    public TweetEvent getNext()
            throws InterruptedException
    {
        if (sharedQueue.isEmpty()) return null;
        return sharedQueue.take();
    }

    /**
     * Clear queue
     * 
     * Only used by junit test
     */
    public void resetQueue()
    {
        sharedQueue.clear();
    }

    /**
     * Get a batch of outgoing messages from the queue. Block until either num
     * messages are available or until the timeout has been reached.
     * 
     * @param num
     *            The number of messages to wait for.
     * @param timeout
     *            The maximum time to wait for messages (in ms)
     * @return The requested messages, if any are available within the timeout,
     *         as a batch of messages
     */
    public List<TweetEvent> get(
                                final int num,
                                long timeout)
    {
        List<TweetEvent> batch = new ArrayList<TweetEvent>();
        for (int i = 0; i < num; i++)
        {
            try
            {
                long before = System.currentTimeMillis();
                TweetEvent result = sharedQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (null == result) break; // We've hit timeout, stop waiting
                                           // for more

                batch.add(result);
                long elapsed = System.currentTimeMillis() - before;
                timeout = timeout - elapsed; // Cumulative timeout
            }
            catch (InterruptedException e)
            {
                LOG.warn(e.getMessage());
            }
        }

        return batch;
    }

    public int getCurrentSize()
    {
        return sharedQueue.size();
    }
}
