package com.j2ee.yummy.serviceImpl;

import com.j2ee.yummy.dao.CanteenDao;
import com.j2ee.yummy.model.canteen.Canteen;
import com.j2ee.yummy.model.canteen.UnauditedCanInfo;
import com.j2ee.yummy.service.CanteenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: yummy
 * @description: 餐厅的业务逻辑实现类
 * @author: Liu Hanyi
 * @create: 2019-02-07 22:05
 **/


@Service
public class CanteenServiceImpl implements CanteenService {
    @Autowired
    CanteenDao canteenDao;

    private static final long START = 1000000L;
    @Override
    public Canteen register(Canteen canteen) {
        canteen = canteenDao.insert(canteen);
        canteen.setId(getID());
        return canteen;
    }

    @Override
    public Canteen login(long id, String password) {
        id = offID(id);
        return canteenDao.login(id,password);
    }

    @Override
    public boolean modify(UnauditedCanInfo canteen) {
        //这里要提供给经理审批，由经理调用modify
        //canteen.notify(canteen);
        canteenDao.update(canteen);
        return true;
    }

    @Override
    public Canteen getCanteenByID(long id) {
        Canteen canteen = canteenDao.getCanteenByID(id);
        canteen.setId(getID(id));
        return canteen;
    }

    private long getID(long id) {
        return id+START;
    }

    private long getID(){
        long currentNum = canteenDao.getExistNum();
        return START+currentNum;
    }

    private long offID(long id){
        return id-START;
    }
}
