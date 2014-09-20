package org.marker.mushroom.dao;

import java.util.List;

import org.marker.mushroom.beans.Permission;


/**
 * 权限管理接口
 * @author Administrator
 *
 */
public interface IPermissionDao extends ISupportDao {

	List<Permission> findPermissionByGroupId(int groupId);

}
