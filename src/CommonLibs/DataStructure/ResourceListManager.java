package CommonLibs.DataStructure;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 20/04/2017.
 */
public class ResourceListManager {
    private static ResourceListManager resourceListManager;

    private ArrayList<Resource> resourceList;
    private ReadWriteLock rwlock;

    private ResourceListManager() {
        resourceList = new ArrayList<>();
        rwlock = new ReentrantReadWriteLock();
    }

    public static ResourceListManager shareResourceListManager() {
        if (null == resourceListManager) {
            resourceListManager = new ResourceListManager();
        }
        return resourceListManager;
    }

    public void newResource(Resource resource) {
        this.rwlock.readLock().lock();
        Resource temp = null;
        //find existed resource
        for (Resource re : resourceList) {
            if (0 == re.getUri().compareTo(resource.getUri())) {
                if (0 == re.getChannel().compareTo(resource.getChannel())) {
                    if (0 == re.getOwner().compareTo(resource.getOwner())) {
                        temp = re;
                        break;
                    }
                }
            }
        }
        this.rwlock.readLock().unlock();
        //update
        this.rwlock.writeLock().lock();
        this.resourceList.remove(temp);
        this.resourceList.add(resource);
        this.rwlock.writeLock().unlock();
    }

    public void removeResource(Resource resource) {
        this.rwlock.writeLock().lock();
        //TODO remove target resource based on the value of argument resource
        this.rwlock.writeLock().unlock();
    }

    public Resource findResource(Resource resource) {
        this.rwlock.readLock().lock();
        //TODO find target resource based on the value of argument resource

        this.rwlock.readLock().unlock();

        return null;
    }

}
