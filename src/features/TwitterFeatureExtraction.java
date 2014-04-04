package features;

import com.twitter.Extractor;
import twitter4j.Status;
import twitter4j.User;

/**
 * Created by phuong on 4/4/14.
 */
public class TwitterFeatureExtraction {
    private Extractor extractor;
    private SentimentAnalyzer sentimenter;
    public TwitterFeatureExtraction() {
        extractor = new Extractor();
        sentimenter = new SentimentAnalyzer();
    }

    /**
     * Exract online (cheap) features from a tweet
     *
     * Returned features:
     *  + Friend count of user who posts the tweet
     *  + Number of user's followers
     *  + Number of user's tweets until now
     *  + Is user verified or not
     *  + Time period (in days) from the time user account is created until the time
     *  user posts the tweet
     *  + Is this tweet a retweet
     *  + Number of retweet
     *  + Number of favorites
     *  + Length of the tweet in characters
     *  + Number of hash tags in the tweet
     *  + Number of mentioned users in the tweet
     *  + Number of URLs mentioned in the tweet
     *
     * @param status
     * @return string of extracted features, seperated by tab
     */
    public String extractOnlineFeatures(Status status) {
        User user = status.getUser();
        String retStr = "";
        retStr = status.getId() + "\t"
                + user.getFriendsCount() + "\t"
                + user.getFollowersCount() + "\t"
                + user.getStatusesCount() + "\t"
                + user.isVerified() + "\t"
                + (user.getDescription() == null || user.getDescription().equals("") ? false : true) + "\t"
                + (user.getURL() == null || user.getURL().equals("") ? false : true) + "\t"
                + (status.getCreatedAt().getTime() - user.getCreatedAt().getTime()) / (1000000 * 24 * 3600) + "\t"
                + status.isRetweet() + "\t"
                + status.getRetweetCount() + "\t"
                + status.getFavoriteCount() + "\t"
                + status.getText().length() + "\t"
                + extractor.extractHashtags(status.getText()).size() + "\t"
                + extractor.extractMentionedScreennames(status.getText()).size() + "\t"
                + extractor.extractURLs(status.getText()).size() + "\t"
                + sentimenter.findSentiment(status.getText()) + "\t"
                + status.getText();
        return retStr;
    }
}
