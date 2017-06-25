package org.marker.mushroom.dao;


public interface DoctorDao extends ISupportDao{


    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(Integer id, int status);
}
