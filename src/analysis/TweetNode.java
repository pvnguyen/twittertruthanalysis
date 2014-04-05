package analysis;

/**
 * Created by phuong on 4/5/14.
 */
public class TweetNode {
    private long id;
    private boolean isVerified;
    private int followerCount;
    private int favouriteCount;
    private int sentiment;

    public TweetNode (long id, boolean isVerified, int followerCount,
                      int favouriteCount, int sentiment) {
        this.id = id;
        this.isVerified = isVerified;
        this.followerCount = followerCount;
        this.favouriteCount = favouriteCount;
        this.sentiment = sentiment;
    }

    public String toString() {
        return "V-" + id;
    }

    public boolean equals(Object obj) {
        TweetNode objNode = (TweetNode) obj;
        if (this.getId() == objNode.getId()) return true;
        else return false;
    }

    public int hashCode() {
        return (int) getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }
}
