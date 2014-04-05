package analysis;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import util.Extractor;
import twitter4j.Status;
import twitter4j.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by phuong on 4/4/14.
 */
public class TwitterTrustAnalyzer {
    private Extractor extractor;
    private SentimentAnalyzer sentimenter;
    private Map<Long, DirectedSparseGraph<TweetNode, RetweetEdge>> graphs;

    public TwitterTrustAnalyzer() {
        extractor = new Extractor();
        sentimenter = new SentimentAnalyzer();
        graphs = new HashMap<Long, DirectedSparseGraph<TweetNode, RetweetEdge>>();
    }

    /**
     * Exract online (cheap) analysis from a tweet
     *
     * Returned analysis:
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
     *  + Is tweet's location included
     *  + Number of hash tags in the tweet
     *  + Number of mentioned users in the tweet
     *  + Number of URLs mentioned in the tweet
     *  + Sentiment of the tweet content (from 0 to 4, 0 is most negative,
     *  4 is most positive, 2 is neutral)
     *
     * @param status
     * @return string of extracted analysis, seperated by tab
     */
    public String extractOnlineFeatures(Status status) {
        User user = status.getUser();
        String cleanedStatus = cleanTweet(status.getText());
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
                + (status.getGeoLocation() == null ? false : true) + "\t"
                + extractor.extractHashtags(status.getText()).size() + "\t"
                + extractor.extractMentionedScreennames(status.getText()).size() + "\t"
                + extractor.extractURLs(status.getText()).size() + "\t"
                + sentimenter.findSentiment(status.getText()) + "\t"
                + formatTweet(status.getText());
        return retStr;
    }

    public TweetNode getTweetNode(Status status) {
        User user = status.getUser();
        TweetNode node = new TweetNode(
                status.getId(),
                user.isVerified(),
                user.getFollowersCount(),
                status.getFavoriteCount(),
                sentimenter.findSentiment(status.getText()));

        return node;
    }

    public boolean isSupicious(Status status) {
        return true;
    }

    private String cleanTweet(String tweet) {
        String retStr = tweet;
        List<String> hashtags = extractor.extractHashtags(tweet);
        for (String hashtag: hashtags) {
            retStr = retStr.replaceAll("#" + hashtag, "");
        }

        List<String> urls = extractor.extractURLs(tweet);
        for (String url: urls) {
            retStr = retStr.replaceAll(url, "");
        }

        List<String> mentions = extractor.extractMentionedScreennames(tweet);
        for (String mention: mentions) {
            retStr = retStr.replaceAll("@" + mention, "");
        }
        return retStr;
    }

    private String formatTweet(String status) {
        // Remove new line, tab characters
        String parsedStr = status.replaceAll("[\t\n\r]", "");
        // Remove double spaces
        parsedStr = parsedStr.replaceAll("\\s+", " ");

        return parsedStr;
    }

    synchronized public void process(Status status) {
        if (!status.getLang().equals("en")) return;
        System.out.println(extractOnlineFeatures(status));

        if (!status.isRetweet() && isSupicious(status)) {
            if (!graphs.containsKey(status.getId())) {
                DirectedSparseGraph<TweetNode, RetweetEdge> graph = new DirectedSparseGraph<TweetNode, RetweetEdge>();
                graph.addVertex(getTweetNode(status));
                graphs.put(status.getId(), graph);
                // System.out.println(graph.toString());
            }
        }

        if (status.isRetweet()) {
            Status origin = status.getRetweetedStatus();
            if (graphs.containsKey(origin.getId())) {
                DirectedSparseGraph<TweetNode, RetweetEdge> curGraph = graphs.get(origin.getId());
//                TweetNode src = null;
//                Iterator<TweetNode> iter = curGraph.getVertices().iterator();
//                while (iter.hasNext()) {
//                    src = iter.next();
//                    if (src.getId() == origin.getId()) break;
//                }
                TweetNode src = getTweetNode(origin);
                TweetNode desc = getTweetNode(status);
                curGraph.addVertex(desc);
                curGraph.addEdge(new RetweetEdge(1), src, desc);
                System.out.println(curGraph.getVertexCount() + " vertices, " + curGraph.getEdgeCount() + " edges.");
                System.out.println(curGraph.toString());
            }
        }
    }
}
