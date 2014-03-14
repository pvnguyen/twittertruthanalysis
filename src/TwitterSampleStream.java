import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by phuong on 3/13/14.
 */
public class TwitterSampleStream {
    public static void main(String[] args) throws TwitterException {
        TwitterSampleStream sampler = new TwitterSampleStream();
        sampler.samplePublicStream("sH1W4DQITPKCuNW0kxJg", "sN3ZoyISpJ5GLvSBCEzmnZFtpUsyVYaKpZcpsiILrw",
                "23776707-GcaPAHrqcgFZxAdvjHGuN9l8JdJz7aejY0AHXGNS2", "CEjcDlkg7aRqNsQ16dJ3JqeEtxuTyJZIkSIzNF0m6cwbb");
    }

    private void samplePublicStream(String consumerKey, String consumerSecret,
                                    String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                User user = status.getUser();
                System.out.println(user.getId() + "\t" + user.getScreenName() + "\t" + user.getFriendsCount() + "\t"
                        + user.getFollowersCount() + "\t" + user.getStatusesCount() + "\t"
                        + user.getCreatedAt() + "\t" + user.isVerified() + "\t"
                        + status.getId() + "\t" + status.getCreatedAt() + "\t" + status.getRetweetCount() + "\t"
                        + status.getFavoriteCount() + "\t" + status.getInReplyToStatusId() + "\t"
                        + status.getInReplyToUserId() + "\t" + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("# Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("# Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("# Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("# Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}
