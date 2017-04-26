package CommonLibs.DataStructure;

import CommonLibs.Commands.ResourceCommand;
import EZShare_Server.ServerSetting;

import java.sql.Ref;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 20/04/2017.
 */
public class ResourceListManager {
//    public enum FindResult {
//        ExactMatch,
//        OwnerError,
//        NoMatch,
//        ;
//    }
//    public class FindResourceResult {
//        private Resource resource;
//        private FindResult findResult;
//
//        public FindResourceResult(Resource resource, FindResult findResult) {
//            this.resource = resource;
//            this.findResult = findResult;
//        }
//
//        public Resource getResource() {
//            return resource;
//        }
//
//        public void setResource(Resource resource) {
//            this.resource = resource;
//        }
//
//        public FindResult getFindResult() {
//            return findResult;
//        }
//
//        public void setFindResult(FindResult findResult) {
//            this.findResult = findResult;
//        }
//    }





    private static ResourceListManager resourceListManager;

    private ArrayList<Resource> resourceList;
    private ReadWriteLock rwlock;

    private ResourceListManager() {
        resourceList = new ArrayList<Resource>();
        rwlock = new ReentrantReadWriteLock();
    }

    public static ResourceListManager shareResourceListManager() {
        if (null == resourceListManager) {

            // TODO:
            // think about the following code, because I think this is thread-safer than directly create instance
//            synchronized(ResourceListManager.class){
//                if(null == resourceListManager);
//                resourceListManager = new ResourceListManager();
//            }
            resourceListManager = new ResourceListManager();
        }
        return resourceListManager;
    }

    /**
     * Publish or Share a resource (they share exactly the same logic in terms of resource list)
     * publish/share it directly, if the resource is new;
     * overwrite the old resource with this one, if the resource is published before;
     * deny the operation, if a new owner attempts to publish an existing resource in a channel
     * @param resource the resource to publish
     * @return true, if the input resource is published, or it overwrites the old one
     *          false, if the publish is denied
     */
    public boolean addResource(Resource resource) {

     boolean result = true;
        rwlock.writeLock().lock();
        // check if the resource with the same uri is published in the same channel;
        int index = -1;
        Resource tmp = null;
        for (int i = 0; i < resourceList.size(); i++) {
            tmp = resourceList.get(i);
            if (resource.getChannel().equals(tmp.getChannel())
                    && resource.getUri().equals(tmp.getUri())){
                index = i;
                break;
            }
        }

        // add, overwrite or deny the command based on the following cases:
        if (index == -1) {
            // case: there is no resource with the same uri in the same channel
            // resource should be published
            resourceList.add(resource);
        } else if (resourceList.get(index).getOwner().equals(resource.getOwner())) {
            // case: there is a resource with the same uri in the same channel, owned by the publisher
            // resource should be overwritten
            resourceList.set(index, resource);
        } else {
            // case: there is a resource witht the same uri in the same channel, owned by someone else
            // resource should be denied
            result = false;
        }
        rwlock.writeLock().unlock();
        return result;
    }

    /**
     * Remove a resource identified by the primary key
     * @param resource the resource (resource template specifying the resource) to remove
     * @return true, if the resource is removed
     *          false, if the resource does not exist
     */
    public boolean removeResource(Resource resource){
        boolean result = false;
        rwlock.writeLock().lock();
        // remove the resource if exists
        for (int i=0; i<resourceList.size(); i++){
            Resource rsc = resourceList.get(i);
            if (resource.getOwner().equals(rsc.getOwner())
                    && resource.getChannel().equals(rsc.getChannel())
                    && resource.getUri().equals(rsc.getUri())){
                resourceList.remove(rsc);
                result = true;
                break;
            }
        }
        rwlock.writeLock().unlock();
        return result;
    }


    /**
     * Find the resource to fetch specified by the resource template
     * a clone would be used as the return object, so it could be further modified without affect this list
     * @param resource the resource template specifying the resource
     * @return null, if the no resource found
     *          a clone for the resource specified, otherwise
     */
    public Resource findResource(Resource resource){
        for (Resource rsc: resourceList){
            if (resource.getChannel().equals(rsc.getChannel())
                    && resource.getUri().equals(rsc.getUri())){
                return rsc.clone();
            }
        }
        return null;
    }

    /**
     * Find the resource(s) that matches the template in the query command
     * a clone would be used for each result matching the template
     * @param template the resource template to match
     * @return a list of resources, (could be empty)
     */
    public ArrayList<Resource> matchTemplate(Resource template){
        ArrayList<Resource> list = new ArrayList<Resource>();
        this.rwlock.readLock().lock();
        for (Resource rsc:resourceList){
            if (matches(template,rsc) ){
                Resource match = rsc.clone();
                list.add(match);
            }
        }
        this.rwlock.readLock().unlock();
        return list;
    }

    /**
     * To check if a template matches a candidate resource
     * @param template the template provided
     * @param candidate the candidate resource
     * @return true, if they match
     *          false, otherwise
     */
    private boolean matches(Resource template, Resource candidate){
        if (!template.getChannel().equals(candidate.getChannel())) return false;
        if (template.getOwner()!="" && (!template.getOwner().equals(candidate.getOwner()))) return false;
        if (template.getUri()!="" && (!template.getUri().equals((candidate.getOwner())))) return false;
        ArrayList<String> tags = template.getTags();
        for (String tag:tags){
            if (!candidate.getTags().contains(tag)) return false;
        }
        
//        if (template.getName() == "" && template.getDescription() == "") return true;
        if (candidate.getName().indexOf(template.getName()) > -1) return true;
        if (candidate.getDescription().indexOf(template.getDescription()) > -1) return true;
        return false;
    }




//    public int findResource(Resource resource) {
//        this.rwlock.readLock().lock();
//        for (Resource re : resourceList) {
//            if (0 == re.getUri().compareTo(resource.getUri())) {
//                if (0 == re.getChannel().compareTo(resource.getChannel())) {
//                    if (0 == re.getOwner().compareTo(resource.getOwner())) {
//                        return resourceList.indexOf(re);//index of the existed resource
//                    }else {
//                        return -1;//different owner error
//                    }
//                }
//            }
//        }
//        this.rwlock.readLock().unlock();
//
//        return 0;//no existed resource
//    }
//
//    public void newResource(Resource resource) {
//        this.rwlock.readLock().lock();
//        Resource temp = null;
//        //find existed resource
//        for (Resource re : resourceList) {
//            if (0 == re.getUri().compareTo(resource.getUri())) {
//                if (0 == re.getChannel().compareTo(resource.getChannel())) {
//                    if (0 == re.getOwner().compareTo(resource.getOwner())) {
//                        temp = re;
//                        break;
//                    }
//                }
//            }
//        }
//        this.rwlock.readLock().unlock();
//        //update
//        this.rwlock.writeLock().lock();
//        this.resourceList.remove(temp);
//        this.resourceList.add(resource);
//        this.rwlock.writeLock().unlock();
//    }
//
//    public void removeResource(Resource resource) {
//        this.rwlock.writeLock().lock();
//        //TODO remove target resource based on the value of argument resource
//        this.rwlock.writeLock().unlock();
//    }


}
