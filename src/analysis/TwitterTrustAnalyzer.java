package analysis;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import util.Extractor;
import twitter4j.Status;
import twitter4j.User;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.*;

import java.io.*;
import java.util.*;

/**
 * Created by phuong on 4/4/14.
 */
public class TwitterTrustAnalyzer {
    private Extractor extractor;
    private SentimentAnalyzer sentimenter;
    private Map<Long, DirectedSparseGraph<TweetNode, RetweetEdge>> graphs;
    private Classifier classifier;

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
                + formatTweet(status.getText());
        return retStr;
    }

    public void extractFeaturesFromJSON(String inputFile, String outputFile) throws IOException, TwitterException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        FileWriter fw = new FileWriter(outputFile);
        String strLine = "";

        while ((strLine = br.readLine()) != null) {
            Status status = TwitterObjectFactory.createStatus(strLine);
            if (!status.getLang().equals("en")) continue;
            fw.write(extractOnlineFeatures(status) + "\n");
        }
        fw.close();
        br.close();
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

    public void process(Status status) {
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
                TweetNode src = getTweetNode(origin);
                TweetNode desc = getTweetNode(status);
                curGraph.addVertex(desc);
                curGraph.addEdge(new RetweetEdge(1), src, desc);
                // System.out.println(curGraph.getVertexCount() + " vertices, " + curGraph.getEdgeCount() + " edges.");
                // System.out.println(curGraph.toString());
            }
        }
    }

    public void processJSONFile(String inputFile) throws IOException, TwitterException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String strLine = "";
        RetweetGraphAnalyzer analyzer = new RetweetGraphAnalyzer();

        while ((strLine = br.readLine()) != null) {
            Status status = TwitterObjectFactory.createStatus(strLine);
            process(status);
        }

        System.out.println(graphs.size());
        int curMaxVertexCount = 0;
        int curMaxEdgeCount = 0;
        DirectedSparseGraph<TweetNode, RetweetEdge> curMaxGraph = null;
        for (Map.Entry<Long, DirectedSparseGraph<TweetNode, RetweetEdge>> graph: graphs.entrySet()) {
            if (graph.getValue().getVertexCount() > curMaxVertexCount) {
                curMaxVertexCount = graph.getValue().getVertexCount();
                curMaxEdgeCount = graph.getValue().getEdgeCount();
                curMaxGraph = graph.getValue();
            }
        }

        System.out.println("Max graph: " + curMaxVertexCount + " vertices, " + curMaxEdgeCount + " edges.");
        // analyzer.visualizeRetweetGraph(curMaxGraph);
        analyzer.visualizeRetweetGraph2(curMaxGraph);
    }

    public void classifyJSONFile(String inputFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String strLine = "";
        RetweetGraphAnalyzer analyzer = new RetweetGraphAnalyzer();

        while ((strLine = br.readLine()) != null) {
            Status status = TwitterObjectFactory.createStatus(strLine);
            System.out.println(classify(status) + "\t" + status.getText());
        }
    }

    public void buildClassifierFromTraining(String trainingFile) throws Exception {
        BufferedReader reader = new BufferedReader(
                new FileReader(trainingFile));
        Instances trainingData = new Instances(reader);
        reader.close();
        // setting class attribute
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        classifier = new J48();
        ((J48)classifier).setUnpruned(true);
        classifier.buildClassifier(trainingData);
    }

    public void buildClassifierFromModel(String modelFile) throws Exception {
        classifier = (Classifier) weka.core.SerializationHelper
                .read(modelFile);
    }

    public double classify(Status status) throws Exception {
        User user = status.getUser();
        FastVector atts = new FastVector();

        atts.addElement(new Attribute("friend_count"));
        atts.addElement(new Attribute("follower_count"));
        atts.addElement(new Attribute("tweet_count"));
        FastVector values = new FastVector();
        values.addElement("TRUE");
        values.addElement("FALSE");
        atts.addElement(new Attribute("is_verified", values));
        atts.addElement(new Attribute("is_user_url", values));
        atts.addElement(new Attribute("is_user_desc", values));

        atts.addElement(new Attribute("post_register_gap"));
        atts.addElement(new Attribute("retweet", values));
        atts.addElement(new Attribute("retweet_count"));
        atts.addElement(new Attribute("favorite_count"));
        atts.addElement(new Attribute("tweet_length"));
        atts.addElement(new Attribute("location_on", values));
        atts.addElement(new Attribute("hashtag_count"));
        atts.addElement(new Attribute("mention_count"));
        atts.addElement(new Attribute("url_count"));
        FastVector classes = new FastVector();
        values.addElement("F");
        values.addElement("T");
        atts.addElement(new Attribute("class", classes));
        Instances rel_struct = new Instances("rel", atts, 0);
        rel_struct.setClassIndex(rel_struct.numAttributes() - 1);
//        Attribute relational = new Attribute("relational_attr", rel_struct);

        // add data
        double[] instanceValues = new double[rel_struct.numAttributes()];
        instanceValues[0] = user.getFriendsCount();
        instanceValues[1] = user.getFollowersCount();
        instanceValues[2] = user.getStatusesCount();
        instanceValues[3] = rel_struct.attribute(3).indexOfValue(user.isVerified() ? "TRUE" : "FALSE");
        instanceValues[4] = rel_struct.attribute(4)
                .indexOfValue(user.getDescription() == null || user.getDescription().equals("") ? "FALSE" : "TRUE");
        instanceValues[5] = rel_struct.attribute(5)
                .indexOfValue(user.getURL() == null || user.getURL().equals("") ? "FALSE" : "TRUE");
        instanceValues[6] = (status.getCreatedAt().getTime() - user.getCreatedAt().getTime()) / (1000000 * 24 * 3600);
        instanceValues[7] = rel_struct.attribute(7).indexOfValue(status.isRetweet() ? "TRUE" : "FALSE");
        instanceValues[8] = status.getRetweetCount();
        instanceValues[9] = status.getFavoriteCount();
        instanceValues[10] = status.getText().length();
        instanceValues[11] = rel_struct.attribute(11).indexOfValue(status.getGeoLocation() == null ? "FALSE" : "TRUE");
        instanceValues[12] = extractor.extractHashtags(status.getText()).size();
        instanceValues[13] = extractor.extractMentionedScreennames(status.getText()).size();
        instanceValues[14] = extractor.extractURLs(status.getText()).size();

        // add data to instance
        rel_struct.add(new Instance(1.0, instanceValues));
        // perform prediction
        double classifiedValue = classifier.classifyInstance(rel_struct.instance(0));

        return classifiedValue;
    }

    public static void main(String [] args) throws Exception{
        TwitterTrustAnalyzer analyzer = new TwitterTrustAnalyzer();
        // analyzer.extractFeaturesFromJSON("data/forthoodshooting.json", "output/forthoodshooting.features");
//        analyzer.extractFeaturesFromJSON("data/mh370.json.1", "output/mh370.features");
        // analyzer.processJSONFile("data/mh370.json.1");
//        analyzer.buildClassifierFromTraining("output/mh370.arff");
        analyzer.buildClassifierFromModel("output/mh370_smo.model");
        analyzer.classifyJSONFile("data/mh370.json");
    }
}
