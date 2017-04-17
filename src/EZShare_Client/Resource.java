package EZShare_Client;

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
//           within each Channel on the server (String). The URI must conform to official URI format. • Channel: optional user supplied channel name (String), default is "".
//    • Owner: optional user supplied owner name (String), default is "".
//    • EZserver: system supplied server:port name that lists the Resource (String).

    private String name;
    private String description;
    private String[] tags;
    private String uri;
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
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
}
