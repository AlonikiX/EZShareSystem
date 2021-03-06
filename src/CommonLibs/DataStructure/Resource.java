package CommonLibs.DataStructure;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 16/04/2017.
 */
public class Resource {
//    • Name: optional user supplied name (String), default is "".
//    • Description: optional user supplied description (String), default is "".
//    • Tags: optional user supplied list of tags (Array of Strings), default is empty list.
//    • URI: mandatory user supplied absolute URI, that is unique for each resource on a given EZShare Server
//           within each Channel on the server (String). The URI must conform to official URI format.
//    • Channel: optional user supplied channel name (String), default is "".
//    • Owner: optional user supplied owner name (String), default is "".
//    • EZserver: system supplied server:port name that lists the Resource (String).

    private String name;
    private String description;
    private ArrayList<String> tags;
    private String uri;
    private String channel;
    private String owner;
    private String ezserver;

    private ReadWriteLock rwlock;

    public Resource() {
        this.rwlock = new ReentrantReadWriteLock();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        rwlock.writeLock().lock();
        this.name = name;
        rwlock.writeLock().unlock();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        rwlock.writeLock().lock();
        this.description = description;
        rwlock.writeLock().unlock();
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        rwlock.writeLock().lock();
        this.tags = tags;
        rwlock.writeLock().unlock();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        rwlock.writeLock().lock();
        this.uri = uri;
        rwlock.writeLock().unlock();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        rwlock.writeLock().lock();
        this.owner = owner;
        rwlock.writeLock().unlock();
    }

    public String getEzserver() {
        return ezserver;
    }

    public void setEzserver(String ezserver) {
        rwlock.writeLock().lock();
        this.ezserver = ezserver;
        rwlock.writeLock().unlock();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return a clone of the resource
     */
    public Resource clone(){
        Resource clone = new Resource();
        rwlock.writeLock().lock();
        clone.name = this.name;
        clone.description = this.description;
        ArrayList<String> cloneTags = new ArrayList<String>(tags);
        clone.tags = cloneTags;
        clone.owner = this.owner;
        clone.channel = this.channel;
        clone.uri = this.uri;
//        clone.ezserver = this.ezserver;
        rwlock.writeLock().unlock();
        return clone;
    }

    /**
     * This method is mainly used to compare <b>resource template<b/>, instead of a concrete resource.
     * This method will be used for subscription, in the case that more than one clients subscribe the same
     * resource template from one server with relay on, then the same template is passed only once.
     * @param r the input resource template
     * @return ture, if the two templates are exactly the same
     *          false, otherwise
     */
    public boolean equals(Resource r){

        if (this.name.compareTo(r.getName())!=0) return false;
        if (this.description.compareTo(r.getDescription())!=0) return false;
        if (this.uri.compareTo(r.getUri())!=0) return false;
        if (this.owner.compareTo(r.getOwner())!=0) return false;
        if (this.channel.compareTo(r.getChannel())!=0) return false;
        if (this.tags.size() != r.getTags().size()) return false;
        // TODO we need to pay attention to duplicated tags
        for (int i=0; i<this.tags.size(); i++){
            if (!r.getTags().contains(this.tags.get(i))) return false;
        }
        return true;
    }
}
