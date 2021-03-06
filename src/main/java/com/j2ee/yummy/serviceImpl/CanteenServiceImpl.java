package com.j2ee.yummy.serviceImpl;

import com.j2ee.yummy.dao.BalanceDao;
import com.j2ee.yummy.dao.CanteenDao;
import com.j2ee.yummy.model.Balance;
import com.j2ee.yummy.model.Manager;
import com.j2ee.yummy.model.canteen.Canteen;
import com.j2ee.yummy.model.canteen.UnauditedCanInfo;
import com.j2ee.yummy.service.CanteenService;
import com.j2ee.yummy.yummyEnum.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    ManagerServiceImpl managerService;
    @Autowired
    BalanceDao balanceDao;

    private static final long START = 1000000L;
    @Override
    public Canteen register(Canteen canteen) {
        canteen = canteenDao.insert(canteen);
        canteen.setAccount(getID(canteen.getId()));
        canteenDao.update(canteen);

        //在银行中注册这个商户
        Balance balance = new Balance();
        balance.setUserID(canteen.getId());
        balance.setUserType(UserType.Canteen);
        balance.setPassword(canteen.getPassword());
        balance.setCost(0);
        balance.setProfit(0);
        balance.setBalance(0);
        balanceDao.insert(balance);

        return canteen;
    }

    @Override
    public Canteen login(long account, String password) {
        return canteenDao.login(account,password);
    }

    @Override
    public boolean modify(UnauditedCanInfo unauditedCanInfo) {
        //这里要提供给经理审批，由经理调用modify
        List<Manager> managers = managerService.getAllMs();
        unauditedCanInfo.attachAll(managers);
        unauditedCanInfo.notifyManagers(managerService);
        return true;
    }

    @Override
    public Canteen getCanteenByID(long id) {
        Canteen canteen = canteenDao.getCanteenByID(id);
        //Canteen newCanteen = new Canteen(canteen.getId(),canteen.getPassword(),canteen.getCanteenName(),canteen.getLandlordName(),canteen.getPhone(),canteen.getAddress(),canteen.getProfile(),canteen.getCategories());
        //canteen.setId(getID(id));
        return canteen;
    }

    @Override
    public List<Canteen> getAll() {
        return canteenDao.getAll();
    }

    public long count(){
        return canteenDao.getExistNum();
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
