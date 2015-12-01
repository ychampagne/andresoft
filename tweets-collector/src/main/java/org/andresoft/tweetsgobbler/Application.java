package org.andresoft.tweetsgobbler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.andresoft.tweetsgobbler.configuration.AppConfig;
import org.andresoft.tweetsgobbler.dto.TweetSubject;
import org.andresoft.tweetsgobbler.tweeter.service.ITweetCollectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.base.Charsets;

public class Application
{
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private AnnotationConfigApplicationContext ctx;
    private ITweetCollectorService tweetCollectorService;

    public static void main(
                            String[] args)
    {
        Application app = new Application();
        app.start();

    }

    private void start()
    {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        tweetCollectorService = ctx.getBean(ITweetCollectorService.class);

        ShutdownHook shutdownHook = new ShutdownHook();
        shutdownHook.attachShutDownHook();

        try
        {
            processInput();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            System.exit(0);
        }
    }

    private void processInput()
            throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8));
        // okay, I guess Charsets.UTF_8 is Guava, but that lets us not worry
        // about
        // catching UnsupportedEncodingException
        System.out.print("ready>");
        String line;
        while ((line = reader.readLine()) != null)
        {
            if ("exit".equalsIgnoreCase(line))
            {
                System.exit(0);
            }
            else
            {
                String[] lineParts = line.split(":");
                String[] subjectParts = lineParts[0].split("=");
                String subject = subjectParts[1];

                String[] keywordsParts = lineParts[1].split("=");
                String[] keywords = keywordsParts[1].split(",");
                List<String> keyWordList = new ArrayList<String>();
                for (String kw : keywords)
                {
                    keyWordList.add(kw);
                }

                TweetSubject tweetSubject = new TweetSubject();
                tweetSubject.setName(subject);

                tweetSubject.setKeyWords(keyWordList);
                tweetCollectorService.starCollection(tweetSubject);
                System.out.print("ready>");

            }
        }
    }

    // Cleanup resources on application exit
    private class ShutdownHook
    {
        public void attachShutDownHook()
        {
            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    ctx.destroy();
                }
            });
        }
    }
}
