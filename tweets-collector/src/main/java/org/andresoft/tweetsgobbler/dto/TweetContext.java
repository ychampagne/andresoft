package org.andresoft.tweetsgobbler.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TweetContext
{

    private Map<String, String> parameters;

    public TweetContext()
    {
        parameters = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public TweetContext(Map<String, String> paramters)
    {
        this();
        this.putAll(paramters);
    }

    /**
     * Removes all of the mappings from this map.
     */
    public void clear()
    {
        parameters.clear();
    }

    /**
     * Associates all of the given map's keys and values in the Context.
     */
    public void putAll(
                       Map<String, String> map)
    {
        parameters.putAll(map);
    }

    /**
     * Associates the specified value with the specified key in this context. If
     * the context previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     * 
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            to be associated with the specified key
     */
    public void put(
                    String key,
                    String value)
    {
        parameters.put(key, value);
    }

    /**
     * Gets value mapped to key, returning defaultValue if unmapped.
     * 
     * @param key
     *            to be found
     * @param defaultValue
     *            returned if key is unmapped
     * @return value associated with key
     */
    public String getString(
                            String key,
                            String defaultValue)
    {
        return get(key, defaultValue);
    }

    /**
     * Gets value mapped to key, returning null if unmapped.
     * 
     * @param key
     *            to be found
     * @return value associated with key or null if unmapped
     */
    public String getString(
                            String key)
    {
        return get(key);
    }

    private String get(
                       String key,
                       String defaultValue)
    {
        String result = parameters.get(key);
        if (result != null)
        {
            return result;
        }
        return defaultValue;
    }

    private String get(
                       String key)
    {
        return get(key, null);
    }

    @Override
    public String toString()
    {
        return "{ parameters:" + parameters + " }";
    }

}
