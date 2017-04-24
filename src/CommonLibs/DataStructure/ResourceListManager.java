package CommonLibs.DataStructure;

import java.sql.Ref;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 20/04/2017.
 */
public class ResourceListManager {
    public enum FindResult {
        ExactMatch,
        OwnerError,
        NoMatch,
        ;
    }
    public class FindResourceResult {
        private Resource resource;
        private FindResult findResult;

        public FindResourceResult(Resource resource, FindResult findResult) {
            this.resource = resource;
            this.findResult = findResult;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public FindResult getFindResult() {
            return findResult;
        }

        public void setFindResult(FindResult findResult) {
            this.findResult = findResult;
        }
    }

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


    public int findResource(Resource resource) {
        this.rwlock.readLock().lock();
        for (Resource re : resourceList) {
            if (0 == re.getUri().compareTo(resource.getUri())) {
                if (0 == re.getChannel().compareTo(resource.getChannel())) {
                    if (0 == re.getOwner().compareTo(resource.getOwner())) {
                        return resourceList.indexOf(re);//index of the existed resource
                    }else {
                        return -1;//different owner error
                    }
                }
            }
        }
        this.rwlock.readLock().unlock();

        return 0;//no existed resource
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


}
