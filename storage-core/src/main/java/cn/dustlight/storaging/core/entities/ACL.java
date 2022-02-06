package cn.dustlight.storaging.core.entities;

import java.util.Collection;

public interface ACL {

    Collection<String> getOwner();

    Collection<String> getCanRead();

    Collection<String> getCanWrite();

}
