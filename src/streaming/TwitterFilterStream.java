package streaming; /**
 * Created by phuong on 3/13/14.
 */
import features.TwitterFeatureExtraction;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFilterStream{
    public static void main(String[] args) throws TwitterException {
        String keywords[] = {"MH370"};
        TwitterFilterStream filter = new TwitterFilterStream();

        filter.filterPublicStream("sH1W4DQITPKCuNW0kxJg",
                "sN3ZoyISpJ5GLvSBCEzmnZFtpUsyVYaKpZcpsiILrw",
                "23776707-GcaPAHrqcgFZxAdvjHGuN9l8JdJz7aejY0AHXGNS2",
                "CEjcDlkg7aRqNsQ16dJ3JqeEtxuTyJZIkSIzNF0m6cwbb",
                keywords);
    }

    private void filterPublicStream(String consumerKey, String consumerSecret,
                                    String accessToken, String accessTokenSecret, String [] keywords) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                User user = status.getUser();
//                System.out.println(user.getId() + "\t" + user.getScreenName() + "\t" + user.getFriendsCount() + "\t"
//                        + user.getFollowersCount() + "\t" + user.getStatusesCount() + "\t"
//                        + user.getCreatedAt() + "\t" + user.isVerified() + "\t"
//                        + status.getId() + "\t" + status.getCreatedAt() + "\t" + "\t" + status.isRetweet() + "\t"
//                        + status.getRetweetCount() + "\t" + status.getFavoriteCount() + "\t" + status.getInReplyToStatusId() + "\t"
//                        + status.getInReplyToUserId() + "\t" + status.getLang() + "\t" + status.getText());
                if (!status.getLang().equals("en")) return;
                TwitterFeatureExtraction extractor = new TwitterFeatureExtraction();
                System.out.println(extractor.extractOnlineFeatures(status));
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        FilterQuery fq = new FilterQuery();

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);
    }

    private String parseStatusText(String status) {
        // Remove new line, tab characters
        String parsedStr = status.replaceAll("[\t\n\r]", "");
        // Remove double spaces
        parsedStr = parsedStr.replaceAll("\\s+", " ");

        return parsedStr;
    }
}
