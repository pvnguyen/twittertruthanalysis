package streaming; /**
 * Created by phuong on 3/13/14.
 */
import analysis.TwitterTrustAnalyzer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFilterStream{
    private TwitterTrustAnalyzer analyzer = new TwitterTrustAnalyzer();
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

            /**
             * Function to process each tweet from Twitter stream
             *
             * @param status
             */
            public void onStatus(Status status) {
                analyzer.process(status);
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

}
