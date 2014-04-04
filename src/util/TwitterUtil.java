package util;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by phuong on 3/22/14.
 */
public class TwitterUtil {

    public static void main(String [] args) throws TwitterException {
        TwitterUtil util = new TwitterUtil();
        util.getCurrentTrend("sH1W4DQITPKCuNW0kxJg",
                "sN3ZoyISpJ5GLvSBCEzmnZFtpUsyVYaKpZcpsiILrw",
                "23776707-GcaPAHrqcgFZxAdvjHGuN9l8JdJz7aejY0AHXGNS2",
                "CEjcDlkg7aRqNsQ16dJ3JqeEtxuTyJZIkSIzNF0m6cwbb");

        util.getTrend("sH1W4DQITPKCuNW0kxJg",
                "sN3ZoyISpJ5GLvSBCEzmnZFtpUsyVYaKpZcpsiILrw",
                "23776707-GcaPAHrqcgFZxAdvjHGuN9l8JdJz7aejY0AHXGNS2",
                "CEjcDlkg7aRqNsQ16dJ3JqeEtxuTyJZIkSIzNF0m6cwbb", 1);
    }

    private HashMap<String, Integer> getCurrentTrend(String consumerKey, String consumerSecret,
                                    String accessToken, String accessTokenSecret) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        ResponseList<Location> locations;
        HashMap<String, Integer> currentTrends = new HashMap<String, Integer>();
        locations = twitter.getAvailableTrends();
        System.out.println("Showing available trends");
        for (Location location : locations) {
            System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
            currentTrends.put(location.getName(), new Integer(location.getWoeid()));
        }
        return currentTrends;
    }

    private ArrayList<String> getTrend(String consumerKey, String consumerSecret,
                                                     String accessToken, String accessTokenSecret, int woeid) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        ArrayList<String> trendAr = new ArrayList<String>();
        Trends trends = twitter.getPlaceTrends(woeid);
        for (int i = 0; i < trends.getTrends().length; i++) {
            System.out.println(trends.getTrends()[i].getName());
            trendAr.add(trends.getTrends()[i].getName());
        }

        return trendAr;
    }
}
