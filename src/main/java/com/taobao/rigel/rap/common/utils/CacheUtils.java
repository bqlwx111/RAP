package com.taobao.rigel.rap.common.utils;
import com.taobao.rigel.rap.common.config.SystemConstant;
import com.taobao.rigel.rap.organization.bo.Corporation;
import com.taobao.rigel.rap.project.bo.Action;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bosn on 14/11/28.
 * Basic cache, need weight for string length.
 */
public class CacheUtils {
    private static final String MOCK_RULE_CACHE_PREFIX = "MOCK_CACHE_PREFIX:";
    private static final int DEFAULT_CACHE_EXPIRE_SECS = 600;

    public static final String KEY_PROJECT_LIST = "KEY_PROJECT_LIST";
    public static final String KEY_CORP_LIST = "KEY_CORP_LIST";
    public static final String KEY_CORP_LIST_TOP_ITEMS = "KEY_CORP_LIST_TOP_ITEMS";

    public static final String KEY_ACCESS_USER_TO_PROJECT = "KEY_ACCESS_USER_TO_PROJECT";

    public static Jedis jedis = null;

    public CacheUtils() {
    }

    /**
     * get cached Mock rule
     *
     * @param action
     * @param pattern
     * @return
     */
    public static String getRuleCache(Action action, String pattern) {
        long actionId = action.getId();
        String requestUrl = action.getRequestUrl();
        if (requestUrl == null) {
            requestUrl = "";
        }
        if (pattern.contains("noCache=true") || requestUrl.contains("{")
                || requestUrl.contains("noCache=true")) {
            return null;
        }
        return jedis.get(MOCK_RULE_CACHE_PREFIX + actionId);
        /**
         String cache = cachedRules.get(actionId);
         if (cache != null) {
         Long fre = rulesFrequency.get(actionId);
         if (fre != null) {
         rulesFrequency.put(actionId, fre + 1);
         }
         }
         return cache;
         **/
    }

    public static List<Corporation> getTeamCache(long userId, int pageNum, int pageSize) {
//        String key = "" +  pageNum + pageSize;
//        Map<String, List<Corporation>> teamCache = cachedTeams.get(userId);
//        if (teamCache != null && teamCache.get(key) != null) {
//            return teamCache.get(key);
//        }
        return null;
    }

    /**
     * set Mock rule cache
     *
     * @param actionId
     * @param result
     */
    public static void setRuleCache(long actionId, String result) {
        jedis.set(MOCK_RULE_CACHE_PREFIX + actionId, result);
        /**
         if (!cachedRules.containsKey(actionId)) {
         cachedRules.put(actionId, result);
         rulesFrequency.put(actionId, 0L);
         cachedRuleSize++;
         cachedSize++;
         };
         */
//        if (!cachedRules.containsKey(actionId)) {
//            cachedRules.put(actionId, result);
//            rulesFrequency.put(actionId, 0L);
//            cachedRuleSize++;
//            cachedSize++;
//        }

    }

    public static void setTeamCache(long userId, int pageNum, int pageSize, List<Corporation> teamList) {
//        String key = "" +  pageNum + pageSize;
//        if (!cachedTeams.containsKey(userId)) {
//            Map<String, List<Corporation>> teamMap = cachedTeams.get(userId);
//            if (teamMap == null) {
//                teamMap = new ConcurrentHashMap<String, List<Corporation>>();
//            }
//            if (!teamMap.containsKey(key)) {
//                teamMap.put(key, teamList);
//                cachedTeams.put(userId, teamMap);
//                teamsFrequency.put(userId, 0L);
//                cachedTeamSize++;
//                cachedSize++;
//            }
//        }
    }

    public static void removeTeamCache(long userId) {
//        if (cachedTeams.containsKey(userId)) {
//            cachedTeams.remove(userId);
//            teamsFrequency.remove(userId);
//            cachedTeamSize--;
//            cachedSize--;
//        }
    }

    /**
     * remove rule cache
     * @param actionId
     */
    /**
     private static void removeRuleCache(long actionId) {
     if (cachedRules.containsKey(actionId)) {
     cachedRules.remove(actionId);
     rulesFrequency.remove(actionId);
     cachedRuleSize--;
     cachedSize--;
     }
     }
     */
    /**
     * public static long getCachedRuleSize() {
     * return cachedRuleSize;
     * }
     */

    public static void removeCacheByActionId(long id) {
        jedis.del(MOCK_RULE_CACHE_PREFIX + id);
        System.out.println("Cache deleted, key: " + MOCK_RULE_CACHE_PREFIX + id);
    }

    public static long getCachedTeamSize() {return 0;}

    public static long getCachedSize() {return 0;}

    public static void put(String [] keys, String value, int expireInSecs) {
        String cacheKey = StringUtils.join(keys, "|");
        jedis.set(cacheKey, value);
        if (expireInSecs > 0)
            jedis.expire(cacheKey, expireInSecs);
    }

    public static void put(String [] keys, String value) {
        put(keys, value, DEFAULT_CACHE_EXPIRE_SECS);
    }

    public static String get(String []keys) {
        return jedis.get(StringUtils.join(keys, "|"));
    }

    public static void del(String[] keys) {
        String cacheKey = StringUtils.join(keys, "|");
        jedis.del(cacheKey);
    }
}
