package org.marker.mushroom.dao;


import org.marker.mushroom.beans.Doctor;

public interface DoctorDao extends ISupportDao{


    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(Integer id, int status);

    boolean exists(Integer id);

    boolean updateInfo(Doctor doctor);
}
